import java.io.*;

/**
 * Created by David Wies.
 */
public class Tester {

    private static PrintStream out;

    public static void main(String[] args) throws FileNotFoundException {
        out = new PrintStream(new FileOutputStream("Tester-Results.txt"));
        for (String path : args) {
            try {
                independenceTest(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void independenceTest(String path) throws IOException {
        System.setErr(out);
        System.setOut(out);
        File directory = new File(path);
        if (directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                System.out.println("Tester " + file.getName() + ":\n");
                String[] parameters = {file.getAbsolutePath()};
                oop.ex6.main.Sjavac.main(parameters);
            }
        } else {
            System.err.println("Path " + path + " is not directory.");
        }
    }

}
