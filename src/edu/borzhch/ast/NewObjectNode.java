/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;

/**
 * Инстанцирование объекта
 * @author Balushkin M.
 */
public class NewObjectNode extends NodeAST {
    /**
     * Идентификатор класс/структуры инстанцируемого объекта
     */
    String identifier;
    /**
     * Инстанцирование объекта
     * @param id Идентификатор класса/структуры
     */
    public NewObjectNode(String id) {
        identifier = id;
        type = BOType.REF;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("New: " + identifier);
    }

    @Override
    public void codegen() {
        JavaCodegen.method().newObject(identifier);
        JavaCodegen.method().dup();
        JavaCodegen.method().invokeInit(identifier);
    }
}
