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
 * Объявление переменной
 * @author Balushkin M.
 */
public class DeclarationNode extends NodeAST {
    BOType varType;
    String varTypeName;
    String varName;
    
    /**
     * Объявление переменных примитивных типов
     * @param name Идентификатор переменной
     * @param type Тип
     */
    public DeclarationNode(String name, BOType type) {
        varName = name;
        varType = type;
        varTypeName = BOHelper.toString(type);
    }
    
    /**
     * Объявление переменных ссылочных типов
     * @param name Идентификатор переменной
     * @param typeName Идентификатор типа
     */
    public DeclarationNode(String name, String typeName) {
        varName = name;
        varType = BOType.REF;
        varTypeName = typeName;
    }

    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Declaration: ");
        
        ++lvl;
        printLevel(lvl);
        System.out.println("Variable: " + varName);
        printLevel(lvl);
        System.out.println("Type: " + varTypeName + " (" 
                + BOHelper.toString(varType) + ")");
    }

    @Override
    public void codegen() {
        // Associate variable name with LocalVariableGen-object
        JavaCodegen.method().addLocalVariable(varName, varType);
    }
}
