/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.codegen.java;

import edu.borzhch.constants.BOType;
import edu.borzhch.helpers.BOHelper;
import java.util.HashMap;
import org.apache.bcel.generic.*;
import static org.apache.bcel.Constants.*;

/**
 * Кодогенератор Java Bytecode
 * @author Balushkin M.
 */
public class JavaCodegen {
    static HashMap<String, ClassBuilder> classes = new HashMap<>();
    static ClassBuilder programClass;

    static MethodBuilder currentMethod;
    /**
     * @return Текущий метод
     */
    public static MethodBuilder method() {
        return currentMethod;
    }
    
    /**
     * Создаёт новый класс
     * @param name Имя класса
     */
    public static void newClass(String name) {
        ClassBuilder cb = new ClassBuilder(name);
        classes.put(name, cb);
    }
    /**
     * Устанавливает запеускаемый класс
     * @param name 
     */
    public static void setMainClass(String name) {
        programClass = classes.get(name);
    }
    /**
     * Компилирует класс
     * @param name Имя класса
     */
    public static void compileClass(String name) {
        classes.get(name).compile();
    }
    
    /**
     * Создаёт метод
     * @param name Имя метода
     * @param returnType Возвращаемый тип
     * @param className Имя класса
     */
    public static void newMethod(String name, BOType returnType, String className) {
        currentMethod = classes.get(className).newMethod(name, returnType);
    }
    /**
     * Компилирует метод
     * @param className Имя класса
     * @param func Имя метода
     */
    public static void compileMethod(String className, String func) {
        classes.get(className).compileMethod(func);
    }
}