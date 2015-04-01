/*
 */
package edu.borzhch.ast;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class ForNode extends NodeAST {
    DeclarationNode declaration = null;
    NodeAST condition;
    NodeAST operation;
    StatementList codeblock;
    
    public ForNode(DeclarationNode declaration, 
                    NodeAST condition,
                    NodeAST operation,
                    StatementList codeblock) {
        this.declaration = declaration;
        this.condition = condition;
        this.operation = operation;
        this.codeblock = codeblock;
    }

    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("For: ");
        
        ++lvl;
        System.out.println("Decl: ");
        declaration.debug(lvl + 1);
        
        System.out.println("Cond: ");
        condition.debug(lvl + 1);
        
        System.out.println("Oper: ");
        operation.debug(lvl + 1);
        
        System.out.println("Block: ");
        codeblock.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
