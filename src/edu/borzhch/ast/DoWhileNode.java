/*
 */
package edu.borzhch.ast;

import edu.borzhch.WaitingTable;
import edu.borzhch.codegen.java.JavaCodegen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.InstructionHandle;

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
    
    @Override
    public void codegen() {
        //0: label top
        InstructionHandle top = JavaCodegen.method().getLastHandler();
        
        //1: codeblock        
        codeblock.codegen();
        WaitingTable.resolveWaitingStart(top);
        
        //2: cond
        condition.codegen();
        
        //3: ifeq goto label top
        IFEQ ifeq = JavaCodegen.method().ifeq();
        ifeq.setTarget(top);
        
        //4: label bottom
        JavaCodegen.method().nop();
        InstructionHandle bottom = JavaCodegen.method().getLastHandler();
        WaitingTable.resolveWaitingEnd(bottom);
    }
}
