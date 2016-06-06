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
     * Convert the string of parameter into variables to be use.
     *
     * @param conditions The string that describe the condition of the block.
     * @throws IllegalException
     */
    private void analysisCondition(String conditions) throws IllegalException {
        variables.add(new HashMap<>());
        String splitter = "(\\&{2})|(\\|{2})";
        String[] parts = conditions.split(splitter);
        for (String condition : parts) {
            if (condition.equals("true") || condition.equals("false")) {
                continue;
            } else {
                while (condition.length() > 0) {

                }
            }
        }
//        for (String part : parts) {
//            if ()
//            Matcher m1 = pattern1.matcher(part);
//            Matcher m2 = pattern2.matcher(part);
//            if (m1.matches()) {
//                m2.find();
//                start = m2.start();
//                end = m2.end();
//                String newPart = part.substring(start, end);
//                String[] typeAndName = newPart.split("\\s");
//                Variable newVar = Variable.createParameter(typeAndName[0], typeAndName[1], ORIGIN_LINE);
//                variables.get(0).put(newVar.getName(), newVar);
//                this.parameters.add(newVar);
//            } else
//                throw new IllegalException(NAME_ERROR, ORIGIN_LINE);
//        }
    }

    /**
     * @return The condition
     */
    public String getCondition() {
        return CONDITION;
    }
}
