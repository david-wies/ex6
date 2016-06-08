package oop.ex6.main;

import java.util.ArrayList;

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
