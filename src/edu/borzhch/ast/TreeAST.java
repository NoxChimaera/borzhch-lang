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
public class TreeAST {
    static NodeAST root;
    public static void setRoot(NodeAST r) {
        root = r;
    }
    
    public void debug(int lvl) {
        root.debug(lvl);
    }
}
