package internship_management_system.ui.screens.filters;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.users.User;
import internship_management_system.filter.InternshipOpportunityFilterSettings;
import internship_management_system.ui.IO;
import internship_management_system.users.Student;
import java.util.Optional;

/**
 * Screen to edit internship opportunity visibility filter
 */
public class EditOpportunityVisibilityFilter implements FilterSettingsToggleableListScreen {
    public static final EditOpportunityVisibilityFilter INSTANCE = new EditOpportunityVisibilityFilter();

    /**
     * Private constructor to enforce singleton pattern
     */
    private EditOpportunityVisibilityFilter() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent()) {
            IO.exitWithError("User not logged in");
        }
        if(uiState.getCurrentUser().get() instanceof Student) {
            IO.exitWithError("Student not allowed to access this screen");
        }
        IO.clearConsole();
        printTitle("Edit Opportunity Visibility Filter", uiState);

        User user = uiState.getCurrentUser().get();
        InternshipOpportunityFilterSettings settings = user.getOpportunityFilterSettings();

        boolean stay = handleToggleableList("Visibility", 
            new String[]{"Visible", "Hidden"},
            new boolean[]{
                settings.isShowVisible(),
                settings.isShowHidden()
            },
            new Runnable[]{
                () -> settings.toggleShowVisible(),
                () -> settings.toggleShowHidden()
            },
            () -> {
                if(!settings.isShowVisible()) settings.toggleShowVisible();
                if(!settings.isShowHidden()) settings.toggleShowHidden();
            }
        );

        if (stay) {
            return Optional.of(EditOpportunityVisibilityFilter.INSTANCE);
        } else {
            return Optional.empty();
        }
    }

}
