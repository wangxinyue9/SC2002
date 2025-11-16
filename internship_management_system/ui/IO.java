package internship_management_system.ui;
import java.io.IOException;
import java.util.Scanner;

/**
 * Singleton class to manage IO operations
 */
public class IO {
    private static final Scanner sc = new Scanner(System.in);

    /**
     * Private constructor to prevent instantiation
     */
    private IO() {
    }

    /**
     * Get the singleton scanner instance
     */
    public static Scanner getScanner() {
        return sc;
    }

    /**
     * Close the singleton scanner instance
     */
    public static void closeScanner() {
        sc.close();
    }

    /**
     * Clear the console screen
     */
    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (final IOException | InterruptedException e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    /**
     * Exit with an error printing something went wrong
     * @param e The exception that was thrown
     */
    public static void exitWithError(Exception e) {
        System.err.println("Something went wrong: " + e.getMessage());
        System.exit(1);
    }

    /**
     * Exit with an error message
     * @param message The error message to print
     */
    public static void exitWithError(String message) {
        System.err.println("Error: " + message);
        System.exit(1);
    }
}
