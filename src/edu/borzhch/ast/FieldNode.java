/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.StructTable;
import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;
import edu.borzhch.helpers.BOHelper;

/**
 *
 * @author Balushkin M.
 */
public class FieldNode extends NodeAST implements IDotNode {
    String id;
    String structName;
    
    public FieldNode(String identifier) {
        id = identifier;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Field: " + id);
    }

    @Override
    public void codegen() {
        if (BOType.REF == type) {
            JavaCodegen.method().getFieldClass(structName, id, StructTable.getFieldType(structName, id));
        } else {
            JavaCodegen.method().getField(structName, id, 
                BOHelper.toJVMType(BOHelper.getType(StructTable.getFieldType(structName, id))));
        }
    }

    @Override
    public void setStructName(String name) {
        structName = name;
        type = BOHelper.getType(StructTable.getFieldType(structName, id));
    }
}
