/*
 */
package edu.borzhch.ast;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class DoWhileNode extends WhileNode {
    public DoWhileNode(NodeAST condition, StatementList codeblock) {
        super(condition, codeblock);
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Do while: ");
        
        ++lvl;
        codeblock.debug(lvl);
        condition.debug(lvl);
    }
}
