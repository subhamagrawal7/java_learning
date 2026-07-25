public class LogLevels {

    public static String message(String logLine) {
        /**
         * ^ starts with
         * \\[ starts with opening bracket
         * .*? one or more word characters present inside
         * \\]: ends with closing bracket and colon
         * \\s* any number of spaces
         * split and return the later part of the string.
         */
        String rawMessage = logLine.split("^\\[.*?\\]:\\s*")[1];

        // remove the special chars as well which are type of whitespace only + only
        // leading or trailing.
        return rawMessage.strip();

    }

    public static String logLevel(String logLine) {
        /**
         * If we would have used direct regex, it doesn't work
         * because when we split string by a delimiter at start of text, java
         * gives before the match and after the match -> thus giving "", <word>
         *
         * instead, split first by ending bracket. then split by opening.
         */
        String logPart = logLine.split("\\]")[0];
        String level = logPart.split("\\[")[1];
        return level.toLowerCase();
    }

    public static String reformat(String logLine) {
        return String.format("%s (%s)", message(logLine), logLevel(logLine));
    }
}
