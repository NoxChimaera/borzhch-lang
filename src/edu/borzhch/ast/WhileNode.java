/*
 */
package edu.borzhch.ast;

import org.apache.bcel.generic.InstructionHandle;
import edu.borzhch.codegen.java.JavaCodegen;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class WhileNode extends NodeAST {
    NodeAST condition = null;
    StatementList codeblock = null;
    
    public WhileNode(NodeAST condition, StatementList codeblock) {
        this.condition = condition;
        this.codeblock = codeblock;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("While: ");
        
        ++lvl;
        condition.debug(lvl);
        codeblock.debug(lvl);
    }

    @Override
    public void codegen() {
        condition.codegen();
        
        InstructionHandle top = JavaCodegen.method();
        
        codeblock.codegen();
        
    }
    
}
