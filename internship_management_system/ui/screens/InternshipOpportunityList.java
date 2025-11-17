package internship_management_system.ui.screens;

import internship_management_system.internships.InternshipOpportunity;
import internship_management_system.model.DataStorage;
import internship_management_system.ui.IO;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.ui.screens.career_center_staff.CCSOpportunityScreen;
import internship_management_system.ui.screens.company_representative.CROpportunityScreen;
import internship_management_system.ui.screens.company_representative.CreateNewOpportunity;
import internship_management_system.ui.screens.student.StudentOpportunityScreen;
import internship_management_system.users.CompanyRepresentative;
import internship_management_system.users.Student;
import internship_management_system.users.User;
import java.util.ArrayList;
import java.util.Optional;


/**
 * Screen to display a list of internship opportunities.
 */
public class InternshipOpportunityList implements Screen {
    public static final InternshipOpportunityList INSTANCE = new InternshipOpportunityList();

    /**
     * Private constructor to enforce singleton pattern.
     */
    private InternshipOpportunityList() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent()) {
            IO.exitWithError("User not logged in");
        }

        IO.clearConsole();
        printTitle("Internship Opportunities List", uiState);

        User user = uiState.getCurrentUser().get();

        ArrayList<InternshipOpportunity> opportunities = new ArrayList<>(DataStorage.getInternshipOpportunities(user.getOpportunityFilterSettings()));

        if (opportunities.isEmpty()) {
            System.out.println("No internship opportunities available.");
        }
        else {
            System.out.println("Available Internship Opportunities:");
            System.out.println();
            for (int i = 0; i < opportunities.size(); i++) {
                InternshipOpportunity opp = opportunities.get(i);
                System.out.println((i + 1) + ". " + opp);
                System.out.println();
            }
        }
        
        System.out.print("");
        if(user instanceof CompanyRepresentative) System.out.print("Type internship opportunity row number (not id) to manage, '+' to create new or 0 to go back: ");
        else if(user instanceof Student) System.out.print("Type internship opportunity row number (not id) to view, or 0 to go back: ");
        else System.out.print("Type internship opportunity row number (not id) to manage, or 0 to go back: ");

        String input = IO.getScanner().nextLine().trim();

        if(input.equals("+") && user instanceof CompanyRepresentative) {
            return Optional.of(CreateNewOpportunity.INSTANCE);
        }

        try {
            int choice = Integer.parseInt(input);
            if (choice == 0) {
                return Optional.empty();
            }
            if (choice < 1 || choice > opportunities.size()) {
                return Optional.of(InternshipOpportunityList.INSTANCE);
            }
            if(user instanceof Student) {
                uiState.setCurrentOpportunity(Optional.of(opportunities.get(choice - 1)));
                return Optional.of(StudentOpportunityScreen.INSTANCE);
            }
            else if(user instanceof CompanyRepresentative) {
                uiState.setCurrentOpportunity(Optional.of(opportunities.get(choice - 1)));
                return Optional.of(CROpportunityScreen.INSTANCE);
            }
            else {
                uiState.setCurrentOpportunity(Optional.of(opportunities.get(choice - 1)));
                return Optional.of(CCSOpportunityScreen.INSTANCE);
            }
        } catch (NumberFormatException e) {
            return Optional.of(InternshipOpportunityList.INSTANCE);
        }
    }
}
