/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch;
import edu.borzhch.ast.*;
import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;
import edu.borzhch.language.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Balushkin M.
 */
public class Program {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        test();
    }
    
    public static void test() {
        NodeList list = new ProgramNode();
        FunctionNode function = new FunctionNode("main", BOType.VOID);
        StatementList stmts = function.getStatements();
        stmts.add(new DeclarationNode("res", BOType.INT));
        stmts.add(new AssignNode("res", new IntegerNode(42)));
        stmts.add(new ReturnNode(new VariableNode("res", BOType.INT)));
        
        list.add(function);
        
        TreeAST.setRoot(list);
        TreeAST.codegen();
        
//        System.out.println(JavaCodegen.foobar);
//        list.debug(0);
    }
}
