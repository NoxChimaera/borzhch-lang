/*
 */
package edu.borzhch.ast;

import edu.borzhch.WaitingTable;
import edu.borzhch.codegen.java.JavaCodegen;
import java.util.ArrayList;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NOP;
import org.apache.bcel.generic.SWITCH;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class SwitchNode extends NodeAST {
    NodeAST input;
    StatementList cases;
    StatementList defaultCase;
    
    public SwitchNode(NodeAST input, StatementList cases, StatementList defaultCase) {
        this.input = input;
        this.cases = cases;
        this.defaultCase = defaultCase;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Switch: ");
        
        ++lvl;
        printLevel(lvl);
        System.out.println("Input:");
        input.debug(lvl);
        if(cases != null) {
            printLevel(lvl);
            System.out.println("Case:");
            cases.debug(lvl);
        }
        if(defaultCase != null) {
            printLevel(lvl);
            System.out.println("Default:");
            defaultCase.debug(lvl);
        }
    }

    @Override
    public void codegen() {
        //switch key
        input.codegen();
        
        //Берём все ноды с кейсами
        ArrayList<NodeAST> nodes = this.cases == null? null : this.cases.nodes;
        
        //Считаем их
        int caseCount = 0;
        if(nodes != null) {
            caseCount = nodes.size();
        }
        
        //Инициализируем переменные для создания SWITCH
        int[] conds = new int[caseCount];
        InstructionHandle[] caseList = new InstructionHandle[caseCount];
        InstructionHandle defCase = null;
        
        //cases
        if(nodes != null) {
            int end = nodes.size();
            for(int i = 0; i < end; i++) {
                CaseNode caseNode = (CaseNode) nodes.get(i);
                conds[i] = caseNode.condition;
                NOP nop = JavaCodegen.method().nop();
                caseList[i] = JavaCodegen.method().getLastHandler();
                caseNode.codegen();
            }
        }
        
        //default
        if(defaultCase != null) {
            NOP nop = JavaCodegen.method().nop();
            defCase = JavaCodegen.method().getLastHandler();
            defaultCase.codegen();
        }
        
        //switch head
        JavaCodegen.method().createSwitch(conds, caseList, defCase);
        
        //resolve cases breaks
        JavaCodegen.method().nop();
        InstructionHandle bottom = JavaCodegen.method().getLastHandler();
        WaitingTable.resolveWaitingEnd(bottom);
    }
}
