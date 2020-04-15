// import java.io.File;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

// なんか全体的にコードが汚いな・・・
public class Main {
    public static void main(String... args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("引数を1つ入力してください");
        }
        String fileName = args[0];
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("指定したファイルは存在しません\r\n" +
                                                "FileName:" + fileName);
        }
        var parser = new Parser(path);
        if (!parser.hasMoreCommands()) {
            throw new IllegalArgumentException("指定したファイルは読み取り出来ません\r\n" +
                                                "FileName:" + fileName);
        }

        // シンボルテーブルの作成
        var symbolTable = new SymbolTable();
        Integer symbolNo = 0;
        while (parser.hasMoreCommands()) {
            if (parser.commandType().equals(CommandType.L_COMMAND)) {
                // System.out.println("L Symbol:" + parser.symbol());
                symbolTable.addEntry(parser.symbol(), symbolNo);

            } else if (parser.commandType().equals(CommandType.A_COMMAND) ||
                parser.commandType().equals(CommandType.C_COMMAND)) {

                symbolNo++;
            }
            parser.advance();
        }
        parser.moveFirst();

        // String型が数値かチェック
        Predicate<String> isDigit = (symbol) -> {
            try {
                Integer.parseInt(symbol);
                return true;
            } catch (NumberFormatException nfex) {
                return false;
            }
        };

        // くそコードだな・・・。
        symbolNo = 16; // 変数は16以降に格納
        var writeByteList = new ArrayList<String>();
        var code = new Code();
        while (parser.hasMoreCommands()) {
            if (parser.commandType().equals(CommandType.A_COMMAND)) {
                var symbol = parser.symbol();
                var binaryNum = 0;
                if (isDigit.test(symbol)) {
                    binaryNum = Integer.parseInt(parser.symbol());
                } else {
                    // System.out.println("A Symbol:" + symbol);
                    if (symbolTable.contains(symbol)) {
                        binaryNum = symbolTable.getAddress(symbol);
                    } else {
                        symbolTable.addEntry(symbol, symbolNo);
                        binaryNum = symbolNo;
                        symbolNo++;
                    }
                }
                writeByteList.add("0" +
                                    String.format("%15s", Integer.toBinaryString(binaryNum))
                                            .replace(" ", "0"));

            } else  if (parser.commandType().equals(CommandType.C_COMMAND)) {
                var binary = "111";
                /*
                System.out.println("row:" + parser.dest() + ", " + parser.comp() + ", " + parser.jump());
                System.out.println("row:"
                                    + ((parser.dest() == null) ? "000" : code.dest(parser.dest())) + ", "
                                    + ((parser.comp() == null) ? "000" : code.comp(parser.comp())) + ", "
                                    + ((parser.jump() == null) ? "000" : code.jump(parser.jump())));
                 */
                binary += (parser.comp() == null) ? "000" : code.comp(parser.comp());
                binary += (parser.dest() == null) ? "000" : code.dest(parser.dest());
                binary += (parser.jump() == null) ? "000" : code.jump(parser.jump());
                writeByteList.add(binary);

            }
            // 以下は何もしない
            // else if (parser.commandType().equals(CommandType.L_COMMAND)) { }
            parser.advance();
        }

        if (writeByteList.size() == 0) {
            throw new IllegalArgumentException("出力データはありません。");
        }

        var writePath = Paths.get(fileName.replace(".asm", ".hack"));
        try {
            System.out.println("path:" + writePath.toAbsolutePath());
            Files.write(writePath,
                        writeByteList,
                        Charset.forName("UTF-8"),
                        StandardOpenOption.CREATE);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println("END");
    }
}

