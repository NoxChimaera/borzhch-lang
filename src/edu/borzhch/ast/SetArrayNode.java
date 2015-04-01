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
public class SetArrayNode extends NodeAST {
    ArrayElementNode element;
    NodeAST value;
    
    public SetArrayNode(ArrayElementNode elementNode, NodeAST valueExpr) {
        element = elementNode;
        value = valueExpr;
    }

    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Set Array:");
        ++lvl;
        printLevel(lvl);
        System.out.println("Array Reference:");
        element.debug(lvl + 1);
        printLevel(lvl);
        System.out.println("Value:");
        value.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        // load reference and push index into stack
        element.codegen();
        // compute value
        value.codegen();
        // iastore
        
    }
    
}
