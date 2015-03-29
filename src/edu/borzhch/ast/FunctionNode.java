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
 * Функция
 * @author Balushkin M.
 */
public class FunctionNode extends NodeAST {
    String funcName;
    BOType returnType;
    String returnTypeName;
    
    NodeList args;
    StatementList statements;
    
    /**
     * Функция, возвращающая примитивный тип
     * @param name Идентификатор функции
     * @param returnType Вовзращаемый тип
     */
    public FunctionNode(String name, BOType returnType) {
        funcName = name;
        this.returnType = returnType;
        returnTypeName = BOHelper.toString(returnType);
        args = new ArgumentList();
        statements = new StatementList();
    }
    
    /**
     * Функция, возвращающая ссылочный тип
     * @param name Идентификатор фукнции
     * @param returnTypeName Идентификатор типа
     */
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
    public void setStatements(StatementList statementList) { 
        statements = statementList;
    }
    public StatementList getStatements() {
        return statements;
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

    @Override
    public void codegen() {
        JavaCodegen.newMethod(funcName, returnType, "Program");
        // TODO: args
        statements.codegen();
        JavaCodegen.compileMethod("Program", funcName);
    }
}
