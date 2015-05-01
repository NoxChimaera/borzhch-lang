/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.helpers.BOHelper;
import org.apache.bcel.generic.Type;

/**
 * Узел AST, представляющий выражение array[index]
 * @author Balushkin M.
 */
public class ArrayElementNode extends NodeAST implements INodeWithVarTypeName {
    VariableNode ref;
    NodeAST index;
    
    boolean load = true;
    /**
     * Загрузить элемент из ячейки?
     * @param load 
     */
    public void setLoad(boolean load) {
        this.load = load;
    }
    
    /**
     * @param arrayRef Идентификатор массива
     * @param expr Индекс
     */
    public ArrayElementNode(VariableNode arrayRef, NodeAST expr) {
        ref = arrayRef;
        index = expr;
        
        type = BOHelper.getType(ref.getVarTypeName());
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Array Element");
        ++lvl;
        printLevel(lvl);
        System.out.println("Identifier: ");
        ref.debug(lvl + 1);
        printLevel(lvl);
        System.out.println("Index: ");
        index.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        // load array reference
        ref.codegen();
        // push index
        index.codegen();
        
        if (load) {
            JavaCodegen.method().getArray("", BOHelper.toJVMType(type));
        }
    }

    @Override
    public String getVarTypeName() {
        return ref.varTypeName;
    }
    
    @Override
    public void setVarTypeName(String name) {
        type = BOHelper.getType(name);
    }
}
