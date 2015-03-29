/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;

/**
 * Узел конструкции return
 * @author Balushkin M.
 */
public class ReturnNode extends NodeAST {
    NodeAST expr;
    
    public ReturnNode(NodeAST retVal) {
        expr = retVal;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Return: ");
        ++lvl;
        printLevel(lvl);
        System.out.println("Expression: ");
        expr.debug(lvl + 1);
    }

    @Override
    public void codegen() {
//        JavaCodegen.returnVoid();
        
//        JavaCodegen.add("return ");
//        expr.codegen();
//        JavaCodegen.add(";");
    }
}
