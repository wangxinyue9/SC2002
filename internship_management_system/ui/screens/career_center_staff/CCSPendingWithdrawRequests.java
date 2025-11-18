package internship_management_system.ui.screens.career_center_staff;

import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.users.CareerCentreStaff;
import internship_management_system.enums.WithdrawStatus;
import internship_management_system.internships.InternshipApplication;
import internship_management_system.model.DataStorage;
import internship_management_system.ui.IO;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Screen to view pending withdrawal requests for Career Center Staff
 */
public class CCSPendingWithdrawRequests implements Screen {
    public static final CCSPendingWithdrawRequests INSTANCE = new CCSPendingWithdrawRequests();
    
    /**
     * Private constructor for singleton pattern
     */
    private CCSPendingWithdrawRequests() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(uiState.getCurrentUser().isEmpty() || !(uiState.getCurrentUser().get() instanceof CareerCentreStaff)) {
            IO.exitWithError("Not logged in as Career Centre Staff");
        }
        IO.clearConsole();
        printTitle("Applications with Pending Withdrawal Requests", uiState);

        ArrayList<InternshipApplication> applications = DataStorage.getInternshipApplications().stream()
            .filter(app -> app.getWithdrawStatus() == WithdrawStatus.PENDING)
            .collect(Collectors.toCollection(ArrayList::new));
        
        if(applications.isEmpty()) {
            System.out.println("No application with pending withdrawal requests found.");
            System.out.println("Press Enter to return to the previous menu...");
            IO.getScanner().nextLine();
            return Optional.empty();
        }

        System.out.println("Applications with Pending Withdrawal Requests:");

        for(int i = 0; i < applications.size(); i++) {
            InternshipApplication app = applications.get(i);
            System.out.print((i+1)+". ");
            System.out.println(app);
            System.out.println("");
        }
        System.out.print("Type application row number (not id) to view, or 0 to go back: ");
        String input = IO.getScanner().nextLine().trim();

        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return Optional.of(INSTANCE);
        }
        if(choice == 0) {
            return Optional.empty();
        }
        if(choice < 1 || choice > applications.size()) {
            return Optional.of(INSTANCE);
        }
        uiState.setCurrentApplication(Optional.of(applications.get(choice-1)));
        return Optional.of(CCSApplicationScreen.INSTANCE);
    }
}