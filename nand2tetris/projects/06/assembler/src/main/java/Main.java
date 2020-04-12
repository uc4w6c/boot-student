// import java.io.File;

public class Main {
    public static void main(String... args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("引数を1つ入力してください");
        }
        String fileName = args[0];

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

