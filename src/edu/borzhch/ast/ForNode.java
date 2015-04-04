/*
 */
package edu.borzhch.ast;

import edu.borzhch.WaitingTable;
import edu.borzhch.codegen.java.JavaCodegen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IfInstruction;
import org.apache.bcel.generic.InstructionHandle;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class ForNode extends NodeAST {
    NodeAST declaration = null;
    NodeAST condition;
    NodeAST operation;
    NodeAST codeblock;
    
    public ForNode(NodeAST declaration, 
                    NodeAST condition,
                    NodeAST operation,
                    NodeAST codeblock) {
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
        printLevel(lvl);
        System.out.println("Iterator: ");
        declaration.debug(lvl + 1);
        
        printLevel(lvl);
        System.out.println("End condition: ");
        condition.debug(lvl + 1);
                
        printLevel(lvl);
        System.out.println("Counter: ");
        operation.debug(lvl + 1);
                
        printLevel(lvl);
        System.out.println("Statement list: ");
        codeblock.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        //0: decl
        declaration.codegen();
        
        //1: label top
        JavaCodegen.method().nop();
        InstructionHandle top = JavaCodegen.method().getLastHandler();
        
        //2: cond
        condition.codegen();
        
        //3: ifne goto bottom
        IfInstruction ifeq = JavaCodegen.method().zcmp("eq");
        //nop
        
        //4: codeblock
        codeblock.codegen();
        WaitingTable.resolveWaitingStart(top);
                
        //5: operation
        operation.codegen();
        
        //6: goto top
        GOTO go = JavaCodegen.method().go();
        go.setTarget(top);
        
        //7: label bottom
        JavaCodegen.method().nop();
        InstructionHandle bottom = JavaCodegen.method().getLastHandler();
        ifeq.setTarget(bottom);
        WaitingTable.resolveWaitingEnd(bottom);
    }
    
}
