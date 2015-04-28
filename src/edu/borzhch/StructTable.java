/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch;

import java.util.HashMap;

/**
 *
 * @author Balushkin M.
 */
public class StructTable {
    private static final HashMap<String, HashMap<String, String>> structs = 
            new HashMap<>();
    private static final HashMap<String, HashMap<String, String>> subtypes =
            new HashMap<>();
    
    public static void addStruct(String id) {
        structs.put(id, new HashMap<>());
        subtypes.put(id, new HashMap<>());
    }
    public static boolean isDefined(String id) {
        return structs.containsKey(id);
    }
    
    public static void putField(String struct, String field, String type) {
        structs.get(struct).put(field, type);
    }
    public static void putFieldSub(String struct, String field, String type) {
        subtypes.get(struct).put(field, type);
    }
    public static void fieldIsDefined(String struct, String field) {
        structs.get(struct).containsKey(field);
    }
    
    public static String getFieldType(String struct, String field) {
        return structs.get(struct).get(field);
    }
    public static String getFieldSub(String struct, String field) {
        return subtypes.get(struct).get(field);
    }
    public static HashMap<String, String> getSchema(String structName) {
        return structs.get(structName);
    }
}
