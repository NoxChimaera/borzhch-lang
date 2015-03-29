/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.codegen.java;

import edu.borzhch.constants.BOType;
import edu.borzhch.helpers.BOHelper;
import java.util.HashMap;
import static org.apache.bcel.Constants.*;
import org.apache.bcel.generic.*;

/**
 * Конструктор методов
 * @author Balushkin M.
 */
public class MethodBuilder {
    String name;
    MethodGen mg;
    InstructionList il;
    InstructionFactory f;
    ClassGen cg;
    
    HashMap<String, LocalVariableGen> localVariables;
    
    public MethodBuilder(String name, BOType returnType, ClassGen cg) {
        il = new InstructionList();
        this.cg = cg;
        mg = new MethodGen(ACC_STATIC | ACC_PUBLIC, 
                BOHelper.toJVMType(returnType), 
                new Type[] { new ArrayType(Type.STRING, 1) },
                new String[] { "argv" }, name, cg.getClassName(),
                il, cg.getConstantPool()
        );
        f = new InstructionFactory(cg);
        localVariables = new HashMap<>();
    }
    
    /**
     * Добавляет локальную переменную
     * @param id Идентификатор переменной
     * @param type Тип переменной
     */
    public void addLocalVariable(String id, BOType type) {
        LocalVariableGen lg = mg.addLocalVariable(id, BOHelper.toJVMType(type), 
                null, null);
        localVariables.put(id, lg);
        
//    public static void istore(String var) {
//        lg = mg.addLocalVariable(var, Type.INT, null, null);
//        int local = lg.getIndex();
//        lg.setStart(il.append(new ISTORE(local)));
//    }
    }
    
    /**
     * Сохраняет значение в переменную
     * @param name Имя переменной
     * @see ISTORE
     */
    public void store(String name) {
        LocalVariableGen lg = localVariables.get(name);
        int index = lg.getIndex();
//        Type type = localVariables.get(name).getType();
        // TODO: Recognizing types
        lg.setStart(il.append(new ISTORE(index)));
    }
    
    /**
     * Добавляет число в стек
     * @param val Целочисленная константа
     * @see PUSH
     */
    public void push(int val) {
        il.append(new PUSH(cg.getConstantPool(), val));
    }

    /**
     * Компилирует метод
     */
    public void compile() {
        // MOCK START    
        il.append(f.createGetStatic("java.lang.System", 
                "out", new ObjectType("java.io.PrintStream")));
        il.append(new ILOAD(1));
        il.append(f.createInvoke("java.io.PrintStream", "println", 
                Type.VOID, new Type[] { Type.INT }, INVOKEVIRTUAL));

        il.append(new RETURN());
        // MOCK END
        
        mg.setMaxStack();
        cg.addMethod(mg.getMethod());
        il.dispose();
    }
}
