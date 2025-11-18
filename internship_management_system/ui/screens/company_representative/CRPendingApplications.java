package internship_management_system.ui.screens.company_representative;

import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;

import java.util.stream.Collectors;

import internship_management_system.enums.CompanyRepresentativeStatus;
import internship_management_system.enums.InternshipApplicationStatus;
import internship_management_system.internships.InternshipApplication;
import internship_management_system.model.DataStorage;
import internship_management_system.ui.IO;
import internship_management_system.users.CompanyRepresentative;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Screen to view pending internship applications for Company Representatives
 */
public class CRPendingApplications implements Screen {
    public static final CRPendingApplications INSTANCE = new CRPendingApplications();

    /**
     * Private constructor for singleton pattern
     */
    private CRPendingApplications() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(uiState.getCurrentUser().isEmpty() || !(uiState.getCurrentUser().get() instanceof CompanyRepresentative)) {
            IO.exitWithError("Not logged in as Company Representative");
        }
        CompanyRepresentative cr = (CompanyRepresentative) uiState.getCurrentUser().get();
        if(cr.getStatus() != CompanyRepresentativeStatus.APPROVED) {
            IO.exitWithError("Company Representative not approved");
        }
        IO.clearConsole();
        printTitle("Pending Internship Applications", uiState);

        ArrayList<InternshipApplication> pendingApplications = DataStorage.getInternshipApplications().stream()
            .filter(app -> app.getStatus() == InternshipApplicationStatus.PENDING)
            .collect(Collectors.toCollection(ArrayList::new));

        if(pendingApplications.isEmpty()) {
            System.out.println("No pending internship applications.");
            System.err.println("Press Enter to return to the previous menu...");
            IO.getScanner().nextLine();
            return Optional.empty();
        }

        System.out.println("Pending Internship Applications:");
        System.out.println();
        for(int i = 0; i < pendingApplications.size(); i++) {
            InternshipApplication app = pendingApplications.get(i);
            System.out.println((i + 1) + ". " + app);
            System.out.println();
        }

        System.out.print("Type internship application row number (not id) to manage, or 0 to go back: ");
        String input = IO.getScanner().nextLine().trim();
        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return Optional.of(INSTANCE);
        }
        
        if (choice == 0) {
            return Optional.empty();
        }
        if (choice < 0 || choice > pendingApplications.size()) {
            return Optional.of(INSTANCE);
        }
        uiState.setCurrentApplication(Optional.of(pendingApplications.get(choice - 1)));
        return Optional.of(CRApplicationScreen.INSTANCE);
    }
    
}
