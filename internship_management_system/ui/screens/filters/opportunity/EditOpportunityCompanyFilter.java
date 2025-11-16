package internship_management_system.ui.screens.filters.opportunity;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.ui.screens.filters.FilterSettingsEditableListScreen;
import internship_management_system.users.User;
import internship_management_system.filter.InternshipOpportunityFilterSettings;
import internship_management_system.ui.IO;
import internship_management_system.users.CompanyRepresentative;
import java.util.Optional;

/**
 * Screen to edit companies for internship opportunity filter settings
 */
public class EditOpportunityCompanyFilter implements FilterSettingsEditableListScreen {
    public static final EditOpportunityCompanyFilter INSTANCE = new EditOpportunityCompanyFilter();

    /**
     * Private constructor to enforce singleton pattern
     */
    private EditOpportunityCompanyFilter() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent()) {
            IO.exitWithError("User not logged in");
        }
        if(uiState.getCurrentUser().get() instanceof CompanyRepresentative) {
            IO.exitWithError("Company Representatives cannot access this screen");
        }

        IO.clearConsole();
        printTitle("Opportunity Filter Setting - Companies Filter", uiState);

        User user = uiState.getCurrentUser().get();
        InternshipOpportunityFilterSettings settings = user.getOpportunityFilterSettings();

        boolean stay = handleListEditing(
            "Company",
            settings.getCompanies(),
            settings::addCompany,
            settings::removeCompany,
            settings::resetCompanies
        );

        if (stay) {
            return Optional.of(EditOpportunityCompanyFilter.INSTANCE);
        } else {
            return Optional.empty();
        }
    }

}
