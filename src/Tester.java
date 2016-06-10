import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Created by David Wies.
 */
public class Tester {

    private static PrintStream out;

    public static void main(String[] args) throws FileNotFoundException {
        out = new PrintStream(new FileOutputStream("Tester-Results.txt"));
        for (String path : args) {
            independenceTest(path);
        }
    }

    private static void independenceTest(String path) {
        System.setErr(out);
        System.setOut(out);
        File directory = new File(path);
        System.out.println("Directory: " + path + "\n");
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                System.out.println("Tester " + file.getName() + ":");
                String[] parameters = {file.getAbsolutePath()};
                oop.ex6.main.Sjavac.main(parameters);
                System.out.print("\n");
            }
        } else {
            System.err.println("Path " + path + " is not directory.");
        }
    }

}
