/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;

/**
 * Присваивание
 * @author Balushkin M.
 */
public class AssignNode extends NodeAST {
    /**
     * Идентификатор переменной
     */
    String left;
    /**
     * Выражение
     */
    NodeAST right;
    
    /**
     * @param id Идентификатор переменной
     * @param expr Выражение
     */
    public AssignNode(String id, NodeAST expr) {
        left = id;
        right = expr;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Assignment");
        ++lvl;
        printLevel(lvl);
        System.out.println("Variable: " + left);
        printLevel(lvl);
        System.out.println("Expression: ");
        right.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        // Generate expression, push result into stack
        // e.g.:
        // 0:   bipush 42
        right.codegen();
        // 1:   istore var_index
        // TODO: types. It works only with integers just now
        JavaCodegen.method().store(left);
    }
}
