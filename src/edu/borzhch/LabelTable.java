/*
 */
package edu.borzhch;
import edu.borzhch.codegen.java.JavaCodegen;
import java.util.HashMap;
import java.util.ArrayList;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class LabelTable {
    ArrayList<HashMap<String, Integer>> labels = null;
    ArrayList<HashMap<String, Integer>> waitingStart = null;
    ArrayList<HashMap<String, Integer>> waitingEnd = null;
    
    //TODO: put(identifier, index)
    public void pushLabel(String identifier) {
        HashMap<String, Integer> label = new HashMap<>();
        label.put(identifier, -1);
        labels.add(label);
    }
    
    public void pushWaitingStart(String identifier) {
        HashMap<String, Integer> label = new HashMap<>();
        label.put(identifier, -1);
        waitingStart.add(label);
    }
    
    public void pushWaitingEnd(String identifier) {
        HashMap<String, Integer> label = new HashMap<>();
        label.put(identifier, -1);
        waitingEnd.add(label);
    }
    
    public boolean resolveWaiting(String identifier, Integer line) {
        boolean result = false;
        
        for (HashMap<String, Integer> waiting : waitingStart) {
            if(waiting.containsKey(identifier)) {
                waiting.put(identifier, line);
                labels.add(waiting);
                waitingStart.remove(waiting);
                result = true;
            }
        }
        
        for (HashMap<String, Integer> waiting : waitingEnd) {
            if(waiting.containsKey(identifier)) {
                waiting.put(identifier, line);
                labels.add(waiting);
                waitingEnd.remove(waiting);
                result = true;
            }
        }
        
        return result;
    }
    
    public Integer getLine(String identifier) {
        Integer result = -1;
        
        for (HashMap<String, Integer> waiting : labels) {
            if(waiting.containsKey(identifier)) {
                result = waiting.get(identifier);
                break;
            }
        }
        
        return result;
    }
}
