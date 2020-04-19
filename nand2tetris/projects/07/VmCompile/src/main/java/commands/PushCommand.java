package commands;

public class PushCommand extends AbstractCommand {
    private static PushCommand singleton = new PushCommand();

    private PushCommand() {}

    public static PushCommand getInstance() {
        return singleton;
    }

    public Command doCheck(String code) {
        if (code.length() >= 4 && code.substring(0, 4).equals("push")) {
            return this;
        }
        return PopCommand.getInstance().doCheck(code);
    }

    public CommandType getType() {
        return CommandType.C_PUSH;
    }

    public String getHack(String code) {
        return null;
    }
}
