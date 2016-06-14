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

    // Useful value's.
    private final static String CHAR = "char", STRING = "String", FINAL = "final";
    private final static String START_LOOP = "while", START_CONDITION = "if", START_FUNCTION = "void";
    private final static String RETURN = "return", END_FILE_NAME = ".sjava", START_COMMENT = "//";

    // Errors string's.
    private final static String BAD_FORMAT_ERROR = "bad format line";
    private final static String TYPE_ERROR_MESSAGE = "Illegal type of value";
    private final static String ALREADY_TOKEN_ERROR_MESSAGE = "Already taken variable";
    private final static String COMMENT_ERROR = "Un legal comment format";
    private final static String RETURN_ERROR = "Un legal return format";
    private final static String BAD_METHOD_FORMAT_ERROR = "bad method format";
    private final static String UNSUPPORTED_COMMAND = "Unsupported command";
    private final static String NAME_ERROR_MESSAGE = "Illegal name variable";
    private final static String INITIALIZE_ERROR_MESSAGE = "Final must initialize";
    private final static String ILLEGAL_METHOD_CALL_ERROR = "UNKNOWN METHOD CALL";


    // Pattern's string's.
    private static final String FIRST_WORD = "\\S+";
    //    private static final String FIRST_BRACKETS_WORD = "['\"]{1}\\S+['\"]{1}";
    private static final String FIRST_NAME = "\\b\\w+\\b";
    private static final String METHOD_NAME = "\\S+\\s*\\(";
    private static final String LEGAL_END = ";\\s*";
    private static final String END_BLOCK = "\\s*}\\s*";
    private static final String START_BLOCK = "\\s*\\S+\\s*\\{\\s*"; // \\S+ and not \\S* dose not work on while/if
    private static final String START_BLOCK_NEW = ".+\\{"; //  working on while/if ( need to see if to use it in the other places)
    private static final String SINGLE_NAME = "\\s*\\S+\\s*";
    private static final String IS_STRING = "\".*\"";
    private static final String LEGAL_RETURN = "\\s*\\breturn\\b\\s*";
    private static final String EMPTY_ROW = "\\s*;?\\s*";
    private static final String SPACE_ROW = "\\s*";
    private static final String METHOD_CALL = "[a-zA-Z]+\\w*\\(";
    private static final String LEGAL_METHOD_RETURN = "\\s*return\\s*;\\s*";


    // Patterns
    private static Pattern singleName = Pattern.compile(SINGLE_NAME);
    private static Pattern firstWordPattern = Pattern.compile(FIRST_WORD);
    private static Pattern methodName = Pattern.compile(METHOD_NAME);
    private static Pattern firstNamePattern = Pattern.compile(FIRST_NAME);
    private static Pattern legalEnd = Pattern.compile(LEGAL_END);
    private static Pattern endBlockPattern = Pattern.compile(END_BLOCK);
    private static Pattern startBlockPattern = Pattern.compile(START_BLOCK);
    private static Pattern isString = Pattern.compile(IS_STRING);
    private static Pattern returnPattern = Pattern.compile(LEGAL_RETURN);
    private static Pattern emptyRowPattern = Pattern.compile(EMPTY_ROW);
    private static Pattern spaceRowPattern = Pattern.compile(SPACE_ROW);
    private static Pattern methodCallPattern = Pattern.compile(METHOD_CALL);
    private static Pattern legalMethodReturn = Pattern.compile(LEGAL_METHOD_RETURN);
    private static Pattern newStartBlock = Pattern.compile(START_BLOCK_NEW);


    // Field's of Parser.
    private HashMap<String, Method> methods;
    static ArrayList<HashMap<String, Variable>> variables;
    private static final int GLOBAL_DEPTH = 0;

    /**
     * The constructor.
     *
     * @throws IOException
     */
    Parser() throws IOException {
        methods = new HashMap<>();
        variables = new ArrayList<>();
        variables.add(new HashMap<>());
    }

    private boolean isLegalFile(String path) {
        return path.endsWith(END_FILE_NAME);
    }

    /**
     * Extract the string that describe the parameter's from the line.
     *
     * @param line       The line that contain the describe of the parameter's.
     * @param numberLine The number of the line in the file.
     * @return String of the parameter's.
     * @throws IllegalException
     */
    private static String extractInnerBrackets(String line, int numberLine) throws IllegalException {
        int startIndex = line.indexOf('('), endIndex = line.indexOf(')');
        if (startIndex < endIndex) {
            return line.substring(startIndex + 1, endIndex); // was returning the parameters with " ( "
        } else {
            throw new IllegalException("Un legal method declaration", numberLine);
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
    static String extractFirstWord(String string, int numberLine) throws IllegalException {
        try {
            Matcher matcher = firstWordPattern.matcher(string);
            if (matcher.find()) {
                return string.substring(matcher.start(), matcher.end());
            } else {
                throw new IllegalException(BAD_FORMAT_ERROR, numberLine);
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalException(BAD_FORMAT_ERROR, numberLine);
        }
    }

    private static String extractFirstName(String string, int numberLine) throws IllegalException {
        Matcher matcher = firstNamePattern.matcher(string);
        if (string.equals(""))
            return string;
        if (matcher.find()) {
            return string.substring(matcher.start(), matcher.end());
        }
        throw new IllegalException(BAD_FORMAT_ERROR, numberLine);
    }

    private static String extractMethodName(String string, int numberLine) throws IllegalException {
        Matcher matcher = methodName.matcher(string);
        if (string.equals(""))
            throw new IllegalException(BAD_FORMAT_ERROR, numberLine);
        if (matcher.find()) {
            string = string.substring(matcher.start(), matcher.end() - 1);
            String[] parts = string.split("\\s+");
            return parts[0];
        }
        throw new IllegalException(BAD_FORMAT_ERROR, numberLine);
    }

    /**
     * get line of variable initialing and update the variable array.
     *
     * @param line       The line of the declare of the variable's.
     * @param lineNumber the number line of the string.
     * @throws IllegalException
     */
    private void updateVariables(int depth, String line, int lineNumber, String firstWord) throws IllegalException {
        HashMap<String, Variable> scopeVariables = variables.get(depth);
        String varType;
        boolean isFinal = false;
        if (firstWord.equals(FINAL)) {
            isFinal = true;
            line = line.substring(line.indexOf(FINAL) + FINAL.length());
            varType = extractFirstWord(line, lineNumber);
            if (!Variable.isLegalVariableType(varType)) {
                throw new IllegalException(TYPE_ERROR_MESSAGE, lineNumber);
            }
        } else {
            varType = firstWord;
        }
        line = line.substring(line.indexOf(varType) + varType.length());
        String[] parts = line.split(",");
        for (String part : parts) {
            if (!part.contains("=")) { //var assignment without value.
                if (isFinal) {
                    throw new IllegalException(INITIALIZE_ERROR_MESSAGE, lineNumber);
                }
                Matcher singleNameMatcher = singleName.matcher(part);
                if (singleNameMatcher.matches()) {
                    String varName = extractFirstWord(part, lineNumber);
                    if (!Variable.isLegalVariableName(varName)) {
                        throw new IllegalException(NAME_ERROR_MESSAGE, lineNumber);
                    }
                    containInSameScope(varName, depth, lineNumber);
                    Variable newVar = new Variable(varType, varName, lineNumber, isFinal);
                    scopeVariables.put(newVar.getName(), newVar);
                } else {
                    throw new IllegalException(BAD_FORMAT_ERROR, lineNumber);
                }
            } else { //var assignment with value .
                String varName = extractFirstWord(part, lineNumber);
                Variable newVar = new Variable(varType, varName, lineNumber, isFinal);
                scopeVariables.put(newVar.getName(), newVar);
                part = part.substring(part.indexOf(varName) + varName.length());
                assignmentValue(part, newVar, lineNumber);
            }
        }
    }

    /**
     * Assignment value to variable.
     *
     * @param assignment THe String of what to assignment to the variable.
     * @param variable   The variable to assignment the value.
     * @param lineNumber The number line of the string.
     * @throws IllegalException THe line format was illegal.
     */
    private void assignmentValue(String assignment, Variable variable, int lineNumber) throws IllegalException {
        String[] parameters = assignment.split("=");
        String varValue;
        if (parameters.length == 2) {
            Matcher matcher = spaceRowPattern.matcher(parameters[0]);
            if (matcher.matches()) {
                switch (variable.getType()) {
                    case STRING:
                        Matcher isStringMatcher = isString.matcher(parameters[1]);
                        boolean stringMatch = isStringMatcher.find();
                        if (stringMatch) {
                            varValue = parameters[1].substring(isStringMatcher.start(), isStringMatcher.end());
                        } else {
                            throw new IllegalException(TYPE_ERROR_MESSAGE, lineNumber);
                        }
                        break;
                    default:
                        varValue = extractFirstWord(parameters[1], lineNumber);
                        break;
                }
                parameters[1] = parameters[1].substring(parameters[1].indexOf(varValue) + varValue.length());
                Matcher spaceRowMatcher = spaceRowPattern.matcher(parameters[1]);
                if (spaceRowMatcher.matches()) {
                    variable.setValue(varValue, lineNumber);
                } else {
                    throw new IllegalException(BAD_FORMAT_ERROR, lineNumber);
                }
            } else {
                throw new IllegalException(BAD_FORMAT_ERROR, lineNumber);
            }
        } else {
            throw new IllegalException(BAD_FORMAT_ERROR, lineNumber);
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
        if (!isLegalFile(path)) {
            throw new IOException("Un support type of file");
        }
        File sJavaFile = new File(path);
        Scanner input = new Scanner(sJavaFile);
        String row;
        int lineNumber = 1, counterBlocks = 1, firstMethodLine = 1;
        ArrayList<String> rows = null;
        String word, parameters = "", methodName = "", subLine;
        Matcher firstWord;
        while (input.hasNext()) {
            row = input.nextLine();
            if (rows == null) {
                firstWord = firstWordPattern.matcher(row);
                if (firstWord.find()) {
                    word = row.substring(firstWord.start(), firstWord.end());
                    switch (word) {
                        case START_CONDITION:
                            throw new IllegalException("Cant start if condition out of a method.", lineNumber);
                        case START_LOOP:
                            throw new IllegalException("Cant start loop out of a method.", lineNumber);
                        case START_FUNCTION:
                            subLine = row.substring(firstWord.end());
                            rows = new ArrayList<>();
                            firstMethodLine = lineNumber;
                            parameters = extractInnerBrackets(subLine, lineNumber);
                            methodName = extractMethodName(subLine, lineNumber);
                            Method.verifyLegalityMethodName(methodName, lineNumber);
                            Matcher startBlockMatcher = startBlockPattern.matcher(row);
                            if (!startBlockMatcher.find(row.indexOf('(') + 1)) {
                                throw new IllegalException("", lineNumber);
                            }
                            break;
                        default:
                            analyzeRow(row, GLOBAL_DEPTH, lineNumber, word);
                            break;
                    }
                }
            } else {
                counterBlocks = blockRunner(rows, row, counterBlocks);
                if (counterBlocks == 0) {
                    Method method = new Method(rows, methodName, firstMethodLine, parameters, GLOBAL_DEPTH
                            + 1);
                    rows = null;
                    counterBlocks = 1;
                    methods.put(method.getName(), method);
                }
            }
            lineNumber++;
        }
    }

    /**
     * Parse all of the Block's
     *
     * @return true if al of the ,method's was valid, false otherwise.
     */
    boolean parseMethods() throws IllegalException {
        for (Method method : methods.values()) {
            parseMethod(method);
        }
        return false;
    }


    /*
     * Analyze one row, assuming that the row isn't start of block or the end of the block.
     *
     * @param row        THe row to analyze.
     * @param depth      The depth of the block that the row of his rows.
     * @param lineNumber The number of the line in the full s-java file.
     * @throws IllegalException The line is not legal.
     */
    private void analyzeRow(String row, int depth, int lineNumber, String firstWord) throws IllegalException {
        Matcher endRow = legalEnd.matcher(row);
        Matcher methodCallMatcher = methodCallPattern.matcher(row);
        if (firstWord.equals(START_COMMENT)) {
            return;
        } else if (endRow.find()) {
            row = row.substring(0, endRow.start());
        } else {
            throw new IllegalException(BAD_FORMAT_ERROR, lineNumber);
        }
        firstWord = extractFirstWord(row, lineNumber);
        if (firstWord.equals(RETURN)) {
            if (!isLegalReturn(row)) {
                throw new IllegalException(RETURN_ERROR, lineNumber);
            }
        } else if (firstWord.equals(FINAL) || Variable.isLegalVariableType(firstWord)) {
            updateVariables(depth, row, lineNumber, firstWord);
        } else if (Variable.isLegalVariableName(firstWord)) {
            Variable variable = getVariable(firstWord);
            if (variable == null) {
                throw new IllegalException("Variable doesn't exists.", lineNumber);
            } else {
                row = row.substring(row.indexOf(firstWord) + firstWord.length());
                assignmentValue(row, variable, lineNumber);
            }
        } else if (methodCallMatcher.find()) { // check if a known method has been called.
            String methodName = row.substring(methodCallMatcher.start(), methodCallMatcher.end() - 1);
            if (!methods.containsKey(methodName)) {
                throw new IllegalException(ILLEGAL_METHOD_CALL_ERROR, lineNumber);
            }
        } else {
            throw new IllegalException(UNSUPPORTED_COMMAND, lineNumber);
        }
    }

    /**
     * Parse a single block.
     */
    private void parseBlock(Block block) throws IllegalException {
        int lineNumber = block.getOriginLine() + 1, counterBlock = 0, firstNewBlockLine = lineNumber;
        ArrayList<String> rows = null;
        String firstWord, condition = "";
        for (String row : block.getRows()) {
//            Matcher endRowMatcher = legalEnd.matcher(row);
            Matcher emptyRowMatcher = emptyRowPattern.matcher(row);
            Matcher firstWordMatcher = firstWordPattern.matcher(row);
            if (firstWordMatcher.find()) {
                firstWord = row.substring(firstWordMatcher.start(), firstWordMatcher.end());
            } else if (row.startsWith(START_COMMENT) || emptyRowMatcher.matches()) {
                return;
            } else {
                throw new IllegalException(BAD_FORMAT_ERROR, lineNumber);
            }
            if (rows == null) {
                if (row.contains("}")) {
                    throw new IllegalException(BAD_FORMAT_ERROR, lineNumber);
                }
                switch (firstWord) {
                    case START_FUNCTION:
                        throw new IllegalException("Can't create method in another method.", lineNumber);
                    case START_CONDITION:
                    case START_LOOP:
                        rows = new ArrayList<>();
                        condition = extractInnerBrackets(row, lineNumber);
                        firstNewBlockLine = lineNumber;
                        counterBlock++;
                        break;
                    default:
                        analyzeRow(row, block.getDepth(), lineNumber, firstWord);
                        break;
                }
            } else {
                counterBlock = blockRunner(rows, row, counterBlock);
                if (counterBlock == 0) {
                    Block newBlock = new ConditionBlock(rows, firstNewBlockLine, condition, block.getDepth() + 1);
                    rows = null;
                    parseBlock(newBlock);
                }
            }
            lineNumber++;
        }

    }

    /*
     * @param rows The array-list of the rows of the new block.
     * @param row The current line.
     * @param counterBlocks Counter of depth of the inner blocks. 
     * @return The update counterBlocks after the current row. 
     */
    private int blockRunner(ArrayList<String> rows, String row, int counterBlocks) {
        Matcher startBlock, endBlock;
        startBlock = newStartBlock.matcher(row);
        endBlock = endBlockPattern.matcher(row);
        if (startBlock.matches()) {
            counterBlocks++;
            rows.add(row);
        } else if (endBlock.matches()) {
            counterBlocks--;
            if (counterBlocks > 0) {
                rows.add(row);
            } else {
                return counterBlocks;
            }
        } else { // if not start of block and not end of block the line is in the block.
            rows.add(row);
        }
        return counterBlocks;
    }

    /**
     * Check if the last row is legal return.
     *
     * @param row The last row of the function
     * @return true if this line of return is legal, false otherwise.
     */
    private boolean isLegalReturn(String row) {
        Matcher matcher = returnPattern.matcher(row);
        return matcher.matches();
    }

    /**
     * Find the variable.
     *
     * @param name The variable name.
     * @return The variable if exists, else null.
     */
    static Variable getVariable(String name) {
        Variable variable = null;
        for (int index = variables.size() - 1; index >= 0; index--) {
            variable = variables.get(index).get(name);
            if (variable != null) {
                break;
            }
        }
        return variable;
    }

    private void containInSameScope(String name, int depth, int lineNumber) throws IllegalException {
        if (variables.get(depth).containsKey(name)) {
            throw new IllegalException(ALREADY_TOKEN_ERROR_MESSAGE, lineNumber);
        }
    }

    private void parseMethod(Method method) throws IllegalException {
        int lastRowIndex = method.getRows().size() - 1;
        String lastLine = method.getRows().get(lastRowIndex);
        Matcher legalEnd = legalMethodReturn.matcher(lastLine);
        if (legalEnd.matches()) {
            method.getRows().remove(lastRowIndex);
            parseBlock(method);
        } else {
            throw new IllegalException(BAD_METHOD_FORMAT_ERROR, method.getOriginLine());
        }

    }
}