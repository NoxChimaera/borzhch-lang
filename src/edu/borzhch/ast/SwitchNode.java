/*
 */
package edu.borzhch.ast;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class SwitchNode extends NodeAST {
    NodeAST input = null;
    StatementList body = null;
    StatementList defaultCase = null;
    
    public SwitchNode(NodeAST input, StatementList body, StatementList defaultCase) {
        this.input = input;
        this.body = body;
        this.defaultCase = defaultCase;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Switch: ");
        
        ++lvl;
        input.debug(lvl);
        body.debug(lvl);
        if(defaultCase != null) defaultCase.debug(lvl);
    }

    @Override
    public void codegen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
