package internship_management_system.ui.screens.filters.application;

import internship_management_system.enums.WithdrawStatus;
import internship_management_system.filter.InternshipApplicationFilterSettings;
import internship_management_system.ui.screens.filters.FilterSettingsToggleableListScreen;
import internship_management_system.ui.IO;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.users.User;
import java.util.Optional;

/*
 * Screen to edit internship application withdraw status filter settings
 */
public class EditApplicationWithdrawStatusFilter implements FilterSettingsToggleableListScreen {
    public static final EditApplicationWithdrawStatusFilter INSTANCE = new EditApplicationWithdrawStatusFilter();

    /**
     * Private constructor to enforce singleton pattern
     */
    private EditApplicationWithdrawStatusFilter() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent()) {
            IO.exitWithError("User not logged in");
        }
        
        IO.clearConsole();
        printTitle("Application Filters - Edit Withdraw Status Filter", uiState);

        User user = uiState.getCurrentUser().get();
        InternshipApplicationFilterSettings settings = user.getApplicationFilterSettings();

        boolean stay = handleToggleableList("Withdraw Status",
            new String[] {"Not requested", "Requested", "Approved", "Rejected"},
            new boolean[] {
                settings.isShowWithdrawStatus(WithdrawStatus.NOT_REQUESTED),
                settings.isShowWithdrawStatus(WithdrawStatus.PENDING),
                settings.isShowWithdrawStatus(WithdrawStatus.APPROVED),
                settings.isShowWithdrawStatus(WithdrawStatus.REJECTED)
            },
            new Runnable[] {
                () -> settings.toggleShowWithdrawStatus(WithdrawStatus.NOT_REQUESTED),
                () -> settings.toggleShowWithdrawStatus(WithdrawStatus.PENDING),
                () -> settings.toggleShowWithdrawStatus(WithdrawStatus.APPROVED),
                () -> settings.toggleShowWithdrawStatus(WithdrawStatus.REJECTED)
            },
            () -> {
                if(!settings.isShowWithdrawStatus(WithdrawStatus.NOT_REQUESTED)) settings.toggleShowWithdrawStatus(WithdrawStatus.NOT_REQUESTED);
                if(!settings.isShowWithdrawStatus(WithdrawStatus.PENDING)) settings.toggleShowWithdrawStatus(WithdrawStatus.PENDING);
                if(!settings.isShowWithdrawStatus(WithdrawStatus.APPROVED)) settings.toggleShowWithdrawStatus(WithdrawStatus.APPROVED);
                if(!settings.isShowWithdrawStatus(WithdrawStatus.REJECTED)) settings.toggleShowWithdrawStatus(WithdrawStatus.REJECTED);
            }
        );

        if(stay) {
            return Optional.of(INSTANCE);
        } else {
            return Optional.empty();
        }

    }
}
