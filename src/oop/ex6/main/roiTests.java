package oop.ex6.main;


import java.io.IOException;
import java.util.HashMap;

public class roiTests {
    public static void main(String[] args) throws IOException {
        Parser parser = new Parser();
        HashMap<String, Variable> vars = new HashMap<String, Variable>();
        String name="hello";
        String para = "final int a ,    double b , char c    ";
//        String[] keys = {"void", "final", "if", "while", "true", "false", "return", "int", "double", "boolean", "char", "String"};
        try {
            Method testMethod = new Method(null, name, 6, para, 1);
            testMethod.analysisParameters(para);
            System.out.println("done");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        String line = "final String b=\"hellohii\" ,c ;";
//        Parser pars = new Parser("C:\\Users\\roishtivi\\IdeaProjects\\ex6\\src\\oop\\ex6\\main");
//        try {
//            pars.updateVariables(vars, line, 5);
//            System.out.println("hi");
//        } catch (IllegalException e) {
//            e.printStackTrace();
//            System.out.println(e.getMessage());
//        }
//        ArrayList<Integer> array = new ArrayList<>();
//        array.add(3);
//        array.add(2);
//        array.add(1);
//        array.remove(1);
//        array.add(1,5);


    }
}
