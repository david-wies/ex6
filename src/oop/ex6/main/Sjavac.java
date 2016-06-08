package oop.ex6.main;

import java.io.IOException;

/**
 * Created by David and Roi.
 */
public class Sjavac {

    private final static String FILE_ERROR = "File does not exists.\n2", LEGAL_FILE = "0", ILLEGAL_FILE = "1";

    public static void main(String[] args) {
        try {
            String path = args[0];
            Parser parser = new Parser(path);
            parser.analyzerFile(path);
            parser.parser();
            System.out.println(LEGAL_FILE);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(FILE_ERROR);
        } catch (IllegalException e) {
            System.err.println(e.getMessage());
            System.out.println(ILLEGAL_FILE);
        }
    }
}
