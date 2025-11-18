package internship_management_system.ui.screens.company_representative;

import internship_management_system.enums.InternshipOpportunityStatus;
import internship_management_system.internships.InternshipOpportunity;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.users.CompanyRepresentative;
import internship_management_system.ui.IO;
import internship_management_system.ui.screens.OpportunityApplications;
import java.util.Optional;


/**
 * Screen for company representatives to view/manage specific internship opportunity
 */
public class CROpportunityScreen implements Screen {
    public static final CROpportunityScreen INSTANCE = new CROpportunityScreen();

    /*
     * Private constructor to enforce singleton pattern.
     */
    private CROpportunityScreen() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent() || !(uiState.getCurrentUser().get() instanceof CompanyRepresentative)) {
            IO.exitWithError("Not logged in as a company representative.");
        }
        if(!uiState.getCurrentOpportunity().isPresent()) {
            IO.exitWithError("No internship opportunity selected.");
        }
        IO.clearConsole();
        printTitle("Manage Internship Opportunity", uiState);
        InternshipOpportunity opp = uiState.getCurrentOpportunity().get();

        System.out.println(opp);
        System.out.println("");
        if(opp.getVisibility()) {
            System.err.println("This opportunity is currently VISIBLE.");
        } else {
            System.err.println("This opportunity is currently HIDDEN.");
        }

        System.out.println("1. Toggle Visibility");
        System.out.println("2. View Applications");
        if(opp.getStatus() == InternshipOpportunityStatus.PENDING) {
            System.out.println("3. Edit Opportunity");
            System.out.println("4. Delete Opportunity");
        }
        System.out.println("0. Back");

        String input = IO.getScanner().nextLine().trim();
        switch(input) {
            case "1" -> {
                opp.toggleVisibility();
                return Optional.of(INSTANCE);
            }
            case "2" -> {
                uiState.setCurrentOpportunity(Optional.of(opp));
                return Optional.of(OpportunityApplications.INSTANCE);
            }
            case "3" -> {
                if(opp.getStatus() != InternshipOpportunityStatus.PENDING) {
                    return Optional.of(INSTANCE);
                }
                uiState.setCurrentOpportunity(Optional.of(opp));
                return Optional.of(CREditOpportunity.INSTANCE);
            }
            case "4" -> {
                if(opp.getStatus() != InternshipOpportunityStatus.PENDING) {
                    return Optional.of(INSTANCE);
                }
                System.out.print("Are you sure you want to delete this opportunity? This action cannot be undone. [Y/n]: ");
                String choice = IO.getScanner().nextLine().trim().toLowerCase();
                if (choice.isEmpty() || choice.equals("y")) {
                    opp.delete();
                    return Optional.empty();
                } else {
                    return Optional.of(INSTANCE);
                }
            }
            case "0" -> {
                return Optional.empty();
            }
            default -> {
                return Optional.of(INSTANCE);
            }
        }
    }    
}
