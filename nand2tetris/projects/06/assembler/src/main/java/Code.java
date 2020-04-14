import java.util.HashMap;
import java.util.Map;

public class Code {
    private static final Map<String, String> DEST_MAP = Map.of(
            "M", "001",
            "D", "010",
            "MD",  "011",
            "A", "100",
            "AM", "101",
            "AD", "110",
            "AMD", "111"
    );
    private static final Map<String, String> COMP_MAP = new HashMap<>() {
        {
            put("0", "0101010");
            put("1", "0111111");
            put("-1", "0111010");
            put("D", "0001100");
            put("A", "0110000");
            put("!D", "0001111");
            put("!A", "0110011");
            put("D+1", "0011111");
            put("A+1", "0110111");
            put("D-1", "0001110");
            put("A-1", "0110010");
            put("D+A", "0000010");
            put("D-A", "0110010");
            put("A-D", "0000111");
            put("D&A", "0000000");
            put("D|A", "0010101");
            put("M", "1110000");
            put("!M", "1110001");
            put("-M", "1110011");
            put("M+1", "1110111");
            put("M-1", "1110010");
            put("D+M", "1000010");
            put("D-M", "1010011");
            put("M-D", "1000111");
            put("D&M", "1000000");
            put("D|M", "1010101");
        }
    };
    private static final Map<String, String> JUMP_MAP = Map.of(
            "JGT", "001",
            "JEQ", "010",
            "JGE", "011",
            "JLT", "100",
            "JNE", "101",
            "JLE", "110",
            "JMP", "111"
    );

    public String dest(String neemock) {
        var binary = this.DEST_MAP.get(neemock);
        return (binary == null) ? "000" : binary;
    }

    public String comp(String neemock) {
        var binary = this.COMP_MAP.get(neemock);
        return (binary == null) ? "000" : binary;
    }

    public String jump(String neemock) {
        var binary = this.JUMP_MAP.get(neemock);
        return (binary == null) ? "000" : binary;
    }
}
