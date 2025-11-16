package internship_management_system.ui.screens.student;

import internship_management_system.enums.InternshipOpportunityStatus;
import internship_management_system.enums.PlacementConfirmationStatus;
import internship_management_system.enums.WithdrawStatus;
import internship_management_system.internships.InternshipApplication;
import internship_management_system.internships.InternshipOpportunity;
import internship_management_system.model.DataStorage;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.ui.IO;
import internship_management_system.users.Student;
import java.util.Optional;


/**
 * Screen for students to view/apply/manage specific internship opportunity
 */
public class StudentOpportunityScreen implements Screen {
    public static final StudentOpportunityScreen INSTANCE = new StudentOpportunityScreen();

    /*
     * Private constructor to enforce singleton pattern.
     */
    private StudentOpportunityScreen() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent() || !(uiState.getCurrentUser().get() instanceof Student)) {
            IO.exitWithError("Not logged in as a student.");
        }
        if(!uiState.getCurrentOpportunity().isPresent()) {
            IO.exitWithError("No internship opportunity selected.");
        }

        Student student = (Student) uiState.getCurrentUser().get();
        InternshipOpportunity opp = uiState.getCurrentOpportunity().get();
        IO.clearConsole();
        printTitle("View internship details", uiState);

        Optional<InternshipApplication> app = DataStorage.getInternshipApplicationForAnOpportunity(opp, student);

        System.out.println(opp);
        System.out.println();

        boolean student_has_accepted_some_offer = DataStorage.getInternshipApplications(student).stream()
                .anyMatch(a -> {
                    return  !a.getWithdrawStatus().equals(WithdrawStatus.APPROVED) &&
                    a.getPlacementConfirmationStatus().equals(PlacementConfirmationStatus.ACCEPTED);
                });

        if(app.isPresent()) {
            System.out.println("You have already applied for this internship.");
            System.out.println("1. View your application");
        } else if(opp.getStatus() != InternshipOpportunityStatus.FILLED && !student_has_accepted_some_offer) {
            System.out.println("1. Apply for this internship");
        } else if(student_has_accepted_some_offer) {
            System.out.println("You have already accepted an internship offer. You cannot apply for another.");
        } else {
            System.out.println("This internship is already filled. You cannot apply.");
        }

        System.out.println("0. Go back");
        System.out.print("Select an option: ");
        String choice = IO.getScanner().nextLine().trim();

        switch (choice) {
            case "1" -> {
                if(app.isPresent()) {
                    uiState.setCurrentApplication(app);
                    return Optional.of(StudentApplicationScreen.INSTANCE);
                }
                else if(opp.getStatus() != InternshipOpportunityStatus.FILLED && !student_has_accepted_some_offer) {
                    InternshipApplication newApp = DataStorage.newInternshipApplication(opp, student);
                    uiState.setCurrentApplication(Optional.of(newApp));
                    return Optional.of(StudentApplicationScreen.INSTANCE);
                }
                else {
                    return Optional.of(StudentOpportunityScreen.INSTANCE);
                }
            }
            case "0" -> {
                uiState.setCurrentOpportunity(Optional.empty());
                return Optional.empty();
            }
            default -> {
                return Optional.of(StudentOpportunityScreen.INSTANCE);
            }
        }
    }
}
