package internship_management_system.ui.screens.filters;

import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.users.CompanyRepresentative;
import internship_management_system.users.Student;
import internship_management_system.users.User;
import internship_management_system.enums.InternshipLevel;
import internship_management_system.enums.InternshipOpportunityStatus;
import internship_management_system.filter.InternshipOpportunityFilterSettings;
import internship_management_system.ui.IO;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Screen to edit internship opportunity filter settings
 */
public class EditOpportunityFilter implements Screen {
    public static final EditOpportunityFilter INSTANCE = new EditOpportunityFilter();

    /**
     * Private constructor to enforce singleton pattern
     */
    private EditOpportunityFilter() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent()) {
            IO.exitWithError("User not logged in");
        }
        IO.clearConsole();
        printTitle("Edit Internship Opportunity Filter Settings", uiState);

        ArrayList<String> operations = new ArrayList<>();
        ArrayList<String> currentValue = new ArrayList<>();
        ArrayList<Screen> operationFunctions = new ArrayList<>();

        User user = uiState.getCurrentUser().get();
        InternshipOpportunityFilterSettings settings = user.getOpportunityFilterSettings();

        if (!(user instanceof Student)) {
            operations.add("Preferred Majors");
            if (settings.getPreferredMajors().isPresent()) {
                currentValue.add("[" + String.join(", ", settings.getPreferredMajors().get()) + "]");
            } else {
                currentValue.add("ALL");
            }
            operationFunctions.add(EditOpportunityPreferredMajorFilter.INSTANCE);
        }
        if (!(user instanceof Student) || ((Student) user).getYearOfStudy() >= 2) {
            operations.add("Level");
            ArrayList<String> levels = new ArrayList<>();
            if (settings.isShowLevel(InternshipLevel.BASIC)) {
                levels.add("Basic");
            }
            if (settings.isShowLevel(InternshipLevel.INTERMEDIATE)) {
                levels.add("Intermediate");
            }
            if (settings.isShowLevel(InternshipLevel.ADVANCED)) {
                levels.add("Advanced");
            }
            if (levels.size() == 3) {
                currentValue.add("ALL");
            } else {
                currentValue.add("[" + String.join(", ", levels) + "]");
            }
            operationFunctions.add(EditOpportunityLevelFilter.INSTANCE);
        }

        operations.add("Status");
        ArrayList<String> statuses = new ArrayList<>();
        if (settings.isShowStatus(InternshipOpportunityStatus.PENDING)) {
            statuses.add("Pending");
        }
        if (settings.isShowStatus(InternshipOpportunityStatus.APPROVED)) {
            if (user instanceof Student) {
                statuses.add("Unfilled"); 
            }else {
                statuses.add("Approved");
            }
        }
        if (settings.isShowStatus(InternshipOpportunityStatus.REJECTED)) {
            statuses.add("Rejected");
        }
        if (settings.isShowStatus(InternshipOpportunityStatus.FILLED)) {
            statuses.add("Filled");
        }
        if (statuses.size() == (user instanceof Student ? 2 : 4)) {
            currentValue.add("ALL");
        } else {
            currentValue.add("[" + String.join(", ", statuses) + "]");
        }
        operationFunctions.add(EditOpportunityStatusFilter.INSTANCE);

        if (!(user instanceof Student)) {
            operations.add("Visibility");
            ArrayList<String> visibilities = new ArrayList<>();
            if (settings.isShowVisible()) {
                visibilities.add("Visible");
            }
            if (settings.isShowHidden()) {
                visibilities.add("Hidden");
            }
            if (visibilities.size() == 2) {
                currentValue.add("ALL");
            } else {
                currentValue.add("[" + String.join(", ", visibilities) + "]");
            }
            operationFunctions.add(EditOpportunityVisibilityFilter.INSTANCE);
        }

        if (!(user instanceof CompanyRepresentative)) {
            operations.add("Companies");
            if (settings.getCompanies().isPresent()) {
                currentValue.add("[" + String.join(", ", settings.getCompanies().get()) + "]");
            } else {
                currentValue.add("ALL");
            }
            operationFunctions.add(EditOpportunityCompanyFilter.INSTANCE);
        }


        operations.add("Closes After");
        if (settings.getClosesAfter().isPresent()) {
            currentValue.add(settings.getClosesAfter().get().toString());
        } else {
            currentValue.add("NA");
        }
        operationFunctions.add(EditOpportunityClosesAfter.INSTANCE);
        
        operations.add("Closes Before");
        if (settings.getClosesBefore().isPresent()) {
            currentValue.add(settings.getClosesBefore().get().toString());
        } else {
            currentValue.add("NA");
        }
        operationFunctions.add(EditOpportunityClosesBefore.INSTANCE);

        for (int i = 0; i < operations.size(); i++) {
            System.out.printf("%d. %s: %s\n", i + 1, operations.get(i), currentValue.get(i));
        }
        System.out.println("0. Go back");
        System.out.println("e. Exit");
        System.out.print("Please choose an operation: ");
        String op = IO.getScanner().nextLine().trim();
        if (op.equals("e")) {
            System.exit(0);
            return Optional.empty();
        }
        try {
            int opi = Integer.parseInt(op);
            if (opi == 0) {
                return Optional.empty();
            }
            if (opi < 1 || opi > operationFunctions.size()) {
                return Optional.of(EditOpportunityFilter.INSTANCE);
            } else {
                return Optional.of(operationFunctions.get(opi - 1));
            }

        } catch (NumberFormatException e) {
            return Optional.of(EditOpportunityFilter.INSTANCE);
        }
    }
}
