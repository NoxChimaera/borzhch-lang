/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.constants.BOType;

/**
 *
 * @author Balushkin M.
 */
public class NullNode extends NodeAST {
    public NullNode() {
        type = BOType.VOID;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.printf("NULL\n");
    }

    @Override
    public void codegen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
