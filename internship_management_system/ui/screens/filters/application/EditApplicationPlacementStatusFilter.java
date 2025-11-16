package internship_management_system.ui.screens.filters.application;

import internship_management_system.filter.InternshipApplicationFilterSettings;
import internship_management_system.ui.screens.filters.FilterSettingsToggleableListScreen;
import internship_management_system.ui.IO;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.users.User;
import java.util.Optional;
import internship_management_system.enums.PlacementConfirmationStatus;

/*
 * Screen to edit internship application placement status filter settings
 */
public class EditApplicationPlacementStatusFilter implements FilterSettingsToggleableListScreen {
    public static final EditApplicationPlacementStatusFilter INSTANCE = new EditApplicationPlacementStatusFilter();

    /**
     * Private constructor to enforce singleton pattern
     */
    private EditApplicationPlacementStatusFilter() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent()) {
            IO.exitWithError("User not logged in");
        }
        
        IO.clearConsole();
        printTitle("Application Filters - Edit Placement Status Filter", uiState);

        User user = uiState.getCurrentUser().get();
        InternshipApplicationFilterSettings settings = user.getApplicationFilterSettings();

        boolean stay = handleToggleableList("Placement Status",
            new String[] {"Pending", "Accepted", "Rejected"},
            new boolean[] {
                settings.isShowPlacementConfirmationStatus(PlacementConfirmationStatus.PENDING),
                settings.isShowPlacementConfirmationStatus(PlacementConfirmationStatus.ACCEPTED),
                settings.isShowPlacementConfirmationStatus(PlacementConfirmationStatus.REJECTED)
            },
            new Runnable[] {
                () -> settings.toggleShowPlacementConfirmationStatus(PlacementConfirmationStatus.PENDING),
                () -> settings.toggleShowPlacementConfirmationStatus(PlacementConfirmationStatus.ACCEPTED),
                () -> settings.toggleShowPlacementConfirmationStatus(PlacementConfirmationStatus.REJECTED)
            },
            () -> {
                if(!settings.isShowPlacementConfirmationStatus(PlacementConfirmationStatus.PENDING)) settings.toggleShowPlacementConfirmationStatus(PlacementConfirmationStatus.PENDING);
                if(!settings.isShowPlacementConfirmationStatus(PlacementConfirmationStatus.ACCEPTED)) settings.toggleShowPlacementConfirmationStatus(PlacementConfirmationStatus.ACCEPTED);
                if(!settings.isShowPlacementConfirmationStatus(PlacementConfirmationStatus.REJECTED)) settings.toggleShowPlacementConfirmationStatus(PlacementConfirmationStatus.REJECTED);
            }
        );

        if(stay) {
            return Optional.of(INSTANCE);
        } else {
            return Optional.empty();    
        }
    }
}
