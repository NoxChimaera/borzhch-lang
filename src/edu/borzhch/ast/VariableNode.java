/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;
import edu.borzhch.helpers.BOHelper;

/**
 * Узел переменной
 * @author Balushkin M.
 */
public class VariableNode extends NodeAST implements INodeWithVarTypeName {
    String id;
    public String id() {
        return id;
    }
    // ???
    String varTypeName;
    public String strType() {
        return varTypeName;
    }
    public void strType(String newType) {
        varTypeName = newType;
    }
    
    // test
    public VariableNode(String identifier) {
        id = identifier;
        type = BOType.VOID;
        varTypeName = "void";
    }
    
    public VariableNode(String identifier, BOType type) {
        id = identifier;
        this.type = type;
        varTypeName = BOHelper.toString(type);
    }
    
    public VariableNode(String identifier, String typeName) {
        id = identifier;
        varTypeName = typeName;
        
        if ("$array".equals(typeName)) {
            type = BOType.ARRAY;
        } else if (BOHelper.isType(typeName)) {
            type = BOHelper.getType(typeName);
        } else {
            type = BOType.REF;
        }
    }
    
    public int getIndex() {
        return JavaCodegen.method().getVariableIndex(id);
    }

    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Variable: " + id);
    }

    @Override
    public void codegen() {
        // iload var_index
        JavaCodegen.method().load(id, BOHelper.toJVMType(type));
    }

    @Override
    public String getVarTypeName() {
        return varTypeName;
    }
}
