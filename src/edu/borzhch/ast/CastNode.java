package edu.borzhch.ast;

import edu.borzhch.Program;
import edu.borzhch.codegen.java.JavaCodegen;
import edu.borzhch.constants.BOType;
import edu.borzhch.helpers.BOHelper;
import edu.borzhch.optimization.IConstant;
import edu.borzhch.optimization.IFoldable;

/**
 *
 * @author Balushkin M.
 */
public class CastNode extends NodeAST implements IFoldable {
    NodeAST expression;
    public CastNode(BOType type, NodeAST exp) {
        this.type = type;
        expression = exp;
    }

    @Override
    public void debug(int lvl) {
        printLevel(lvl);
        System.out.println("Cast:");
        ++lvl;
        printLevel(lvl);
        System.out.println("Type:" + BOHelper.toString(type));
        printLevel(lvl);
        System.out.println("Expression:");
        expression.debug(lvl + 1);
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
        
        expression.codegen();
        if (BOType.STRING == expression.type) {
            anyFromString(type);
            return;
        }
        JavaCodegen.method().convert(expression.type, type);
    }
    
    private void anyFromString(BOType any) {
        JavaCodegen.method().valueOf(any);
    }

    @Override
    public NodeAST fold() {
        if (expression instanceof IFoldable) {
            expression = ((IFoldable) expression).fold();
        }
        
        if (expression instanceof IConstant) {
            switch (expression.type) {
                case BOOL:
                    return foldAnyToBoolean((IConstant) expression);
                case FLOAT:
                    return foldAnyToFloat((IConstant) expression);
                case INT:
                    return foldAnyToInt((IConstant) expression);
                case STRING:
                    return foldAnyToString((IConstant) expression);
            }
        }
        
        return this;
    }
    
    /**
     * Конвертирует константное выражение
     * @param c Константа
     * @return Логическая константа
     */
    private NodeAST foldAnyToBoolean(IConstant c) {
        return new BooleanNode(c.coerceBoolean());
    }    
    /**
     * Конвертирует константное выражение
     * @param c Константа
     * @return Константа с плавающей точкой
     */
    private NodeAST foldAnyToFloat(IConstant c) {
        return new FloatNode(c.coerceFloat());
    }
    /**
     * Конвертирует константное выражение
     * @param c Константа
     * @return Целочисленная константа
     */
    private NodeAST foldAnyToInt(IConstant c) {
        return new IntegerNode(c.coerceInt());
    }
    /**
     * Конвертирует константное выражение
     * @param c Константа
     * @return Строковая константа
     */
    private NodeAST foldAnyToString(IConstant c) {
        return new StringNode(c.coerceString());
    }
}
