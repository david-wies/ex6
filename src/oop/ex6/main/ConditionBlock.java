package oop.ex6.main;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by David and Roi.
 */
class ConditionBlock extends Block {

    private final String CONDITION;
    /**
     * The constructor.
     *
     * @param rows       The row's of this bock.
     * @param originLine the line where the block begin.
     * @param variables The variable's that this method known.
     */
    ConditionBlock(ArrayList<String> rows, int originLine, ArrayList<HashMap<String, Variable>> variables,
                   String condition) {
        super(rows, originLine, variables);
        CONDITION = condition;
    }

    /**
     * @return The condition
     */
    public String getCondition() {
        return CONDITION;
    }
}
