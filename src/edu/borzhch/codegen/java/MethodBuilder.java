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
    
    public int getLastIndex() {
        return il.getEnd().getPosition();
    }
    public InstructionHandle getHandler(int pos) {
        return il.findHandle(pos);
    }
    public InstructionHandle getLastHandler() {
        return il.getEnd();
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

    }
    public int getVariableIndex(String id) {
        return localVariables.get(id).getIndex();
    }
    
    /**
     * Сохраняет значение в переменную
     * @param name Имя переменной
     * @see ISTORE
     */
    public void store(String name, Type type) {
        LocalVariableGen lg = localVariables.get(name);
        int index = lg.getIndex();
//        Type type = localVariables.get(name).getType();
        // TODO: Recognizing types
        lg.setStart(il.append(f.createStore(type, index)));
//        lg.setStart(il.append(new ISTORE(index)));
    }
    public void load(String name, Type type) {
        int index = getVariableIndex(name);
        il.append(f.createLoad(type, index));
    }
    
    /**
     * Добавляет число в стек
     * @param ival Целочисленная константа
     * @see PUSH
     */
    public void push(int ival) {
        il.append(new PUSH(cg.getConstantPool(), ival));
    }
    public void push(float fval) {
        il.append(new PUSH(cg.getConstantPool(), fval));
    }
    public void push(String str) {
        il.append(new PUSH(cg.getConstantPool(), str));
    }
    public void push(boolean bval) {
        il.append(new PUSH(cg.getConstantPool(), bval));
    }
 
    public void and() {
        il.append(new IAND());
    }
    public void or() {
        il.append(new IOR());
    }
    public void xor() {
        il.append(new IXOR());
    }
    public void not() {
        il.append(new INEG());
    }
    
    public void neg(BOType type) {
        switch (type) {
            case INT:
                il.append(new INEG());
                break;
            case FLOAT:
                il.append(new FNEG());
                break;
        }
    }
    public void add(BOType type) {
        switch (type) {
            case INT:
                il.append(new IADD());
                break;
            case FLOAT:
                il.append(new FADD());
        }
    }
    public void sub(BOType type) {
        switch (type) {
            case INT:
                il.append(new ISUB());
                break;
            case FLOAT:
                il.append(new FSUB());
        }
    }
    public void mul(BOType type) {
        switch (type) {
            case INT:
                il.append(new IMUL());
                break;
            case FLOAT:
                il.append(new FMUL());
        }
    }
    public void div(BOType type) {
        switch (type) {
            case INT:
                il.append(new IDIV());
                break;
            case FLOAT:
                il.append(new FDIV());
        }
    }
    
    public void inc(int index) {
        il.append(new IINC(index, 1));
    }
    public void dec(int index) {
        il.append(new IINC(index, -1));
    }
    
    public void fcmpl() {
        il.append(new FCMPL());
    }
    public IfInstruction icmp(String opcode) {
        IfInstruction icmp = null;
        switch (opcode) {
            case "eq":
                icmp = new IF_ICMPEQ(null);
                break;
            case "ne":
                icmp = new IF_ICMPNE(null);
                break;
            case "gt":
                icmp = new IF_ICMPGT(null);
                break;
            case "ge":
                icmp = new IF_ICMPGE(null);
                break;
            case "lt":
                icmp = new IF_ICMPLT(null);
                break;
            case "le":
                icmp = new IF_ICMPLE(null);
                break;
        }
        il.append(icmp);
        return icmp;
    }
    
    public IFEQ ifeq() {
        IFEQ ifeq = new IFEQ(null);
        il.append(ifeq);
        return ifeq;
    }
    
    public GOTO go() {
        GOTO go = new GOTO(null);
        il.append(go);
        return go;
    }
    
    public void nop() {
        NOP nop = new NOP();
        il.append(nop);
    }
    
    public void convertToDouble(BOType from) {
        switch (from) {
            case INT:
                il.append(new I2D());
                break;
            case FLOAT:
                il.append(new F2D());
                break;
        }
    }
    public void convertDouble(BOType to) {
        switch (to) {
            case INT:
                il.append(new D2I());
                break;
            case FLOAT:
                il.append(new D2F());
                break;
        }
    }
    
    public void convert(BOType from, BOType to) {
        if (from == to) return;
        
        switch (from) {
            case INT:
                if (BOType.FLOAT == to) {
                    il.append(new I2F());
                }
            break;
            case FLOAT:
                if (BOType.INT == to) {
                    il.append(new F2I());
                }
        }
    }

    public void invokeStatic(String function) {
        switch (function) {
            case "pow":
                il.append(f.createInvoke("java.lang.Math", "pow", Type.DOUBLE, 
                    new Type[] { Type.DOUBLE, Type.DOUBLE }, INVOKESTATIC));
                break;
        }
    }
    
    public void getStdout() {
        il.append(f.createGetStatic("java.lang.System", 
            "out", new ObjectType("java.io.PrintStream")));
    }
    
   public void printLine(Type type) {
       il.append(f.createInvoke("java.io.PrintStream", "println", 
               Type.VOID, new Type[] { type }, INVOKEVIRTUAL));
    }
   public void printLine(String str) {
       il.append(f.createPrintln(str));
   }
   
    /**
     * Компилирует метод
     */
    public void compile() {
        // MOCK START   
        getStdout();
        il.append(f.createLoad(Type.BOOLEAN, 1));
        printLine(Type.BOOLEAN);
        il.append(new RETURN());
        // MOCK END
        
        mg.setMaxStack();
        cg.addMethod(mg.getMethod());
        il.dispose();
    }
}
