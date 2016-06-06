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

    // Errors string's.
    static final private String BAD_FORMAT_ERROR = "bad format line";
    private final static String TYPE_ERROR_MESSAGE = "Illegal type of value";

    // Useful value's.
    private final String FINAL = "final";

    // Pattern's string's.
    private static final String FIRST_WORD = "(\\b\\S+\\b)";
    private static final String LEGAL_END = "[^;]*;\\s*";
    private static final String END_BLOCK = "\\s*}\\s*";
    private static final String START_BLOCK = "\\s*\\{\\s*";
    private static final String SINGLE_NAME = "\\s*\\S+\\s*";

    // Pattern's string's.
    private static Pattern singleName = Pattern.compile(SINGLE_NAME);
    private static Pattern firstWordPattern = Pattern.compile(FIRST_WORD);
    private static Pattern legalEnd = Pattern.compile(LEGAL_END);
    private static Pattern endBlock = Pattern.compile(END_BLOCK);
    private static Pattern startBlock = Pattern.compile(START_BLOCK);

    // Field's of Parser.
    private HashMap<String, Method> methods;
    private HashMap<String, Variable> globalVariables;

    /**
     * @param filePath The path to the s-java file.
     * @throws IOException
     */
    Parser(String filePath) throws IOException {
        methods = new HashMap<>();
        globalVariables = new HashMap<>();
    }

    /**
     * Extract the string that describe the parameter's from the line.
     *
     * @param line       The line that contain the describe of the parameter's.
     * @param numberLine The number of the line in the file.
     * @return String of the parameter's.
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
    public static String extractFirstWord(String string, int numberLine) throws IllegalException {
        Matcher matcher = firstWordPattern.matcher(string);
        if (matcher.find()) {
            return string.substring(matcher.start(), matcher.end());
        }
        throw new IllegalException(BAD_FORMAT_ERROR, numberLine);
    }

    /**
     * get line of variable initialing and update the variable array.
     *
     * @param line
     * @param lineNumber
     * @throws IllegalException
     */
    public void updateVariables(HashMap<String, Variable> variables, String line, int lineNumber)
            throws IllegalException {
        Matcher legalEndMatcher = legalEnd.matcher(line);
        if (!legalEndMatcher.matches()) {
            throw new IllegalException(BAD_FORMAT_ERROR, lineNumber);
        }
        int indexOfSemiColon = line.indexOf(";");
        line = line.substring(0, indexOfSemiColon);
        boolean isFinal = false;
        String firstWord = extractFirstWord(line, lineNumber);
        if (firstWord.equals(FINAL)) {
            isFinal = true;
            line = line.substring(line.indexOf(FINAL) + FINAL.length());
        }
        String varType = extractFirstWord(line, lineNumber);
        if (!Variable.isLegalVariableType(varType)) {
            throw new IllegalException(TYPE_ERROR_MESSAGE, lineNumber);
        }
        line = line.substring(line.indexOf(varType) + varType.length());
        String[] parts = line.split(",");
        for (String part : parts) {
            if (!part.contains("=")) { //var assignment without value.
                Matcher singleNameMatcher = singleName.matcher(part);
                if (singleNameMatcher.matches()) {
                    String varName = extractFirstWord(part, lineNumber);
                    Variable.verifyLegalityVariableName(varName, lineNumber, variables);
                    Variable newVar = new Variable(varType, varName, lineNumber);
                    variables.put(newVar.getName(), newVar);
                } else {
                    throw new IllegalException(BAD_FORMAT_ERROR, lineNumber);
                }
            } else { //var assignment with value.
                String[] parameters = part.split("=");
                if (parameters.length == 2) {
                    Variable.verifyLegalityVariableName(parameters[0], lineNumber, variables);
                    Variable newVar = new Variable(varType, parameters[0], extractFirstWord(parameters[1],
                            lineNumber), lineNumber, isFinal);
                    variables.put(newVar.getName(), newVar);
                } else {
                    throw new IllegalException(BAD_FORMAT_ERROR, lineNumber);
                }


            }
        }
    }

    /**
     * Go other all of the s-java file and do the first analysis.
     *
     * @param path The path tp the s-java file.
     * @throws IOException      The file does'nt exist.
     * @throws IllegalException The file contain illegal command.
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
                if (Variable.isLegalVariableType(word) || word.equals("final")) { // This line create a
                    // variable.
                    updateVariables(globalVariables, line, lineNumber);
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