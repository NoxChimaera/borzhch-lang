/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.constants.BOType;
import edu.borzhch.helpers.BOHelper;

/**
 *
 * @author Balushkin M.
 */
public class FunctionNode extends NodeAST {
    String funcName;
    BOType returnType;
    String returnTypeName;
    
    NodeList args;
    NodeList instructions;
    
    public FunctionNode(String name, BOType returnType) {
        funcName = name;
        this.returnType = returnType;
        returnTypeName = BOHelper.toString(returnType);
        args = new NodeList();
        instructions = new NodeList();
    }
    
    public FunctionNode(String name, String returnTypeName) {
        funcName = name;
        returnType = BOType.REF;
        this.returnTypeName = returnTypeName;        
        args = new NodeList();
        instructions = new NodeList();
    }
    
    @Override
    public void debug(int lvl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
