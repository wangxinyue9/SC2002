package internship_management_system.ui.screens;

import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.ui.IO;
import java.util.Optional;

/**
 * Home screen of the Internship Management System
 */
public class HomeScreen implements Screen {
    public static final HomeScreen INSTANCE = new HomeScreen();

    /**
     * Private constructor to enforce singleton pattern
     */
    private HomeScreen() {}


    @Override
    public Optional<Screen> show(UIState uiState) {
        if(uiState.getCurrentUser().isPresent()) {
            IO.exitWithError("User already logged in");
        }
        IO.clearConsole();
        printTitle("Login/Register", uiState);

        System.out.println("1. Login");
        System.out.println("2. Register as Company Representative");
        System.out.println("e. Exit");
        System.out.print("Select an option: ");
        String choice = IO.getScanner().nextLine().trim();

        switch (choice) {
            case "1" -> {
                return Optional.of(LoginScreen.INSTANCE);
            }
            case "2" -> {
                return Optional.of(CompanyRepRegister.INSTANCE);
            }
            case "e" -> {
                System.exit(0);
            }
            default -> {
                return Optional.of(HomeScreen.INSTANCE);
            }
        }
        return Optional.of(HomeScreen.INSTANCE);
    }

}
