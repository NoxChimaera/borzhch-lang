/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

/**
 *
 * @author Balushkin M.
 */
public class ArrayElementNode extends NodeAST {
    VariableNode ref;
    NodeAST index;
    public ArrayElementNode(VariableNode arrayRef, NodeAST expr) {
        ref = arrayRef;
        index = expr;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Array Element");
        ++lvl;
        printLevel(lvl);
        System.out.println("Identifier: ");
        ref.debug(lvl + 1);
        printLevel(lvl);
        System.out.println("Index: ");
        index.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        // load array reference
        ref.codegen();
        // push index
        index.codegen();
    }
}
