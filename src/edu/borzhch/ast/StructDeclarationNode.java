package edu.borzhch.ast;

import edu.borzhch.StructTable;
import edu.borzhch.codegen.java.JavaCodegen;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class StructDeclarationNode extends NodeAST {
    String identifier;
    FieldList fields;
    boolean isClass;
    
    public StructDeclarationNode(String identifier, FieldList statementList, boolean isClass) {
        this.identifier = identifier;
        fields = statementList;
        this.isClass = isClass;
        
        StructTable.addStruct(identifier);
        
        if (fields == null) return;
        fields.nodes.stream().map((field) -> (DeclarationNode) field).forEach((d) -> {
            StructTable.putField(identifier, d.getName(), d.varTypeName);
        });
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        String type = isClass? "Class " : "Struct ";
        System.out.println(type + identifier);
        ++lvl;
        if (null == fields) return;
        fields.debug(lvl);
    }

    @Override
    public void codegen() {
        JavaCodegen.newClass(identifier);
        JavaCodegen.switchClass(identifier);
        
        if (null != fields) {
            fields.codegen();
        }
        
        JavaCodegen.compileClass(identifier);
        JavaCodegen.switchClass("Program");
    }
}
