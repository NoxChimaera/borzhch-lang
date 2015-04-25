/*
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class ClassNode extends NodeAST {
    String identifier;
    StatementList body;
    
    public ClassNode(String identifier, StatementList body) {
        this.identifier = identifier;
        this.body = body;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.print("Class:\n");
        
        lvl++;
        if (body != null) body.debug(lvl);
    }

    @Override
    public void codegen() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //Создаём новый класс
        JavaCodegen.newClass(identifier);
        
        //Переключаемся на него
        JavaCodegen.switchClass(identifier);
        
        //Генерим нутро класса
        body.codegen();
        
        //Возвращаемся к главному классу
        JavaCodegen.switchToMainClass();
    }
    
}
