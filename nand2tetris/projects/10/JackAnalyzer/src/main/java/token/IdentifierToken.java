package token;

public class IdentifierToken implements Token {
    private static IdentifierToken singleton = new IdentifierToken();

    private IdentifierToken() {}

    public static IdentifierToken getInstance() {
        return singleton;
    }

    public Boolean isToken(String code) {
        if (code.length() >= 4 && code.substring(0, 4).equals("goto")) {
            return true;
        }
        return false;
    }

    public TokenType getType() {
        return TokenType.KEYWORD;
    }
}
