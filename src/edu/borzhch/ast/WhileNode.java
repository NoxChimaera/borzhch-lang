/*
 */
package edu.borzhch.ast;

import edu.borzhch.WaitingTable;
import org.apache.bcel.generic.InstructionHandle;
import edu.borzhch.codegen.java.JavaCodegen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFNE;

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
        //0: label top
        InstructionHandle top = JavaCodegen.method().getLastHandler();
        
        //1: cond
        condition.codegen();
        
        //2: ifn goto label end
        IFNE ifne = JavaCodegen.method().ifne();
        
        //3: codeblock        
        codeblock.codegen();
        WaitingTable.resolveWaitingStart(top);
        
        //4: jmp Label1
        GOTO goTop = JavaCodegen.method().go();
        goTop.setTarget(top);
        
        //5: label dno
        JavaCodegen.method().nop();
        InstructionHandle bottom = JavaCodegen.method().getLastHandler();
        ifne.setTarget(bottom);
        WaitingTable.resolveWaitingEnd(bottom);
    }
    
}
