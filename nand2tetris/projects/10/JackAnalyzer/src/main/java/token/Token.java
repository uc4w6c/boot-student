package token;

public interface Token {
    public Boolean isToken(String code);
    public TokenType getType();
}
