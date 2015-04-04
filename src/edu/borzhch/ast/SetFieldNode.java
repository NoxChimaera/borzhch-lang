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
public class SetFieldNode extends NodeAST {
    DotOpNode dot;
    NodeAST value;
    
    public SetFieldNode(DotOpNode left, NodeAST value) {
        dot = left;
        dot.generateLastNode(false);
        this.value = value;
    }

    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Set Field: ");
        dot.debug(lvl + 1);
        value.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        dot.codegen();
        value.codegen();
        FieldNode field = dot.getLastNode();
    }
}
