/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.helpers;

import edu.borzhch.constants.BOType;

/**
 *
 * @author Balushkin M.
 */
public class BOHelper {
    public static BOType getType(String type) {
        switch (type) {
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
    
    public static String toString(BOType type) {
        switch (type) {
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
}
