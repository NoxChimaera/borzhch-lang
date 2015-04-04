/*
 */
package edu.borzhch.ast;

import edu.borzhch.WaitingTable;
import edu.borzhch.codegen.java.JavaCodegen;
import org.apache.bcel.generic.GOTO;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class ContinueNode extends NodeAST {
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Continue");
    }

    @Override
    public void codegen() {
        GOTO go = JavaCodegen.method().go();
        WaitingTable.pushWaitingStart(go);
    }
    
}
