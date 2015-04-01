/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;

/**
 *
 * @author Balushkin M.
 */
public class LogOpNode extends OpNode {
    NodeAST l;
    NodeAST r;
    String op;
    public LogOpNode(NodeAST left, NodeAST right, String operator) {
        l = left;
        r = right;
        op = operator;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Logic " + op);
        ++lvl;
        l.debug(lvl);
        r.debug(lvl);
    }

    @Override
    public void codegen() {
        l.codegen();
        r.codegen();
        switch (op) {
            case "and":
                JavaCodegen.method().and();
                break;
            case "or":
                JavaCodegen.method().or();
                break;
            case "xor":
                JavaCodegen.method().xor();
                break;
        }
    }
}
