package commands;

public class GotoCommand extends AbstractCommand {
    private static GotoCommand singleton = new GotoCommand();

    private GotoCommand() {}

    public static GotoCommand getInstance() {
        return singleton;
    }

    public Command doCheck(String code) {
        if (code.length() >= 4 && code.substring(0, 4).equals("goto")) {
            return this;
        }
        return IfCommand.getInstance().doCheck(code);
    }

    public CommandType getType() {
        return CommandType.C_GOTO;
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
