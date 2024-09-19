package lovelace;

import java.util.Arrays;

/**
 * @author nasser
 */
public class Utils {


    public static final String WHITE_SPACE = "\\s+";

    public static String[] prepareCommand(String s) {
        if (s.isBlank()) {
            return new String[0];
        }
        return Arrays.stream(s.split(WHITE_SPACE))
                .filter(str -> !str.isBlank())
                .toArray(String[]::new);
    }

    public static String ensureWithNewLine(String s) {
        return s.charAt(s.length() - 1) != '\n' ? s + "\n": s;
    }
}
