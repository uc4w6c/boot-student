package commands;

public class IfCommand extends AbstractCommand {
    private static IfCommand singleton = new IfCommand();

    private IfCommand() {}

    public static IfCommand getInstance() {
        return singleton;
    }

    public Command doCheck(String code) {
        if (code.length() >= 2 && code.substring(0, 2).equals("if")) {
            return this;
        }
        return FunctionCommand.getInstance().doCheck(code);
    }

    public CommandType getType() {
        return CommandType.C_IF;
    }

    /**
     * nullを返却
     * @param code
     * @return null
     */
    public String getArg2(String code) {
        return null;
    }

    public String getHack(String code) {
        // TODO:
        return null;
    }
}
