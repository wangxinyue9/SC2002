package internship_management_system.ui.screens.career_center_staff;

import internship_management_system.enums.InternshipOpportunityStatus;
import internship_management_system.internships.InternshipOpportunity;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.users.CareerCentreStaff;
import internship_management_system.ui.IO;
import internship_management_system.ui.screens.OpportunityApplications;
import java.util.Optional;


/**
 * Screen for career center staff to view/manage specific internship opportunity
 */
public class CCSOpportunityScreen implements Screen {
    public static final CCSOpportunityScreen INSTANCE = new CCSOpportunityScreen();

    /*
     * Private constructor to enforce singleton pattern.
     */
    private CCSOpportunityScreen() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent() || !(uiState.getCurrentUser().get() instanceof CareerCentreStaff)) {
            IO.exitWithError("Not logged in as career center staff.");
        }
        if(!uiState.getCurrentOpportunity().isPresent()) {
            IO.exitWithError("No internship opportunity selected.");
        }

        IO.clearConsole();
        printTitle("Manage Internship Opportunity", uiState);
        InternshipOpportunity opp = uiState.getCurrentOpportunity().get();

        System.out.println(opp);
        System.out.println();

        if(opp.getStatus() == InternshipOpportunityStatus.PENDING) {
            System.out.println("1. Approve Opportunity");
            System.out.println("2. Reject Opportunity");
        }
        else {
            System.out.println("1. View Applications");
        }

        System.out.println("0. Go back");
        System.out.print("Select an option: ");

        String choice = IO.getScanner().nextLine().trim();
        switch (choice) {
            case "1" -> {
                if(opp.getStatus() == InternshipOpportunityStatus.PENDING) {
                    opp.setStatus(InternshipOpportunityStatus.APPROVED);
                    System.out.println("Internship opportunity approved.");
                    System.out.print("Press any button to continue...");
                    IO.getScanner().nextLine();
                    return Optional.of(CCSOpportunityScreen.INSTANCE);
                } else {
                    uiState.setCurrentOpportunity(Optional.of(opp));
                    return Optional.of(OpportunityApplications.INSTANCE);
                }
            }
            case "2" -> {
                if(opp.getStatus() == InternshipOpportunityStatus.PENDING) {
                    opp.setStatus(InternshipOpportunityStatus.REJECTED);
                    System.out.println("Internship opportunity rejected.");
                    System.out.print("Press any button to continue...");
                    IO.getScanner().nextLine();
                }
                return Optional.of(CCSOpportunityScreen.INSTANCE);
            }
            case "0" -> {
                uiState.setCurrentOpportunity(Optional.empty());
                return Optional.empty();
            }
            default -> {
                return Optional.of(CCSOpportunityScreen.INSTANCE);
            }
        }
    } 
}
