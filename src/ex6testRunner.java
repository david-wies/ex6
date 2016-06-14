//package test;

//import oop.ex6.main.Constants.RePatterns;
//import oop.ex6.main.Exceptions.IOErrorException;
//import oop.ex6.main.Exceptions.SyntaxErrorException;
//import oop.ex6.main.ScopeVar.Scope;

import oop.ex6.main.Sjavac;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ex6testRunner {
    private static final int MIN_ARG_LEN = 1;
    private static final int FILE_LOCATION = 0;
    private static final String NEXT_LINE = "\n";
    private static final String TEST_PATH = ".\\tests\\";
    private static final Pattern p = Pattern.compile("test([0-9]+).sjava\\s+([0-2])");

    public static void main(String[] args) {
//        runAndCheck();
//        System.out.println(findTest("001"));
//        runAndCheck();
        runSpecificFileWPrint("test412.sjava");
    }

    private static void runAndCheck() {
        File folder = new File(TEST_PATH);
//        Scope scope;
        String[] m = new String[1];
        try {
            PrintStream out = new PrintStream(new FileOutputStream("results.txt"));
            PrintStream errs = new PrintStream(new FileOutputStream("errs.txt"));
            System.setOut(out);
            System.setErr(errs);
            for (File test : folder.listFiles()) {
                runSpecificFile(test.getName());
            }
            Matcher mat = p.matcher(convertFile("results.txt"));
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
            while (mat.find()) {
                if (!mat.group(2).contains(findTest(mat.group(1)))) {
                    System.out.println("error in test: " + mat.group(1));
                    System.out.println("received error code: " + mat.group(2));
                    System.out.println("wanted error code: " + findTest(mat.group(1)) + "\n");
                    printTest(TEST_PATH + "test" + mat.group(1) + ".sjava");
                    System.out.println("\n");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static String findTest(String test) {
        try {
            Matcher m = p.matcher(convertFile("text.txt"));
            while (m.find()) {
                if (m.group(1).equals(test)) {
                    return m.group(2);
                }
            }
            return null;
        } catch (Exception ex) {
            System.out.println("Hey");
        }
        return null;
    }

    public static void runAll() {
        File folder = new File(TEST_PATH);
//        Scope scope;
        String[] m = new String[1];
        for (File test : folder.listFiles()) {
            runSpecificFile(test.getName());
        }
    }

    private static void runSpecificFile(String name) {
        System.out.println(name);
        String pre = TEST_PATH;
        String[] m = new String[1];
        m[0] = pre + name;
        Sjavac.main(m);
    }

    public static void runSpecificFileWPrint(String name) {
        System.out.println(name);
        String pre = TEST_PATH;
        String[] m = new String[1];
        m[0] = pre + name;
        printTest(pre + name);
        Sjavac.main(m);
    }


    private static void printTest(String path) {
        try {
            System.out.println(convertFile(path));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static String convertFile(String path) throws IOException {
        try {
            Path file = Paths.get(path);
            if (path.substring(path.lastIndexOf(".")).equalsIgnoreCase("sjava")) {
                throw new IOException("Cant parse non sJava files");
            }
            String content = "";
            for (String s : Files.readAllLines(file)) {
                content += s + NEXT_LINE;
            }
            return content.substring(0, content.length() - 1);
        } catch (IOException ex) {
            throw new IOException();
        }
    }
}
