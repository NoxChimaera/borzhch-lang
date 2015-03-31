/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.helpers.BOHelper;

/**
 *
 * @author Balushkin M.
 */
public class ArOpNode extends OpNode {
    NodeAST l;
    NodeAST r;
    String op;
    public ArOpNode(NodeAST left, NodeAST right, String operator) {
        l = left;
        r = right;
        op = operator;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Arithmetic " + op + " (" 
                + BOHelper.toString(type) + ")");
        ++lvl;
        l.debug(lvl);
        r.debug(lvl);
    }

    @Override
    public void codegen() {
        l.codegen();
        r.codegen();
        
        switch (type) {
            case INT:
                codegenInt(); 
                break;
        }
    }
    
    private void codegenInt() {
        switch (op) {
            case "+":
                JavaCodegen.method().addInt();
                break;
            case "-":
                JavaCodegen.method().subInt();
                break;
            case "*":
                JavaCodegen.method().mulInt();
                break;
            case "/":
                JavaCodegen.method().divInt();
                break;
        }
    }
}
