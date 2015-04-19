/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.StructTable;
import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.helpers.BOHelper;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;

/**
 *
 * @author Balushkin M.
 */
public class SetFieldNode extends NodeAST {
    DotOpNode dot;
    NodeAST value;
    
    String struct;
    
    GetFieldNode l;
    public SetFieldNode(GetFieldNode left, NodeAST value) {
        l = left;
        l.generateLastNode(false);
        
        struct = l.var.varTypeName;
        this.value = value;
    }
    
//    public SetFieldNode(DotOpNode left, NodeAST value) {
////        struct = structName;
//        dot = left;
//        dot.generateLastNode(false);
//        this.value = value;
//        
//        VariableNode var = dot.getFirstNode();
//        struct = var.varTypeName;
//    }

    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Set Field: ");
        l.debug(lvl + 1);
//        dot.debug(lvl + 1);
        value.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        l.codegen();
        value.codegen();
        FieldNode field = l.getLastNode();

//        dot.codegen();
//        value.codegen();
//        FieldNode field = dot.getLastNode();
//        
//        JavaCodegen.method().putField(struct, field.id, 
//                BOHelper.toJVMType(BOHelper.getType(
//                        StructTable.getFieldType(struct, field.id))
//                )
//        );
        String fieldType = StructTable.getFieldType(field.structName, field.id);
        
        if (StructTable.isDefined(fieldType)) {
            JavaCodegen.method().putFieldClass(field.structName, field.id, fieldType);
        } else {
            JavaCodegen.method().putField(field.structName, field.id, 
                    BOHelper.toJVMType(field.type()));
        }
        
//        if (StructTable.isDefined(fieldType)) {
//            JavaCodegen.method().putFieldClass(field.structName, field.id, 
//                    StructTable.getFieldType(field.structName, field.id));
//        } else
//        
//        
//        JavaCodegen.method().putField(field.structName, field.id, BOHelper.toJVMType(field.type()));
    }
}
