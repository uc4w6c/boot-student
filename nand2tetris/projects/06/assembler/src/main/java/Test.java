import java.util.Map;

public class Test {
    public static void main(String[] args) {
        var map = Map.of("aa", "aa");
        if (map.get("bb") == null) {
            System.out.println("Null");
        }
    }
}
