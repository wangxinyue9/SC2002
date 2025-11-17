package internship_management_system;
import internship_management_system.ui.UIManager;

/**
 * Main class to start the Internship Management System
 */
public class Main {
    /**
     * Main method to launch the application
     *
     * @param args command line arguments: student-file, company-rep-file, career-staff-file
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: ims <student-file.csv> <company-rep-file.csv> career-staff-file.csv>");
            System.exit(1);
        }

        UIManager uiManager = new UIManager(args[0], args[1], args[2]);
        uiManager.run();
    }
}
