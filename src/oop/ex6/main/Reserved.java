package oop.ex6.main;

/**
 * Created by David and Roi.
 */
class Reserved {

    private static final String[] RESERVED_KEYWORDS = {"void", "final", "if", "while", "true", "false", "return",
            "int", "double", "boolean", "char", "String"};

    /**
     * Check if the name is in the reserved keyboard of s-java list.
     *
     * @param name The name to check.
     * @return true if the name in the reserved keyboard, false otherwise.
     */
    static boolean isReserved(String name) {
        for (String keyword : RESERVED_KEYWORDS) {
            if (name.equals(keyword))
                return true;
        }
        return false;
    }
}
