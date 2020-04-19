package commands;

public class LabelCommand extends AbstractCommand {
    private static LabelCommand singleton = new LabelCommand();

    private LabelCommand() {}

    public static LabelCommand getInstance() {
        return singleton;
    }

    public Command doCheck(String code) {
        if (code.length() >= 5 && code.substring(0, 5).equals("label")) {
            return this;
        }
        return GotoCommand.getInstance().doCheck(code);
    }

    public CommandType getType() {
        return CommandType.C_LABEL;
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
