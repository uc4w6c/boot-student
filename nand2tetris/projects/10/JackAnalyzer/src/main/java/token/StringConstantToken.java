package token;

public class StringConstantToken implements Token {
    private static StringConstantToken singleton = new StringConstantToken();

    private StringConstantToken() {}

    public static StringConstantToken getInstance() {
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
