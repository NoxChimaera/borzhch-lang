/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.optimization;

/**
 * Константа
 * @author Balushkin M.
 */
public interface IConstant {
    /**
     * Привести к типу Boolean
     * @return 
     */
    boolean coerceBoolean();
    /**
     * Привести к типу Float
     * @return 
     */
    float coerceFloat();
    /**
     * Привести к типу Int
     * @return 
     */
    int coerceInt();
    /**
     * Привести к типу String
     * @return 
     */
    String coerceString();
}
