import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Parser {
    private List<String> asmList;
    private int point = 0;
    private boolean isFirst = true;

    Parser(Path path) {
        if (Files.isReadable(path)) {
            throw new IllegalArgumentException("指定したファイルは読み取り不可です");
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                asmList.add(line);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean hasMoreCommands() {
        return (this.point < this.asmList.size());
    }

    public void advance() {
        if (this.isFirst) {
            this.isFirst = false;
            return;
        }
        if (this.point < this.asmList.size()) {
            this.point++;
        }
    }

    public CommandType commandType() {
        return CommandType.A_COMMAND;
    }
}
