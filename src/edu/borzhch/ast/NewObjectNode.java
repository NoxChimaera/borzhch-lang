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
public class NewObjectNode extends NodeAST {
    String identifier;
    public NewObjectNode(String id) {
        identifier = id;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("New: " + identifier);
    }

    @Override
    public void codegen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
