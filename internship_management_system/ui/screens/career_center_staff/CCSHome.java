package internship_management_system.ui.screens.career_center_staff;

import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.ui.IO;
import internship_management_system.ui.screens.ChangePassword;
import internship_management_system.ui.screens.HomeScreen;
import internship_management_system.ui.screens.InternshipApplicationList;
import internship_management_system.ui.screens.InternshipOpportunityList;
import internship_management_system.ui.screens.filters.application.EditApplicationFilter;
import internship_management_system.ui.screens.filters.opportunity.EditOpportunityFilter;
import internship_management_system.users.CareerCentreStaff;
import java.util.Optional;

/**
 * Career Center Staff Home Screen
 */
public class CCSHome implements Screen {
    public static final CCSHome INSTANCE = new CCSHome();

    /**
     * Private constructor to enforce singleton pattern
     */
    private CCSHome() {}


    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!(uiState.getCurrentUser().isPresent())) {
            IO.exitWithError("User not logged in");
        }
        if(!(uiState.getCurrentUser().get() instanceof CareerCentreStaff)) {
            IO.exitWithError("Current user is not Career Center Staff");
        }
        IO.clearConsole();
        printTitle("Career Center Staff Home", uiState);

        String operations[] = {
            "Change password",
            "Edit internship opportunity filter settings",
            "Edit internship application filter settings",
            "Manage company representative applications",
            "Manage internship opportunities",
            "Manage pending internship opportunities",
            "Manage internship applications",
            "Manage applications with pending withdrawal requests",
            "Generate report"
        };
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
                return Optional.of(EditApplicationFilter.INSTANCE);
            }
            case "4" -> {
                return Optional.of(CCSManageCRApplications.INSTANCE);
            }
            case "5" -> {
                return Optional.of(InternshipOpportunityList.INSTANCE);
            }
            case "6" -> {
                return Optional.of(CCSPendingOpportunities.INSTANCE);
            }
            case "7" -> {
                return Optional.of(InternshipApplicationList.INSTANCE);
            }
            case "8" -> {
                return Optional.of(CCSPendingWithdrawRequests.INSTANCE);
            }
            case "9" -> {
                return Optional.of(CCSReportScreen.INSTANCE);
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
                return Optional.of(CCSHome.INSTANCE);
            }
        }
    }

}
