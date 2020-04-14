import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final String COMMENT = "//";
    private List<String> asmList = new ArrayList<String>();
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

    public boolean hasMoreCommands() {
        return (this.point < this.asmList.size());
    }

    public void advance() {
        /*
        if (this.isFirst) {
            this.isFirst = false;
            return;
        }
         */
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
        if (!this.commandType().equals(CommandType.C_COMMAND)) {
            return null;
        }
        var asm = this.asmList.get(this.point);
        var neemockPoint = asm.indexOf("=");
        // 先頭が = で始まる想定はない
        if (neemockPoint < 0) {
            return null;
        }
        return asm.substring(0, neemockPoint);
    }

    public String comp() {
        if (!this.commandType().equals(CommandType.C_COMMAND)) {
            return null;
        }
        var asm = this.asmList.get(this.point);
        var eqNeemockPoint = asm.indexOf("=");
        if (eqNeemockPoint > 0) {
            return asm.substring(eqNeemockPoint + 1);
        }
        var semicolonNeemockPoint = asm.indexOf(";");
        if (semicolonNeemockPoint > 0) {
            return asm.substring(0, semicolonNeemockPoint);
        }
        return null;

        /*
        var compNeemock = asm.substring(neemockPoint + 1);
        switch (compNeemock) {
            case "0":   return "1101010";
            case "1":   return "1111111";
            case "-1":  return "111010";
            case "D":   return "1001100";
            case "A":   return "1110000";
            case "!D":  return "1001111";
            case "!A":  return "1110011";
            case "D+1": return "1011111";
            case "A+1": return "1110111";
            case "D-1": return "1001110";
            case "A-1": return "1110010";
            case "D+A": return "1000010";
            case "D-A": return "1110010";
            case "A-D": return "1000111";
            case "D&A": return "1000000";
            case "D|A": return "1010101";
            case "M":   return "0110000";
            case "!M":  return "0110001";
            case "-M":  return "0110011";
            case "M+1": return "0110111";
            case "M-1": return "0110010";
            case "D+M": return "0000010";
            case "D-M": return "0010011";
            case "M-D": return "0000111";
            case "D&M": return "0000000";
            case "D|M": return "0010101";
        }
        // ここには遷移しないが、Exception返却した方がいいかも
        return null;
         */
    }

    public String jump() {
        if (!this.commandType().equals(CommandType.C_COMMAND)) {
            return null;
        }
        var asm = this.asmList.get(this.point);
        var neemockPoint = asm.indexOf(";");
        if (neemockPoint < 0) {
            return null;
        }
        return asm.substring(neemockPoint + 1);
        /*
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
        // ここには遷移しないが、Exception返却した方がいいかも
        return null;
         */
    }
}
