package internship_management_system;
import internship_management_system.ui.UIManager;

/**
 * Main class to start the Internship Management System
 */
public class Main {
    /**
     * Main method to launch the application
     *
     * @param args command line arguments: student-file, career-staff-file
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: ims <student-file.csv> <career-staff-file.csv>");
            System.exit(1);
        }

        UIManager uiManager = new UIManager(args[0], args[1]);
        uiManager.run();
    }
}
