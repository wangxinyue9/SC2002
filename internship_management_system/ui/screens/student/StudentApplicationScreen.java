package internship_management_system.ui.screens.student;

import internship_management_system.enums.InternshipApplicationStatus;
import internship_management_system.enums.PlacementConfirmationStatus;
import internship_management_system.enums.WithdrawStatus;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.users.Student;
import internship_management_system.internships.InternshipApplication;
import internship_management_system.ui.IO;
import java.util.Optional;

/*
 * Screen for students to view/manage individual internship application
 */
public class StudentApplicationScreen implements Screen {
    public static final StudentApplicationScreen INSTANCE = new StudentApplicationScreen();

    /*
     * Private constructor to enforce singleton pattern.
     */
    private StudentApplicationScreen() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent() || !(uiState.getCurrentUser().get() instanceof Student)) {
            IO.exitWithError("Not logged in as a student.");
        }
        if(!uiState.getCurrentApplication().isPresent()) {
            IO.exitWithError("No internship application selected.");
        }

        IO.clearConsole();
        printTitle("View internship application", uiState);

        InternshipApplication app = uiState.getCurrentApplication().get();

        System.out.println(app);
        System.out.println();

        if(app.getWithdrawStatus() == WithdrawStatus.APPROVED) {
            System.err.println("This application has been withdrawn and is no longer active.");
        }
        else if(app.getStatus() == InternshipApplicationStatus.SUCCESSFUL && app.getPlacementConfirmationStatus() == PlacementConfirmationStatus.PENDING) {
            System.out.println("1. Accept placement");
            System.out.println("2. Reject placement");
            if(app.getWithdrawStatus() == WithdrawStatus.NOT_REQUESTED) {
                System.out.println("3. Request withdrawal");
            }
        }
        else {
            if(app.getPlacementConfirmationStatus() == PlacementConfirmationStatus.ACCEPTED) {
                System.out.println("You have accepted the placement offer.");
            }
            if(app.getWithdrawStatus() == WithdrawStatus.NOT_REQUESTED) {
                System.out.println("1. Request withdrawal");
            }
        }
        System.out.println("0. Back");
        System.out.print("Select an option: ");
        String choice = IO.getScanner().nextLine().trim();

        if(app.getWithdrawStatus() == WithdrawStatus.APPROVED) {
            if(choice.equals("0")) {
                uiState.setCurrentApplication(Optional.empty());
                return Optional.empty();
            } else {
                return Optional.of(INSTANCE);
            }
        }
        if(app.getStatus() == InternshipApplicationStatus.SUCCESSFUL && app.getPlacementConfirmationStatus() == PlacementConfirmationStatus.PENDING) {
            switch (choice) {
                case "1" -> {
                    app.confirmPlacement(true);
                    return Optional.of(INSTANCE);
                }
                case "2" -> {
                    app.confirmPlacement(false);
                    return Optional.of(INSTANCE);
                }
                case "3" -> {
                    app.requestWithdraw();
                    return Optional.of(INSTANCE);
                }
                case "0" -> {
                    uiState.setCurrentApplication(Optional.empty());
                    return Optional.empty();
                }
                default -> {
                    return Optional.of(INSTANCE);
                }
            }
        } else {
            switch (choice) {
                case "1" -> {
                    app.requestWithdraw();
                    return Optional.of(INSTANCE);
                }
                case "0" -> {
                    uiState.setCurrentApplication(Optional.empty());
                    return Optional.empty();
                }
                default -> {
                    return Optional.of(INSTANCE);
                }
            }
        }
    }
}
