package internship_management_system.ui.screens.filters.opportunity;
import internship_management_system.enums.InternshipLevel;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.ui.screens.filters.FilterSettingsToggleableListScreen;
import internship_management_system.users.User;
import internship_management_system.filter.InternshipOpportunityFilterSettings;
import internship_management_system.ui.IO;
import java.util.Optional;

/**
 * Screen to edit internship opportunity level filter settings
 */
public class EditOpportunityLevelFilter implements FilterSettingsToggleableListScreen {
    public static final EditOpportunityLevelFilter INSTANCE = new EditOpportunityLevelFilter();

    /**
     * Private constructor to enforce singleton pattern
     */
    private EditOpportunityLevelFilter() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent()) {
            IO.exitWithError("User not logged in");
        }
        IO.clearConsole();
        printTitle("Opportunity Filter Setting - Level Filter", uiState);

        User user = uiState.getCurrentUser().get();
        InternshipOpportunityFilterSettings settings = user.getOpportunityFilterSettings();

        boolean stay = handleToggleableList("Level", 
            new String[]{"Basic", "Intermediate", "Advanced"},
            new boolean[]{
                settings.isShowLevel(InternshipLevel.BASIC),
                settings.isShowLevel(InternshipLevel.INTERMEDIATE),
                settings.isShowLevel(InternshipLevel.ADVANCED)
            },
            new Runnable[]{
                () -> settings.toggleShowLevel(InternshipLevel.BASIC),
                () -> settings.toggleShowLevel(InternshipLevel.INTERMEDIATE),
                () -> settings.toggleShowLevel(InternshipLevel.ADVANCED)
            },
            () -> {
                if(!settings.isShowLevel(InternshipLevel.BASIC)) settings.toggleShowLevel(InternshipLevel.BASIC);
                if(!settings.isShowLevel(InternshipLevel.INTERMEDIATE)) settings.toggleShowLevel(InternshipLevel.INTERMEDIATE);
                if(!settings.isShowLevel(InternshipLevel.ADVANCED)) settings.toggleShowLevel(InternshipLevel.ADVANCED);
            }
        );

        if (stay) {
            return Optional.of(EditOpportunityLevelFilter.INSTANCE);
        } else {
            return Optional.empty();
        }
    }

}
