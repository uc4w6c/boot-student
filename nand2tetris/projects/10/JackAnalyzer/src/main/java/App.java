import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("引数を1つ入力してください");
        }
        String fileName = args[0];
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            throw new IllegalArgumentException("指定したファイルは存在しません\r\n" +
                    "FileName:" + fileName);
        }

        List<String> filePaths = new ArrayList<>();
        Optional<String> outDirPath = null;
        if (Files.isDirectory(path)) {
            try {
                var jackFilePaths = Files.list(path)
                        .filter(file -> file.getFileName().toString().endsWith(".jack"))
                        .map(file -> file.getFileName().toString())
                        .collect(Collectors.toList());
                filePaths.addAll(jackFilePaths);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            outDirPath = Optional.of(path.toString());
        } else {
            filePaths.add(path.getFileName().toString());
            outDirPath = Optional.empty();
        }

        for (String filePath : filePaths) {
            new CompilationEngine(outDirPath, filePath).save();
        }
        System.out.println("End");
    }
}
