package internship_management_system.ui.screens.company_representative;

import internship_management_system.enums.InternshipApplicationStatus;
import internship_management_system.enums.WithdrawStatus;
import internship_management_system.internships.InternshipApplication;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.ui.IO;
import java.util.Optional;
import internship_management_system.users.CompanyRepresentative;

/**
 * Screen for Company Representative to view/manage individual internship application
 */
public class CRApplicationScreen implements Screen {
    public static final CRApplicationScreen INSTANCE = new CRApplicationScreen();
    /*
     * Private constructor to enforce singleton pattern.
     */
    private CRApplicationScreen() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent() || !(uiState.getCurrentUser().get() instanceof CompanyRepresentative)) {
            IO.exitWithError("Not logged in as Company Representative.");
        }
        if(!uiState.getCurrentApplication().isPresent()) {
            IO.exitWithError("No internship application selected.");  
        }
        IO.clearConsole();
        printTitle("View internship application", uiState);

        InternshipApplication app = uiState.getCurrentApplication().get();

        System.out.println(app);
        System.out.println("");

        if(app.getStatus() == InternshipApplicationStatus.PENDING && app.getWithdrawStatus() != WithdrawStatus.APPROVED && app.getOpportunity().getStatus() != internship_management_system.enums.InternshipOpportunityStatus.FILLED) {
            System.out.println("1. Offer Internship");
            System.out.println("2. Reject Application");
        }

        System.out.println("0. Back");
        System.out.print("Select an option: ");
        String choice = IO.getScanner().nextLine().trim();
        if(app.getStatus() == InternshipApplicationStatus.PENDING && app.getWithdrawStatus() != WithdrawStatus.APPROVED) {
            if(choice.equals("1")) {
                app.finalizeApplicationStatus(true);
                return Optional.of(INSTANCE);
            }
            if(choice.equals("2")) {
                app.finalizeApplicationStatus(false);
                return Optional.of(INSTANCE);
            }
        }
        if(choice.equals("0")) {
            uiState.setCurrentApplication(Optional.empty());
            return Optional.empty();
        }
        return Optional.of(INSTANCE);
    }
}
