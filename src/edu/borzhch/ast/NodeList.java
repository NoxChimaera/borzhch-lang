/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import java.util.ArrayList;
import org.apache.bcel.generic.InstructionList;

/**
 * Список узлов
 * @author Balushkin M.
 */
public abstract class NodeList extends NodeAST {
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
        if(list != null) nodes.addAll(list.nodes);
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Node List");
        ++lvl;
        for (NodeAST node : nodes) {
            node.debug(lvl);
        }
    }

    @Override
    public void codegen() {
        for (NodeAST node : nodes) {
            node.codegen();
        }
    }
}