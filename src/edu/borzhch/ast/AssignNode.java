/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.helpers.BOHelper;
import org.apache.bcel.generic.Type;

/**
 * Узел AST, представляющий присваивание
 * @author Balushkin M.
 */
public class AssignNode extends NodeAST {
    /**
     * Идентификатор переменной
     */
    VariableNode left;
    /**
     * Выражение
     */
    NodeAST right;
    
    /**
     * @param id Идентификатор переменной
     * @param expr Выражение
     */
    public AssignNode(VariableNode id, NodeAST expr) {
        left = id;
        right = expr;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Assignment");
        ++lvl;
        printLevel(lvl);
        System.out.println("Variable: " + left.id + " (" 
                + BOHelper.toString(left.type) + ")");
        printLevel(lvl);
        System.out.println("Expression: ");
        right.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        // Generate expression, push result into stack
        // e.g.:
        // 0:   bipush 42
        boolean isField = JavaCodegen.struct().hasField(left.id);
        if(isField) {
            JavaCodegen.method().load("this", Type.OBJECT);
        }
        right.codegen();
        // And convert it to variable type
        JavaCodegen.method().convert(right.type, left.type);
        // 1:   istore var_index
        // TODO: types. It works only with integers just now
        if(isField) {
            JavaCodegen.method().putField(JavaCodegen.struct().getName(), left.id, BOHelper.toJVMType(left.type));
        } else {
            JavaCodegen.method().store(left.id, BOHelper.toJVMType(left.type));
        }
    }
}
