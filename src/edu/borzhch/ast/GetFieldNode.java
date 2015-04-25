/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.StructTable;
import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.helpers.BOHelper;
import java.util.ArrayList;
import org.apache.bcel.generic.Type;

/**
 *
 * @author Balushkin M.
 */
public class GetFieldNode extends NodeAST {
    private boolean generateLast = true;
    public void generateLast(boolean val) {
        generateLast = val;
    }
    
    ArrayList<NodeAST> fields;
    public GetFieldNode(ArrayList<NodeAST> nodes){
        fields = nodes;
    }
    
    public NodeAST getLast() {
        return fields.get(fields.size() - 1);
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Get Field: ");
        fields.stream().forEach((f) -> {
            f.debug(lvl + 1);
        });
    }

    public String schema;
    private void load(NodeAST node) {
        if (ArrayElementNode.class == node.getClass()) {
            GetArrayNode getItem = new GetArrayNode((ArrayElementNode) node);
            getItem.codegen();
            schema = getItem.arrayRef.ref.strType();
            type = getItem.arrayRef.ref.type;
        }
    }
    private void getField(NodeAST node) {
        if (VariableNode.class == node.getClass()) {
            VariableNode field = (VariableNode) node;
            switch (field.type) {
                case REF:
                    JavaCodegen.method().getFieldClass(schema, field.id, 
                            StructTable.getFieldType(schema, field.id));
                    schema = field.strType();
                    type = field.type;
                    break;
                default:
                    JavaCodegen.method().getField(schema, field.id, 
                            BOHelper.toJVMType(field.type));
                    type = field.type;
                    break;
            }
        }
    }
    
    @Override
    public void codegen() {
        load(fields.get(0));
        
        int last = generateLast ? fields.size() : fields.size() - 1;
        for (int i = 1; i < last; ++i) {
            getField(fields.get(i));
        }
    }
}
