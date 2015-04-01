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
    private ArrayList<HashMap<String, String>> symbols;
    
    private SymTable previous;
    
    public SymTable(SymTable previous) {
        this.previous = previous;
        this.symbols = new ArrayList<>();
    }
    
    public SymTable getPrevious() {
        return this.previous;
    }
    
    public void setPrevious(SymTable value) {
        this.previous = value;
    }
    
    public void pushSymbol(String identifier, String type) {
        HashMap<String, String> symbol = new HashMap<>();
        symbol.put(identifier, type);
        symbols.add(symbol);
    }
    
    public boolean findSymbol(String identifier) {
        boolean result = false;
        if(symbols != null)
            for(HashMap<String, String> symbol : symbols) {
                result = symbol.containsKey(identifier);
            }
        if(this.previous != null && !result) {
            result = this.previous.findSymbol(identifier);
        }
        return result;
    }
    
    public String getSymbolType(String identifier) {
        String result = null;
        for(HashMap<String, String> symbol : symbols) {
            result = symbol.get(identifier);
            if (result != null) break;
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
