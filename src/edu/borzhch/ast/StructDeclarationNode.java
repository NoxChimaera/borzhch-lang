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
    FieldList functions;
    boolean isClass;
    
    public StructDeclarationNode(String identifier, FieldList fieldList, boolean isClass) {
        this.identifier = identifier;
        this.isClass = isClass;
        
        try {
            dissectFieldList(fieldList);
        } catch (Exception e) { }
        
        StructTable.addStruct(identifier);
    }
    
    /**
     * Разбирает список полей на поля и функции и помещает их в соответствующие
     * переменные этого класса. Бросает исключение, если вдруг ему попалось 
     * нечто не типа DeclNode или FuncNode. Ничего не делает, если список полей
     * пуст.
     * @param fieldList Список полей.
     * TODO: Можно упростить функцию, если сделать получение имени и типа в
     * DeclNode и FuncNode одинаковыми.
     */
    private void dissectFieldList(FieldList fieldList) throws Exception {
        if (fields == null) return;
        FieldList newFields = new FieldList();
        FieldList newFunctions = new FieldList();
        for(NodeAST node : fieldList.nodes) {
            if (node instanceof DeclarationNode) {
                DeclarationNode declNode = (DeclarationNode) node;
                newFields.nodes.add(declNode);
                if (BOType.ARRAY == declNode.type) {
                    StructTable.putField(identifier, declNode.getName(), "$array");
                    StructTable.putFieldSub(identifier, declNode.getName(), declNode.varTypeName);
                } else {
                    StructTable.putField(identifier, declNode.getName(), declNode.varTypeName);
                }
            } else if(node instanceof FunctionNode) {
                FunctionNode funcNode = (FunctionNode) node;
                newFunctions.nodes.add(funcNode);
                StructTable.putField(identifier, funcNode.getFuncName(), funcNode.getReturnTypeName());
            } else {
                String msg = String.format("Unknown type for class/struct field.");
                throw new Exception(msg);
            }
        }
        this.fields = newFields;
        this.functions = newFunctions;
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
