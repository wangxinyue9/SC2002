package internship_management_system.ui.screens.filters.opportunity;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.ui.screens.filters.FilterSettingsEditableListScreen;
import internship_management_system.users.User;
import internship_management_system.filter.InternshipOpportunityFilterSettings;
import internship_management_system.ui.IO;
import java.util.Optional;

/**
 * Screen to edit preferred majors for internship opportunity filter settings
 */
public class EditOpportunityPreferredMajorFilter implements FilterSettingsEditableListScreen {
    public static final EditOpportunityPreferredMajorFilter INSTANCE = new EditOpportunityPreferredMajorFilter();

    /**
     * Private constructor to enforce singleton pattern
     */
    private EditOpportunityPreferredMajorFilter() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent()) {
            IO.exitWithError("User not logged in");
        }
        IO.clearConsole();
        printTitle("Opportunity Filter Setting - Preferred Majors Filter", uiState);

        User user = uiState.getCurrentUser().get();
        InternshipOpportunityFilterSettings settings = user.getOpportunityFilterSettings();

        boolean stay = handleListEditing(
            "Preferred Major",
            settings.getPreferredMajors(),
            settings::addPreferredMajor,
            settings::removePreferredMajor,
            settings::resetPreferredMajors
        );

        if (stay) {
            return Optional.of(EditOpportunityPreferredMajorFilter.INSTANCE);
        } else {
            return Optional.empty();
        }
    }

}
