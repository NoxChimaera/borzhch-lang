/*
 */
package edu.borzhch;

import java.util.ArrayList;
import edu.borzhch.ast.FunctionNode;
import edu.borzhch.ast.NodeAST;
import edu.borzhch.helpers.BOHelper;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class FuncTable {
    private ArrayList<FunctionNode> functions = null;
    
    public FuncTable() {
        functions = new ArrayList<>();
    }
    
    public void push(FunctionNode function) {
        functions.add(function);
    }
    
    public boolean find(String identifier) {
        boolean result= false;
        
        for(FunctionNode function : functions) {
            if(function.getFuncName().equals(identifier)) {
                result = true;
                break;
            }
        }
        
        return result;
    }
    
    public String getType(String identifier) {
        String result = null;
        
        for(FunctionNode function : functions) {
            if(function.getFuncName().equals(identifier)) {
                result = function.getReturnTypeName();
                break;
            }
        }
        
        return result;
    }
    
    public Integer getArity(String identifier) {
        Integer result = null;
        
        for(FunctionNode function : functions) {
            if(function.getFuncName().equals(identifier)) {
                result = function.getArgumentsCount();
                break;
            }
        }
        
        return result;
    }
    
    public ArrayList<String> getParamTypes(String identifier) {
        ArrayList<String> result = null;
        
        for(FunctionNode function : functions) {
            if(function.getFuncName().equals(identifier)) {
                result = new ArrayList<>();
                ArrayList<NodeAST> arguments = function.getArguments();
                for(NodeAST argument : arguments) {
                    result.add(BOHelper.toString(argument.type()));
                }
                
                break;
            }
        }
        
        return result;
    }
}
