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
 *
 * @author Balushkin M.
 */
public class PostOpNode extends OpNode {
    VariableNode var;
    String op;
    boolean needPush = false;
    public void setPush(boolean push) {
        needPush = push;
    }
    
    public PostOpNode(VariableNode variable, String operator) {
        var = variable;
        op = operator;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Post " + op);
        var.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        int index = var.getIndex();
        switch (op) {
            case "#":
                JavaCodegen.method().inc(index);
                break;
            case "@":
                JavaCodegen.method().dec(index);
                break;
        }
        if (needPush) {
            JavaCodegen.method().load(var.id, BOHelper.toJVMType(var.type));
        }
    }
}
