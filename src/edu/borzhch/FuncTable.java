/*
 */
package edu.borzhch;

import edu.borzhch.ast.DeclarationNode;
import java.util.ArrayList;
import edu.borzhch.ast.FunctionNode;
import edu.borzhch.ast.NodeAST;
import edu.borzhch.helpers.BOHelper;
import java.util.Optional;
import java.util.stream.Stream;

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
    
//    public String getArrayType(String identifier) {
//        String result = null;
//        for (FunctionNode f : functions) {
//            if (f.getFuncName().equals(identifier)) {
//                result = f.
//            }
//        }
//    }
    
    public Integer getArity(String identifier) {
        Integer result = null;
        
        for (FunctionNode function : functions) {
            if (function.getFuncName().equals(identifier)) {
                result = function.getArgumentsCount();
                break;
            }
        }
        if(result == null && previous != null) {
            result = previous.getArity(identifier);
        }
        
        return result;
    }
    
    public String getParamArrayType(String func, String argName) {
        Optional<FunctionNode> function = functions.stream().filter((FunctionNode f) -> f.getFuncName().equals(func)).findFirst();
        if (!function.isPresent()) {
            return null;
        }
        ArrayList<NodeAST> arguments = function.get().getArguments();
        for (NodeAST arg : arguments) {
            DeclarationNode node = (DeclarationNode) arg;
            if (node.getName().equals(argName)) {
//                node.
            }
        }
        return null;
    }
    
    public ArrayList<String> getParamTypes(String identifier) {
        ArrayList<String> result = new ArrayList<String>();
        
        for (FunctionNode function : functions) {
            if (function.getFuncName().equals(identifier)) {
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
