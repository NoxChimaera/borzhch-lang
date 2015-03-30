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
public class StringNode extends NodeAST {
    String sval;
    public StringNode(String val) {
        sval = val;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println(sval);
    }

    @Override
    public void codegen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
