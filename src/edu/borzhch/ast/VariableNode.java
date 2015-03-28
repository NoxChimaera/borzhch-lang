/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.constants.BOType;
import edu.borzhch.helpers.BOHelper;

/**
 *
 * @author Balushkin M.
 */
public class VariableNode extends NodeAST {
    String id;
    BOType varType;
    String varTypeName;
    
    public VariableNode(String identifier, BOType type) {
        id = identifier;
        varType = type;
        varTypeName = BOHelper.toString(type);
    }
    
    public VariableNode(String identifier, String typeName) {
        id = identifier;
        varType = BOType.REF;
        varTypeName = typeName;
    }

    @Override
    public void debug(int lvl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}