/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.StructTable;
import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.helpers.BOHelper;
import org.apache.bcel.generic.Type;

/**
 *
 * @author Balushkin M.
 */
public class SetFieldNode extends NodeAST {
    DotOpNode dot;
    NodeAST value;
    
    String struct;
    
    public SetFieldNode(DotOpNode left, NodeAST value) {
//        struct = structName;
        dot = left;
        dot.generateLastNode(false);
        this.value = value;
        
        VariableNode var = dot.getFirstNode();
        struct = var.varTypeName;
    }

    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Set Field: ");
        dot.debug(lvl + 1);
        value.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        dot.codegen();
        value.codegen();
        FieldNode field = dot.getLastNode();
        
        JavaCodegen.method().putField(struct, field.id, 
                BOHelper.toJVMType(BOHelper.getType(
                        StructTable.getFieldType(struct, field.id))
                )
        );
    }
}
