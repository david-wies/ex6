package oop.ex6.main;


import java.io.IOException;
import java.util.HashMap;

public class roiTests {
    public static void main(String[] args) throws IOException {
        HashMap<String, Variable> vars = new HashMap<String, Variable>();
//        String name="hello";
//        String para = "int a ,    double b , char c    ";
////        String[] keys = {"void", "final", "if", "while", "true", "false", "return", "int", "double", "boolean", "char", "String"};
//        try {
//            Method testMethod = new Method(null, name, 6, vars);
//            testMethod.analysisParameters(para);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        String line = "int a,b=2 ,c ;";
        Parser pars = new Parser("C:\\Users\\roishtivi\\IdeaProjects\\ex6\\src\\oop\\ex6\\main");
        try {
            pars.updateVariables(vars, line, 5);
        } catch (IllegalException e) {
            System.out.println(e.getMessage());
        }


    }
}
