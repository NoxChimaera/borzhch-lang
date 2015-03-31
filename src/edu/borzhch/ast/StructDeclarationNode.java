/*
 */
package edu.borzhch.ast;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class StructDeclarationNode extends NodeAST {
    String identifier = null;
    StatementList statementList = null;
    
    public StructDeclarationNode(String identifier, StatementList statementList) {
        this.identifier = identifier;
        this.statementList = statementList;
    }
    
    @Override
    public void debug(int lvl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void codegen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
