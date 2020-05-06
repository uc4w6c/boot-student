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

        var path = this.dirPath.isEmpty() ? this.fileName : (this.dirPath.get() + "/" + this.fileName);
        this.jackTokenizer = new JackTokenizer(path);

        jackTokenizer.advance();
        if ((!jackTokenizer.tokenType().equals(TokenType.KEYWORD)) ||
            (jackTokenizer.getToken() != "class")) {

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
        final String ELEMENT_TAG_NAME = "classVarDec";
        this.writeElementStart(ELEMENT_TAG_NAME);


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

        // subroutine終了までループ
        while (jackTokenizer.hasMoreTokens()) {
            if (jackTokenizer.tokenType().equals(TokenType.KEYWORD)) {
                switch (jackTokenizer.getToken()) {
                    case "":
                }
            } else if (jackTokenizer.tokenType().equals(TokenType.SYMBOL)) {
            } else {
            }
            jackTokenizer.advance();
        }
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
