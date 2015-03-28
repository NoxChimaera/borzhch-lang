/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch;
import edu.borzhch.language.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import com.beust.jcommander.JCommander;

/**
 *
 * @author Balushkin M.
 */
public class Program {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        OptParser op = new OptParser();
        new JCommander(op, args);
        
        FileReader r = null;
        try {
            for (String arg : op.getFiles()) {
                r = new FileReader(arg);
                Parser parser = new Parser(r, op.getDebugParser());
                parser.run();
            }
        } catch (FileNotFoundException fileNotFoundException){}
    }
}
