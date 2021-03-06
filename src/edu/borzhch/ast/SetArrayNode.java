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
 * Запись элемента массива
 * @author Balushkin M.
 */
public class SetArrayNode extends NodeAST {
    /**
     * Элемент массива
     */
    ArrayElementNode element;
    /**
     * Значение
     */
    NodeAST value;
    
    public SetArrayNode(ArrayElementNode elementNode, NodeAST valueExpr) {
        element = elementNode;
        value = valueExpr;
        
        element.setLoad(false);
    }

    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Set Array:");
        ++lvl;
        printLevel(lvl);
        System.out.println("Array Reference:");
        element.debug(lvl + 1);
        printLevel(lvl);
        System.out.println("Value:");
        value.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        // load reference and push index into stack
        element.codegen();
        // compute value
        value.codegen();
        
        JavaCodegen.method().convert(value.type, element.type);
        
        // iastore
        JavaCodegen.method().setArray(element.ref.id(), 
                BOHelper.toJVMType(BOHelper.getType(element.ref.getVarTypeName())));
    }
    
}
