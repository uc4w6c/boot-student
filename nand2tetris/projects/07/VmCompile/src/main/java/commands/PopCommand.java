package commands;

public class PopCommand extends AbstractCommand {
    private static PopCommand singleton = new PopCommand();

    private PopCommand() {}

    public static PopCommand getInstance() {
        return singleton;
    }

    public Command doCheck(String code) {
        if (code.length() >= 3 && code.substring(0, 3).equals("pop")) {
            return this;
        }
        return LabelCommand.getInstance().doCheck(code);
    }

    public CommandType getType() {
        return CommandType.C_POP;
    }

    public String getHack(String code) {
        return "M=D";
    }
}
