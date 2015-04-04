/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.InstructionHandle;

/**
 * Узел AST, представляющий операцию сравнения
 * @author Balushkin M.
 */
public class CmpOpNode extends OpNode {
    NodeAST l;
    NodeAST r;
    String op;
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
     * @param isEqual 
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
}
