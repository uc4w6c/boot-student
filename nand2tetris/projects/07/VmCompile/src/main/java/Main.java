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
        String outDirPath = null;
        String outFileName = null;
        if (Files.isDirectory(path)) {
            try {
                /*
                System.out.println("Start");
                System.out.println("Path:" + path.toString());
                 */
                /*
                Files.list(path)
                        .forEach(System.out::println);
                */
                /*
                Files.list(path)
                        .filter(fileName -> fileName.toString().endsWith("Sys.vm"))
                        .forEach(System.out::println);
                Files.list(path)
                        .filter(fileName -> !fileName.toString().endsWith("Sys.vm"))
                        .filter(fileName -> fileName.toString().endsWith(".vm"))
                        .forEach(System.out::println);
                System.out.println("End");
                */
                var mainFilePaths = Files.list(path)
                                .filter(fileName -> fileName.toString().endsWith("Sys.vm"))
                                .collect(Collectors.toList());
                filePaths.addAll(mainFilePaths);
                var notMainFilePaths = Files.list(path)
                                .filter(fileName -> !fileName.toString().endsWith("Sys.vm"))
                                .filter(fileName -> fileName.toString().endsWith(".vm"))
                                .collect(Collectors.toList());
                filePaths.addAll(notMainFilePaths);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            outDirPath = path.toString();
            outFileName = path.toString() + ".asm";
        } else {
            filePaths.add(path);
            outDirPath = null;
            outFileName = path.toString().replace(".vm", ".asm");
        }

        CodeWriter codeWriter = new CodeWriter(outDirPath, outFileName);
        for(Path filePath : filePaths) {
            System.out.println("filePath:" + filePath.toString());
            Parser parser = new Parser(filePath);
            while (parser.hasMoreCommands()) {
                if (parser.commandType().equals(CommandType.C_PUSH) ||
                    parser.commandType().equals(CommandType.C_POP)) {

                    codeWriter.writePushPop(parser.commandType(),
                                            parser.arg1(),
                                            Integer.parseInt(parser.arg2()));
                } else if (parser.commandType().equals(CommandType.C_ARITHMETIC)) {
                    codeWriter.writeArithmetic(parser.getCode());
                } else if (parser.commandType().equals(CommandType.C_LABEL)) {
                    codeWriter.writeLabel(parser.arg1());
                } else if (parser.commandType().equals(CommandType.C_GOTO)) {
                    codeWriter.writeGoto(parser.arg1());
                } else if (parser.commandType().equals(CommandType.C_IF)) {
                    codeWriter.writeIf(parser.arg1());
                } else if (parser.commandType().equals(CommandType.C_RETURN)) {
                    codeWriter.writeReturn();
                } else if (parser.commandType().equals(CommandType.C_FUNCTION)) {
                    codeWriter.writeFunction(parser.arg1(), Integer.parseInt(parser.arg2()));
                } else if (parser.commandType().equals(CommandType.C_CALL)) {
                    codeWriter.writeCall(parser.arg1(), Integer.parseInt(parser.arg2()));
                }
                parser.advance();
            }
        }
        codeWriter.save();
        System.out.println("end");
    }
}
