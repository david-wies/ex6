package oop.ex6.main;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by David and Roi.
 */
class ConditionBlock extends Block {

    private final String CONDITION;
    private static final String SPLITTER = "(&{2})|(\\|{2})";


    /**
     * The constructor.
     *
     * @param rows       The row's of this bock.
     * @param originLine the line where the block begin.
     * @param variables  The variable's that this method known.
     */
    ConditionBlock(ArrayList<String> rows, int originLine, ArrayList<HashMap<String, Variable>> variables,
                   String condition) throws IllegalException {
        super(rows, originLine, variables);
        CONDITION = condition;
        analysisCondition(condition);
    }

    /**
     * Convert the string of parameter into variables to be use.
     *
     * @param conditions The string that describe the condition of the block.
     * @throws IllegalException
     */
    void analysisCondition(String conditions) throws IllegalException {
        variables.add(new HashMap<>());
        String name;
        String[] parts = conditions.split(SPLITTER);
        for (String condition : parts) {
            if (!condition.equals("true") && !condition.equals("false")) {
                name = Parser.extractFirstWord(condition, getOriginLineNumber(), false);
                Variable variable = getVariable(name);
                if (variable == null || !(variable.isBooleanExpression())) {
                    throw new IllegalException("Variable doesn't exists", getOriginLineNumber());
                } else {
                    variables.get(variables.size()).put(variable.getName(), variable);
                }
            }

        }
    }

    /**
     * @return The condition
     */
    public String getCondition() {
        return CONDITION;
    }

    /**
     * Find the variable.
     *
     * @param name The variable name.
     * @return The variable if exists, else null.
     */
    Variable getVariable(String name) {
        Variable variable = null;
        for (int index = variables.size() - 1; index >= 0; index--) {
            variable = variables.get(index).get(name);
            if (variable != null) {
                break;
            }
        }
        return variable;
    }

}
