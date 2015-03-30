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
public class UnOpNode extends OpNode {
    NodeAST expr;
    String op;
    public UnOpNode(NodeAST expression, String operation) {
        expr = expression;
        op = operation;
    }

    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Unary " + op);
        expr.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
