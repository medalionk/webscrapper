package ee.bilal.dev.engine.webscraper.util;

public final class StringUtil {
    private StringUtil() {
    }

    public static boolean isNullOrEmpty(String property){
        return property == null || property.trim().isEmpty();
    }

    public static String replaceWhiteSpaces(String str, String replacement){
        return str.replaceAll("\\s+", replacement);
    }

    public static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }

    public static String getFirstXCharsInStr(final String str, int count){
        if(str == null || str.trim().isEmpty()) return null;

        if (str.length() > count) return str.substring(0, count).trim();
        else return str;
    }

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
