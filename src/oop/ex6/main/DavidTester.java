package oop.ex6.main;

/**
 * Created by David .
 */
public class DavidTester {
    public static void main(String[] args) {
//        try {
//            System.out.println(Variable.verifyLegalityVariableName("legal", 1));
//        } catch (IllegalException e) {
//            e.printStackTrace();
//        }
//        try {
//            System.out.println(Variable.verifyLegalityVariableName("_ ", 1));
//        } catch (IllegalException e) {
//            e.printStackTrace();
//        }
        boolean a = Variable.isInt("2");
        System.out.println(a);
    }
}
