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
    
    private FuncTable previous;
    
    public FuncTable() {
        createFuncArray();
    }
    
    public FuncTable(FuncTable previous) {
        createFuncArray();
        this.previous = previous;
    }
    
    public void push(FunctionNode function) {
        functions.add(function);
    }
    
    /**
     * Проверяет наличие идентификатора функции в текущей и предыдущей таблицах.
     * @param identifier Идентификатор функции.
     * @return true, если идентификатор найден; иначе - false.
     */
    public boolean find(String identifier) {
        boolean result= false;
        
        for(FunctionNode function : functions) {
            if(function.getFuncName().equals(identifier)) {
                result = true;
                break;
            }
        }
        if(!result && previous != null) {
            result = previous.find(identifier);
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
        if(result == null && previous != null) {
            result = previous.getArity(identifier);
        }
        
        return result;
    }
    
    public ArrayList<String> getParamTypes(String identifier) {
        ArrayList<String> result = null;
        
        for(FunctionNode function : functions) {
            if(function.getFuncName().equals(identifier)) {
                result = new ArrayList<>();
                ArrayList<NodeAST> arguments = function.getArguments();
                if (arguments != null) {
                    for(NodeAST argument : arguments) {
                        result.add(BOHelper.toString(argument.type()));
                    }
                }
                
                break;
            }
        }
        if(result == null && previous != null) {
            result = previous.getParamTypes(identifier);
        }
        
        return result;
    }
    
    private void createFuncArray() {
        functions = new ArrayList<>();
    }
}
