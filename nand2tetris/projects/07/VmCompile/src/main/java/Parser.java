import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final String COMMENT = "//";
    private List<String> asmList = new ArrayList<>();
    private int point = 0;
    // private boolean isFirst = true;

    Parser(Path path) {
        if (!Files.isReadable(path)) {
            throw new IllegalArgumentException("指定したファイルは読み取り不可です");
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;
                if (line.trim().substring(0, 2).equals(COMMENT)) continue;
                // コメント除外
                var slashPoint = line.indexOf(COMMENT);
                if (slashPoint > 0) {
                    line = line.substring(0, slashPoint);
                }
                this.asmList.add(line.trim());
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
