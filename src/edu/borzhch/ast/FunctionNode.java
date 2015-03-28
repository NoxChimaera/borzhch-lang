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
    NodeList statements;
    
    public FunctionNode(String name, BOType returnType) {
        funcName = name;
        this.returnType = returnType;
        returnTypeName = BOHelper.toString(returnType);
        args = new ArgumentList();
        statements = new StatementList();
    }
    
    public FunctionNode(String name, String returnTypeName) {
        funcName = name;
        returnType = BOType.REF;
        this.returnTypeName = returnTypeName;        
        args = new ArgumentList();
        statements = new StatementList();
    }
    
    public void setArguments(NodeList argumentList) {
        args = argumentList;
    }
    public void setStatements(NodeList statementList) { 
        statements = statementList;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Function: " + funcName);

        ++lvl;
        printLevel(lvl);
        System.out.println("Type: " + returnTypeName + " ("
                + BOHelper.toString(returnType) + ")");
        printLevel(lvl);
        System.out.println("Statement List:");
        ++lvl;
        statements.debug(lvl);
    }
}
