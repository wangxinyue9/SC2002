package internship_management_system.ui.screens;

import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.users.CompanyRepresentative;
import internship_management_system.Model.DataStorage;
import internship_management_system.ui.IO;
import java.util.Optional;

/**
 * Company Representatives can be registered here
 */
public class CompanyRepRegister implements Screen {
    public static final CompanyRepRegister INSTANCE = new CompanyRepRegister();

    /**
     * Private constructor to enforce singleton pattern
     */
    private CompanyRepRegister() {}


    @Override
    public Optional<Screen> show(UIState uiState) {
        IO.clearConsole();
        printTitle("Company Representative Registration", uiState);

         System.out.print("Choose a user ID: ");
        String userID = IO.getScanner().nextLine().trim();
        if (userID.isEmpty()) {
            System.out.println("User ID cannot be empty.");
            System.out.print("Press Enter to try again...");
            IO.getScanner().nextLine();
            return Optional.of(CompanyRepRegister.INSTANCE);
        }
        if (DataStorage.getUserByUserID(userID).isPresent()) {
            System.out.printf("User ID \"%s\" is already taken.%n", userID);
            System.out.print("Press Enter to try again...");
            IO.getScanner().nextLine();
            return Optional.of(CompanyRepRegister.INSTANCE);
        }

        System.out.print("Full name: ");
        String name = IO.getScanner().nextLine().trim();
        System.out.print("Company name: ");
        String companyName = IO.getScanner().nextLine().trim();
        System.out.print("Department: ");
        String department = IO.getScanner().nextLine().trim();
        System.out.print("Position: ");
        String position = IO.getScanner().nextLine().trim();
        if (name.isEmpty() || companyName.isEmpty() || department.isEmpty() || position.isEmpty()) {
            System.out.println("All fields are required.");
            System.out.print("Press Enter to try again...");
            IO.getScanner().nextLine();
            return Optional.of(CompanyRepRegister.INSTANCE);
        }

        System.out.print("Create a password (min 6 characters): ");
        String password = IO.getScanner().nextLine();
        if (password.length() < 6) {
            System.out.println("Password must be at least 6 characters long.");
            System.out.print("Press Enter to try again...");
            IO.getScanner().nextLine();
            return Optional.of(CompanyRepRegister.INSTANCE);
        }

        CompanyRepresentative newRep = DataStorage.newCompanyRep(userID, name, companyName, department, position);
        newRep.changePassword(password);


        System.out.println("\nRegistration submitted. A Career Centre Staff must approve your account before you can log in.");
        System.out.print("Press Enter to return to login...");
        IO.getScanner().nextLine();
        return Optional.of(LoginScreen.INSTANCE);
    }

}
