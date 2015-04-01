/*
 */
package edu.borzhch.ast;

import edu.borzhch.WaitingTable;
import edu.borzhch.codegen.java.JavaCodegen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFNE;
import org.apache.bcel.generic.InstructionHandle;

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
        //0: decl
        declaration.codegen();
        
        //1: label top
        JavaCodegen.method().nop();
        InstructionHandle top = JavaCodegen.method().getLastHandler();
        
        //2: cond
        condition.codegen();
        
        //3: ifne goto Label2
        IFNE ifne = JavaCodegen.method().ifne();
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
        ifne.setTarget(bottom);
        WaitingTable.resolveWaitingEnd(bottom);
    }
    
}
