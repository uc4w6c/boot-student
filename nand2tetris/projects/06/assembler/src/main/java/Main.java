// import java.io.File;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        /*
        TODO: java.nio のFilesに書き換えること
        File file = new File(fileName);
        if (file.exists()) {
            throw new IllegalArgumentException("指定したファイルは存在しません");
        }
        */

        System.out.println("Hello");
    }
}

