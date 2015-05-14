/*
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFNE;
import org.apache.bcel.generic.InstructionHandle;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class IfNode extends NodeAST {
    NodeAST condition = null;
    StatementList statementList = null;
//    IfNode elseNode = null;
    NodeAST elseBranch;
    
    
    public IfNode(NodeAST condition, StatementList statementList, NodeAST elseNode) {
        this.condition = condition;
        this.statementList = statementList;
        
        elseBranch = elseNode;
//        this.elseNode = elseNode;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("If: ");
        
        ++lvl;
        if(condition != null) condition.debug(lvl);
        
        statementList.debug(lvl);
        
        if(elseBranch != null) elseBranch.debug(lvl);
    }

    @Override
    public void codegen() {
        // 0 or 1 on stack
        condition.codegen();
        // 0: ifne 3 // to else
        IFEQ ifeq = JavaCodegen.method().ifeq();
        
        // 1: if-branch
        statementList.codegen();
        // 2: goto 5 // get off statement
        GOTO go = JavaCodegen.method().go();
        // 3: nop-nop-nop
        JavaCodegen.method().nop();
        ifeq.setTarget(JavaCodegen.method().getLastHandler());
        // 4: el-branch
        if (elseBranch != null) {
            elseBranch.codegen();
        }
        // 5: magic crutchy mega super nop
        JavaCodegen.method().nop();
        go.setTarget(JavaCodegen.method().getLastHandler());
    }
    
}
