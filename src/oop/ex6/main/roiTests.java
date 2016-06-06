package oop.ex6.main;


import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class roiTests {
    static final private String BAD_FORMAT_ERROR = "bad format line";
    private static final String FIRST_WORD = "(\\b\\w+\\b)";
    private static final String LEGAL_END = "[^;]*;\\s*";
    private static final String END_BLOCK = "\\s*}\\s*";
    private static Pattern firstWordPattern = Pattern.compile(FIRST_WORD);
    private static Pattern legalEnd = Pattern.compile(LEGAL_END);
    private static Pattern endBlock = Pattern.compile(END_BLOCK);
    private HashMap<String, Method> methods;
    private HashMap<String, Variable> globalVariables;

    private static String extractFirstWord(String string, int numberLine) throws IllegalException {
        Matcher matcher = firstWordPattern.matcher(string);
        if (matcher.find()) {
            return string.substring(matcher.start(), matcher.end());
        }
        throw new IllegalException(BAD_FORMAT_ERROR, numberLine);
    }

    public static void main(String[] args) throws IOException {
//        ArrayList<HashMap<String, Variable>> vars = new ArrayList<HashMap<String, Variable>>();
//        String name="hello";
//        String para = "int a ,    double b , char c    ";
////        String[] keys = {"void", "final", "if", "while", "true", "false", "return", "int", "double", "boolean", "char", "String"};
//        try {
//            Method testMethod = new Method(null, name, 6, vars);
//            testMethod.analysisParameters(para);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Parser pars = new Parser("C:\\Users\\roishtivi\\IdeaProjects\\ex6\\src\\oop\\ex6\\main");
        try {
            System.out.println(extractFirstWord("", 3));
        } catch (IllegalException e) {
            System.out.println(e.getMessage());
        }


    }
}
