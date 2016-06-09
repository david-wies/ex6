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
            e.printStackTrace();
            System.err.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(FILE_ERROR_MESSAGE);
            System.out.println(FILE_ERROR);
        } catch (IllegalException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.out.println(ILLEGAL_FILE);
        }
    }
}
