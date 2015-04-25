/*
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;
import org.apache.bcel.generic.InstructionHandle;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class CaseNode extends NodeAST {
    Integer condition;
    StatementList body;
    
    public CaseNode(Integer condition, StatementList body) {
        this.condition = condition;
        this.body = body;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Case:");
        
        ++lvl;
        printLevel(lvl);
        System.out.println(String.format("Condition: %d", condition));
        if(body != null) {
            printLevel(lvl);
            System.out.println("Body:");
            body.debug(lvl);
        }
    }
    
    @Override
    public void codegen() {
        if(body != null) body.codegen();
    }
}
