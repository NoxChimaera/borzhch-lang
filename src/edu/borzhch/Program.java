/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch;
import edu.borzhch.language.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import com.beust.jcommander.JCommander;
import edu.borzhch.ast.TreeAST;

/**
 *
 * @author Balushkin M.
 */
public class Program {
    public static OptParser config;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        config = new OptParser();
        new JCommander(config, args);
        
        FileReader r;
        try {
            Parser parser = new Parser(null, config.getDebugParser());
            for (String arg : config.getFiles()) {
                r = new FileReader(arg);
                parser.newLexer(r, arg);
                parser.run();
                        
                if(config.getDebugTree()) TreeAST.debug(0);
                if (!Parser.wasParseError()) {
                    TreeAST.codegen();
                }
            }
        } catch (FileNotFoundException fileNotFoundException){
            System.err.println(fileNotFoundException);
            System.out.println(fileNotFoundException);
        }
    }
}
