package oop.ex6.main;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by David and Roi.
 */
class ConditionBlock extends Block {
    /**
     * The constructor.
     *
     * @param rows       The row's of this bock.
     * @param originLine the line where the block begin.
     * @param variables
     */
    ConditionBlock(ArrayList<String> rows, int originLine, ArrayList<HashMap<String, Variable>> variables,
                   String condition) {
        super(rows, originLine, variables);
    }
}
