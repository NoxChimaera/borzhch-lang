/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import com.sun.corba.se.spi.ior.iiop.JavaCodebaseComponent;
import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;
import edu.borzhch.helpers.BOHelper;

/**
 *
 * @author Balushkin M.
 */
public class CastNode extends NodeAST {
    NodeAST expression;
    public CastNode(BOType type, NodeAST exp) {
        this.type = type;
        expression = exp;
    }

    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Cast:");
        ++lvl;
        printLevel(lvl);
        System.out.println("Type:" + BOHelper.toString(type));
        printLevel(lvl);
        System.out.println("Expression:");
        expression.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        expression.codegen();
        JavaCodegen.method().convert(expression.type, type);
    }
}
