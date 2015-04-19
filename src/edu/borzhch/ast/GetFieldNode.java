/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.StructTable;
import java.util.ArrayList;

/**
 *
 * @author Balushkin M.
 */
public class GetFieldNode extends NodeAST {
    VariableNode var;
    ArrayList<FieldNode> fields;
    boolean generateLast = true;
    public void generateLastNode(boolean generate) {
        generateLast = generate;
    }
    
    public GetFieldNode(VariableNode variable, ArrayList<FieldNode> nodes) {
        var = variable;
        fields = nodes;
        
        foo();
        type = getLastNode().type;
    }
    
    public FieldNode getLastNode() {
        return fields.get(fields.size() - 1);
    }
    
    private void foo() {
        String struct = var.strType();
        fields.get(0).setStructName(struct);
        
        for (int i = 1; i < fields.size(); ++i) {
            String type = StructTable.getFieldType(
                    fields.get(i - 1).structName, 
                    fields.get(i - 1).id
            );
            
//            String type = StructTable.getFieldType(field, fields.get(i).structName);
            fields.get(i).setStructName(type);
        }
    }

    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Get Field: ");
        var.debug(lvl + 1);
        fields.stream().forEach((f) -> {
            f.debug(lvl + 1);
        });
    }

    @Override
    public void codegen() {
        var.codegen();
        for (int i = 0; i < fields.size() - 1; ++i) {
            fields.get(i).codegen();
        }
        if (generateLast) {
            fields.get(fields.size() - 1).codegen();
        }
    }
}
