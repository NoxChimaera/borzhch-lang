/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.StructTable;
import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.helpers.BOHelper;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ObjectType;
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
        
        NodeAST tmp = field.getLast();
        // If last item of dot-op is identifier, e.g. foo.bar._baz_
        if (VariableNode.class == tmp.getClass()) {
            VariableNode var = (VariableNode) tmp;     
            value.codegen();

            switch (var.type) {
                case REF:
                    if (var.varTypeName.equals("$array")) {
                        String t = StructTable.getFieldSub(field.schema, var.id);
                        
                        if (BOHelper.isType(t)) {
                            JavaCodegen.method().putField(field.schema, var.id
                                , new ArrayType(BOHelper.toJVMType(BOHelper.getType(t)), 1));
                        } else {
                            JavaCodegen.method().putField(field.schema, var.id
                                , new ArrayType(new ObjectType(t), 1));
                        }
                    } else {
                        JavaCodegen.method().putField(field.schema, var.id
                                , new ObjectType(var.varTypeName));
                    }
                    break;
                case ARRAY:
                    JavaCodegen.method().putField(field.schema, var.id, 
                            BOHelper.toJVMArrayType(var.varTypeName));
                    break;
                default: 
                    JavaCodegen.method().putField(field.schema, var.id, 
                            BOHelper.toJVMType(var.type));
                    break;
            }   
        } else if (ArrayElementNode.class == tmp.getClass()) {
            ArrayElementNode item = (ArrayElementNode) tmp;
            JavaCodegen.method().getField(field.schema, item.ref.id, 
                        BOHelper.toJVMArrayType(item.ref.varTypeName));
            
//            item.index.codegen();
//            JavaCodegen.method().getArray(BOHelper.toJVMType(
//                BOHelper.getType(item.ref.varTypeName)));
//            
            item.index.codegen();
            value.codegen();
            JavaCodegen.method().setArray(BOHelper.toJVMType(
                BOHelper.getType(item.ref.varTypeName)));
            

//            ArrayElementNode item = (ArrayElementNode) tmp;
//            
//            GetArrayNode get = new GetArrayNode((ArrayElementNode) tmp);
//            get.codegen();
        }
    } 
}
