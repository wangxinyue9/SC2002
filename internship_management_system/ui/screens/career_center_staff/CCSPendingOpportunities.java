package internship_management_system.ui.screens.career_center_staff;

import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.users.CareerCentreStaff;
import internship_management_system.enums.InternshipOpportunityStatus;
import internship_management_system.internships.InternshipOpportunity;
import internship_management_system.model.DataStorage;
import internship_management_system.ui.IO;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Screen to view pending internship opportunities for Career Center Staff
 */
public class CCSPendingOpportunities implements Screen {
    public static final CCSPendingOpportunities INSTANCE = new CCSPendingOpportunities();

    /**
     * Private constructor for singleton pattern
     */
    private CCSPendingOpportunities() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(uiState.getCurrentUser().isEmpty() || !(uiState.getCurrentUser().get() instanceof CareerCentreStaff)) {
            IO.exitWithError("Not logged in as Career Centre Staff");
        }
        IO.clearConsole();
        printTitle("Pending Internship Opportunities", uiState);

        ArrayList<InternshipOpportunity> opps = DataStorage.getInternshipOpportunities().stream()
            .filter(opportunity -> opportunity.getStatus() == InternshipOpportunityStatus.PENDING)
            .collect(Collectors.toCollection(ArrayList::new));
        
        if(opps.isEmpty()) {
            System.out.println("No pending internship opportunities found.");
            System.out.println("Press Enter to return to the previous menu...");
            IO.getScanner().nextLine();
            return Optional.empty();
        }

        System.err.println("Pending Internship Opportunities:");
        for(int i = 0; i < opps.size(); i++) {
            InternshipOpportunity opp = opps.get(i);
            System.out.print((i+1)+". ");
            System.out.println(opp);
            System.out.println("");
        }
        System.out.print("Type internship opportunity row number (not id) to view, or 0 to go back: ");
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
        if(choice < 1 || choice > opps.size()) {
            return Optional.of(INSTANCE);
        }
        uiState.setCurrentOpportunity(Optional.of(opps.get(choice-1)));
        return Optional.of(CCSOpportunityScreen.INSTANCE);
    }
    
}
