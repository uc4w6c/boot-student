package token;

import java.util.ArrayList;
import java.util.List;

public class KeywordToken implements Token {
    private static KeywordToken singleton = new KeywordToken();
    private static List<String> keywordList = new ArrayList<String>();

    static {
        keywordList.add("class");
        keywordList.add("constructor");
        keywordList.add("function");
        keywordList.add("method");
        keywordList.add("field");
        keywordList.add("static");
        keywordList.add("var");
        keywordList.add("int");
        keywordList.add("char");
        keywordList.add("boolean");
        keywordList.add("void");
        keywordList.add("true");
        keywordList.add("false");
        keywordList.add("null");
        keywordList.add("this");
        keywordList.add("let");
        keywordList.add("do");
        keywordList.add("if");
        keywordList.add("else");
        keywordList.add("while");
        keywordList.add("return");
    }

    private KeywordToken() {}

    public static KeywordToken getInstance() {
        return singleton;
    }

    public Boolean isToken(String code) {
        return keywordList.contains(code);
    }

    public TokenType getType() {
        return TokenType.KEYWORD;
    }
}
