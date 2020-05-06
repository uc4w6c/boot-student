package token;

public class IntegerConstantToken implements Token {
    private static IntegerConstantToken singleton = new IntegerConstantToken();

    private IntegerConstantToken() {}

    public static IntegerConstantToken getInstance() {
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
