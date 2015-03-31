/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;
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
            case FLOAT:
                codegenFloat();
                break;
        }
    }
    private void codegenFloat() {
        JavaCodegen.method().convert(r.type, l.type);
        switch (op) {
            case "+":
                JavaCodegen.method().add(BOType.FLOAT);
                break;
            case "-":
                JavaCodegen.method().sub(BOType.FLOAT);
                break;
            case "*":               
                JavaCodegen.method().mul(BOType.FLOAT);
                break;
            case "/":                
                JavaCodegen.method().div(BOType.FLOAT);
                break;
        }
    }
    private void codegenInt() {
        JavaCodegen.method().convert(r.type, l.type);
        switch (op) {
            case "+":
                JavaCodegen.method().add(BOType.INT);
                break;
            case "-":
                JavaCodegen.method().sub(BOType.INT);
                break;
            case "*":               
                JavaCodegen.method().mul(BOType.INT);
                break;
            case "/":                
                JavaCodegen.method().div(BOType.INT);
                break;
        }
    }
}
