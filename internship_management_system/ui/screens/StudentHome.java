package internship_management_system.ui.screens;

import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.ui.IO;
import internship_management_system.ui.screens.filters.EditOpportunityFilter;
import internship_management_system.users.Student;
import java.util.Optional;

/**
 * Student's home screen
 */
public class StudentHome implements Screen {
    public static final StudentHome INSTANCE = new StudentHome();

    /**
     * Private constructor to enforce singleton pattern
     */
    private StudentHome() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent()) {
            IO.exitWithError("User not logged in");
        }
        if(!(uiState.getCurrentUser().get() instanceof Student)) {
            IO.exitWithError("Current user is not a student");
        }

        IO.clearConsole();
        printTitle("Student Home", uiState);

        String operations[] = {
            "Change password",
            "Edit internship opportunity filter settings",
            "Edit internship application filter settings",
            "Show all internship opportunities",
            "Show my internship applications",};
        for (int i = 0; i < operations.length; i++) {
            System.out.printf("%d. %s\n", i + 1, operations[i]);
        }
        System.out.println("0. Logout");
        System.out.println("e. Exit");

        System.out.print("Please choose an operation: ");
        String op = IO.getScanner().nextLine().trim();

        switch (op) {
            case "1" -> {
                return Optional.of(ChangePassword.INSTANCE);
            }
            case "2" -> {
                return Optional.of(EditOpportunityFilter.INSTANCE);
            }
            case "3" -> {
                // TODO: TBI
                return Optional.of(StudentHome.INSTANCE);
            }
            case "4" -> {
                // TODO: TBI
                return Optional.of(StudentHome.INSTANCE);
            }
            case "5" -> {
                // TODO: TBI
                return Optional.of(StudentHome.INSTANCE);
            }
            case "0" -> {
                uiState.setCurrentUser(Optional.empty());
                return Optional.of(HomeScreen.INSTANCE);
            }
            case "e" -> {
                System.exit(0);
                return Optional.empty();
            }
            default -> {
                return Optional.of(StudentHome.INSTANCE);
            }
        }
    }

}
