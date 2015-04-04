/*
 */
package edu.borzhch;
import java.util.HashMap;
import java.util.ArrayList;

/**
 *
 * @author Tursukov A.E. <goldenflame412@gmail.com>
 */
public class SymTable {
    private HashMap<String, String> symbols = null;
    private HashMap<String, String> baseTypes = null;
    
    private SymTable previous = null;
    
    public SymTable(SymTable previous) {
        this.previous = previous;
        this.symbols = new HashMap<>();
        this.baseTypes = new HashMap<>();
    }
    
    public SymTable getPrevious() {
        return this.previous;
    }
    
    public void setPrevious(SymTable value) {
        this.previous = value;
    }
    
    public void pushSymbol(String identifier, String type, String baseType) {
        this.pushSymbol(identifier, type);
        
        baseTypes.put(identifier, baseType);
    }
    
    public void pushSymbol(String identifier, String type) {
        symbols.put(identifier, type);
    }
    
    public boolean findSymbol(String identifier) {
        boolean result = false;
        if(symbols != null) {
            result = symbols.containsKey(identifier);
        }
        if(this.previous != null && !result) {
            result = this.previous.findSymbol(identifier);
        }
        return result;
    }
    
    /**
     * Ищет базовый тип ссылочного объекта в таблице baseTypes по идентификатору.
     * @param identifier
     * @return Строку, содержащую базовый тип; иначе null.
     */
    public String getBaseType(String identifier) {
        String result = null;
        
        if(baseTypes != null) {
                result = baseTypes.get(identifier);
        }
        
        return result;
    }
    
    public String getSymbolType(String identifier) {
        String result = null;
            result = symbols.get(identifier);
        if (result == null && null != previous) {
            result = previous.getSymbolType(identifier);
        }
        
        return result;
    }
    
    public void clear() {
        symbols.clear();
        symbols = null;
    }
}
