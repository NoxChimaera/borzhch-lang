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
public class AssignNode extends NodeAST {
    String left;
    NodeAST right;
    
    public AssignNode(String id, NodeAST expr) {
        left = id;
        right = expr;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Assignment");
        ++lvl;
        printLevel(lvl);
        System.out.println("Variable: " + left);
        printLevel(lvl);
        System.out.println("Expression: ");
        right.debug(lvl + 1);
    }
}
