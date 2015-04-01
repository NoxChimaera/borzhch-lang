/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.borzhch.ast;

import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;
import edu.borzhch.helpers.BOHelper;

/**
 *
 * @author Balushkin M.
 */
public class CmpOpNode extends OpNode {
    NodeAST l;
    NodeAST r;
    String op;
    public CmpOpNode(NodeAST left, NodeAST right, String operator) {
        l = left;
        r = right;
        op = operator;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Comparison " + op);
        ++lvl;
        l.debug(lvl);
        r.debug(lvl);
    }

    @Override
    public void codegen() {
        switch (op) {
            case "==":
            case "!=":
                if (BOType.FLOAT == l.type || BOType.FLOAT == r.type) {
                    fcmp();
                }
//                if (BOHelper.isNumber(l.type) && BOHelper.isNumber(r.type)) {
//                    eqNumber(op.equals("=="));
//                } else if (l.type == BOType.BOOL && l.type == r.type) {
////                    eqBool(op.equals("=="));
//                }
                break;
            case ">":
            case "<":
                break;
            case ">=":
            case "<=":
                break;
        }
    }
    
    private void fcmp() {
        l.codegen();
        if (BOType.FLOAT == l.type) {
            JavaCodegen.method().convert(l.type, BOType.FLOAT);
        }
        r.codegen();
        if (BOType.FLOAT == r.type) {
            JavaCodegen.method().convert(r.type, BOType.FLOAT);
        }
       
        JavaCodegen.method().fcmpl();
        
        
        
//fcmpl
//ifle 9
//iconst_1
//goto 10
//iconst_0
//istore_3
//return
    }
}
