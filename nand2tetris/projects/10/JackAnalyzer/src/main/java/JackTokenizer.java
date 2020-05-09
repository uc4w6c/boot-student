import token.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

public class JackTokenizer {
    BufferedReader reader;
    String line;
    Boolean hasMoreTokens;
    int lineReadPoint;
    String token;

    JackTokenizer(String readFilePath) {
        var path = Paths.get(readFilePath);

        if (!Files.isReadable(path)) {
            throw new IllegalArgumentException("File Path: " + path + "指定したファイルは読み取り不可です");
        }
        try {
            this.reader = Files.newBufferedReader(path);
            this.hasMoreTokens = true;
            this.lineReadPoint = 0;
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public Boolean hasMoreTokens() {
        return this.hasMoreTokens;
    }

    public void advance() {
        if (this.lineReadPoint == 0) {
            try {
                var tmpLine = Optional.ofNullable(this.reader.readLine());
                boolean isComment = false;
                while (true) {
                    // 最終行の時
                    if (tmpLine.isEmpty()) {
                        System.out.println("EOF");
                        this.reader.close();
                        this.hasMoreTokens = false;
                        this.token = null;
                        return;
                    }
                    // 空白行もコメント扱いにする
                    if (tmpLine.get().trim().isEmpty()) {
                        tmpLine = Optional.ofNullable(this.reader.readLine());
                        continue;
                    }
                    if (tmpLine.get().trim().startsWith("//")) {
                        tmpLine = Optional.ofNullable(this.reader.readLine());
                        continue;
                    }
                    // 該当行内でコメントが完結する場合
                    if (tmpLine.get().trim().startsWith("/*") &&
                        tmpLine.get().trim().endsWith("*/")) {
                        isComment = false;
                        tmpLine = Optional.ofNullable(this.reader.readLine());
                        continue;
                    }
                    if (tmpLine.get().trim().startsWith("/*")) {
                        isComment = true;
                    }
                    if (tmpLine.get().trim().endsWith("*/")) {
                        isComment = false;
                        tmpLine = Optional.ofNullable(this.reader.readLine());
                        continue;
                    }
                    if (isComment) {
                        tmpLine = Optional.ofNullable(this.reader.readLine());
                    } else {
                        break;
                    }
                }
                System.out.println("tmpLine:" + tmpLine.get());
                this.line = tmpLine.get();
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
        // if (line.get().isEmpty()) continue;
        int readCount = 1;
        boolean isStringConst = false;
        while (true) {
            var tmpToken = this.line.substring(this.lineReadPoint,
                                                this.lineReadPoint + readCount)
                                    .trim();

            // 読み込み対象文字列が存在しないときはtoken確定
            if (this.lineReadPoint + readCount >= this.line.length()) {
                this.token = tmpToken;
                this.lineReadPoint = 0;
                readCount = 0;
                break;
            }
            // ';' の場合は後続を読み込まないようにする
            if (tmpToken.equals(";")) {
                this.token = tmpToken;
                this.lineReadPoint = 0;
                readCount = 0;
                break;
            }

            var nextChar = this.line.substring(this.lineReadPoint + readCount,
                                                this.lineReadPoint + readCount + 1)
                                    .trim();

            // '//' の場合は後続を読み込まないようにする
            if ((tmpToken + nextChar).equals("//")) {
                this.lineReadPoint = 0;
                readCount = 0;
                this.advance();
                // this.token = tmpToken;
                // this.lineReadPoint = 0;
                // readCount = 0;
                break;
            }

            if (SymbolToken.getInstance().isToken(tmpToken)) {
                this.token = tmpToken;
                break;
            }

            // 最後が " の時は、コメント終了
            if (isStringConst && tmpToken.endsWith("\"")) {
                isStringConst = false;
            }
            // " の時は、コメント開始
            if (tmpToken.equals("\"") && !isStringConst) {
                isStringConst = true;
            }

            // 次の1文字が 空白 or Symbolの場合、それ以前の文字でtoken確定
            if (!tmpToken.isEmpty() &&
                    ((nextChar.isEmpty() && !isStringConst) ||
                            SymbolToken.getInstance().isToken(nextChar))) {
                this.token = tmpToken;
                break;
            }
            readCount++;
        }
        this.lineReadPoint = this.lineReadPoint + readCount;
    }

    public TokenType tokenType() {
        if (KeywordToken.getInstance().isToken(this.token)) {
            return TokenType.KEYWORD;
        } else if (SymbolToken.getInstance().isToken(this.token)) {
            return TokenType.SYMBOL;
        // 数字のみ
        } else if (Pattern.compile("^[0-9]*$").matcher(this.token).find()) {
            return TokenType.INT_CONST;
        // 文字列宣言
        } else if (Pattern.compile("\".*\"").matcher(this.token).find()) {
            return TokenType.STRING_CONST;
        // 変数
        } else {
            return TokenType.IDENTIFIER;
        }
    }

    // 全部tokenを返却するだけだからこのメソッドを用意してみた
    public String getToken() {
        return this.token;
    }

    public Optional<String> keyWord() {
        if (this.tokenType() != TokenType.KEYWORD) {
            return Optional.empty();
        }
        return Optional.of(this.token);
    }

    public Optional<String> symbol() {
        if (this.tokenType() != TokenType.SYMBOL) {
            return Optional.empty();
        }
        return Optional.of(this.token);
    }

    public Optional<String> identifier() {
        if (this.tokenType() != TokenType.IDENTIFIER) {
            return Optional.empty();
        }
        return Optional.of(this.token);
    }

    public Optional<String> intVal() {
        if (this.tokenType() != TokenType.INT_CONST) {
            return Optional.empty();
        }
        return Optional.of(this.token);
    }

    public Optional<String> stringVal() {
        if (this.tokenType() != TokenType.STRING_CONST) {
            return Optional.empty();
        }
        return Optional.of(this.token);
    }
}
