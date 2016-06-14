package oop.ex6.main;

/**
 * A class that represent a generic type 1 error that can be occur in the compilation.
 */
class IllegalException extends Exception {

    private final static String ERROR_MESSAGE = "error in line:";

    /**
     * The constructor.
     *
     * @param message The thing that cause the error.
     * @param line    The number of the line that the error happened.
     */
    IllegalException(String message, int line) {
        super(ERROR_MESSAGE + " " + line + ", \t" + message);
    }
}
