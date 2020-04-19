package commands;

public class CallCommand extends AbstractCommand {
    private static CallCommand singleton = new CallCommand();

    private CallCommand() {}

    public static CallCommand getInstance() {
        return singleton;
    }

    public Command doCheck(String code) {
        if (code.length() >= 4 && code.substring(0, 4).equals("call")) {
            return this;
        }
        throw new IllegalArgumentException("コードが不正:" + code);
    }

    public CommandType getType() {
        return CommandType.C_CALL;
    }

    public String getHack(String code) {
        // TODO:
        return null;
    }
}
