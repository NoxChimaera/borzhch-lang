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
public class ErrorHelper {
    public static String incompatibleTypes(BOType from, BOType to) {
        return String.format("incompatible types: %s can not be converted to %s", 
                BOHelper.toString(from), BOHelper.toString(to));
    }
    
    public static String incompatibleTypes(BOType from, String to) {
        return String.format("incompatible types: %s can not be converted to %s", 
                BOHelper.toString(from), to);
    }
    
    public static String identifierExists(String identifier) {
        return String.format("identifier <$s> is already defined\n", identifier);
    }
    
    public static String unknownType(String type) {
        return String.format("unknown type <%s>\n", type);
    }
    
    public static String notDeclared(String identifier) {
        return String.format("identifier <%s> is not declared\n", identifier);
    }
}
