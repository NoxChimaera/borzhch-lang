/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.constants;

import edu.borzhch.helpers.BOHelper;

/**
 *
 * @author Balushkin M.
 */
public class InferenceTypeTable {
    static BOType[][] table;
    static {
        int l = BOType.values().length;
        table = new BOType[l][l];
        
        // VOID - ANY -> VOID
        for (int i = 0; i < l; ++i)
            table[BOType.VOID.ordinal()][i] = BOType.VOID;
        // REF - ANY -> VOID, REF - REF -> REF
        int ref = BOType.REF.ordinal();
        for (int i = 0; i < l; ++i) {
            if (ref != i)
                table[ref][i] = BOType.VOID;
            else
                table[ref][i] = BOType.REF;
        }
        // STRING - ANY -> VOID, STRING - STRING -> STRING
        int str = BOType.STRING.ordinal();
        for (int i = 0; i < l; ++i) {
            if (ref != i)
                table[str][i] = BOType.VOID;
            else
                table[str][i] = BOType.STRING;
        }
        // BOOL - BOOL -> BOOL, BOOL - ANY -> VOID
        int b = BOType.BOOL.ordinal();
        for (int i = 0; i < l; i++) {
            if (b != i)
                table[b][i] = BOType.VOID;
            else
                table[b][i] = BOType.BOOL;
        }
        // INT - FLOAT | INT -> INT
        int it = BOType.INT.ordinal();
        int f = BOType.FLOAT.ordinal();
        for (int i = 0; i < l; ++i) {
            if (it == i || f == i)
                table[it][i] = BOType.INT;
            else
                table[it][i] = BOType.VOID;
        }
        // FLOAT - INT | FLOAT -> FLOAT
        for (int i = 0; i < l; ++i) {
            if (it == i || f == i)
                table[f][i] = BOType.FLOAT;
            else
                table[f][i] = BOType.VOID;
        }
        // TODO: STRING - INT | FLOAT | STRING -> STRING
        
        table[BOType.VOID.ordinal()][BOType.VOID.ordinal()] = BOType.VOID;
    }
    
    public static BOType inferType(BOType left, BOType right) {
        return table[left.ordinal()][right.ordinal()];
    }
}
