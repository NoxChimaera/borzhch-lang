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
public class PostOpNode extends OpNode {
    VariableNode var;
    String op;
    public PostOpNode(VariableNode variable, String operator) {
        var = variable;
        op = operator;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Post " + op);
        ++lvl;
        printLevel(lvl);
        System.out.println("to");
        var.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
