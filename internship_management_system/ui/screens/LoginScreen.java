package internship_management_system.ui.screens;

import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.users.User;
import internship_management_system.model.DataStorage;
import internship_management_system.ui.IO;
import internship_management_system.users.CompanyRepresentative;
import internship_management_system.users.Student;
import java.util.Optional;

/**
 * Login screen of the Internship Management System
 * prompts for user to login
 */
public class LoginScreen implements Screen {
    public static final LoginScreen INSTANCE = new LoginScreen();

    /**
     * Private constructor to enforce singleton pattern
     */
    private LoginScreen() {}


    @Override
    public Optional<Screen> show(UIState uiState) {
        if(uiState.getCurrentUser().isPresent()) {
            IO.exitWithError("User already logged in");
        }
        IO.clearConsole();
        printTitle("Login", uiState);

        System.out.print("Username (keep empty to go back): ");
        String username = IO.getScanner().nextLine().trim();
        if (username.isEmpty()) {
            System.out.print("Are you sure you want to go back? [Y/n] ");
            String confirm = IO.getScanner().nextLine().trim().toLowerCase();
            if (confirm.equals("y") || confirm.equals("")) {
                return Optional.of(HomeScreen.INSTANCE);
            }
            else {
                return Optional.of(INSTANCE);
            }
        }
        System.out.print("Password: ");
        String password = IO.getScanner().nextLine();
        Optional<User> u = DataStorage.getUserByUserID(username);
        if (!u.isPresent() || !u.get().validatePassword(password)) {
            System.out.println("Invalid username or password");
            System.out.print("Try again? [Y/n] ");
            String input = IO.getScanner().nextLine();
            if(input.isEmpty() || input.trim().equalsIgnoreCase("y")) {
                return Optional.of(LoginScreen.INSTANCE);
            }
            return Optional.of(HomeScreen.INSTANCE);
        }

        uiState.setCurrentUser(u);
        if(u.get() instanceof Student) {
            return Optional.of(StudentHome.INSTANCE);
        }
        else if(u.get() instanceof CompanyRepresentative) {
            return Optional.of(CompanyRepHome.INSTANCE);
        }
        else {
            return Optional.of(CareerCentreStaffHome.INSTANCE);
        }
    }

}
