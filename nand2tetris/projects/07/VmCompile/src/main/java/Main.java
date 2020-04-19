import commands.CommandType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("引数を1つ入力してください");
        }
        String pathName = args[0];
        Path path = Paths.get(pathName);
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("指定したファイルは存在しません\r\n" +
                    "FileName:" + pathName);
        }
        List<Path> filePaths = new ArrayList<>();
        String outputPath = null;
        if (Files.isDirectory(path)) {
            try {
                filePaths = Files.list(path)
                                .filter(fileName -> fileName.endsWith(".csv"))
                                .collect(Collectors.toList());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            outputPath = path + "asm";
        } else {
            filePaths.add(path);
            outputPath = path.toString().replace(".vm", ".asm");
        }

        CodeWriter codeWriter = new CodeWriter(outputPath);
        for(Path filePath : filePaths) {
            Parser parser = new Parser(filePath);
            while (parser.hasMoreCommands()) {
                if (parser.commandType().equals(CommandType.C_PUSH) ||
                    parser.commandType().equals(CommandType.C_POP)) {

                    codeWriter.writePushPop(parser.commandType(),
                                            parser.arg1(),
                                            Integer.parseInt(parser.arg2()));
                } else if (parser.commandType().equals(CommandType.C_ARITHMETIC)) {
                    codeWriter.writeArithmetic(parser.getCode());
                }
                parser.advance();
            }
        }
        codeWriter.save();
        System.out.println("end");
    }
}
