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
public class GetArrayNode extends NodeAST {
    ArrayElementNode arrayRef;
    
    public GetArrayNode(ArrayElementNode array) {
        arrayRef = array;
        type = arrayRef.type;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Get Array:");
        arrayRef.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        // generate _aload
        arrayRef.codegen();
        // _astore
    }
}
