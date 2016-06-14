package oop.ex6.main;

import java.io.IOException;

/**
 * A class that execute ex6 program.
 */
public class Sjavac {

    private final static String FILE_ERROR_MESSAGE = "File does not exists.", LEGAL_FILE = "0";
    private final static String ILLEGAL_FILE = "1", FILE_ERROR = "2";

    /**
     * The main method of the ex6 exercise, that execute the program from start to end.
     * @param args The path of the s-java file.
     */
    public static void main(String[] args) {
        try {
            String path = args[0];
            Parser parser = new Parser();
            parser.analyzerFile(path);
            parser.parseMethods();
            System.out.println(LEGAL_FILE);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(FILE_ERROR_MESSAGE);
            System.out.println(FILE_ERROR);
        } catch (IllegalException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.out.println(ILLEGAL_FILE);
        }
    }
}
