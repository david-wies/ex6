package oop.ex6.main;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by David and Roi.
 */
class Block {

    private final int ORIGIN_LINE, DEPTH;
    ArrayList<HashMap<String, Variable>> variables;
    private ArrayList<String> rows;

    /**
     * The constructor.
     *
     * @param rows       The row's of this bock.
     * @param originLine the line where the block begin.
     */
    Block(ArrayList<String> rows, int originLine, ArrayList<HashMap<String, Variable>> variables, int depth) {
        this.rows = rows;
        ORIGIN_LINE = originLine;
        this.variables = variables;
        DEPTH = depth;
    }

    /**
     * @return The row's of this block.
     */
    ArrayList<String> getRows() {
        return rows;
    }

    /**
     * @return The line number of the start of this block.
     */
    int getOriginLineNumber() {
        return ORIGIN_LINE;
    }
}
