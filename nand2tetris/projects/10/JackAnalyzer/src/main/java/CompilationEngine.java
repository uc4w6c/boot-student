import token.TokenType;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class CompilationEngine {
    private Optional<String> dirPath;
    private String fileName;
    private List<String> outputCode = new ArrayList<String>();
    private JackTokenizer jackTokenizer;
    private int indentCount;

    public CompilationEngine(Optional<String> dirPath, String fileName) {
        this.dirPath = dirPath;
        this.fileName = fileName;
        // System.out.println("File Path: " + dirPath.get() + ", " + fileName);

        var path = this.dirPath.isEmpty() ? this.fileName : (this.dirPath.get() + "/" + this.fileName);
        this.jackTokenizer = new JackTokenizer(path);

        jackTokenizer.advance();
        if ((!jackTokenizer.tokenType().equals(TokenType.KEYWORD)) ||
            (!jackTokenizer.getToken().equals("class"))) {

            throw new IllegalArgumentException("classが見つかりません。");
        }
        this.compileClass();
    }

    /*
    public void compile() {
        jackTokenizer.advance();
        while (jackTokenizer.hasMoreTokens()) {
            this.compileClass();

            jackTokenizer.advance();
        }
    }
     */

    private void writeElement(String tagName, String value) {
        var indent = "  ".repeat(this.indentCount);
        outputCode.add("${indent}<${tagName}> ${value} </${tagName}>");
    }

    private void writeElementStart(String tagName) {
        var indent = "  ".repeat(this.indentCount);
        outputCode.add("${indent}<${tagName}>");
        this.indentCount = this.indentCount + 1;
    }

    private void writeElementEnd(String tagName) {
        this.indentCount = this.indentCount - 1;
        var indent = "  ".repeat(this.indentCount);
        outputCode.add("${indent}</${tagName}>");
    }

    private void compileVarDec() {
        final String VAR_DEC_TAG_NAME = "classVarDec";
        this.writeElementStart(VAR_DEC_TAG_NAME);

        this.writeElement("keyword", jackTokenizer.getToken());
        jackTokenizer.advance();
        this.writeElement("keyword", jackTokenizer.getToken());
        jackTokenizer.advance();
        this.writeElement("identifier", jackTokenizer.getToken());
        jackTokenizer.advance();
        this.writeElement("symbol", jackTokenizer.getToken());

        this.writeElementEnd(VAR_DEC_TAG_NAME);
    }

    private void compileClass() {
        this.writeElementStart("class");
        this.writeElement("keyword", "class");

        jackTokenizer.advance();
        if (!jackTokenizer.tokenType().equals(TokenType.IDENTIFIER)) {
            throw new IllegalArgumentException("class定義に誤りがあります。");
        }
        this.writeElement("identifier", jackTokenizer.getToken());

        jackTokenizer.advance();
        if (!jackTokenizer.tokenType().equals(TokenType.SYMBOL)) {
            throw new IllegalArgumentException("class定義に誤りがあります。");
        }
        this.writeElement("symbol", jackTokenizer.getToken());

        jackTokenizer.advance();
        while (jackTokenizer.hasMoreTokens()) {
            if (jackTokenizer.tokenType().equals(TokenType.KEYWORD)) {
                switch (jackTokenizer.getToken()) {
                    case "field":
                    case "static": {
                        this.compileClassVarDec();
                        break;
                    }
                    case "constructor":
                    case "method":
                    case "function": {
                        this.compileSubroutine();
                        break;
                    }
                    case "var": {
                        this.compileVarDec();
                        break;
                    }
                }

            // SYMBOLが見つかった場合はクラス定義終了
            } else if (jackTokenizer.tokenType().equals(TokenType.SYMBOL)) {
                this.writeElement("symbol", jackTokenizer.getToken());
                jackTokenizer.advance();
                break;

            // 値
            } else {
                throw new IllegalArgumentException("class定義に誤りがあります。");
            }
            jackTokenizer.advance();
        }
        this.writeElementEnd("class");
    }

    private void compileClassVarDec() {
        final String ELEMENT_TAG_NAME = "varDec";
        this.writeElementStart(ELEMENT_TAG_NAME);

        this.writeElement("keyword", jackTokenizer.getToken());
        jackTokenizer.advance();
        this.writeElement("keyword", jackTokenizer.getToken());
        jackTokenizer.advance();
        this.writeElement("identifier", jackTokenizer.getToken());
        jackTokenizer.advance();
        while (true) {
            if (jackTokenizer.getToken().equals(";")) break;

            // , は処理対象から除外
            if (jackTokenizer.getToken().equals(TokenType.SYMBOL) &&
                jackTokenizer.getToken().equals(",")) {
                jackTokenizer.advance();
            }
            jackTokenizer.advance();
            this.writeElement("identifier", jackTokenizer.getToken());
        }

        this.writeElement("symbol", jackTokenizer.getToken());

        this.writeElementEnd(ELEMENT_TAG_NAME);
    }

    private void compileSubroutine() {
        final String ELEMENT_TAG_NAME = "subroutineDec";
        this.writeElementStart(ELEMENT_TAG_NAME);

        this.writeElement("keyword", jackTokenizer.getToken());

        jackTokenizer.advance();
        this.writeElement("identifier", jackTokenizer.getToken());

        jackTokenizer.advance();
        this.writeElement("identifier", jackTokenizer.getToken());

        jackTokenizer.advance();
        this.compileParameterList();

        // var
        while (true) {
            if (jackTokenizer.hasMoreTokens()) {
                break;
            }
            if (!jackTokenizer.tokenType().equals(TokenType.KEYWORD) ||
                !jackTokenizer.getToken().equals("var")) {
                break;
            }
            this.compileClassVarDec();
            jackTokenizer.advance();
        }

        // statements
        this.compileStatements();

        this.writeElementEnd(ELEMENT_TAG_NAME);
    }

    private void compileParameterList() {
        // parameterList作成
        this.writeElementStart("parameterList");

        jackTokenizer.advance();
        while (true) {
            if (jackTokenizer.tokenType().equals(TokenType.KEYWORD) ||
                jackTokenizer.getToken().equals(")")) {
                jackTokenizer.advance();
                break;
            }
            this.writeElement("keyword", jackTokenizer.getToken());
            jackTokenizer.advance();
            this.writeElement("identifier", jackTokenizer.getToken());

            if (jackTokenizer.tokenType().equals(TokenType.SYMBOL) &&
                    jackTokenizer.getToken().equals(",")) {

                jackTokenizer.advance();
            }
            jackTokenizer.advance();
        }
        this.writeElementEnd("parameterList");

    }

    private void compileStatements() {
        final String STATEMENTS_TAG_NAME = "statements";

        this.writeElementStart(STATEMENTS_TAG_NAME);
        // subroutine終了までループ
        while (jackTokenizer.hasMoreTokens()) {
            if (jackTokenizer.tokenType().equals(TokenType.KEYWORD)) {
                switch (jackTokenizer.getToken()) {
                    case "let": this.compileLet();
                    case "if": this.compileIf();
                    case "while": this.compileWhile();
                    case "do": this.compileDo();
                    case "return": this.compileReturn();
                }
            } else if (jackTokenizer.tokenType().equals(TokenType.SYMBOL)) {
            } else {
            }
            jackTokenizer.advance();
        }
        this.writeElementEnd(STATEMENTS_TAG_NAME);
    }

    private void compileDo() {

    }

    private void compileLet() {
        final String STATEMENTS_TAG_NAME = "letStatement";
        this.writeElementStart(STATEMENTS_TAG_NAME);

        this.writeElement("keyword", jackTokenizer.getToken());

        jackTokenizer.advance();
        this.writeElement("identifier", jackTokenizer.getToken());

        jackTokenizer.advance();
        if (jackTokenizer.tokenType().equals(TokenType.SYMBOL) &&
                jackTokenizer.getToken().equals("[")) {

            this.writeElement("symbol", jackTokenizer.getToken());

            this.compileExpression();

            this.writeElement("symbol", jackTokenizer.getToken());
            jackTokenizer.advance();
        }

        // '=' part
        this.writeElement("symbol", jackTokenizer.getToken());

        // subroutine終了までループ
        while (true) {
            if (jackTokenizer.tokenType().equals(TokenType.SYMBOL) &&
                jackTokenizer.getToken().equals(";")) {
                break;
            }

            if (jackTokenizer.tokenType().equals(TokenType.SYMBOL) &&
                    jackTokenizer.getToken().equals("=")) {
                this.writeElement("symbol", jackTokenizer.getToken());
            }

            if (jackTokenizer.tokenType().equals(TokenType.KEYWORD)) {
                switch (jackTokenizer.getToken()) {
                    case "let": this.compileLet();
                    case "if": this.compileIf();
                    case "while": this.compileWhile();
                    case "do": this.compileDo();
                    case "return": this.compileReturn();
                }
            } else if (jackTokenizer.tokenType().equals(TokenType.SYMBOL)) {
            } else {
            }
            jackTokenizer.advance();
        }

        this.writeElement("symbol", jackTokenizer.getToken());

        this.writeElementEnd(STATEMENTS_TAG_NAME);
    }

    private void compileWhile() {
        final String WHILE_TAG_NAME = "whileStatement";
        this.writeElementStart(WHILE_TAG_NAME);

        this.writeElement("keyword", jackTokenizer.getToken());

        // '(' expression ')' の間の条件文
        jackTokenizer.advance();
        this.writeElement("symbol", jackTokenizer.getToken());

        jackTokenizer.advance();
        this.compileExpression();

        this.writeElement("symbol", jackTokenizer.getToken());

        // '{' statements '}' の間の処理文
        jackTokenizer.advance();
        this.writeElement("symbol", jackTokenizer.getToken());

        jackTokenizer.advance();
        this.compileStatements();

        this.writeElement("symbol", jackTokenizer.getToken());


        this.writeElementEnd(WHILE_TAG_NAME);
    }

    private void compileReturn() {
        final String RETURN_TAG_NAME = "returnStatement";
        this.writeElementStart(RETURN_TAG_NAME);

        // ';' でない場合はexpression
        if (!jackTokenizer.tokenType().equals(TokenType.SYMBOL) ||
                !jackTokenizer.getToken().equals(";")) {

            this.compileExpression();
        }

        this.writeElementEnd(RETURN_TAG_NAME);
    }

    private void compileIf() {
        final String IF_TAG_NAME = "ifStatement";
        this.writeElementStart(IF_TAG_NAME);

        this.writeElement("keyword", jackTokenizer.getToken());

        // '(' expression ')' の間の条件文
        jackTokenizer.advance();
        this.writeElement("symbol", jackTokenizer.getToken());

        this.compileExpression();

        this.writeElement("symbol", jackTokenizer.getToken());

        // '{' statements '}' の間の処理文
        jackTokenizer.advance();
        this.writeElement("symbol", jackTokenizer.getToken());

        jackTokenizer.advance();
        this.compileStatements();

        this.writeElement("symbol", jackTokenizer.getToken());

        jackTokenizer.advance();

        // 'else' が存在する場合は継続
        while (true) {
            if (!jackTokenizer.tokenType().equals(TokenType.SYMBOL) ||
                    !jackTokenizer.getToken().equals("else")) {

                break;
            }
            this.writeElement("keyword", jackTokenizer.getToken());

            // '{' statements '}' の間の処理文
            jackTokenizer.advance();
            this.writeElement("symbol", jackTokenizer.getToken());

            jackTokenizer.advance();
            this.compileStatements();

            this.writeElement("symbol", jackTokenizer.getToken());

            jackTokenizer.advance();
        }


        this.writeElementEnd(IF_TAG_NAME);
    }

    private void compileExpression() {
        final String EXPRESSION_TAG_NAME = "expression";
        this.writeElementStart(EXPRESSION_TAG_NAME);

        while (true) {
            if (jackTokenizer.tokenType().equals(TokenType.SYMBOL)) {
                // ')' or ']' の場合はExpression処理終了
                if (jackTokenizer.getToken().equals(")") ||
                    jackTokenizer.getToken().equals("]")) {
                    break;
                }
                if (jackTokenizer.getToken().equals(")") ||
                    jackTokenizer.getToken().equals("]")) {

                    this.writeElement("symbol", jackTokenizer.getToken());
                    this.compileExpression();
                    this.writeElement("symbol", jackTokenizer.getToken());
                }
                if (jackTokenizer.getToken().equals("+") ||
                        jackTokenizer.getToken().equals("-") ||
                        jackTokenizer.getToken().equals("-") ||
                        jackTokenizer.getToken().equals("*") ||
                        jackTokenizer.getToken().equals("/") ||
                        jackTokenizer.getToken().equals("&") ||
                        jackTokenizer.getToken().equals("|") ||
                        jackTokenizer.getToken().equals("<") ||
                        jackTokenizer.getToken().equals(">") ||
                        jackTokenizer.getToken().equals("=") ||
                        jackTokenizer.getToken().equals("~")) {

                    this.writeElement("symbol", jackTokenizer.getToken());

                }
                if (jackTokenizer.getToken().equals(";")) {
                    break;
                }
            }
            // 変数, 文字列, 数値型の場合はterm処理
            if (jackTokenizer.tokenType().equals(TokenType.STRING_CONST) ||
                    jackTokenizer.tokenType().equals(TokenType.INT_CONST) ||
                    jackTokenizer.tokenType().equals(TokenType.IDENTIFIER)) {
                this.compileTerm();
            }

            jackTokenizer.advance();
        }

        this.writeElementEnd(EXPRESSION_TAG_NAME);
    }

    private void compileTerm() {
        final String TERM_TAG_NAME = "term";
        this.writeElementStart(TERM_TAG_NAME);

        while (true) {
            if (jackTokenizer.tokenType().equals(TokenType.SYMBOL)) {
                // ')' or ']' の場合はExpression処理終了
                if (jackTokenizer.getToken().equals(")") ||
                        jackTokenizer.getToken().equals("]")) {

                    this.writeElement("symbol", jackTokenizer.getToken());
                    jackTokenizer.advance();
                    break;
                }
                if (jackTokenizer.getToken().equals(".")) {
                    this.writeElement("symbol", jackTokenizer.getToken());
                    jackTokenizer.advance();
                }
                if (jackTokenizer.getToken().equals("(") ||
                        jackTokenizer.getToken().equals("[")) {

                    this.compileExpressionList();
                }

                if (jackTokenizer.getToken().equals("+") ||
                        jackTokenizer.getToken().equals("-") ||
                        jackTokenizer.getToken().equals("-") ||
                        jackTokenizer.getToken().equals("*") ||
                        jackTokenizer.getToken().equals("/") ||
                        jackTokenizer.getToken().equals("&") ||
                        jackTokenizer.getToken().equals("|") ||
                        jackTokenizer.getToken().equals("<") ||
                        jackTokenizer.getToken().equals(">") ||
                        jackTokenizer.getToken().equals("=") ||
                        jackTokenizer.getToken().equals("~") ||
                        jackTokenizer.getToken().equals(";")) {

                    break;
                }
            }
            // 変数, 文字列, 数値型の処理
            if (jackTokenizer.tokenType().equals(TokenType.STRING_CONST)) {
                var stringConstant = jackTokenizer.getToken().replace("\"", "");
                this.writeElement("stringConstant", stringConstant);
            }
            if (jackTokenizer.tokenType().equals(TokenType.INT_CONST)) {
                this.writeElement("integerConstant", jackTokenizer.getToken());
            }
            if (jackTokenizer.tokenType().equals(TokenType.IDENTIFIER)) {
                this.writeElement("identifier", jackTokenizer.getToken());
            }

            jackTokenizer.advance();
        }

        this.writeElementEnd(TERM_TAG_NAME);
    }

    private void compileExpressionList() {
        final String EXPRESSION_LIST_TAG_NAME = "expressionList";
        this.writeElementStart(EXPRESSION_LIST_TAG_NAME);

        while (true) {
            // ',' の場合はExpressionList継続
            if (jackTokenizer.tokenType().equals(TokenType.SYMBOL)) {
                if (!jackTokenizer.getToken().equals(",")) {
                    break;
                }
                jackTokenizer.advance();
            }
            this.compileExpression();
        }

        this.writeElementEnd(EXPRESSION_LIST_TAG_NAME);
    }

    public void save() {
        var path = Paths.get(this.dirPath.isEmpty() ?
                this.fileName :
                (this.dirPath.get() + "/" + this.fileName));
        try {
            System.out.println("path:" + path.toAbsolutePath());
            Files.write(path,
                        outputCode,
                        Charset.forName("UTF-8"),
                        StandardOpenOption.CREATE);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
