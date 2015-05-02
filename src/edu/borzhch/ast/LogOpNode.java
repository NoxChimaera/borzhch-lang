package edu.borzhch.ast;

import edu.borzhch.Program;
import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.optimization.IConstant;
import edu.borzhch.optimization.IFoldable;

/**
 *
 * @author Balushkin M.
 */
public class LogOpNode extends NodeAST implements IFoldable {
    NodeAST l;
    NodeAST r;
    String op;
    public LogOpNode(NodeAST left, NodeAST right, String operator) {
        l = left;
        r = right;
        op = operator;
    }
    
    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Logic " + op);
        ++lvl;
        l.debug(lvl);
        r.debug(lvl);
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
        
        l.codegen();
        r.codegen();
        switch (op) {
            case "and":
                JavaCodegen.method().and();
                break;
            case "or":
                JavaCodegen.method().or();
                break;
            case "xor":
                JavaCodegen.method().xor();
                break;
        }
    }

    @Override
    public NodeAST fold() {
        if (l instanceof IFoldable) {
            l = (NodeAST) ((IFoldable) l).fold();
        }
        if (r instanceof IFoldable) {
            r = (NodeAST) ((IFoldable) r).fold();
        }
        
        if (l instanceof IConstant && r instanceof IConstant) {
            return foldToBool((IConstant) l, (IConstant) r);
        } 
        
        return this;
    }
    
    /**
     * Сворачивает константное выражение
     * @param left Константа слева
     * @param right Константа справа
     * @return Новое значение, полученне после оптимизации, или this, если что-то пошло не так
     */
    private NodeAST foldToBool(IConstant left, IConstant right) {
        switch (op) {
            case "and":
                return new BooleanNode(left.coerceBoolean() 
                        && right.coerceBoolean());
            case "or":
                return new BooleanNode(left.coerceBoolean() 
                        || right.coerceBoolean());
            case "xor":
                return new BooleanNode(left.coerceBoolean() 
                        ^ right.coerceBoolean());
        }
        
        return this;
    }
}
