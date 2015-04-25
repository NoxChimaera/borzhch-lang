/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;
import edu.borzhch.helpers.BOHelper;
import org.apache.bcel.generic.ObjectType;

/**
 *
 * @author Balushkin M.
 */
public class NewArrayNode extends NodeAST {
    BOType arrayType;
    String arrayObjectType;
    
    NodeAST size;

    public NewArrayNode(String itemsType, NodeAST sizeExpression) {
        arrayType = BOHelper.getType(itemsType);
        arrayObjectType = itemsType;
        size = sizeExpression;
        type = BOType.ARRAY;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("New Array:");
        ++lvl;
        printLevel(lvl);
        System.out.println(String.format("Array Type: %s (%s)", 
                BOHelper.toString(arrayType), arrayObjectType));
        printLevel(lvl);
        System.out.println("Size:");
        size.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        size.codegen();
        if (BOType.REF == arrayType) {
            JavaCodegen.method().newArray(new ObjectType(arrayObjectType));
        } else {
            JavaCodegen.method().newArray(BOHelper.toJVMType(arrayType));
        }
    }
}
