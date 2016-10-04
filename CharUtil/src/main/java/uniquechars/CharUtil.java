package uniquechars;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Char util.
 */
public class CharUtil {

    /**
     * Gets unique chars of string.
     *
     * @param inputString the input string
     * @return substring of the unique chars of the input string
     */
    public static String getUniqueCharsOfString(String inputString) {

        Map<Character, Object> charMap = new HashMap<>();

        for (int i = 0; i < inputString.length(); i++) {

            Character c = inputString.charAt(i);

            // If current char already in charMap, then go to next char
            if (charMap.containsKey(c))
                continue;

            // Else put current char in map
            charMap.put(c, null);

        }

        // Get result string and format it
        String result = charMap.keySet().toString();
        result = result.substring(1, result.length() - 1).replace(", ", "");

        return result;
    }

    /**
     * Gets unique chars of string by nested loop.
     *
     * @param inputString the input string
     * @return substring of the unique chars of the input string
     */
    public static String getUniqueCharsOfStringBruteForce(String inputString) {
        char[] chars = inputString.toCharArray();
        StringBuilder uniqueCharsString = new StringBuilder();
        boolean repeat;

        for (int i = 0; i < chars.length; i++) {

            repeat = false;
            char current = chars[i];

            for (int j = i; j < chars.length; j++) {

                if (chars[i] == chars[j] && i != j) {

                    repeat = true;
                    break;

                } // end if

            } // end for

            // If current char duplicates, don't include it
            if (repeat) continue;

            uniqueCharsString.append(current);
        } // end for

        return uniqueCharsString.toString();
    }

    public static void main(String[] args) {
        for (String arg : args) {
            System.out.println("Unique chars of string: \"" + arg + "\" are: \n"
                    + getUniqueCharsOfString(arg));
        }
    }

}
