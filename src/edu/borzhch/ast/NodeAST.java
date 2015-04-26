/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.constants.BOType;
import org.apache.bcel.generic.InstructionList;

/**
 * Узел АСД
 * @author Balushkin M.
 */
public abstract class NodeAST {
    /**
     * BO-тип объекта
     */
    protected BOType type;
    /**
     * Возвращает BO-тип объекта
     * @return 
     */
    public BOType type() {
        return type;
    }
    /**
     * Устанавливает BO-тип объекта
     * @param newType 
     */
    public void type(BOType newType) {
        type = newType;
    }
    
    /**
     * Отладочный вывод
     * @param lvl Уровень отступа
     */
    public abstract void debug(int lvl);
    /**
     * Генерация кода
     */
    public abstract void codegen();
    
    /**
     * Печатает отступ
     * @param lvl Уровень
     */
    protected void printLevel(int lvl) {
        for (int i = 0; i < lvl - 1; ++i) {
            System.out.print("   ");
        }
        System.out.print("|---");
    }
}
