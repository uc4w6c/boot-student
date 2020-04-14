// import java.io.File;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

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

        var writeByteList = new ArrayList<String>();
        var code = new Code();
        while (parser.hasMoreCommands()) {
            if (parser.commandType().equals(CommandType.A_COMMAND)) {
                writeByteList.add(
                        Integer.toBinaryString(
                                Integer.parseInt(parser.symbol())));
            }
            if (parser.commandType().equals(CommandType.C_COMMAND)) {
                var binary = code.dest(parser.dest());
                binary += code.comp(parser.comp());
                binary += code.jump(parser.jump());
                writeByteList.add(binary);
            }
            parser.advance();
        }

        if (writeByteList.size() == 0) {
            throw new IllegalArgumentException("出力データはありません。");
        }

        var writePath = Paths.get(fileName.replace(".asam", ".hack"));
        try {
            Files.write(writePath,
                        writeByteList,
                        Charset.forName("UTF-8"),
                        StandardOpenOption.WRITE);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println("END");
    }
}

