/*
 */
package edu.borzhch.ast;

import com.sun.org.apache.bcel.internal.Constants;
import edu.borzhch.codegen.java.JavaCodegen;
import org.apache.bcel.generic.Type;
import edu.borzhch.language.Parser;
import edu.borzhch.FuncTable;
import edu.borzhch.constants.BOType;
import edu.borzhch.helpers.BOHelper;
import java.util.ArrayList;
import org.apache.bcel.generic.ArrayType;
import org.apache.bcel.generic.ObjectType;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class FunctionCallNode extends NodeAST implements INodeWithVarTypeName {
    String identifier;
    StatementList args;
    String varTypeName;
    String className;
    
    boolean popLast = false;
    
    public void popLast(boolean value) {
        this.popLast = value;
    }
    
    public boolean isProcedure() {
        return (type == BOType.VOID);
    }
    
    public FunctionCallNode(String identifier, StatementList args, String className) {
        this.identifier = identifier;
        this.args = null == args ? new StatementList() : args;
        this.className = className;
        
        this.type = BOHelper.getType(Parser.getFuncTable().getType(identifier));
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Funcall:");
        
        ++lvl;
        printLevel(lvl);
        System.out.println(String.format("Identifier: %s", identifier));
        printLevel(lvl);
        System.out.println("Arguments:");
        if(args != null) args.debug(lvl + 1);
    }
    
    @Override
    public void codegen() {
        if(args != null && !args.nodes.isEmpty()) args.codegen();
        
        FuncTable funcTable = Parser.getFuncTable();
        String funType = funcTable.getType(identifier);
        Type retType = BOHelper.getJVMRetType(funType);
        
        Type[] argTypes = new Type[funcTable.getArity(identifier)];
        ArrayList<String> args = funcTable.getParamTypes(identifier);
        int i = 0;
        for(String arg : args) {
            argTypes[i] = BOHelper.toJVMType(arg);
            i++;
        }
        
        if (className.equals("Program")) {
            JavaCodegen.method().funCall(className, identifier, retType, argTypes, Constants.INVOKESTATIC); 
        } else {
            JavaCodegen.method().funCall(className, identifier, retType, argTypes, Constants.INVOKEVIRTUAL); 
        }
        
        if (popLast) JavaCodegen.method().pop();
    }

    @Override
    public String getVarTypeName() {
        return Parser.getFuncTable().getType(identifier);
    }
    
    @Override
    public void setVarTypeName(String name) {
        varTypeName = name;
    }
}
