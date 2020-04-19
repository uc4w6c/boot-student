package commands;

public interface Command {
    public Command doCheck(String code);
    public CommandType getType();
    public String getArg1(String code);
    public String getArg2(String code);
    public String getHack(String code);
}
