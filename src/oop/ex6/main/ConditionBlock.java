package oop.ex6.main;

import java.util.ArrayList;

/**
 * Created by David and Roi.
 */
class ConditionBlock extends Block {

    // Pattern's string's.
    private static final String SPLITTER = "(&{2})|(\\|{2})";

    // Errors string's.
    private final static String BOOLEAN_EXPRESSION_ERROR_MESSAGE = "Variable doesn't legal boolean " +
            "expression";

    // Useful value's.
    private final static String TRUE = "true", FALSE = "false";

    /**
     * The constructor.
     *
     * @param rows       The row's of this bock.
     * @param originLine the line where the block begin.
     */
    ConditionBlock(ArrayList<String> rows, int originLine, String condition, int depth) throws
            IllegalException {
        super(rows, originLine, depth);
        analysisCondition(condition);
    }

    /**
     * Convert the string of parameter into variables to be use.
     *
     * @param conditions The string that describe the condition of the block.
     * @throws IllegalException
     */
    private void analysisCondition(String conditions) throws IllegalException {
        String name;
        String[] parts = conditions.split(SPLITTER);
        for (String condition : parts) {
            if (!condition.equals(TRUE) && !condition.equals(FALSE)) {
                name = Parser.extractFirstWord(condition, getOriginLineNumber(), false);
                Variable variable = Parser.getVariable(name);
                if (variable == null || !(variable.isBooleanExpression())) {
                    throw new IllegalException(BOOLEAN_EXPRESSION_ERROR_MESSAGE, getOriginLineNumber());
                } else {
                    Parser.variables.get(getDepth()).put(variable.getName(), variable);
                }
            }

        }
    }


}
