package top.archiem.jmp.util.modrinth;

class StringUtil {
    /**
     * Removes leading whitespace and the margin prefix '|' from each line of the given text.
     * This mimics Kotlin's String.trimMargin() with '|' as the default prefix.
     *
     * @param text The input string, potentially a multi-line text block.
     * @return The string with margins trimmed.
     */
    public static String trimMargin(String text) {
        // (?m) enables multi-line mode for ^
        // ^\\s*\\| matches the start of a line, followed by any whitespace, then a '|'
        return text.replaceAll("(?m)^\\s*\\|", "");
    }
}
