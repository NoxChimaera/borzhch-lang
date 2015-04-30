/*
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;
import org.apache.bcel.generic.Type;
import edu.borzhch.language.Parser;
import edu.borzhch.FuncTable;
import edu.borzhch.constants.BOType;
import edu.borzhch.helpers.BOHelper;
import java.util.ArrayList;
import org.apache.bcel.generic.ArrayType;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class FunctionCallNode extends NodeAST implements INodeWithVarTypeName {
    String identifier;
    StatementList args;
    
    public FunctionCallNode(String identifier, StatementList args) {
        this.identifier = identifier;
        this.args = null == args ? new StatementList() : args;
        
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
        if (null == args) return;
        args.debug(lvl + 1);
    }

    public void foo() {
        if (!args.nodes.isEmpty()) args.codegen();
        
        
    }
    
    @Override
    public void codegen() {
        if(!args.nodes.isEmpty()) args.codegen();
        
        FuncTable funcTable = Parser.getFuncTable();
        Type retType = BOHelper.toJVMType(BOHelper.getType(funcTable.getType(identifier)));
        Type[] argTypes = new Type[funcTable.getArity(identifier)];
        ArrayList<String> args = funcTable.getParamTypes(identifier);
        int i = 0;
        for(String arg : args) {
            argTypes[i] = BOHelper.toJVMType(BOHelper.getType(arg));
            i++;
        }
        //TODO: first argument is a class
        JavaCodegen.method().funCall("Program", identifier, retType, argTypes); 
    }

    @Override
    public String getVarTypeName() {
        return Parser.getFuncTable().getType(identifier);
    }
}
