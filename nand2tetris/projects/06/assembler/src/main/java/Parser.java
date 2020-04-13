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
                if (line.isEmpty()) continue;
                if (line.trim().substring(2).equals("//")) continue;
                asmList.add(line.trim());
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
        var asm = this.asmList.get(this.point);
        if (asm.indexOf(";") > 0) return CommandType.C_COMMAND;
        if (asm.substring(0).equals("(")) return CommandType.L_COMMAND;
        return CommandType.A_COMMAND;
    }
}
