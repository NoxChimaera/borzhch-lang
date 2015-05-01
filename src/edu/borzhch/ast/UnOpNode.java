/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;

/**
 * Унарная операция
 * @author Balushkin M.
 */
public class UnOpNode extends NodeAST {
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
        expr.codegen();
        switch (op) {
            case "!":
                JavaCodegen.method().not();
                break;
            case "-":
                JavaCodegen.method().neg(expr.type);
                break;
        }
    }
}
