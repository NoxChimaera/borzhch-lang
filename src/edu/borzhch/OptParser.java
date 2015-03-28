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
 
//    @Parameter(names = { "-log", "-verbose" }, description = "Level of verbosity")
//    private Integer verbose = 1;
//
//    @Parameter(names = "-groups", description = "Comma-separated list of group names to be run")
//    private String groups;
    
    @Parameter(names = "-dparser", description = "Parser debug mode")
    private boolean debugParser = false;
    
    public List<String> getFiles() {
        return this.files;
    }
    public boolean getDebugParser() {
        return this.debugParser;
    }
}
