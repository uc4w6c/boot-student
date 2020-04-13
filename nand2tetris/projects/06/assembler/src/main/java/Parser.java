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
                if (line.trim().substring(0, 2).equals("//")) continue;
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
        if (asm.substring(0, 1).equals("@")) return CommandType.A_COMMAND;
        if (asm.substring(0, 1).equals("(")) return CommandType.L_COMMAND;
        return CommandType.C_COMMAND;
    }

    public String symbol() {
        if (this.commandType().equals(CommandType.A_COMMAND)) {
            return this.asmList.get(this.point).substring(1);
        }
        if (this.commandType().equals(CommandType.L_COMMAND)) {
            return this.asmList.get(this.point)
                                .replace("", "(")
                                .replace("", ")");
        }
        return null;
    }

    public String dest() {
        // TODO: 実装すること
        // this.asmList.get(this.point);
        return null;
    }

    public String comp() {

    }

    public String jump() {
        var asm = this.asmList.get(this.point);
        var neemockPoint = asm.indexOf(";");
        if (neemockPoint < 0) {
            return null;
        }
        var jumpNeemock = asm.substring(neemockPoint + 1);

        switch(jumpNeemock) {
            case "JGT": return "001";
            case "JEQ": return "010";
            case "JGE": return "011";
            case "JLT": return "100";
            case "JNE": return "101";
            case "JLE": return "110";
            case "JMP": return "110";
            default:
        }
        // ここには遷移しない
        return null;
    }
}
