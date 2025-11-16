package internship_management_system.ui.screens.student;

import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.ui.IO;
import internship_management_system.users.Student;
import java.util.Optional;


/**
 * Screen for students to view/apply/manage specific internship opportunity
 */
public class StudentOpportunityScreen implements Screen {
    public static final StudentOpportunityScreen INSTANCE = new StudentOpportunityScreen();

    /*
     * Private constructor to enforce singleton pattern.
     */
    private StudentOpportunityScreen() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent() || !(uiState.getCurrentUser().get() instanceof Student)) {
            IO.exitWithError("Not logged in as a student.");
        }

        Student student = (Student) uiState.getCurrentUser().get();
        IO.clearConsole();
        printHeader("View internship details", uiState);

    }
}
