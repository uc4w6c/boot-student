import commands.ArithMeticCommand;
import commands.Command;
import commands.CommandType;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final String COMMENT = "//";
    private List<String> codeList = new ArrayList<>();
    private int point = 0;
    private Command command = ArithMeticCommand.getInstance();

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
                this.codeList.add(line.trim());
            }
            this.setCommand();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean hasMoreCommands() {
        return (this.point < this.codeList.size());
    }

    public void advance() {
        if (this.point < this.codeList.size()) {
            this.point++;
            this.setCommand();
        }
    }

    private void setCommand() {
        if (this.hasMoreCommands()) {
            var code = this.codeList.get(this.point);
            this.command = ArithMeticCommand.getInstance().doCheck(code);
        }
    }

    public void moveFirst() {
        this.point = 0;
        this.setCommand();
    }

    public String getCode() {
        if (this.hasMoreCommands()) {
            return this.codeList.get(this.point);
        }
        return null;
    }

    public CommandType commandType() {
        return this.command.getType();
    }

    public String arg1() {
        var code = this.codeList.get(this.point);
        return this.command.getArg1(code);
    }

    public String arg2() {
        var code = this.codeList.get(this.point);
        return this.command.getArg2(code);
    }
}
