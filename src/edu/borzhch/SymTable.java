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
    private ArrayList<HashMap<String, String>> symbols = null;
    private ArrayList<HashMap<String, String>> baseTypes = null;
    
    private SymTable previous = null;
    
    public SymTable(SymTable previous) {
        this.previous = previous;
        this.symbols = new ArrayList<>();
        this.baseTypes = new ArrayList<>();
    }
    
    public SymTable getPrevious() {
        return this.previous;
    }
    
    public void setPrevious(SymTable value) {
        this.previous = value;
    }
    
    public void pushSymbol(String identifier, String type, String baseType) {
        this.pushSymbol(identifier, type);
        
        HashMap<String, String> bType = new HashMap<>();        
        bType.put(identifier, baseType);
        baseTypes.add(bType);
    }
    
    public void pushSymbol(String identifier, String type) {
        HashMap<String, String> symbol = new HashMap<>();
        symbol.put(identifier, type);
        symbols.add(symbol);
    }
    
    public boolean findSymbol(String identifier) {
        boolean result = false;
        if(symbols != null) {
            for(HashMap<String, String> symbol : symbols) {
                result = symbol.containsKey(identifier);
                if(result) break;
            }
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
            for(HashMap<String, String> type : baseTypes) {
                result = type.get(identifier);
                if(result != null) break;
            }
        }
        
        return result;
    }
    
    public String getSymbolType(String identifier) {
        String result = null;
        for(HashMap<String, String> symbol : symbols) {
            result = symbol.get(identifier);
            if(result != null) break;
        }
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
