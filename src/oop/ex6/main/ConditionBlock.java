package oop.ex6.main;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by David and Roi.
 */
class ConditionBlock extends Block {

    private static final String SPLITTER = "(&{2})|(\\|{2})";


    /**
     * The constructor.
     *
     * @param rows       The row's of this bock.
     * @param originLine the line where the block begin.
     */
    ConditionBlock(ArrayList<String> rows, int originLine, String condition, int depth) throws
            IllegalException {
        super(rows, originLine, depth);
        if (Parser.variables.size() < getDepth() + 1) {
            Parser.variables.add(new HashMap<>());
        } else {
            Parser.variables.remove(getDepth());
            Parser.variables.add(getDepth(), new HashMap<>());
        }
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
            if (!condition.equals("true") && !condition.equals("false")) {
                name = Parser.extractFirstWord(condition, getOriginLineNumber(), false);
                Variable variable = Parser.getVariable(name);
                if (variable == null || !(variable.isBooleanExpression())) {
                    throw new IllegalException("Variable doesn't exists", getOriginLineNumber());
                } else {
                    Parser.variables.get(getDepth()).put(variable.getName(), variable);
                }
            }

        }
    }


}
