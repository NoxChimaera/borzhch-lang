/*
 */
package edu.borzhch;
import java.util.*;
import com.beust.jcommander.Parameter;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class OptParser {
    @Parameter(required = true, description = "Files")
    private List<String> files = new ArrayList<String>();
    
    @Parameter(names = "-dparser", description = "Parser debug mode")
    private boolean debugParser = false;
    
    @Parameter(names = "-dtree", description = "Tree debug mode")
    private boolean debugTree = false;
    
    @Parameter(names = "-folding", description = "Optimization: constant folding")
    private boolean constantFolding = false;
    
    @Parameter(names = "-reduction", description = "Optimization: strength reduction")
    private boolean strengthReduction = false;
    
    public List<String> getFiles() {
        return this.files;
    }
    public boolean getDebugParser() {
        return this.debugParser;
    }
    public boolean getDebugTree() {
        return this.debugTree;
    }
    public boolean getConstantFolding() {
        return this.constantFolding;
    }
    public boolean getStrengthReduction() {
        return strengthReduction;
    }
}
