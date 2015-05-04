package edu.borzhch.ast;

import edu.borzhch.Program;
import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.optimization.IConstant;
import edu.borzhch.optimization.IFoldable;

/**
 * Унарная операция
 * @author Balushkin M.
 */
public class UnOpNode extends NodeAST implements IFoldable {
    NodeAST expr;
    String op;
    public UnOpNode(NodeAST expression, String operation) {
        expr = expression;
        op = operation;
    }

    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Unary " + op);
        expr.debug(lvl + 1);
    }

    @Override
    public void codegen() {
        if (Program.config.getConstantFolding()) {
            NodeAST fold = fold();
            if (fold != this) {
                fold.codegen();
                return;
            }
        }
        
        expr.codegen();
        switch (op) {
            case "not":
                JavaCodegen.method().not();
                break;
            case "-":
                JavaCodegen.method().neg(expr.type);
                break;
        }
    }

    @Override
    public NodeAST fold() {
        NodeAST ex = expr;
        if (ex instanceof IFoldable) {
            ex = ((IFoldable) ex).fold();
        }
        
        if (ex instanceof IConstant) {
            switch (op) {
                case "not":
                    return new BooleanNode(!((IConstant) ex).coerceBoolean());
                case "-":
                    return foldNegation((IConstant) ex);
            }
        }

        return this;
    }
    
    private NodeAST foldNegation(IConstant c) {
        switch (((NodeAST) c).type) {
            case INT:
                return new IntegerNode(-c.coerceInt());
            case FLOAT:
                return new FloatNode(-c.coerceFloat());
        }
        return this;
    }
}
