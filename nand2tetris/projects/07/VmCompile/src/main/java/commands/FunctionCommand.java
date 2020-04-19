package commands;

public class FunctionCommand extends AbstractCommand {
    private static FunctionCommand singleton = new FunctionCommand();

    private FunctionCommand() {}

    public static FunctionCommand getInstance() {
        return singleton;
    }

    public Command doCheck(String code) {
        if (code.length() >= 8 && code.substring(0, 8).equals("function")) {
            return this;
        }
        return ReturnCommand.getInstance().doCheck(code);
    }

    public CommandType getType() {
        return CommandType.C_FUNCTION;
    }

    public String getHack(String code) {
        // TODO:
        return null;
    }
}
