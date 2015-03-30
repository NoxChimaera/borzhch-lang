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
}
