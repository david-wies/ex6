package oop.ex6.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse the s-java file.
 */
class Parser {

    static final private String BAD_FORMAT_ERROR = "bad format line";
    private static final String FIRST_WORD = "(\\b\\w+\\b)";
    private static final String LEGAL_END = "[^;]*;\\s*";
    private static final String END_BLOCK = "\\s*}\\s*";
    private static final String START_BLOCK = "\\s*\\{\\s*";
    private static Pattern firstWordPattern = Pattern.compile(FIRST_WORD);
    private static Pattern legalEnd = Pattern.compile(LEGAL_END);
    private static Pattern endBlock = Pattern.compile(END_BLOCK);
    private static Pattern startBlock = Pattern.compile(START_BLOCK);
    private HashMap<String, Method> methods;
    private HashMap<String, Variable> globalVariables;

    /**
     * @param filePath The path to the s-java file.
     * @throws IOException
     */
    Parser(String filePath) throws IOException {

    }

    /**
     * @param line
     * @param numberLine
     * @return
     * @throws IllegalException
     */
    private static String extractParameters(String line, int numberLine) throws IllegalException {
        int startIndex = line.indexOf('('), endIndex = line.indexOf(')');
        if (startIndex < endIndex) {
            return line.substring(startIndex, endIndex);
        } else {
            throw new IllegalException("", numberLine);
        }
    }

    /**
     * Give a string the method return the first separated by spaces word
     *
     * @param string     the string to extract from
     * @param numberLine the number line of the string.
     * @return the first word in the string
     * @throws IllegalException
     */
    private static String extractFirstWord(String string, int numberLine) throws IllegalException {
        Matcher matcher = firstWordPattern.matcher(string);
        if (matcher.find()) {
            return string.substring(matcher.start(), matcher.end());
        }
        throw new IllegalException(BAD_FORMAT_ERROR, numberLine);
    }

    /**
     * get line of variable initialing and update the variable array.
     * @param line
     * @param lineNumber
     * @throws IllegalException
     */
    public void updateVariables(HashMap<String,Variable> variables, String line, int lineNumber)
            throws IllegalException{
        String variableType = extractFirstWord(line,lineNumber);
        line = line.substring(variableType.length());
        String[] parts = line.split(",");
    }

    /**
     * Go other all of the s-java file and do the first analysis.
     */
    void analyzerFile(String path) throws IOException, IllegalException {
        File sJavaFile = new File(path);
        Scanner input = new Scanner(sJavaFile);
        String line;
        int lineNumber = 1, counterBlocks = 0, firstMethodLine = 1;
        ArrayList<String> rows = null;
        String word, parameters = "", methodName = "";
        while (input.hasNext()) {
            line = input.nextLine();
            Matcher matcher = firstWordPattern.matcher(line);
            if (matcher.find()) {
                word = line.substring(matcher.start(), matcher.end());
            } else if (line.startsWith("//")) { //if the line is a comment line.
                lineNumber++;
                continue;
            } else { // if the line is only with spaces.
                lineNumber++;
                continue;
            }
            line = line.substring(matcher.end());
            if (rows == null) {
                if (Variable.isLegalityVariableType(word)) { // This line create a variable. 
                    String type = word;
                    String name = extractFirstWord(line, lineNumber);
                    Variable.verifyLegalityVariableName(name, lineNumber, globalVariables);

                    // while and if blocks must be in method in s-java,
                } else if (word.equals("}") || word.equals("if") || word.equals("while")) { // done!
                    throw new IllegalException("", lineNumber);
                } else if (word.equals("void")) { // done !
                    rows = new ArrayList<>();
                    firstMethodLine = lineNumber;
                    parameters = extractParameters(line, lineNumber);
                    methodName = extractFirstWord(line, lineNumber);
                    Method.verifyLegalityMethodName(methodName, lineNumber);
                    Matcher startBlockMatcher = startBlock.matcher(line);
                    if (!startBlockMatcher.find(line.indexOf('(') + 1)) {
                        throw new IllegalException("", lineNumber);
                    }
                } else {
                    throw new IllegalException("", lineNumber);
                }
            } else if (word.equals("}")) {
                if (counterBlocks > 1) {
                    rows.add(line);
                    counterBlocks--;
                } else {
                    ArrayList<HashMap<String, Variable>> variables = new ArrayList<>();
                    variables.add(globalVariables);
                    Method method = new Method(rows, methodName, firstMethodLine, variables, parameters);
                    methods.put(method.getName(), method);
                    rows = null;
                }
            } else {
                rows.add(line);
                if (line.contains("{")) {
                    counterBlocks++;
                }
            }

            lineNumber++;
        }
    }

    HashMap<String, Method> getMethods() {
        return methods;
    }

    HashMap<String, Variable> getGlobalVariables() {
        return globalVariables;
    }

    /**
     * Parse all of the Block's
     *
     * @return true if al of the ,method's was valid, false otherwise.
     * @throws IllegalException
     */
    boolean parser() throws IllegalException {
//        if (parseBlock() && endWithReturn()) {
//
//        } else {
//            throw new  IllegalException();
//        }
        return false;
    }

    /**
     * Parse a single block.
     *
     * @param block     The block to parse.
     * @param variables the variable's that the block know.
     */
    boolean parseBlock(Block block, HashMap<String, Variable> variables) {
        return true;
    }

    /**
     * Check if the last row is legal return.
     *
     * @param lastRow The last row of the function
     * @return true if this line of return is legal, false otherwise.
     */
    boolean endWithReturn(String lastRow) {
        String rePattern = "\breturn\b\\s*";
        Pattern pattern = Pattern.compile(rePattern);
        Matcher matcher = pattern.matcher(lastRow);
        return matcher.matches();
    }
}