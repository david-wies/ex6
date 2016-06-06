package oop.ex6.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by David and Roi.
 */
class Method extends Block {

    static final private String NAME_ERROR = "illegal method name";
    static final private String PARAMETERS_ERROR = "unmatched paremeters";
    private final String parameterPattern1 = "\\s*(int|double|String|boolean|char)\\s+\\S+\\s*";
    private final String parameterPattern2 = "\\S+\\s\\S+";
    private ArrayList<Variable> parameters;
    private int originLine;
    private String name;
    private Pattern pattern1 = Pattern.compile(parameterPattern1);
    private Pattern pattern2 = Pattern.compile(parameterPattern2);

    /**
     * The constructor.
     *
     * @param rows       The string's of this method.
     * @param name       The name of this method.
     * @param originLine The number of th e first line in the original file.
     * @param variables  The variable's that this method known.
     * @throws Exception
     */
    Method(ArrayList<String> rows, String name, int originLine, ArrayList<HashMap<String, Variable>>
            variables, String parameters) throws IllegalException {
        super(rows, originLine, variables);
        this.originLine = originLine;
        this.name = name;
        analysisParameters(parameters);
    }

    /**
     * Check if the method name was legal.
     *
     * @throws IllegalException
     */
    static void verifyLegalityMethodName(String name, int originLine) throws IllegalException {
        String namePattern = "[a-zA-Z]+\\w*";
        Pattern pattern = Pattern.compile(namePattern);
        Matcher m = pattern.matcher(name);
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
                Variable newVar = Variable.createParameter(typeAndName[0], typeAndName[1], originLine);
                variables.get(0).put(newVar.getName(), newVar);
                this.parameters.add(newVar);
            } else
                throw new IllegalException(NAME_ERROR, originLine);
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
        return name;
    }
}
