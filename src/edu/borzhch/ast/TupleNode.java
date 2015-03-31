/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

/**
 *
 * @author Balushkin M.
 */
public class TupleNode extends NodeAST {
    StatementList statementList = null;    
    
    public TupleNode(StatementList statementList) {
        this.statementList = statementList;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Tuple:");
        
        ++lvl;
        statementList.debug(lvl);
    }

    @Override
    public void codegen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
