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
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;

/**
 * Функция
 * @author Balushkin M.
 */
public class FunctionNode extends NodeAST {
    String funcName;
    String varTypeName;
    
    String className;
    
    NodeList args;
    StatementList statements;
    
    /**
     * Функция, возвращающая примитивный тип
     * @param name Идентификатор функции
     * @param returnType Вовзращаемый тип
     * @param className Имя класса, которому принадлежит функция
     */
    public FunctionNode(String name, BOType returnType, String className) {
        funcName = name;
        this.type = returnType;
        this.className = className;
        varTypeName = BOHelper.toString(returnType);
        args = new ArgumentList();
        statements = new StatementList();
    }
    
    /**
     * Функция, возвращающая ссылочный тип
     * @param name Идентификатор фукнции
     * @param returnTypeName Идентификатор типа
     * @param className Имя класса, которому принадлежит функция
     */
    public FunctionNode(String name, String returnTypeName, String className) {
        funcName = name;
        this.varTypeName = returnTypeName;
        this.className = className;
        if (BOHelper.isType(returnTypeName)) {
            type = BOHelper.getType(returnTypeName);
        } else {
            type = BOType.REF;
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
        return this.args == null ? 0 : this.args.nodes.size();
    }
    public ArrayList<NodeAST> getArguments() {
        return this.args == null ? null : this.args.nodes;
    }
    public String getReturnTypeName() {
        return this.varTypeName;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Function: " + funcName);

        ++lvl;
        printLevel(lvl);
        System.out.println("Type: " + varTypeName + " ("
                + BOHelper.toString(type) + ")");
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
                
                switch (decl.type) {
                    case ARRAY:
                        if (BOHelper.isType(decl.varTypeName)) {
                            argsTypes[i] = BOHelper.toJVMArrayType(decl.varTypeName);
                        } else {
                            argsTypes[i] = new ArrayType(decl.varTypeName, 1);
                        }
                        break;
                    case REF:
                        argsTypes[i] = new ObjectType(decl.varTypeName);
                        break;
                    default:
                        argsTypes[i] = BOHelper.toJVMType(decl.type);
                }
                
                i++;
            }
        }
        Type retType = BOHelper.getJVMRetType(varTypeName);
        JavaCodegen.newMethod(funcName, retType, argsTypes, argsNames, className);
        
        if (statements != null) {
            statements.codegen();
        }
        
        if(type == BOType.VOID) JavaCodegen.method().createReturn(Type.VOID);
        
        JavaCodegen.compileMethod(className, funcName);
    }
}
