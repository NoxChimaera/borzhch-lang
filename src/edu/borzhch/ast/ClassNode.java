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
        //Создаём новый класс
        JavaCodegen.newClass(identifier);
        
        //Переключаемся на него
        JavaCodegen.switchClass(identifier);
        
        //Генерим нутро класса
        if(body != null) body.codegen();
        
        JavaCodegen.struct().compile();
        
        //Возвращаемся к главному классу
        JavaCodegen.switchToMainClass();
    }
    
}
