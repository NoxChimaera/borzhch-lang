package edu.borzhch.ast;

import edu.borzhch.StructTable;
import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;

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
        
        StructTable.addStruct(identifier);
        
        if (fields == null) return;
        for (NodeAST node : fields.nodes) {
            DeclarationNode n = (DeclarationNode) node;
            if (BOType.ARRAY == n.type) {
                StructTable.putField(identifier, n.getName(), "$array");
                StructTable.putFieldSub(identifier, n.getName(), n.varTypeName);
            } else {
                StructTable.putField(identifier, n.getName(), n.varTypeName);
            }
        }
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Struct " + identifier);
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
