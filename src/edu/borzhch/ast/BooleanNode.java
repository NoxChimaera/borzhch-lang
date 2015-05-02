/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;
import edu.borzhch.optimization.IConstant;

/**
 * Узел AST, представляющий логическую константу
 * @author Balushkin M.
 */
public class BooleanNode extends NodeAST implements IConstant {
    boolean bval;
    public BooleanNode(int val) {
        bval = val != 0;
        type = BOType.BOOL;
    }
    
    public BooleanNode(boolean val) {
        bval = val;
        type = BOType.BOOL;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.printf("%b;B\n", bval);
    }

    @Override
    public void codegen() {
        JavaCodegen.method().push(bval);
    }  

    @Override
    public boolean coerceBoolean() {
        return bval;
    }

    @Override
    public float coerceFloat() {
        return bval ? 1.0f : 0.0f;
    }

    @Override
    public int coerceInt() {
        return bval ? 1 : 0;
    }

    @Override
    public String coerceString() {
        return String.valueOf(bval);
    }
}
