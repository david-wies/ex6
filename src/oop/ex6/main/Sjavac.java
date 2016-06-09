package oop.ex6.main;

import java.io.IOException;

/**
 * Created by David and Roi.
 */
public class Sjavac {

    private final static String FILE_ERROR_MESSAGE = "File does not exists.", LEGAL_FILE = "0";
    private final static String ILLEGAL_FILE = "1", FILE_ERROR = "2";

    public static void main(String[] args) {
        try {
            String path = args[0];
            Parser parser = new Parser();
            parser.analyzerFile(path);
            parser.parser();
            System.out.println(LEGAL_FILE);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
            e.getStackTrace();
        } catch (IOException e) {
            System.err.println(FILE_ERROR_MESSAGE);
            e.getStackTrace();
            System.out.println(FILE_ERROR);
        } catch (IllegalException e) {
            System.err.println(e.getMessage());
            e.getStackTrace();
            System.out.println(ILLEGAL_FILE);
        }
    }
}
