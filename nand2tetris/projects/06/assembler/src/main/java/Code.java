import java.util.HashMap;
import java.util.Map;

public class Code {
    private static final Map<String, String> DEST_MAP = Map.of(
            "M", "011",
            "D", "010",
            "MD",  "011",
            "A", "100",
            "AM", "101",
            "AD", "110",
            "AMD", "111"
    );
    private static final Map<String, String> COMP_MAP = new HashMap<>() {
        {
            put("0", "1101010");
            put("1", "1111111");
            put("-1", "111010");
            put("D", "1001100");
            put("A", "1110000");
            put("!D", "1001111");
            put("!A", "1110011");
            put("D+1", "1011111");
            put("A+1", "1110111");
            put("D-1", "1001110");
            put("A-1", "1110010");
            put("D+A", "1000010");
            put("D-A", "1110010");
            put("A-D", "1000111");
            put("D&A", "1000000");
            put("D|A", "1010101");
            put("M", "0110000");
            put("!M", "0110001");
            put("-M", "0110011");
            put("M+1", "0110111");
            put("M-1", "0110010");
            put("D+M", "0000010");
            put("D-M", "0010011");
            put("M-D", "0000111");
            put("D&M", "0000000");
            put("D|M", "0010101");
        }
    };
    private static final Map<String, String> JUMP_MAP = Map.of(
            "JGT", "001",
            "JEQ", "010",
            "JGE", "011",
            "JLT", "100",
            "JNE", "101",
            "JLE", "110",
            "JMP", "110"
    );

    public String dest(String neemock) {
        return this.DEST_MAP.get(neemock);
    }

    public String comp(String neemock) {
        return this.COMP_MAP.get(neemock);
    }

    public String jump(String neemock) {
        return this.JUMP_MAP.get(neemock);
    }
}
