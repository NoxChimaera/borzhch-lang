/*
 */
package edu.borzhch.ast;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class IfNode extends NodeAST {
    NodeAST condition = null;
    StatementList statementList = null;
    IfNode elseNode = null;
    
    public IfNode(NodeAST condition, StatementList statementList, IfNode elseNode) {
        this.condition = condition;
        this.statementList = statementList;
        this.elseNode = elseNode;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("If: ");
        
        ++lvl;
        if(condition != null) condition.debug(lvl);
        
        statementList.debug(lvl);
        
        if(elseNode != null) elseNode.debug(lvl);
    }

    @Override
    public void codegen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
