package oop.ex6.main;


import java.io.IOException;

public class roiTests {
    public static void main(String[] args) throws IOException {
//        Parser parser = new Parser();
//        String name="hello";
        String row = "void foo(int a){";
        Parser pars = new Parser();

////        String[] keys = {"void", "final", "if", "while", "true", "false", "return", "int", "double", "boolean", "char", "String"};
//        try {
//            Method testMethod = new Method(null, name, 6, row, 1);
//            pars.analyzeRow(row,0,2);
//            System.out.println("done");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        String line = " char a ='r',b, c='r' ";
        try {
            System.out.println(Parser.extractFirstWord("abc%%", 3));
//            pars.updateVariables(0, line, 5, "char");
//            System.out.println("update variable is done");
        } catch (IllegalException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
//        ArrayList<Integer>  array = new ArrayList<>();
//        array.add(3);
//        array.add(2);
//        array.add(1);
//        array.remove(1);
//        array.add(1,5);


    }
}
