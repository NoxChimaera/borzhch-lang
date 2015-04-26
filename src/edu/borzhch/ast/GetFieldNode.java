/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.StructTable;
import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.helpers.BOHelper;
import java.util.ArrayList;
import org.apache.bcel.generic.Type;

/**
 * Получение поля объекта
 * @author Balushkin M.
 */
public class GetFieldNode extends NodeAST {
    private boolean generateLast = true;
    public void generateLast(boolean val) {
        generateLast = val;
    }
    
    /**
     * Класс объекта (зачем-то)
     */
    public String schema;
    ArrayList<NodeAST> fields;
    /**
     * Получение поля объекта
     * @param nodes Последовательность операндов (foo . bar . baz)
     */
    public GetFieldNode(ArrayList<NodeAST> nodes){
        fields = nodes;
        get(false);
    }
    
    /**
     * Возвращает последний узел
     * @return Последний узел
     */
    public NodeAST getLast() {
        return fields.get(fields.size() - 1);
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Get Field: ");
        fields.stream().forEach((f) -> {
            f.debug(lvl + 1);
        });
    }

    /**
     * Загружает значение (вычисляет)
     * @param node Узел
     * @param genCode Генерировать код?
     */
    private void load(NodeAST node, boolean genCode) {
        if (ArrayElementNode.class == node.getClass()) {
            GetArrayNode getItem = new GetArrayNode((ArrayElementNode) node);
            if (genCode) {
                getItem.codegen();
            }
            schema = getItem.arrayRef.ref.strType();
            type = getItem.arrayRef.ref.type;
        }
    }
    /**
     * Получает поле объекта
     * @param node Узел
     * @param genCode Генерировать код?
     */
    private void getField(NodeAST node, boolean genCode) {
        if (VariableNode.class == node.getClass()) {
            VariableNode field = (VariableNode) node;
            switch (field.type) {
                case REF:
                    
                    if (genCode)
                        JavaCodegen.method().getFieldClass(schema, field.id, 
                                StructTable.getFieldType(schema, field.id));
                    schema = field.strType();
                    type = field.type;
                    break;
                default:
                    if (genCode)
                        JavaCodegen.method().getField(schema, field.id, 
                                BOHelper.toJVMType(field.type));
                    type = field.type;
                    break;
            }
        }
    }
    
    /**
     * Вывод типа узла
     * @param generateCode Генерировать код?
     */
    private void get(boolean generateCode) {
        load(fields.get(0), generateCode);
        
        int last = generateLast ? fields.size() : fields.size() - 1;
        for (int i = 1; i < last; ++i) {
            getField(fields.get(i), generateCode);
        }
    }
    
    @Override
    public void codegen() {
        get(true);
    }
}
