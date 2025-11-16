package internship_management_system.ui.screens;

import internship_management_system.internships.InternshipApplication;
import internship_management_system.internships.InternshipOpportunity;
import internship_management_system.model.DataStorage;
import internship_management_system.ui.IO;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.ui.screens.career_center_staff.CCSApplicationScreen;
import internship_management_system.ui.screens.company_representative.CRApplicationScreen;
import internship_management_system.ui.screens.student.StudentApplicationScreen;
import internship_management_system.users.CompanyRepresentative;
import internship_management_system.users.Student;
import internship_management_system.users.User;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Screen to display a list of internship applications for a specific opportunity.
 */
public class OpportunityApplications implements Screen {

    public static final OpportunityApplications INSTANCE = new OpportunityApplications();

    /**
     * Private constructor to enforce singleton pattern.
     */
    private OpportunityApplications() {
    }

    @Override
    public Optional<Screen> show(UIState uiState) {
        if (!uiState.getCurrentUser().isPresent()) {
            IO.exitWithError("User not logged in");
        }
        if(uiState.getCurrentUser().get() instanceof Student) {
            IO.exitWithError("Students cannot access this screen");
        }
        if(uiState.getCurrentOpportunity().isEmpty()) {
            IO.exitWithError("No opportunity selected");
        }

        InternshipOpportunity opp = uiState.getCurrentOpportunity().get();

        IO.clearConsole();
        printTitle(String.format("Internship Applications for (id=%d) %s", opp.getId(), opp.getTitle()), uiState);

        User user = uiState.getCurrentUser().get();

        ArrayList<InternshipApplication> applications = DataStorage.getInternshipApplications(opp).stream()
                .filter(app -> app.matchesFilter(user.getApplicationFilterSettings()))
                .collect(Collectors.toCollection(ArrayList::new));

        if (applications.isEmpty()) {
            System.out.println("No internship application.");
        } else {
            System.out.println("Internship Applications:");
            System.out.println();
            for (int i = 0; i < applications.size(); i++) {
                InternshipApplication app = applications.get(i);
                System.out.println((i + 1) + ". " + app);
                System.out.println();
            }
        }

        System.out.print("");

        System.out.print("Type internship application row number (not id) to manage, or 0 to go back: ");

        String input = IO.getScanner().nextLine().trim();

        try {
            int choice = Integer.parseInt(input);
            if (choice == 0) {
                uiState.setCurrentApplication(Optional.empty());
                return Optional.empty();
            }
            if (choice < 1 || choice > applications.size()) {
                return Optional.of(INSTANCE);
            }
            if (user instanceof Student) {
                uiState.setCurrentApplication(Optional.of(applications.get(choice - 1)));
                return Optional.of(StudentApplicationScreen.INSTANCE);
            } else if (user instanceof CompanyRepresentative) {
                uiState.setCurrentApplication(Optional.of(applications.get(choice - 1)));
                return Optional.of(CRApplicationScreen.INSTANCE);
            } else {
                uiState.setCurrentApplication(Optional.of(applications.get(choice - 1)));
                return Optional.of(CCSApplicationScreen.INSTANCE);
            }
        } catch (NumberFormatException e) {
            return Optional.of(INSTANCE);
        }
    }
}
