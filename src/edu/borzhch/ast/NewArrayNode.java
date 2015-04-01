/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;
import edu.borzhch.helpers.BOHelper;

/**
 *
 * @author Balushkin M.
 */
public class NewArrayNode extends NodeAST {
    BOType arrayType;
    NodeAST size;

    public NewArrayNode(String elementsType, NodeAST sizeExpression) {
        arrayType = BOHelper.getType(elementsType);
        size = sizeExpression;
        type = BOType.REF;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("New Array:");
        ++lvl;
        printLevel(lvl);
        System.out.println("Array Type: " + BOHelper.toString(arrayType));
        printLevel(lvl);
        System.out.println("Size:");
        size.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        size.codegen();
        JavaCodegen.method().newArray(BOHelper.toJVMType(arrayType));
    }
}
