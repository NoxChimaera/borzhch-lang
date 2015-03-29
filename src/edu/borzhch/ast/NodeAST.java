/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

/**
 *
 * @author Balushkin M.
 */
public abstract class NodeAST {
    public abstract void debug(int lvl);
    public abstract void codegen();
    
    protected void printLevel(int lvl) {
        for (int i = 0; i < lvl; ++i) {
            System.out.print(" ");
        }
        System.out.print("|>");
    }
}
