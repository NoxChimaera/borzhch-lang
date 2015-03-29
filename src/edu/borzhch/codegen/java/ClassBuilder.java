/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.codegen.java;

import edu.borzhch.constants.BOType;
import java.util.HashMap;
import static org.apache.bcel.Constants.*;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;

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
            cg.getJavaClass().dump(name + ".class");
        } catch (java.io.IOException e) {
            System.err.println(e);
        }
    }
    
    /**
     * Создать новый метод
     * @param name Имя метода
     * @param retType Возвращаемый тип
     * @return MethodGen созданного метода
     */
    public MethodBuilder newMethod(String name, BOType retType) {
        MethodBuilder mb = new MethodBuilder(name, retType, cg);
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
