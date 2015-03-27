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

/**
 *
 * @author Balushkin M.
 */
public class Program {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        FileReader r = null;
        try {
            r = new FileReader(args[0]);
        } catch (FileNotFoundException fileNotFoundException){}
        
        Parser parser = new Parser(false);
        parser.run();
    }
    
}
