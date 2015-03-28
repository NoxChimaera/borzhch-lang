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
public class NodeList extends NodeAST {
    ArrayList<NodeAST> nodes;
    public NodeList() {
        nodes = new ArrayList<>();
    }
    
    public void add(NodeAST node) {
        nodes.add(node);
    }
    public void addAll(ArrayList<NodeAST> col) {
        nodes.addAll(col);
    }
    public void addAll(NodeList list) {
        nodes.addAll(list.nodes);
    }
    
    @Override
    public void debug(int lvl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
