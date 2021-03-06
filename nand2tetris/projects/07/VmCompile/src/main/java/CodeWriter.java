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
    private String saveDirPath;
    private String saveFileName;
    private int stackNo = 256;
    private int labelNumForReturnAddress = 0;

    CodeWriter(String saveDirPath, String saveFileName) {
        this.saveDirPath = saveDirPath;
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

    public void writeLabel(String label) {
        this.writeAsmList.add("(" + label + ")");
    }

    public void writeGoto(String label) {
        this.writeAsmList.add("@" + label);
        this.writeAsmList.add("0;JMP");
    }

    public void writeIf(String label) {
        this.writeAsmList.add("@" + label);
        this.writeAsmList.add("D;JGT");
    }

    public void writeCall(String functionName, int numArgs) {
        // push return-address
        this.writeAsmList.add("@RETURN_ADDRESS_" + this.labelNumForReturnAddress);
        this.writeAsmList.add("D=A");
        this.writeAsmList.add("@" + this.stackPush());
        this.writeAsmList.add("M=D");

        // push LCL
        this.writeAsmList.add("@LCL");
        // this.writeAsmList.add("A=M");
        this.writeAsmList.add("D=M");
        this.writeAsmList.add("@" + this.stackPush());
        this.writeAsmList.add("M=D");

        // push ARG
        this.writeAsmList.add("@ARG");
        // this.writeAsmList.add("A=M");
        this.writeAsmList.add("D=M");
        this.writeAsmList.add("@" + this.stackPush());
        this.writeAsmList.add("M=D");

        // push THIS
        this.writeAsmList.add("@THIS");
        // this.writeAsmList.add("A=M");
        this.writeAsmList.add("D=M");
        this.writeAsmList.add("@" + this.stackPush());
        this.writeAsmList.add("M=D");

        // push THAT
        this.writeAsmList.add("@THAT");
        this.writeAsmList.add("A=M");
        this.writeAsmList.add("D=M");
        this.writeAsmList.add("@" + this.stackPush());
        this.writeAsmList.add("M=D");

        // ARG = SP - n - 5
        // this.writeAsmList.add("@SP"); // SPで管理していない・・・から
        // this.writeAsmList.add("D=M");
        this.writeAsmList.add("@" + this.stackNo);
        this.writeAsmList.add("D=A");
        for (int i = 0; i < numArgs + 5; i++) {
            this.writeAsmList.add("D=D-1");
        }
        this.writeAsmList.add("@ARG");
        this.writeAsmList.add("M=D");
         /*
        this.writeAsmList.add("A=M");
        for (int i = 0; i < numArgs + 5; i++) {
            this.writeAsmList.add("A=A-1");
        }
        this.writeAsmList.add("@ARG");
        this.writeAsmList.add("M=A");
         */

        // LCL = SP
        // this.writeAsmList.add("@SP"); // SPで管理していない・・・から
        // this.writeAsmList.add("D=M");
        this.writeAsmList.add("@" + this.stackNo);
        this.writeAsmList.add("D=A");
        this.writeAsmList.add("@LCL");
        this.writeAsmList.add("M=D");

        // goto f
        this.writeAsmList.add("@" + functionName);
        this.writeAsmList.add("0;JMP");

        // (return-address)
        this.writeAsmList.add("(RETURN_ADDRESS_" +
                                    this.labelNumForReturnAddress++ + ")");
    }

    public void writeReturn() {
        // FRAME = LCL
        this.writeAsmList.add("@LCL");
        this.writeAsmList.add("D=M");
        this.writeAsmList.add("@R13");
        this.writeAsmList.add("M=D");

        // RET = *(FRAME-5)
        /*
        this.writeAsmList.add("@R13");
        this.writeAsmList.add("A=M");
        for (int i = 0; i < 5; i++) {
            this.writeAsmList.add("A=A-1");
        }
        this.writeAsmList.add("D=M");
         */
        this.writeAsmList.add("@5");
        this.writeAsmList.add("D=A");
        this.writeAsmList.add("@R13");
        this.writeAsmList.add("A=M-D");
        this.writeAsmList.add("D=M");

        this.writeAsmList.add("@R14");
        this.writeAsmList.add("M=D");

        // *ARG = pop()
        this.writeAsmList.add("@" + this.stackPop());
        this.writeAsmList.add("D=M");
        this.writeAsmList.add("@ARG");
        this.writeAsmList.add("A=M");
        this.writeAsmList.add("M=D");

        // SP = ARG + 1
        this.writeAsmList.add("@ARG");
        this.writeAsmList.add("D=M");
        this.writeAsmList.add("@SP");
        this.writeAsmList.add("M=D+1");

        // THAT = *(FRAME-1)
        this.writeAsmList.add("@R13");
        /*
        this.writeAsmList.add("A=M");
        this.writeAsmList.add("A=A-1");
         */
        this.writeAsmList.add("AM=M-1");
        this.writeAsmList.add("D=M");
        this.writeAsmList.add("@THAT");
        this.writeAsmList.add("M=D");

        // THIS = *(FRAME-2)
        this.writeAsmList.add("@R13");
        /*
        this.writeAsmList.add("A=M");
        for (int i = 0; i < 2; i++) {
            this.writeAsmList.add("A=A-1");
        }
         */
        this.writeAsmList.add("M=M-1");
        this.writeAsmList.add("D=M");
        this.writeAsmList.add("@THIS");
        this.writeAsmList.add("M=D");

        // ARG = *(FRAME-3)
        this.writeAsmList.add("@R13");
        /*
        this.writeAsmList.add("A=M");
        for (int i = 0; i < 3; i++) {
            this.writeAsmList.add("A=A-1");
        }
         */
        this.writeAsmList.add("M=M-1");
        this.writeAsmList.add("D=M");
        this.writeAsmList.add("@ARG");
        this.writeAsmList.add("M=D");

        // LCL = *(FRAME-4)
        this.writeAsmList.add("@R13");
        /*
        this.writeAsmList.add("A=M");
        for (int i = 0; i < 4; i++) {
            this.writeAsmList.add("A=A-1");
        }
         */
        this.writeAsmList.add("M=M-1");
        this.writeAsmList.add("D=M");
        this.writeAsmList.add("@LCL");
        this.writeAsmList.add("M=D");

        // goto RET
        this.writeAsmList.add("@R14");
        this.writeAsmList.add("A=M");
        this.writeAsmList.add("D;JGT");
    }

    public void writeFunction(String functionName, int numLocals) {
        this.writeLabel(functionName);
        /*
        for (int i = 0; i < numLocals; i++) {
            this.writeAsmList.add("@LCL");
            this.writeAsmList.add("A=M");
            for (int j = 0; j < numLocals; j++) {
                this.writeAsmList.add("A=A+1");
            }
            this.writeAsmList.add("@0");
            this.writeAsmList.add("M=0");
        }
         */
        this.writeAsmList.add("D=0");
        for (int i = 0; i < numLocals; i++) {
            this.writeAsmList.add("@" + this.stackPush());
            this.writeAsmList.add("M=D");
        }
    }

    public void save() {
        String savePath = (saveDirPath == null) ?
                            this.saveFileName :
                            (this.saveDirPath + "/" + this.saveFileName);
        var writePath = Paths.get(savePath);
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
