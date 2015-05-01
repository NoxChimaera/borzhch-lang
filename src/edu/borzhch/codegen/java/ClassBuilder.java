/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.codegen.java;

import edu.borzhch.constants.BOType;
import java.util.HashMap;
import static org.apache.bcel.Constants.*;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.FieldGen;
import org.apache.bcel.generic.Type;

/**
 * Конструктор классов
 * @author Balushkin M.
 */
public class ClassBuilder {
    String name;
    ClassGen cg;
    ConstantPoolGen cp;

    HashMap<String, MethodBuilder> methods;
    
    /**
     * @param className Имя класса
     */
    public ClassBuilder(String className) {
        name = className;
        cg = new ClassGen(className, "java.lang.Object",
            "<generated>", ACC_PUBLIC | ACC_SUPER, null);
        cp = cg.getConstantPool();
        
        methods = new HashMap<>();
    }
    
    /**
     * Скомпилировать класс
     */
    public void compile() {
        cg.addEmptyConstructor(ACC_PUBLIC);
        // This is test feature
        try {
            cg.getJavaClass().dump("jarbuild\\" + name + ".class");
        } catch (java.io.IOException e) {
            System.err.println(e);
        }
    }
    
    /**
     * Возвращает имя класса.
     * @return Имя класса.
     */
    public String getName() {
        return name;
    }
 
    public void addField(String name, Type type) {
        FieldGen fg = new FieldGen(ACC_PUBLIC, type, name, cp);
        cg.addField(fg.getField());
    }
    
    /**
     * Проверяет, есть ли у текущего класса поле с указанным идентификатором.
     * @param name Идентификатор поля.
     * @return true, если поле есть в классе; иначе - false.
     */
    public boolean hasField(String name) {
        return cg.containsField(name) != null;
    }
    
    /**
     * Создать новый метод
     * @param name Имя метода
     * @param retType Возвращаемый тип
     * @return MethodGen созданного метода
     */
    public MethodBuilder newMethod(String name, Type retType, Type[] argsTypes, String[] argsNames) {
        MethodBuilder mb = new MethodBuilder(name, retType, argsTypes, argsNames, cg);
        methods.put(name, mb);
        return mb;
    }
    /**
     * Скомпилировать метод
     * @param name Имя метода
     */
    public void compileMethod(String name) {
        methods.get(name).compile();
    }
}
