import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private Map<String, Integer> symbolMap = new HashMap<>() {
        {
            put("SP", 0);
            put("LCL", 1);
            put("ARG", 2);
            put("THIS", 3);
            put("THAT", 4);
            put("R0", 0);
            put("R1", 1);
            put("R2", 2);
            put("R3", 3);
            put("R4", 4);
            put("R5", 5);
            put("R6", 6);
            put("R7", 7);
            put("R8", 8);
            put("R9", 9);
            put("R10", 10);
            put("R11", 11);
            put("R12", 12);
            put("R13", 13);
            put("R14", 14);
            put("R15", 15);
            put("SCREEN", 16384);
            put("KBD", 24576);
        }
    };

    public void addEntry(String symbol, Integer address) {
        this.symbolMap.put(symbol, address);
    }

    public boolean contains(String symbol) {
        return this.symbolMap.containsKey(symbol);
    }

    public Integer getAddress(String symbol) {
        return this.symbolMap.get(symbol);
    }
}
