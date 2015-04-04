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
    NodeAST l;
    NodeAST r;
    
    boolean generateLastNode = true;
    public void generateLastNode(boolean generate) {
        generateLastNode = generate;
    }
    public FieldNode getLastNode() {
        if (FieldNode.class == r.getClass())
            return (FieldNode) r;
        else
            return ((DotOpNode) r).getLastNode();
    }
    public VariableNode getFirstNode() {
        if (VariableNode.class == l.getClass())
            return (VariableNode) l;
        else
            return ((DotOpNode) l).getFirstNode();
    }
    
    public DotOpNode(NodeAST left, NodeAST right) {
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
        l.codegen();
        if (generateLastNode) {
            r.codegen();
        }
    }
}
