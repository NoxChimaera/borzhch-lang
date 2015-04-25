/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;
import edu.borzhch.helpers.BOHelper;
import java.util.ArrayList;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.Type;

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
        this.returnTypeName = returnTypeName;
        if (BOHelper.isType(returnTypeName)) {
            returnType = BOHelper.getType(returnTypeName);
        } else {
            returnType = BOType.REF;
        }
   
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
    public String getFuncName() {
        return this.funcName;
    }
    public Integer getArgumentsCount() {
        return this.args.nodes.size();
    }
    public ArrayList<NodeAST> getArguments() {
        return this.args == null ? null : this.args.nodes;
    }
    public String getReturnTypeName() {
        return this.returnTypeName;
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
        if (statements != null) {
            statements.debug(lvl);
        }
    }

    @Override
    public void codegen() {
        ArrayList<NodeAST> args = this.getArguments();
        String[] argsNames = null;
        Type[] argsTypes = null;
        if(args != null && args.size() != 0) {
            argsNames = new String[args.size()];
            argsTypes = new Type[args.size()];
            int i = 0;
            for(NodeAST arg : args) {
                DeclarationNode decl = ((DeclarationNode) arg);
                argsNames[i] = decl.varName;
                
                if (BOType.REF == decl.type() && BOHelper.isType(decl.varTypeName)) {
                    argsTypes[i] = BOHelper.toJVMArrayType(decl.varTypeName);
                } else {
                    argsTypes[i] = BOHelper.toJVMType(decl.type);
                }
                i++;
            }
        }
        
        JavaCodegen.newMethod(funcName, returnType, argsTypes, argsNames, "Program");
        
        if (statements != null) {
            statements.codegen();
        }
        
        if(returnType == BOType.VOID) JavaCodegen.method().createReturn(Type.VOID);
        
        JavaCodegen.compileMethod("Program", funcName);
    }
}
