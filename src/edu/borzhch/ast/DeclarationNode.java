/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.StructTable;
import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;
import edu.borzhch.helpers.BOHelper;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ObjectType;

/**
 * Объявление переменной
 * @author Balushkin M.
 */
public class DeclarationNode extends NodeAST {
//    BOType varType;
    String varTypeName;
    String varName;
    
    boolean isField = false;
    public void isField(boolean is) {
        isField = is;
    }
    
    public String getName() {
        return varName;
    }
    
    /**
     * Объявление переменных примитивных типов
     * @param name Идентификатор переменной
     * @param type Тип
     */
    public DeclarationNode(String name, BOType type) {
        varName = name;
        this.type = type;
//        varType = type;
        varTypeName = BOHelper.toString(type);
    }
    
    /**
     * Объявление переменных ссылочных типов
     * @param name Идентификатор переменной
     * @param typeName Идентификатор типа
     */
    public DeclarationNode(String name, String typeName) {
        varName = name;
        this.type = BOType.REF;
//        varType = BOType.REF;
        type = BOType.REF;
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
                + BOHelper.toString(type) + ")");
    }

    @Override
    public void codegen() {
        if (isField) {
            if (BOType.ARRAY == type) {
                if (StructTable.isDefined(varName)){
                    JavaCodegen.struct().addField(varName, 
                            new ArrayType(new ObjectType(varTypeName), 1));
                } else {
                    JavaCodegen.struct().addField(varName, 
                            BOHelper.toJVMArrayType(varTypeName));
                }
//                JavaCodegen.struct().addField(varName, new ArrayType);
            } else if (StructTable.isDefined(varTypeName)) {
                JavaCodegen.struct().addField(varName, new ObjectType(varTypeName));
            } else {
                JavaCodegen.struct().addField(varName, 
                        BOHelper.toJVMType(BOHelper.getType(varTypeName)));
            }
        } else {
            // Associate variable name with LocalVariableGen-object
            JavaCodegen.method().addLocalVariable(varName, type);
        }
    }
}
