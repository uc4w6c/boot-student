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
            this.writeAsmList.add("M=D+M");
            this.writeAsmList.add("@" + this.stackPop());
            this.writeAsmList.add("M=0");
        } else if (code.equals("sub")) {
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("M=D-M");
            this.writeAsmList.add("@" + this.stackPop());
            this.writeAsmList.add("M=0");
        } else if (code.equals("neg")) {
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("M=-M");
        } else if (code.equals("eq")) {

            // MEMO: 一旦作ったけど、もっといい方法がありそう
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("M=D-M");
            this.writeAsmList.add("@" + this.stackPop());
            this.writeAsmList.add("M=0");

            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
            this.writeAsmList.add("@EQ_FALSE" + (this.stackNo - 1));
            this.writeAsmList.add("D;JNE");

            this.writeAsmList.add("(EQ_TRUE" + (this.stackNo - 1) + ")");
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("M=1");
            this.writeAsmList.add("@EQ_END" + (this.stackNo - 1));
            this.writeAsmList.add("D;JMP");

            this.writeAsmList.add("(EQ_FALSE" + (this.stackNo - 1) + ")");
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("M=0");

            this.writeAsmList.add("(EQ_END" + (this.stackNo - 1) + ")");

        } else if (code.equals("gt")) {

            // MEMO: 一旦作ったけど、もっといい方法がありそう
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("M=D-M");
            this.writeAsmList.add("@" + this.stackPop());
            this.writeAsmList.add("M=0");

            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
            this.writeAsmList.add("@GT_FALSE" + (this.stackNo - 1));
            this.writeAsmList.add("D;JGT");

            this.writeAsmList.add("(GT_TRUE" + (this.stackNo - 1) + ")");
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("M=1");
            this.writeAsmList.add("@GT_END" + (this.stackNo - 1));
            this.writeAsmList.add("D;JMP");

            this.writeAsmList.add("(GT_FALSE" + (this.stackNo - 1) + ")");
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("M=0");

            this.writeAsmList.add("(GT_END" + (this.stackNo - 1) + ")");

        } else if (code.equals("lt")) {

            // MEMO: 一旦作ったけど、もっといい方法がありそう
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("M=D-M");
            this.writeAsmList.add("@" + this.stackPop());
            this.writeAsmList.add("M=0");

            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
            this.writeAsmList.add("@GT_FALSE" + (this.stackNo - 1));
            this.writeAsmList.add("D;JLT");

            this.writeAsmList.add("(LT_TRUE" + (this.stackNo - 1) + ")");
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("M=1");
            this.writeAsmList.add("@LT_END" + (this.stackNo - 1));
            this.writeAsmList.add("D;JMP");

            this.writeAsmList.add("(LT_FALSE" + (this.stackNo - 1) + ")");
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("M=0");

            this.writeAsmList.add("(LT_END" + (this.stackNo - 1) + ")");

        } else if (code.equals("and")) {
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("M=D&M");
            this.writeAsmList.add("@" + this.stackPop());
            this.writeAsmList.add("M=0");
        } else if (code.equals("or")) {
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("M=D|M");
            this.writeAsmList.add("@" + this.stackPop());
            this.writeAsmList.add("M=0");
        }
        if (code.equals("not")) {
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("M=!M");
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
