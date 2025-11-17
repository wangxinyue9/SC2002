package internship_management_system.ui.screens.career_center_staff;

import internship_management_system.enums.WithdrawStatus;
import internship_management_system.internships.InternshipApplication;
import internship_management_system.ui.Screen;
import internship_management_system.ui.IO;
import internship_management_system.ui.UIState;
import internship_management_system.users.CareerCentreStaff;
import java.util.Optional;

/**
 * Screen for Career Center Staff to view/manage individual internship application
 */
public class CCSApplicationScreen implements Screen {
    public static final CCSApplicationScreen INSTANCE = new CCSApplicationScreen();

    /*
     * Private constructor to enforce singleton pattern.
     */
    private CCSApplicationScreen() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent() || !(uiState.getCurrentUser().get() instanceof CareerCentreStaff)) {
            IO.exitWithError("Not logged in as Career Centre Staff.");
        }
        if(!uiState.getCurrentApplication().isPresent()) {
            IO.exitWithError("No internship application selected.");
        }

        IO.clearConsole();
        printTitle("View internship application", uiState);
        InternshipApplication app = uiState.getCurrentApplication().get();

        System.out.println(app);
        System.out.println("");

        if(app.getWithdrawStatus() == WithdrawStatus.PENDING) {
            System.out.println("1. Approve Withdrawal Request");
            System.out.println("2. Reject Withdrawal Request");
        }
        System.out.println("0. Back");
        System.out.print("Select an option: ");
        String choice = IO.getScanner().nextLine().trim();

        if(app.getWithdrawStatus() == WithdrawStatus.PENDING && choice.equals("1")) {
            app.confirmWithdraw(true);
            return Optional.of(INSTANCE);
        }
        if(app.getWithdrawStatus() == WithdrawStatus.PENDING && choice.equals("2")) {
            app.confirmWithdraw(false);
            return Optional.of(INSTANCE);
        }
        if(choice.equals("0")) {
            uiState.setCurrentApplication(Optional.empty());
            return Optional.empty();
        }

        return Optional.of(INSTANCE);
    }

}
