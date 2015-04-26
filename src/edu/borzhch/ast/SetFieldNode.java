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
 * Запись в поле объекта
 * @author Balushkin M.
 */
public class SetFieldNode extends NodeAST {
    /**
     * Поле объекта
     */
    GetFieldNode field;
    /**
     * Значение
     */
    NodeAST value;

    public SetFieldNode(GetFieldNode field, NodeAST value) {
        this.field = field;
        this.value = value;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Set Field: ");
        field.debug(lvl + 1);
        value.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        field.generateLast(false);
        field.codegen();
        value.codegen();
        
        VariableNode last = (VariableNode) field.getLast();
        switch (last.type) {
            case REF:
                break;
            default: 
                JavaCodegen.method().putField(field.schema, last.id, 
                        BOHelper.toJVMType(last.type));
                break;
        }
    } 
}
