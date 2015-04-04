/*
 */
package edu.borzhch.ast;

import edu.borzhch.WaitingTable;
import edu.borzhch.codegen.java.JavaCodegen;
import java.util.ArrayList;
import org.apache.bcel.generic.InstructionHandle;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class SwitchNode extends NodeAST {
    NodeAST input;
    StatementList cases;
    CaseNode defaultCase;
    
    public SwitchNode(NodeAST input, StatementList cases, CaseNode defaultCase) {
        this.input = input;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Switch: ");
        
        ++lvl;
        input.debug(lvl);
        cases.debug(lvl);
        if(defaultCase != null) defaultCase.debug(lvl);
    }

    @Override
    public void codegen() {
        ArrayList<NodeAST> nodes = this.cases.nodes;
        int caseCount = nodes.size();
        InstructionHandle[] caseList = new InstructionHandle[caseCount];
        InstructionHandle defCase = null;
        int[] conds = new int[caseCount];
        
        int i = 0;
        for(NodeAST node : nodes) {
            CaseNode caseNode = (CaseNode) node;
            conds[i] = caseNode.condition;
            i++;
        }
        
        //switch head
        JavaCodegen.method().createSwitch(conds, caseList, defCase);
        //cases
        i = 0;
        for(NodeAST node : nodes) {
            CaseNode caseNode = (CaseNode) node;
            caseList[i] = caseNode.getPosition();
            caseNode.codegen();
            i++;
        }
        //default
        defCase = defaultCase.getPosition();
        defaultCase.codegen();
        
        //resolve cases breaks
        JavaCodegen.method().nop();
        InstructionHandle bottom = JavaCodegen.method().getLastHandler();
        WaitingTable.resolveWaitingEnd(bottom);
    }
}
