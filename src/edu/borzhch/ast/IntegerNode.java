/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;

/**
 * Целочтсленная константа
 * @author Balushkin M.
 */
public class IntegerNode extends ConstantNode {
    int val;
    public IntegerNode(int value) {
        val = value;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println(val);
    }

    @Override
    public void codegen() {
        // Push variable into stack
        JavaCodegen.method().push(val);
    }
}
