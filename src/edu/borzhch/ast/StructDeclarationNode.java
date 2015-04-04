package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class StructDeclarationNode extends NodeAST {
    String identifier;
    FieldList fields;
    
    public StructDeclarationNode(String identifier, FieldList statementList) {
        this.identifier = identifier;
        fields = statementList;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Struct " + identifier);
        ++lvl;
        fields.debug(lvl);
    }

    @Override
    public void codegen() {
        JavaCodegen.newClass(identifier);
        JavaCodegen.switchClass(identifier);
        
        fields.codegen();
        
        JavaCodegen.compileClass(identifier);
        JavaCodegen.switchClass("Program");
    }
}
