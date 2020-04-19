package commands;

public class ArithMeticCommand extends AbstractCommand {
    private static ArithMeticCommand singleton = new ArithMeticCommand();

    private ArithMeticCommand() {}

    public static ArithMeticCommand getInstance() {
        return singleton;
    }

    public Command doCheck(String code) {
        if (code.equals("add") ||
            code.equals("sub") ||
            code.equals("neg") ||
            code.equals("eq") ||
            code.equals("gt") ||
            code.equals("lt") ||
            code.equals("and") ||
            code.equals("or") ||
            code.equals("not")
        ) {
            return this;
        }
        return PushCommand.getInstance().doCheck(code);
    }

    public CommandType getType() {
        return CommandType.C_ARITHMETIC;
    }

    /**
     * コードをそのまま返却
     * @param code
     * @return code
     */
    public String getArg1(String code) {
        return code;
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
        if (code.equals("add")) {
            return "D=D+A";
        }
        if (code.equals("sub")) {
            return "D=D-A";
        }
        if (code.equals("neg")) {
            return "D=-D";
        }

        // MEMO: ここから下はあっているかわからない。8章での確認内容と思う
        if (code.equals("eq") ||
            code.equals("gt") ||
            code.equals("lt")) {

            return "D=D-A";
        }
        if (code.equals("and")) {
            return "D=D&A";
        }
        if (code.equals("or")) {
            return "D=D|A";
        }
        if (code.equals("not")) {
            return "D=!D";
        }
        return null;
    }
}
