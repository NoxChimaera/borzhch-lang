/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.helpers.BOHelper;
import org.apache.bcel.generic.Type;

/**
 * Печать на stdout
 * @author Balushkin M.
 */
public class PrintNode extends NodeAST {
    /**
     * Выражение
     */
    NodeAST node;
    /**
     * Печать на стандартный вывод
     * @param node Выражение для печати
     */
    public PrintNode(NodeAST node) {
        this.node = node;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Print: ");
        
        ++lvl;
        node.debug(lvl);
    }

    @Override
    public void codegen() {
        JavaCodegen.method().getStdout();
        node.codegen();
        JavaCodegen.method().printLine(BOHelper.toJVMType(node.type));
    }
}
