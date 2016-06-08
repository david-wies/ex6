package oop.ex6.main;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by David .
 */
public class DavidTester {
    public static void main(String[] args) {
        try {
            Variable variable = new Variable("int", "a", "2", 1, false);
            HashMap<String, Variable> hashMap = new HashMap<>();
            hashMap.put(variable.getName(), variable);
            ArrayList<HashMap<String, Variable>> variables = new ArrayList<>();
            variables.add(hashMap);
//            ConditionBlock condition = new ConditionBlock(new ArrayList<>(), 2, variables, "a");
            System.out.println("done");
        } catch (IllegalException e) {
            e.printStackTrace();
        }
    }
}
