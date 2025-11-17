package internship_management_system.ui.screens.filters.application;

import internship_management_system.enums.InternshipApplicationStatus;
import internship_management_system.filter.InternshipApplicationFilterSettings;
import internship_management_system.ui.screens.filters.FilterSettingsToggleableListScreen;
import internship_management_system.ui.IO;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.users.User;
import java.util.Optional;

/*
 * Screen to edit internship application status filter settings
 */
public class EditApplicationStatusFilter implements FilterSettingsToggleableListScreen {
    public static final EditApplicationStatusFilter INSTANCE = new EditApplicationStatusFilter();

    /**
     * Private constructor to enforce singleton pattern
     */
    private EditApplicationStatusFilter() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent()) {
            IO.exitWithError("User not logged in");
        }
        
        IO.clearConsole();
        printTitle("Application Filters - Edit Status Filter", uiState);

        User user = uiState.getCurrentUser().get();
        InternshipApplicationFilterSettings settings = user.getApplicationFilterSettings();

        boolean stay = handleToggleableList("Status",
            new String[] {"Pending", "Successful", "Unsuccessful"},
            new boolean[] {
                settings.isShowStatus(InternshipApplicationStatus.PENDING),
                settings.isShowStatus(InternshipApplicationStatus.SUCCESSFUL),
                settings.isShowStatus(InternshipApplicationStatus.UNSUCCESSFUL)
            },
            new Runnable[] {
                () -> settings.toggleShowStatus(InternshipApplicationStatus.PENDING),
                () -> settings.toggleShowStatus(InternshipApplicationStatus.SUCCESSFUL),
                () -> settings.toggleShowStatus(InternshipApplicationStatus.UNSUCCESSFUL)
            },
            () -> {
                if(!settings.isShowStatus(InternshipApplicationStatus.PENDING)) settings.toggleShowStatus(InternshipApplicationStatus.PENDING);
                if(!settings.isShowStatus(InternshipApplicationStatus.SUCCESSFUL)) settings.toggleShowStatus(InternshipApplicationStatus.SUCCESSFUL);
                if(!settings.isShowStatus(InternshipApplicationStatus.UNSUCCESSFUL)) settings.toggleShowStatus(InternshipApplicationStatus.UNSUCCESSFUL);
            }
        );

        if(stay) {
            return Optional.of(INSTANCE);
        } else {
            return Optional.empty();
        }

    }
}
