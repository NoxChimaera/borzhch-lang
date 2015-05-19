/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.Program;
import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;
import edu.borzhch.optimization.IConstant;
import edu.borzhch.optimization.IFoldable;
import edu.borzhch.optimization.IReductable;
import java.util.function.Function;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.InstructionHandle;

/**
 * Узел AST, представляющий операцию сравнения
 * @author Balushkin M.
 */
public class CmpOpNode extends NodeAST implements IFoldable, IReductable {
    NodeAST l;
    NodeAST r;
    String op;
    
    /**
     * Операция сравнения
     * @param left Левое выражение
     * @param right Правое вырадение
     * @param operator Оператор { >, <, >=, <=, ==, != }
     */
    public CmpOpNode(NodeAST left, NodeAST right, String operator) {
        l = left;
        r = right;
        op = operator;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Comparison " + op);
        ++lvl;
        l.debug(lvl);
        r.debug(lvl);
    }

    @Override
    public void codegen() {
        if (Program.config.getConstantFolding()) {
            NodeAST fold = fold();
            if (fold != this) {
                fold.codegen();
                return;
            }
        }
        if (Program.config.getStrengthReduction()) {
            NodeAST reduct = reduct();
            if (this != reduct) {
                reduct.codegen();
                return;
            }
        }
        
        switch (op) {
            case "==":
            case "!=":
                if (BOType.FLOAT == l.type || BOType.FLOAT == r.type) {
                    feq(op.equals("=="));
                } else if (BOType.INT == l.type && BOType.INT == r.type) {
                    ieq(op.equals("=="));
                }
                break;
            case ">":
            case "<":            
            case ">=":
            case "<=":
                if (BOType.INT == l.type && BOType.INT == r.type) {
                    iineq(op.equals(">") || op.equals(">="));
                } 
                if (BOType.FLOAT == l.type || BOType.FLOAT == r.type) {
                    fineq(op.equals(">") || op.equals(">="));
                }
                break;
        }
    }
   
    /**
     * Сравнение чисел с плавающей точкой (неравенство)
     * @param isRight isRight ? >, >= : <, <=
     */
    private void fineq(boolean isRight) {
        l.codegen();
        if (BOType.FLOAT == l.type) {
            JavaCodegen.method().convert(l.type, BOType.FLOAT);
        }
        r.codegen();
        if (BOType.FLOAT == r.type) {
            JavaCodegen.method().convert(r.type, BOType.FLOAT);
        }
        
        // 0: fcmpl // push -1, 0 or +1 into stack
        JavaCodegen.method().fcmpl();
        
        // 1: if<op>
        IfInstruction zcmp = null;
        switch (op) {
            case ">":
            case "<":
                zcmp = JavaCodegen.method().zcmp("gt");
                break;
            case ">=":
            case "<=":
                zcmp = JavaCodegen.method().zcmp("ge");
                break;
        }
  
        // 2: iconst_0 // false
        if (isRight)
            JavaCodegen.method().push(0);
        else
            JavaCodegen.method().push(1);
        // 3: goto 5
        GOTO go = JavaCodegen.method().go();
        // 4: iconst_1 // true
        if (isRight)
            JavaCodegen.method().push(1);
        else
            JavaCodegen.method().push(0);
        zcmp.setTarget(JavaCodegen.method().getLastHandler());
        // 5: nop
        JavaCodegen.method().nop();
        go.setTarget(JavaCodegen.method().getLastHandler());
    }
    
    /**
     * Сравнение чисел с плавающей точкой (либо INT - FLOAT)
     * @param isEqual isEqual ? == : !=
     */
    private void feq(boolean isEqual) {
        l.codegen();
        if (BOType.FLOAT == l.type) {
            JavaCodegen.method().convert(l.type, BOType.FLOAT);
        }
        r.codegen();
        if (BOType.FLOAT == r.type) {
            JavaCodegen.method().convert(r.type, BOType.FLOAT);
        }
       
        // 0: fcmpl // 0 if a==b, 1 if a>b, -1 if a<b
        JavaCodegen.method().fcmpl();
        // 1: ifeq 4 // to iconst_1
        IFEQ ifeq = JavaCodegen.method().ifeq();
        // 2: iconst_0
        if (isEqual)
            JavaCodegen.method().push(0);
        else
            JavaCodegen.method().push(1);
        // 3: goto 5 // after iconst_1
        GOTO go = JavaCodegen.method().go();
        // 4: iconst_1
        if (isEqual)
            JavaCodegen.method().push(1);
        else
            JavaCodegen.method().push(0);
        InstructionHandle ihTrue = JavaCodegen.method().getLastHandler();
        ifeq.setTarget(ihTrue);
        // 5: nop // hacked by Aen Sidhe
        JavaCodegen.method().nop();
        InstructionHandle crutch = JavaCodegen.method().getLastHandler();
        go.setTarget(crutch);
    }
    
