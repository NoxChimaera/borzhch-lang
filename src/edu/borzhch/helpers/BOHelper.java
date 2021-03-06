/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.helpers;

import edu.borzhch.constants.BOType;
import org.apache.bcel.generic.*;


/**
 * Хелпер для работы с типами Borzhch и JVM
 * @author Balushkin M.
 */
public class BOHelper {
    public static boolean isType(String str) {
        switch (str) {
            case "void":
            case "bool":
            case "int":
            case "float":
            case "string":
                return true;
            default:
                return false;
        }
    }
    
    public static boolean isNumber(BOType type) {
        return BOType.INT == type || BOType.FLOAT == type;
    }
    
    /**
     * Конвертация из строки в Borzhch
     * @param type Имя типа
     * @return Тип Borzhch
     */
    public static BOType getType(String type) {
        switch (type) {
            case "void":
                return BOType.VOID;
            case "bool":
                return BOType.BOOL;
            case "int":
                return BOType.INT;
            case "float":
                return BOType.FLOAT;
            case "string":
                return BOType.STRING;
            case "$array":
                return BOType.ARRAY;
            default:
                return BOType.REF;
        }
    }
    
    /**
     * Конвертация из Borzhch в строку
     * @param type Тип Borzhch
     * @return Имя типа
     */
    public static String toString(BOType type) {
        switch (type) {
            case VOID:
                return "void";
            case BOOL:
                return "bool";
            case INT:
                return "int";
            case FLOAT:
                return "float";
            case STRING:
                return "string";
            case ARRAY:
                return "$array";
            default:
                return "ref";
        }
    }
    
    /**
     * Конвертация из Borzhch в JVM
     * @param type Borzhch тип
     * @return JVM тип
     */
    public static Type toJVMType(BOType type) {
         switch (type) {
            case VOID:
                return Type.VOID;
            case BOOL:
                return Type.BOOLEAN;
            case INT:
                return Type.INT;
            case FLOAT:
                return Type.FLOAT;
            case STRING:
                return Type.STRING;
            default:
                return Type.OBJECT;
        }
    }
    
    public static Type toJVMType(String type) {
        BOType t = getType(type);
        if (BOType.REF != t) {
            return toJVMType(t);
        } else if (type.contains("$")) {
            String tmp = type.substring(1);
            Type tmpType = toJVMType(tmp);
            return new ArrayType(tmpType, 1);
        } else{
            return new ObjectType(type);
        }
    }
    
    public static ArrayType toJVMArrayType(String type) {
        switch (type) {
            case "void":
                return null;
            case "bool":
                return new ArrayType(Type.BOOLEAN, 1);
            case "int":
                return new ArrayType(Type.INT, 1);
            case "float":
                return new ArrayType(Type.FLOAT, 1);
            case "string":
                return new ArrayType(Type.STRING, 1);
            default:
                return new ArrayType(type, 1);
        }
    }
    
    public static Type getJVMRetType(String typeName) {
        BOType bt = BOHelper.getType(typeName);
        Type retType = BOHelper.toJVMType(bt);
        if (bt == BOType.REF) {
            retType = new ObjectType(typeName);
        } else if (bt == BOType.ARRAY) {
            retType = BOHelper.toJVMArrayType(typeName);
        }
        return retType;
    }
}
