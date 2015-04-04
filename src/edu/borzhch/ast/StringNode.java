/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;

/**
 *
 * @author Balushkin M.
 */
public class StringNode extends NodeAST {
    String sval;
    public StringNode(String val) {
        sval = val;
        type = BOType.STRING;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.printf("'%s';S\n", sval);
    }

    @Override
    public void codegen() {
        JavaCodegen.method().push(sval);
    }   
}
