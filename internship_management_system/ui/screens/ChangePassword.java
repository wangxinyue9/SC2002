package internship_management_system.ui.screens;

import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.ui.IO;
import java.util.Optional;

/**
 * Users can change their password here
 */
public class ChangePassword implements Screen {
    public static final ChangePassword INSTANCE = new ChangePassword();

    /**
     * Private constructor to enforce singleton pattern
     */
    private ChangePassword() {}


    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent()) {
            IO.exitWithError("User not logged in");
        }
        IO.clearConsole();
        printTitle("Change Password", uiState);

        System.out.print("Old password: ");
        String oldPassword = IO.getScanner().nextLine();
        System.out.print("New password: ");
        String newPassword = IO.getScanner().nextLine();

        boolean done = false;
        if (!uiState.getCurrentUser().get().validatePassword(oldPassword)) {
            System.out.println("Old password is incorrect");
        } else if (newPassword.length() < 6) {
            System.out.println("Password must be at least 6 characters long");
        } else {
            uiState.getCurrentUser().get().changePassword(newPassword);
            done = true;
        }

        if (!done) {
            System.out.print("Try again? [Y/n] ");
            String nxt = IO.getScanner().nextLine().trim();
            if (nxt.isEmpty() || nxt.toLowerCase().equals("y")) {
                return Optional.of(ChangePassword.INSTANCE);
            }
        }

        return Optional.empty();
    }

}
