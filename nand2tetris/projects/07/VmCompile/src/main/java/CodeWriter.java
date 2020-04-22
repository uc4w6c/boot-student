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
            this.writeAsmList.add("M=M-D");
            this.writeAsmList.add("@" + this.stackPop());
            this.writeAsmList.add("M=0");

        } else if (code.equals("neg")) {
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("M=-M");
            /*
            this.writeAsmList.add("D=-M");
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("M=D");
            */

        } else if (code.equals("eq")) {

            // MEMO: 一旦作ったけど、もっといい方法がありそう
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
            /*
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("M=D-M");
            this.writeAsmList.add("@" + this.stackPop());
            this.writeAsmList.add("M=0");

            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
             */
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("D=D-M");
            this.writeAsmList.add("@EQ_TRUE" + (this.stackNo - 2));
            this.writeAsmList.add("D;JEQ");

            this.writeAsmList.add("(EQ_FALSE" + (this.stackNo - 2) + ")");
            this.writeAsmList.add("@" + (this.stackNo - 2));
            // this.writeAsmList.add("M=0");
            this.writeAsmList.add("M=0");

            this.writeAsmList.add("@EQ_END" + (this.stackNo - 2));
            this.writeAsmList.add("D;JMP");

            this.writeAsmList.add("(EQ_TRUE" + (this.stackNo - 2) + ")");
            this.writeAsmList.add("@" + (this.stackNo - 2));
            // this.writeAsmList.add("M=1");
            this.writeAsmList.add("M=-1");

            this.writeAsmList.add("(EQ_END" + (this.stackNo - 2) + ")");
            this.writeAsmList.add("@" + this.stackPop());
            this.writeAsmList.add("M=0");

        } else if (code.equals("gt")) {

            // MEMO: 一旦作ったけど、もっといい方法がありそう
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
            /*
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("M=D-M");
            this.writeAsmList.add("@" + this.stackPop());
            this.writeAsmList.add("M=0");

            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
             */

            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("D=M-D");
            this.writeAsmList.add("@GT_TRUE" + (this.stackNo - 2));
            this.writeAsmList.add("D;JGT");

            this.writeAsmList.add("(GT_FALSE" + (this.stackNo - 2) + ")");
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("M=0");
            this.writeAsmList.add("@GT_END" + (this.stackNo - 2));
            this.writeAsmList.add("D;JMP");

            this.writeAsmList.add("(GT_TRUE" + (this.stackNo - 2) + ")");
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("M=-1");

            this.writeAsmList.add("(GT_END" + (this.stackNo - 2) + ")");
            this.writeAsmList.add("@" + this.stackPop());
            this.writeAsmList.add("M=0");

        } else if (code.equals("lt")) {

            // MEMO: 一旦作ったけど、もっといい方法がありそう
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
            /*
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("M=D-M");
            this.writeAsmList.add("@" + this.stackPop());
            this.writeAsmList.add("M=0");

            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");
             */
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("D=M-D");

            this.writeAsmList.add("@LT_TRUE" + (this.stackNo - 2));
            this.writeAsmList.add("D;JLT");

            this.writeAsmList.add("(LT_FALSE" + (this.stackNo - 2) + ")");
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("M=0");
            this.writeAsmList.add("@LT_END" + (this.stackNo - 2));
            this.writeAsmList.add("D;JMP");


            this.writeAsmList.add("(LT_TRUE" + (this.stackNo - 2) + ")");
            this.writeAsmList.add("@" + (this.stackNo - 2));
            this.writeAsmList.add("M=-1");

            this.writeAsmList.add("(LT_END" + (this.stackNo - 2) + ")");
            this.writeAsmList.add("@" + this.stackPop());
            this.writeAsmList.add("M=0");

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
            /*
            this.writeAsmList.add("D=!M");
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("M=D");
             */
        }
    }

    // TODO: 一旦適当に作成するため、codeはここで変換
    public void writePushPop(CommandType commandType, String segment, int index) {
        if (commandType.equals(CommandType.C_PUSH)) {
            // this.writeAsmList.add("D=M");
            // this.writeAsmList.add("M=" + Integer.toString(index));
            // this.writeAsmList.add("D=A");
            // this.writeAsmList.add("D=A");
            switch(segment) {
                case "local":
                    this.writeAsmList.add("@LCL");
                    this.writeAsmList.add("A=M");
                    for (int i = 0; i < index; i++) {
                        this.writeAsmList.add("A=A+1");
                    }
                    this.writeAsmList.add("D=M");
                    break;
                case "argument":
                    this.writeAsmList.add("@ARG");
                    this.writeAsmList.add("A=M");
                    for (int i = 0; i < index; i++) {
                        this.writeAsmList.add("A=A+1");
                    }
                    this.writeAsmList.add("D=M");
                    break;
                case "this":
                    this.writeAsmList.add("@THIS");
                    this.writeAsmList.add("A=M");
                    for (int i = 0; i < index; i++) {
                        this.writeAsmList.add("A=A+1");
                    }
                    this.writeAsmList.add("D=M");
                    break;
                case "that":
                    this.writeAsmList.add("@THAT");
                    this.writeAsmList.add("A=M");
                    for (int i = 0; i < index; i++) {
                        this.writeAsmList.add("A=A+1");
                    }
                    this.writeAsmList.add("D=M");
                    break;
                case "pointer":
                    this.writeAsmList.add("@3");
                    // ここは絶対位置指定なので A=Aになる
                    this.writeAsmList.add("A=A");
                    for (int i = 0; i < index; i++) {
                        this.writeAsmList.add("A=A+1");
                    }
                    this.writeAsmList.add("D=M");
                    break;
                case "temp":
                    this.writeAsmList.add("@5");
                    // ここは絶対位置指定なので A=Aになる
                    this.writeAsmList.add("A=A");
                    for (int i = 0; i < index; i++) {
                        this.writeAsmList.add("A=A+1");
                    }
                    this.writeAsmList.add("D=M");
                    break;
                case "constant":
                    this.writeAsmList.add("@" +  Integer.toString(index));
                    this.writeAsmList.add("D=A");
                    break;
                case "static":
                    // this.writeAsmList.add("@THAT");
                    // this.writeAsmList.add("D=M");
                    this.writeAsmList.add("@" + saveFileName + "." + index);
                    this.writeAsmList.add("D=M");
                    break;
                default:
                    break;
            }
            this.writeAsmList.add("@" + this.stackPush());
            this.writeAsmList.add("M=D");

        } else if (commandType.equals(CommandType.C_POP)) {
            this.writeAsmList.add("@" + (this.stackNo - 1));
            this.writeAsmList.add("D=M");

            switch(segment) {
                case "local":
                    this.writeAsmList.add("@LCL");
                    this.writeAsmList.add("A=M");
                    for (int i = 0; i < index; i++) {
                        this.writeAsmList.add("A=A+1");
                    }
                    this.writeAsmList.add("M=D");
                    break;
                case "argument":
                    // this.writeAsmList.add("@ARG");
                    // this.writeAsmList.add("M=D");
                    this.writeAsmList.add("@ARG");
                    this.writeAsmList.add("A=M");
                    for (int i = 0; i < index; i++) {
                        this.writeAsmList.add("A=A+1");
                    }
                    this.writeAsmList.add("M=D");
                    break;
                case "this":
                    this.writeAsmList.add("@THIS");
                    this.writeAsmList.add("A=M");
                    for (int i = 0; i < index; i++) {
                        this.writeAsmList.add("A=A+1");
                    }
                    this.writeAsmList.add("M=D");
                    break;
                case "that":
                    this.writeAsmList.add("@THAT");
                    this.writeAsmList.add("A=M");
                    for (int i = 0; i < index; i++) {
                        this.writeAsmList.add("A=A+1");
                    }
                    this.writeAsmList.add("M=D");
                    break;
                case "pointer":
                    // TODO: この処理は怪しい
                    this.writeAsmList.add("@3");
                    for (int i = 0; i < index; i++) {
                        this.writeAsmList.add("A=A+1");
                    }
                    this.writeAsmList.add("M=D");
                    break;
                case "temp":
                    // TODO: この処理は怪しい
                    this.writeAsmList.add("@5");
                    for (int i = 0; i < index; i++) {
                        this.writeAsmList.add("A=A+1");
                    }
                    this.writeAsmList.add("M=D");
                    break;
                case "constant":
                    // ここには遷移しないはず
                    break;
                case "static":
                    this.writeAsmList.add("@" + saveFileName + "." + index);
                    this.writeAsmList.add("M=D");
                    // this.writeAsmList.add("D=M");
                    // this.writeAsmList.add("@" + (this.stackNo - 1));
                    // this.writeAsmList.add("M=D");
                    break;
                default:
                    break;
            }

            this.writeAsmList.add("@" + this.stackPop());
            this.writeAsmList.add("M=0");
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
