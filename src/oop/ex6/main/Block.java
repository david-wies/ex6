package oop.ex6.main;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by David and Roi.
 */
class Block {

    private final int ORIGIN_LINE, DEPTH;
    private ArrayList<String> rows;

    /**
     * The constructor.
     *
     * @param rows       The row's of this bock.
     * @param originLine the line where the block begin.
     */
    Block(ArrayList<String> rows, int originLine, int depth) {
        this.rows = rows;
        ORIGIN_LINE = originLine;
        DEPTH = depth;
        if (Parser.variables.size() < DEPTH + 1) {
            Parser.variables.add(new HashMap<>());
        } else {
            Parser.variables.remove(getDepth());
            Parser.variables.add(DEPTH, new HashMap<>());
        }
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
    int getOriginLine() {
        return ORIGIN_LINE;
    }

    int getDepth() {
        return DEPTH;
    }
}
