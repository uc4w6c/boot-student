package commands;

public class ReturnCommand extends AbstractCommand {
    private static ReturnCommand singleton = new ReturnCommand();

    private ReturnCommand() {}

    public static ReturnCommand getInstance() {
        return singleton;
    }

    public Command doCheck(String code) {
        if (code.equals("return")) {
            return this;
        }
        return CallCommand.getInstance().doCheck(code);
    }

    public CommandType getType() {
        return CommandType.C_RETURN;
    }

    /**
     * nullを返却
     * @param code
     * @return null
     */
    public String getArg1(String code) {
        return null;
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
