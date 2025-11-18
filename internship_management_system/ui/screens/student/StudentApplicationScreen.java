package internship_management_system.ui.screens.student;

import internship_management_system.enums.InternshipApplicationStatus;
import internship_management_system.enums.InternshipOpportunityStatus;
import internship_management_system.enums.WithdrawStatus;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.users.Student;
import internship_management_system.internships.InternshipApplication;
import internship_management_system.ui.IO;

import java.util.ArrayList;
import java.util.Optional;

/*
 * Screen for students to view/manage individual internship application
 */
public class StudentApplicationScreen implements Screen {

    public static final StudentApplicationScreen INSTANCE = new StudentApplicationScreen();

    /*
     * Private constructor to enforce singleton pattern.
     */
    private StudentApplicationScreen() {
    }

    @Override
    public Optional<Screen> show(UIState uiState) {
        if (!uiState.getCurrentUser().isPresent() || !(uiState.getCurrentUser().get() instanceof Student)) {
            IO.exitWithError("Not logged in as a student.");
        }
        if (!uiState.getCurrentApplication().isPresent()) {
            IO.exitWithError("No internship application selected.");
        }

        IO.clearConsole();
        printTitle("View internship application", uiState);

        InternshipApplication app = uiState.getCurrentApplication().get();

        System.out.println(app);
        System.out.println();

        ArrayList<String> options = new ArrayList<>();

        if (app.getWithdrawStatus() == WithdrawStatus.APPROVED) {
            System.out.println("This application has been withdrawn and is no longer active.");
        } else if (app.getStatus() == InternshipApplicationStatus.PENDING) {
            options.add("Request withdrawal");
        } else if (app.getStatus() == InternshipApplicationStatus.SUCCESSFUL) {
            switch (app.getPlacementConfirmationStatus()) {
                case PENDING -> {
                    if (app.getOpportunity().getStatus() == InternshipOpportunityStatus.FILLED) {
                        System.out.println("You had been offered a placement, but you didn't respond in time, and the placement has been given to another student.");
                    } else {
                        options.add("Accept placement offer");
                        options.add("Reject placement offer");
                        if (app.getWithdrawStatus() == WithdrawStatus.NOT_REQUESTED) {
                            options.add("Request withdrawal");
                        }
                    }
                }
                case ACCEPTED -> {
                    System.out.println("You have accepted the placement offer.");
                    if (app.getWithdrawStatus() == WithdrawStatus.NOT_REQUESTED) {
                        options.add("Request withdrawal");
                    }
                }
                case REJECTED ->
                    System.out.println("You have rejected the placement offer.");
                default -> {
                    IO.exitWithError("Unknown placement confirmation status.");
                }
            }
        } else if (app.getStatus() == InternshipApplicationStatus.UNSUCCESSFUL) {
            System.out.println("Your application has been rejected.");
        } else {
            IO.exitWithError("Unknown internship application status.");
        }

        for(int i = 1; i <= options.size(); i++) {
            System.out.printf("%d. %s\n", i, options.get(i - 1));
        }
        System.out.println("0. Back");
        System.out.print("Select an option: ");
        String choice = IO.getScanner().nextLine().trim();

        switch (choice) {
            case "0" -> {
                uiState.setCurrentApplication(Optional.empty());
                return Optional.empty();
            }
            case "1", "2", "3" -> {
                int choiceInt = Integer.parseInt(choice) - 1;
                if (choiceInt > options.size() - 1) {
                    return Optional.of(INSTANCE);
                }
                switch (options.get(choiceInt)) {
                    case "Request withdrawal" ->
                        app.requestWithdraw();
                    case "Accept placement offer" ->
                        app.confirmPlacement(true);
                    case "Reject placement offer" ->
                        app.confirmPlacement(false);
                    default -> {
                    }
                }
                return Optional.of(INSTANCE);
            }
            default -> {
                return Optional.of(INSTANCE);
            }
        }
    }
}
