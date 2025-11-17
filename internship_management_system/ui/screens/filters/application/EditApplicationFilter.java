package internship_management_system.ui.screens.filters.application;

import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.users.User;

import java.util.ArrayList;
import java.util.Optional;

import internship_management_system.enums.InternshipApplicationStatus;
import internship_management_system.enums.PlacementConfirmationStatus;
import internship_management_system.enums.WithdrawStatus;
import internship_management_system.filter.InternshipApplicationFilterSettings;
import internship_management_system.ui.IO;


/*
 * Screen to edit internship application filter settings
 */
public class EditApplicationFilter implements Screen {
    public static final EditApplicationFilter INSTANCE = new EditApplicationFilter();

    /**
     * Private constructor to enforce singleton pattern
     */
    private EditApplicationFilter() {}

    @Override
    public java.util.Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent()) {
            IO.exitWithError("User not logged in");
        }
        IO.clearConsole();
        printTitle("Edit Internship Application Filter Settings", uiState);

        ArrayList<String> operations = new ArrayList<>();
        ArrayList<String> currentValue = new ArrayList<>();
        ArrayList<Screen> operationFunctions = new ArrayList<>();

        User user = uiState.getCurrentUser().get();
        InternshipApplicationFilterSettings settings = user.getApplicationFilterSettings();

        operations.add("Apply Opportunity Filters");
        currentValue.add(settings.isUseOpportunityFilters() ? "Yes" : "No");
        operationFunctions.add(null); // Handled separately

        operations.add("Status");
        ArrayList<String> statuses = new ArrayList<>();
        if (settings.isShowStatus(InternshipApplicationStatus.PENDING)) {
            statuses.add("Pending");
        }
        if (settings.isShowStatus(InternshipApplicationStatus.SUCCESSFUL)) {
            statuses.add("Successful");
        }
        if (settings.isShowStatus(InternshipApplicationStatus.UNSUCCESSFUL)) {
            statuses.add("Unsuccessful");
        }
        if (statuses.size() == 3) {
            currentValue.add("ALL");
        } else {
            currentValue.add("[" + String.join(", ", statuses) + "]");
        }
        operationFunctions.add(EditApplicationStatusFilter.INSTANCE);

        operations.add("Withdraw Status");
        ArrayList<String> withdrawStatuses = new ArrayList<>();
        if (settings.isShowWithdrawStatus(WithdrawStatus.NOT_REQUESTED)) {
            withdrawStatuses.add("Not requested");
        }
        if (settings.isShowWithdrawStatus(WithdrawStatus.PENDING)) {
            withdrawStatuses.add("Pending");
        }
        if (settings.isShowWithdrawStatus(WithdrawStatus.APPROVED)) {
            withdrawStatuses.add("Approved");
        }
        if (settings.isShowWithdrawStatus(WithdrawStatus.REJECTED)) {
            withdrawStatuses.add("Rejected");
        }
        if (withdrawStatuses.size() == 4) {
            currentValue.add("ALL");
        } else {
            currentValue.add("[" + String.join(", ", withdrawStatuses) + "]");
        }
        operationFunctions.add(EditApplicationWithdrawStatusFilter.INSTANCE);

        operations.add("Placement Confirmation Status");
        ArrayList<String> placementConfirmationStatuses = new ArrayList<>();
        if (settings.isShowPlacementConfirmationStatus(PlacementConfirmationStatus.PENDING)) {
            placementConfirmationStatuses.add("Pending");
        }
        if (settings.isShowPlacementConfirmationStatus(PlacementConfirmationStatus.ACCEPTED)) {
            placementConfirmationStatuses.add("Accepted");
        }
        if (settings.isShowPlacementConfirmationStatus(PlacementConfirmationStatus.REJECTED)) {
            placementConfirmationStatuses.add("Rejected");
        }
        if (placementConfirmationStatuses.size() == 3) {
            currentValue.add("ALL");
        } else {
            currentValue.add("[" + String.join(", ", placementConfirmationStatuses) + "]");
        }
        operationFunctions.add(EditApplicationPlacementStatusFilter.INSTANCE);

        for (int i = 0; i < operations.size(); i++) {
            System.out.printf("%d. %s: %s\n", i + 1, operations.get(i), currentValue.get(i));
        }
        System.out.println("0. Go back");
        System.out.print("Please choose an operation: ");
        String op = IO.getScanner().nextLine().trim();

        try {
            int opi = Integer.parseInt(op);
            if (opi == 0) {
                return Optional.empty();
            }
            if (opi < 1 || opi > operationFunctions.size()) {
                return Optional.of(INSTANCE);
            } else {
                if (opi == 1) {
                    settings.toggleUseOpportunityFilters();
                    return Optional.of(INSTANCE);
                } else {
                    return Optional.of(operationFunctions.get(opi - 1) );
                }
            }

        } catch (NumberFormatException e) {
            return Optional.of(INSTANCE);
        }
    }
}
