/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;
import edu.borzhch.optimization.IConstant;
import edu.borzhch.optimization.IFoldable;

/**
 * Целочтсленная константа
 * @author Balushkin M.
 */
public class IntegerNode extends NodeAST implements IConstant {
    int val;
    public IntegerNode(int value) {
        val = value;
        type = BOType.INT;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.printf("%d;I\n", val);
    }

    @Override
    public void codegen() {
        // Push variable into stack
        JavaCodegen.method().push(val);
    }

    @Override
    public boolean coerceBoolean() {
        return val > 0;
    }

    @Override
    public float coerceFloat() {
        return (float) val;
    }

    @Override
    public int coerceInt() {
        return val;
    }

    @Override
    public String coerceString() {
        return String.valueOf(val);
    }
}
