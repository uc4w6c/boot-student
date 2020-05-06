package token;

import java.util.List;
import java.util.ArrayList;

public class SymbolToken implements Token {
    private static SymbolToken singleton = new SymbolToken();

    private static List<String> symbolChar = new ArrayList<String>();

    static {
        symbolChar.add("{");
        symbolChar.add("}");
        symbolChar.add("(");
        symbolChar.add(")");
        symbolChar.add("[");
        symbolChar.add("]");
        symbolChar.add(".");
        symbolChar.add(",");
        symbolChar.add(";");
        symbolChar.add("+");
        symbolChar.add("-");
        symbolChar.add("*");
        symbolChar.add("/");
        symbolChar.add("&");
        symbolChar.add("|");
        symbolChar.add("<");
        symbolChar.add(">");
        symbolChar.add("=");
        symbolChar.add("~");
    }

    private SymbolToken() {}

    public static SymbolToken getInstance() {
        return singleton;
    }

    public Boolean isToken(String code) {
        return symbolChar.contains(code);
    }

    public TokenType getType() {
        return TokenType.KEYWORD;
    }
}
