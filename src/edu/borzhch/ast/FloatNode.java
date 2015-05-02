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
 *
 * @author Balushkin M.
 */
public class FloatNode extends NodeAST implements IConstant {
    float val;
    public FloatNode(float fval) {
        val = fval;
        type = BOType.FLOAT;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.printf("%f;F\n", val);
    }

    @Override
    public void codegen() {
        JavaCodegen.method().push(val);
    }

    @Override
    public boolean coerceBoolean() {
        return val > 0;
    }

    @Override
    public float coerceFloat() {
        return val;
    }

    @Override
    public int coerceInt() {
        return (int) val;
    }

    @Override
    public String coerceString() {
        return String.valueOf(val);
    }
    
}
