package internship_management_system.ui.screens.company_representative;

import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.ui.screens.HomeScreen;
import internship_management_system.users.CompanyRepresentative;
import internship_management_system.model.DataStorage;
import internship_management_system.ui.IO;
import java.util.Optional;

/**
 * Company Representatives can be registered here
 */
public class CRRegister implements Screen {
    public static final CRRegister INSTANCE = new CRRegister();

    /**
     * Private constructor to enforce singleton pattern
     */
    private CRRegister() {}


    @Override
    public Optional<Screen> show(UIState uiState) {
        IO.clearConsole();
        printTitle("Company Representative Registration", uiState);

        System.out.print("Full name (keep empty to go back): ");
        String name = IO.getScanner().nextLine().trim();
        if (name.isEmpty()) {
            System.out.print("Are you sure you want to go back? [Y/n] ");
            String confirm = IO.getScanner().nextLine().trim().toLowerCase();
            if (confirm.equals("y") || confirm.equals("")) {
                return Optional.of(HomeScreen.INSTANCE);
            }
            else {
                return Optional.of(INSTANCE);
            }
        }

        System.out.print("Email: ");
        String email = IO.getScanner().nextLine().trim();
        System.out.print("Company name: ");
        String companyName = IO.getScanner().nextLine().trim();
        System.out.print("Department: ");
        String department = IO.getScanner().nextLine().trim();
        System.out.print("Position: ");
        String position = IO.getScanner().nextLine().trim();
        if (name.isEmpty() || companyName.isEmpty() || department.isEmpty() || position.isEmpty() || email.isEmpty()) {
            System.out.println("All fields are required.");
            System.out.print("Press Enter to try again...");
            IO.getScanner().nextLine();
            return Optional.of(INSTANCE);
        }

        if(DataStorage.getCompanyReps().stream().anyMatch(cr -> cr.getCompanyName().equals(companyName))) {
            System.out.println("A Company Representative from this company already exists.");
            System.out.print("Press Enter to try again...");
            IO.getScanner().nextLine();
            return Optional.of(INSTANCE);
        }

        System.out.print("Create a password (min 6 characters): ");
        String password = IO.getScanner().nextLine();
        if (password.length() < 6) {
            System.out.println("Password must be at least 6 characters long.");
            System.out.print("Press Enter to try again...");
            IO.getScanner().nextLine();
            return Optional.of(INSTANCE);
        }

        CompanyRepresentative newRep = DataStorage.newCompanyRep(name, companyName, department, position, email);
        newRep.changePassword(password);


        System.out.printf("\nRegistration submitted. Your user id is \"%s\". A Career Centre Staff must approve your account before you can log in.\n", newRep.getUserID());
        System.out.print("Press Enter to return to login...");
        IO.getScanner().nextLine();
        return Optional.of(HomeScreen.INSTANCE);
    }

}
