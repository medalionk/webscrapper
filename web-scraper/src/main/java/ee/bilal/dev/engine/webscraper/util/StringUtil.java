package ee.bilal.dev.engine.webscraper.util;

/**
 * Created by bilal90 on 9/27/2017.
 */
public final class StringUtil {
    private StringUtil() {
        throw new AssertionError();
    }

    /**
     * Validate if string property is null or empty
     * @param property to validate
     * @return true if null or empty
     */
    public static boolean isNullOrEmpty(String property){
        return property == null || property.trim().isEmpty();
    }

    /**
     * Replace white spaces in string
     * @param str original string
     * @param replacement string
     * @return modified string with spaces replaced
     */
    public static String replaceWhiteSpaces(String str, String replacement){
        return str.replaceAll("\\s+", replacement);
    }

    /**
     * Trim string with given width
     * @param s original string
     * @param width to trim
     * @return trimmed string
     */
    public static String trim(String s, int width) {
        if (s.length() > width){
            return s.substring(0, width-1) + ".";
        }

        return s;
    }

    /**
     * Get first nth characters in string
     * @param str original string
     * @param count of characters to return
     * @return first nth characters
     */
    public static String getFirstXCharsInStr(final String str, int count){
        if(str == null || str.trim().isEmpty()) {
            return null;
        }

        else if (str.length() > count) {
            return str.substring(0, count).trim();
        }

        return str;
    }

    /**
     * Convert string to title case
     * @param input original string
     * @return title case string
     */
    public static String toTitleCase(final String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }
}
