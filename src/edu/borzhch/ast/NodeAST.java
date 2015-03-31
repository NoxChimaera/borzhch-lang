/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.constants.BOType;

/**
 *
 * @author Balushkin M.
 */
public abstract class NodeAST {
    protected BOType type;
    public BOType type() {
        return type;
    }
    public void type(BOType newType) {
        type = newType;
    }
    
    public abstract void debug(int lvl);
    public abstract void codegen();
    
    protected void printLevel(int lvl) {
        for (int i = 0; i < lvl - 1; ++i) {
            System.out.print("   ");
        }
        System.out.print("|---");
        
//        System.out.print("|>");
    }
}
