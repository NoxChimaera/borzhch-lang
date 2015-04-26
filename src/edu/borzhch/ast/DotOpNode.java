/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import java.util.ArrayList;

/**
 *
 * @author Balushkin M.
 */
public class DotOpNode extends NodeAST {
    NodeAST left;
    NodeAST right;

    public DotOpNode(NodeAST left, NodeAST right) {
        this.left = left;
        this.right = right;
    }
    
    public ArrayList<NodeAST> reduce() {
        ArrayList<NodeAST> tmp = new ArrayList<>();
        tmp.add(left);

        if (DotOpNode.class == right.getClass()) {
            tmp.addAll(((DotOpNode) right).reduce());
        } else {
            tmp.add(right);
        }
        return tmp;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("DOT");
        ++lvl;
        printLevel(lvl);
        System.out.println("Left: ");
        left.debug(lvl + 1);
        printLevel(lvl);
        System.out.println("Right: ");
        right.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
