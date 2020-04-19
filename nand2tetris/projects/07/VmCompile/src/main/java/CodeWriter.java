import commands.CommandType;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class CodeWriter {
    private List<String> writeAsmList = new ArrayList<>();
    private String saveFileName;
    private int stackNo = 256;

    CodeWriter(String saveFileName) {
        this.saveFileName = saveFileName;
    }

    private int stackPush() {
        return this.stackNo++;
    }
    private int stackPop() {
        if (stackNo > 256) {
            return --this.stackNo;
        }
        return this.stackNo;
    }

    // TODO: 一旦適当に作成するため、codeはここで変換
    public void writeArithmetic(String code) {
        if (code.equals("add")) {
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("D=D+M");
        } else if (code.equals("sub")) {
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("D=D-M");
        } else if (code.equals("neg")) {
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=-M");
        } else if (code.equals("eq") ||
                   code.equals("gt") ||
                   code.equals("lt")) {

            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("D=D-M");
        } else if (code.equals("and")) {
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("D=D&M");
        } else if (code.equals("or")) {
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("D=D|M");
        }
        if (code.equals("not")) {
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=!M");
        }
    }

    // TODO: 一旦適当に作成するため、codeはここで変換
    public void writePushPop(CommandType commandType, String segment, int index) {
        switch(segment) {
            case "local":
                this.writeAsmList.add("@LCL");
                break;
            case "argument":
                this.writeAsmList.add("@ARG");
                break;
            case "this":
                this.writeAsmList.add("@THIS");
                break;
            case "pointer":
                this.writeAsmList.add("@THIS+" + Integer.toString(index));
                break;
            case "temp":
                this.writeAsmList.add("@THAT+" + Integer.toString(index));
                break;
            case "constant":
                this.writeAsmList.add("@" +  Integer.toString(index));
                this.writeAsmList.add("D=A");
                this.writeAsmList.add("@" + this.stackPush());
                this.writeAsmList.add("M=D");
                break;
            case "static":
                this.writeAsmList.add("@THAT");
                break;
            default:
                break;
        }
        if (commandType.equals(CommandType.C_PUSH)) {
            // this.writeAsmList.add("D=M");
            // this.writeAsmList.add("M=" + Integer.toString(index));
            // this.writeAsmList.add("D=A");
            // this.writeAsmList.add("D=A");
        } else if (commandType.equals(CommandType.C_POP)) {
            // this.writeAsmList.add("M=" + Integer.toString(index));
            // TODO: popは一旦適当に置いている
            this.stackPop();
            this.writeAsmList.add("D=M");
        }
    }

    public void save() {
        var writePath = Paths.get(saveFileName);
        try {
            System.out.println("path:" + writePath.toAbsolutePath());
            Files.write(writePath,
                    this.writeAsmList,
                    Charset.forName("UTF-8"),
                    StandardOpenOption.CREATE);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
