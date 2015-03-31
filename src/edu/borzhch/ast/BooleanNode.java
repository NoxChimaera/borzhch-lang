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
public class BooleanNode extends ConstantNode {
    boolean bval;
    public BooleanNode(int val) {
        bval = val != 0;
        type = BOType.BOOL;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.printf("%b;B\n", bval);
        
//        System.out.println(bval);
    }

    @Override
    public void codegen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
