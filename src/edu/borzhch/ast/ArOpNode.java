/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.Program;
import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;
import edu.borzhch.helpers.BOHelper;
import edu.borzhch.optimization.IConstant;
import edu.borzhch.optimization.IFoldable;
import edu.borzhch.optimization.IReductable;

/**
 * Узел AST, представляющий арифметическую операцию
 * @author Balushkin M.
 */
public class ArOpNode extends NodeAST implements IFoldable, IReductable {
    NodeAST l;
    NodeAST r;
    String op;
    
    /**
     * Арифметическая операция
     * @param left Левое выражение
     * @param right Правое выражение
     * @param operator Оператор { +, -, *, /, ** }
     */
    public ArOpNode(NodeAST left, NodeAST right, String operator) {
        l = left;
        r = right;
        op = operator;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Arithmetic " + op + " (" 
                + BOHelper.toString(type) + ")");
        ++lvl;
        l.debug(lvl);
        r.debug(lvl);
    }

    @Override
    public void codegen() {
        if (Program.config.getConstantFolding()) {
            NodeAST fold = fold();
            if (this != fold) {
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

        if (op.equals("**")) {
            codegenPower();
            return;
        }
        
        l.codegen();
        
        if (l == r) {
            JavaCodegen.method().dup();
        } else {
            r.codegen();
        }
        switch (type) {
            case INT:
            case FLOAT:
                operandsAreNumbers();
                break;
        }
    }

    @Override
    public NodeAST reduct() {
        switch (op) {
            case "**":
                if (l instanceof VariableNode && r instanceof IntegerNode) {
                    String ll = ((VariableNode) l).id;
                    int rr = ((IntegerNode) r).val;
                    if (2 == rr) {
                        ArOpNode tmp = new ArOpNode(l, l, "*");
                        tmp.type(l.type);
                        type(l.type);
                        return tmp;
                    }
                }
                break;
            case "*":
                if (r instanceof IntegerNode) {
                    int count = ((IntegerNode) r).val;
                    switch (count) {
                        case 0:
                            return new IntegerNode(0);
                        case 1:
                            return l;
                        case 2:
                            ArOpNode tmp = new ArOpNode(l, l, "+");
                            tmp.type(l.type);
                            type(l.type);
                            return tmp;
                    }
                }
                break;
            case "/":
                if (r instanceof IntegerNode) {
                    int val = ((IntegerNode) r).val;
                    switch (val) {
                        case 0:
                            throw new ArithmeticException();
                        case 1:
                            return l;
                        case 2:
                            ArOpNode tmp = new ArOpNode(new FloatNode(0.5f), l, "*");
                            tmp.type(BOType.FLOAT);
                            type(BOType.FLOAT);
                            return tmp;
                        case 4:
                            ArOpNode tmp4 = new ArOpNode(new FloatNode(0.25f), l, "*");
                            tmp4.type(BOType.FLOAT);
                            type(BOType.FLOAT);
                            return tmp4;
                    }
                }
            case "+":
            case "-":
                if (r instanceof IConstant) {
                    float val = ((IConstant) r).coerceFloat();
                    if (val == 0f) return l;
                } else if (l instanceof IConstant) {
                    float val = ((IConstant) l).coerceFloat();
                    if (val == 0f) return r;
                }
                break;
        } 
        return this;
    }
    
    /**
     * Генерация кода для операции возведения в степень
     * Тип операндов - double
     * Stack:  ..., value1, value2
     * Result: ..., result
     * Code:
     * 0:   код l
     * 1:   [if]2d // конвертация в double
     * 2:   код r
     * 3:   [if]2d
     * 4:   invokestatic Math.pow
     * 5:   d2f // результат операции всегда float
     */
    private void codegenPower() {
        l.codegen();
        JavaCodegen.method().convertToDouble(l.type);
        r.codegen();
        JavaCodegen.method().convertToDouble(r.type);
        JavaCodegen.method().invokeStatic("pow");
        JavaCodegen.method().convertDouble(BOType.FLOAT);
        type = BOType.FLOAT;
    }
        
    /**
     * Генерация кода для узлов типа float/int
     * Stack:  ..., value1, value2
     * Result: ..., result
     */
    private void operandsAreNumbers() {
        // Левый операнд ВСЕГДА соответствует типу узла
        JavaCodegen.method().convert(r.type, l.type);
        switch (op) {
            case "+":
                JavaCodegen.method().add(type);
                break;
            case "-":
                JavaCodegen.method().sub(type);
                break;
            case "*":               
                JavaCodegen.method().mul(type);
                break;
            case "/":                
                JavaCodegen.method().div(type);
                break;
        }
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
            return foldConst((IConstant) l, (IConstant) r);
        } else {
            return this;
        }
    }
    
    /**
     * Сворачивает константное выражение
     * @param l Константа слева
     * @param r Константа справа
     * @return Новое значение, полученне после оптимизации, или this, если что-то пошло не так
     */
    private NodeAST foldConst(IConstant l, IConstant r) {
        switch (op) {
            case "+":
                switch (((NodeAST) l).type) {
                    case INT:
                        return new IntegerNode(l.coerceInt() + r.coerceInt());
                    case FLOAT:
                        return new FloatNode(l.coerceFloat() + r.coerceFloat());
                    default:
                        return this;
                }
            case "-":
                switch (((NodeAST) l).type) {
                    case INT:
                        return new IntegerNode(l.coerceInt() - r.coerceInt());
                    case FLOAT:
                        return new FloatNode(l.coerceFloat() - r.coerceFloat());
                    default:
                        return this;
                }
            case "*":                
                switch (((NodeAST) l).type) {
                    case INT:
                        return new IntegerNode(l.coerceInt() * r.coerceInt());
                    case FLOAT:
                        return new FloatNode(l.coerceFloat() * r.coerceFloat());
                    default:
                        return this;
                }
            case "/":                
                switch (((NodeAST) l).type) {
                    case INT:
                        return new IntegerNode(l.coerceInt() / r.coerceInt());
                    case FLOAT:
                        return new FloatNode(l.coerceFloat() / r.coerceFloat());
                    default:
                        return this;
                }
            case "**":
                switch (((NodeAST) l).type) {
                    case INT:
                    case FLOAT:
                        return new FloatNode((float) Math.pow(l.coerceFloat(), r.coerceFloat()));
                    default:
                        return this;
                }
        }
        
        return this;
    }
}
