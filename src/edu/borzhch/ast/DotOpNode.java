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
public class DotOpNode extends NodeAST {
    VariableNode l;
    NodeAST r;
    public DotOpNode(VariableNode left, NodeAST right) {
        l = left;
        r = right;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("DOT");
        ++lvl;
        printLevel(lvl);
        System.out.println("Left: ");
        l.debug(lvl + 1);
        printLevel(lvl);
        System.out.println("Right: ");
        r.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
