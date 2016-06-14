package oop.ex6.main;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by David and Roi.
 */
class Method extends Block {

    // Errors string's.
    static final private String NAME_ERROR = "illegal method NAME";
    static final private String PARAMETERS_ERROR = "unmatched parameter's";

    // Pattern's string.
    private final static String TYPES_AND_NAMES_PATTERN = "\\s*(int|double|String|boolean|char)\\s+\\S+\\s*";
    private final static String SEPARATED_WORDS_PATTERNS = "\\S+\\s+\\S+";
    private final static String NAME_PATTERN = "[a-zA-Z]+\\w*";

    // Pattern's
    private static Pattern typeAndName = Pattern.compile(TYPES_AND_NAMES_PATTERN);
    private static Pattern separatedWords = Pattern.compile(SEPARATED_WORDS_PATTERNS);
    private static Pattern namePattern = Pattern.compile(NAME_PATTERN);

    // Field's of Method.
    private ArrayList<Variable> parameters;
    private final String NAME;
    private static final String FINAL = "final";


    /**
     * The constructor.
     *
     * @param rows       The string's of this method.
     * @param name       The NAME of this method.
     * @param originLine The number of th e first line in the original file.
     * @throws IllegalException The Parameter's ar illegal.
     */
    Method(ArrayList<String> rows, String name, int originLine, String parameters, int depth) throws IllegalException {
        super(rows, originLine, depth);
        NAME = name;
        ArrayList<Variable> methodVars = analysisParameters(parameters);
        for (Variable variable: methodVars) {
            if (!Parser.addVariable(variable, getDepth())) {
                throw new IllegalException("Two parameters has to have two different names.",
                        getOriginLine());
            }
        }
    }

    /**
     * Check if the method NAME was legal.
     *
     * @throws IllegalException
     */
    static void verifyLegalityMethodName(String name, int originLine) throws IllegalException {
        Matcher m = namePattern.matcher(name);
        if (!m.matches() || Reserved.isReserved(name)) {
            throw new IllegalException(NAME_ERROR, originLine);
        }
    }

    /**
     * Convert the string of parameter into variables to be use.
     *
     * @param parameters The string that describe the parameter's of the method.
     * @throws IllegalException
     */
    private ArrayList<Variable> analysisParameters(String parameters) throws IllegalException {
        this.parameters = new ArrayList<>();
        ArrayList<Variable> vars = new ArrayList<>();
        if (parameters.equals("")) {
            return vars;
        }
        int start;
        int end;
        boolean helper;
        String[] parts = parameters.split(",");
        for (String part : parts) {
            boolean isFinal = false;
            if (Parser.extractFirstWord(part, getOriginLine()).equals(FINAL)) {
                isFinal = true;
                part = part.substring(part.indexOf(FINAL) + FINAL.length());
            }
            Matcher typeAndNameMatcher = typeAndName.matcher(part);
            Matcher separatedWordsMatcher = separatedWords.matcher(part);
            if (typeAndNameMatcher.matches() && separatedWordsMatcher.find()) {
//                helper = separatedWordsMatcher.find();
                start = separatedWordsMatcher.start();
                end = separatedWordsMatcher.end();
                String newPart = part.substring(start, end);
                String[] typeAndName = newPart.split("\\s+");
                Variable newVar = Variable.createParameter(typeAndName[0], typeAndName[1], getOriginLine(), isFinal);
                vars.add(newVar);
            } else
                throw new IllegalException(NAME_ERROR, getOriginLine());
        }
        return vars;
    }


    /**
     * get hash map of parameters and throw exception if the method call was illegal.
     *
     * @param parameters Array of the parameter that has been called with.
     * @param lineNumber the number of the line of call.
     * @throws IllegalException
     */
    void calledThisMethod(String parameters, int lineNumber) throws IllegalException {
        // if the sizes of the arrays isn't equal that it mean that parameters is unmatched.
        ArrayList<Variable> varsParameters = analysisParameters(parameters);
        if (varsParameters.size() != this.parameters.size()) {
            throw new IllegalException(PARAMETERS_ERROR, lineNumber);
        }
        for (int index = 0; index < varsParameters.size(); index++) {
            if (!this.parameters.get(index).getType().equals(varsParameters.get(index).getType()) &&
                    varsParameters.get(index).hasValue()) {
                throw new IllegalException(PARAMETERS_ERROR, lineNumber);
            }
        }
    }

    String getName() {
        return NAME;
    }
}