    /**
     * Неравенство (целочисленное)
     * @param isRight True if left is greater right
     */
    private void iineq(boolean isRight) {
        l.codegen();
//        if (BOType.FLOAT == l.type || BOType.BOOL == l.type) {
//            JavaCodegen.method().convert(l.type, BOType.INT);
//        }
        r.codegen();
//        if (BOType.FLOAT == r.type || BOType.BOOL == r.type) {
//            JavaCodegen.method().convert(r.type, BOType.INT);
//        }
        
        // 0: if_cmpgt
        IfInstruction ineq = null;
        switch (op) {
            case ">":
            case "<":
                ineq = JavaCodegen.method().icmp(isRight ? "gt" : "lt");
                break;
            case ">=":
            case "<=":
                ineq = JavaCodegen.method().icmp(isRight ? "ge" : "le");
                break;
        }
        
        // 1: iconst_0
        JavaCodegen.method().push(0);
        // 2: goto _
        GOTO go = JavaCodegen.method().go();
        // 3: iconst_1
        JavaCodegen.method().push(1);
        InstructionHandle ihTrue = JavaCodegen.method().getLastHandler();
        ineq.setTarget(ihTrue);
        // 4: nop // hacked by elves
        JavaCodegen.method().nop();
        InstructionHandle crutch = JavaCodegen.method().getLastHandler();
        go.setTarget(crutch);
    }
    
    /**
     * Целочисленное сравнение
     * @param isEqual true if "==", false if "!="
     */
    private void ieq(boolean isEqual) {
        l.codegen();
//        if (BOType.INT == l.type) {
//            JavaCodegen.method().convert(l.type, BOType.INT);
//        }
        r.codegen();
//        if (BOType.INT == r.type) {
//            JavaCodegen.method().convert(r.type, BOType.INT);
//        }
        
        // 0: if_icmpeq _ 
        IfInstruction eq = JavaCodegen.method().icmp("eq");
        // 1: iconst_0
        if (isEqual)
            JavaCodegen.method().push(0);
        else
            JavaCodegen.method().push(1);
        // 2: goto _
        GOTO go = JavaCodegen.method().go();
        // 3: iconst_1
        if (isEqual)
            JavaCodegen.method().push(1);
        else
            JavaCodegen.method().push(0);
        InstructionHandle ihTrue = JavaCodegen.method().getLastHandler();
        eq.setTarget(ihTrue);
        // 4: nop // hacked by elves
        JavaCodegen.method().nop();
        InstructionHandle crutch = JavaCodegen.method().getLastHandler();
        go.setTarget(crutch);
    }

    @Override
    public NodeAST fold() {
        if (l instanceof IFoldable) {
            l = (NodeAST) ((IFoldable) l).fold();
        }
        if (r instanceof IFoldable) {
            r = (NodeAST) ((IFoldable) r).fold();
        }
        
        if (l instanceof IConstant && r instanceof IConstant) {
            if (BOType.FLOAT == l.type || BOType.FLOAT == r.type) {
                return foldAsFloat((IConstant) l, (IConstant) r);
            } else if (l.type == r.type && BOType.INT == l.type) {
                return foldAsInt((IConstant) l, (IConstant) r);
            }
        } 
        
        return this;
    }

    /**
     * Сворачивает константное выражение, как целочисленное
     * @param l Константа слева
     * @param r Константа справа
     * @return Новое значение, полученне после оптимизации, или this, если что-то пошло не так
     */
    private NodeAST foldAsInt(IConstant left, IConstant right) {
        switch (op) {
            case ">":
                return new BooleanNode(left.coerceInt() 
                        > right.coerceInt());
            case "<":
                return new BooleanNode(left.coerceInt() 
                        < right.coerceInt());
            case ">=":
                return new BooleanNode(left.coerceInt() 
                        >= right.coerceInt());
            case "<=":
                return new BooleanNode(left.coerceInt() 
                        <= right.coerceInt());
            case "==":
                return new BooleanNode(left.coerceInt() 
                        == right.coerceInt());
            case "!=":
                return new BooleanNode(left.coerceInt() 
                        != right.coerceInt());
        }
        return this;       
    }
        
    /**
     * Сворачивает константное выражение, как с плавающей точкой
     * @param l Константа слева
     * @param r Константа справа
     * @return Новое значение, полученне после оптимизации, или this, если что-то пошло не так
     */
    private NodeAST foldAsFloat(IConstant left, IConstant right) {
        switch (op) {
            case ">":
                return new BooleanNode(left.coerceFloat() 
                        > right.coerceFloat());
            case "<":
                return new BooleanNode(left.coerceFloat() 
                        < right.coerceFloat());
            case ">=":
                return new BooleanNode(left.coerceFloat() 
                        >= right.coerceFloat());
            case "<=":
                return new BooleanNode(left.coerceFloat() 
                        <= right.coerceFloat());
            case "==":
                return new BooleanNode(left.coerceFloat() 
                        == right.coerceFloat());
            case "!=":
                return new BooleanNode(left.coerceFloat() 
                        != right.coerceFloat());
        }
        return this;
    }

    @Override
    public NodeAST reduct() {
        if (l instanceof IConstant && r instanceof IConstant) {
            float lt = ((IConstant) l).coerceFloat();
            float rt = ((IConstant) r).coerceFloat();
            
            if (lt == rt) {
                switch (op) {
                    case "==":
                    case ">=":
                    case "<=":
                        return new BooleanNode(true);
                    case "!=":
                    case ">":
                    case "<":
                        return new BooleanNode(false);
                }
            }
        } else if (l instanceof VariableNode && r instanceof VariableNode) {
            VariableNode lvar = (VariableNode) l;
            VariableNode rvar = (VariableNode) r;
            if (lvar.id.equals(rvar.id)) {
                switch (op) {
                    case "==":
                    case ">=":
                    case "<=":
                        return new BooleanNode(true);
                    case "!=":
                    case ">":
                    case "<":
                        return new BooleanNode(false);
                }
            }
        }
        return this;
    }
}