package internship_management_system.ui.screens.company_representative;

import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.ui.screens.filters.opportunity.EditOpportunityFilter;
import internship_management_system.enums.CompanyRepresentativeStatus;
import internship_management_system.ui.IO;
import internship_management_system.ui.screens.ChangePassword;
import internship_management_system.ui.screens.HomeScreen;
import internship_management_system.ui.screens.InternshipApplicationList;
import internship_management_system.ui.screens.InternshipOpportunityList;
import internship_management_system.ui.screens.filters.application.EditApplicationFilter;
import internship_management_system.users.CompanyRepresentative;
import java.util.Optional;

/**
 * Company Representative Home Screen
 */
public class CRHome implements Screen {
    public static final CRHome INSTANCE = new CRHome();

    /**
     * Private constructor to enforce singleton pattern
     */
    private CRHome() {}


    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!(uiState.getCurrentUser().isPresent())) {
            IO.exitWithError("User not logged in");
        }
        if(!(uiState.getCurrentUser().get() instanceof CompanyRepresentative)) {
            IO.exitWithError("Current user is not a Company Representative");
        }
        CompanyRepresentative user = (CompanyRepresentative) uiState.getCurrentUser().get();
        
        IO.clearConsole();
        printTitle("Company Representative Home", uiState);

        if (user.getStatus() != CompanyRepresentativeStatus.APPROVED) {
            if (user.getStatus() == CompanyRepresentativeStatus.REJECTED) {
                System.out.println("Your application to be a Company Representative has been rejected by the Career Center Staff.");
            } else {
                System.out.println("Your account is still pending approval by the Career Center Staff.");
            }
            System.out.println("Please contact the Career Center for more information.");
            System.out.println("Press Enter to return to login screen...");
            IO.getScanner().nextLine();
            uiState.setCurrentUser(Optional.empty());
            return Optional.of(HomeScreen.INSTANCE);
        }

        String operations[] = {
            "Change password",
            "Edit internship opportunity filter settings",
            "Edit internship application filter settings",
            "Manage internship opportunities",
            "Manage internship applications",
            "Manage pending internship applications"
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
                return Optional.of(InternshipOpportunityList.INSTANCE);
            }
            case "5" -> {
                return Optional.of(InternshipApplicationList.INSTANCE);
            }
            case "6" -> {
                return Optional.of(CRPendingApplications.INSTANCE);
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
                return Optional.of(CRHome.INSTANCE);
            }
        }
    }

}
