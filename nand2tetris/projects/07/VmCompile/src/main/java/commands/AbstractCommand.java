package commands;

abstract class AbstractCommand implements Command {
    public abstract Command doCheck(String code);
    public abstract CommandType getType();

    public String getArg1(String code) {
        return code.split(" ")[1];
    }

    public String getArg2(String code) {
        return code.split(" ")[2];
    }

    public abstract String getHack(String code);
}
