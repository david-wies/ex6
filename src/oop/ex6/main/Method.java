package oop.ex6.main;

import java.util.ArrayList;
import java.util.HashMap;
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
    private final static String TYPES_AND_NAMES_PARTAKEN = "\\s*(int|double|String|boolean|char)\\s+\\S+\\s*";
    private final static String SEPARATED_WORDS = "\\S+\\s\\S+";
    private final static String NAME_PATTERN = "[a-zA-Z]+\\w*";

    // Pattern's
    private static Pattern pattern1 = Pattern.compile(TYPES_AND_NAMES_PARTAKEN);
    private static Pattern pattern2 = Pattern.compile(SEPARATED_WORDS);
    private static Pattern namePattern = Pattern.compile(NAME_PATTERN);

    // Field's of Method.
    private ArrayList<Variable> parameters;
    private final int ORIGIN_LINE;
    private final String NAME;


    /**
     * The constructor.
     *
     * @param rows       The string's of this method.
     * @param name       The NAME of this method.
     * @param originLine The number of th e first line in the original file.
     * @param variables  The variable's that this method known.
     * @throws Exception
     */
    Method(ArrayList<String> rows, String name, int originLine, ArrayList<HashMap<String, Variable>>
            variables, String parameters) throws IllegalException {
        super(rows, originLine, variables);
        ORIGIN_LINE = originLine;
        NAME = name;
        analysisParameters(parameters);
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
    void analysisParameters(String parameters) throws IllegalException {
        variables.add(new HashMap<>());
        this.parameters = new ArrayList<>();
        int start;
        int end;
        String[] parts = parameters.split(",");
        for (String part : parts) {
            Matcher m1 = pattern1.matcher(part);
            Matcher m2 = pattern2.matcher(part);
            if (m1.matches()) {
                m2.find();
                start = m2.start();
                end = m2.end();
                String newPart = part.substring(start, end);
                String[] typeAndName = newPart.split("\\s");
                Variable newVar = Variable.createParameter(typeAndName[0], typeAndName[1], ORIGIN_LINE);
                variables.get(0).put(newVar.getName(), newVar);
                this.parameters.add(newVar);
            } else
                throw new IllegalException(NAME_ERROR, ORIGIN_LINE);
        }
    }


    /**
     * get hash map of parameters and throw exception if the method call was illegal.
     *
     * @param parameters Array of the parameter that has been called with.
     * @param lineNumber the number of the line of call.
     * @throws IllegalException
     */
    void calledThisMethod(ArrayList<Variable> parameters, int lineNumber) throws IllegalException {
        // if the sizes of the arrays isn't equal that it mean that parameters is unmatched.
        if (parameters.size() != this.parameters.size()) {
            throw new IllegalException(PARAMETERS_ERROR, lineNumber);
        }
        for (int index = 0; index < parameters.size(); index++) {
            if (!this.parameters.get(index).getTYPE().equals(parameters.get(index).getTYPE()) &&
                    parameters.get(index).hasValue()) {
                throw new IllegalException(PARAMETERS_ERROR, lineNumber);
            }
        }
    }

    public String getName() {
        return NAME;
    }
}
