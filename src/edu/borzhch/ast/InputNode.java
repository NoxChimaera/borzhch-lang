/*
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class InputNode extends NodeAST {
    public InputNode() {
        type = BOType.STRING;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Input: ");
    }

    @Override
    public void codegen() {
        JavaCodegen.method().getStdin();
        JavaCodegen.method().getLine();
    }
    
}
