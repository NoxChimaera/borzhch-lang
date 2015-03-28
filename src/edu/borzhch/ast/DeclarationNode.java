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
public class DeclarationNode extends NodeAST {
    BOType varType;
    String varTypeName;
    String varName;
    
    public DeclarationNode(String name, BOType type) {
        varName = name;
        varType = type;
        varTypeName = BOHelper.toString(type);
    }
    
    public DeclarationNode(String name, String typeName) {
        varName = name;
        varType = BOType.REF;
        varTypeName = typeName;
    }

    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Declaration: ");
        
        ++lvl;
        printLevel(lvl);
        System.out.println("Variable: " + varName);
        printLevel(lvl);
        System.out.println("Type: " + varTypeName + " (" 
                + BOHelper.toString(varType) + ")");
    }
}
