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
public class FieldNode extends NodeAST {
    String id;
    public FieldNode(String identifier) {
        id = identifier;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Field: " + id);
    }

    @Override
    public void codegen() {
    }
}
