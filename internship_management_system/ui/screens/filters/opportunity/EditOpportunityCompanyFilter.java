package internship_management_system.ui.screens.filters.opportunity;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.users.User;
import internship_management_system.filter.InternshipOpportunityFilterSettings;
import internship_management_system.model.DataStorage;
import internship_management_system.ui.IO;
import internship_management_system.ui.screens.filters.FilterSettingsToggleableListScreen;
import internship_management_system.users.CompanyRepresentative;
import java.util.Optional;

/**
 * Screen to edit companies for internship opportunity filter settings
 */
public class EditOpportunityCompanyFilter implements FilterSettingsToggleableListScreen {
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



        final String[] companies = DataStorage.getCompanyReps().stream().map(CompanyRepresentative::getCompanyName).toArray(String[]::new);
        int sz = companies.length;
        boolean[] currentValue = new boolean[sz];
        Runnable[] toggler = new Runnable[sz];
        for(int i = 0; i < sz; i++) {
            final int finalI = i;
            currentValue[i] = (settings.getCompanies().isPresent() && settings.getCompanies().get().contains(companies[i]));
            if(currentValue[i]) {
                toggler[i] = () -> {
                    settings.removeCompany(companies[finalI]);
                };
            } else {
                toggler[i] = () -> {
                    settings.addCompany(companies[finalI]);
                };
            }
        }


        boolean stay = handleToggleableList("Company",
            companies,
            currentValue,
            toggler,
            () -> { settings.resetCompanies(); }
        );

        if (stay) {
            return Optional.of(EditOpportunityCompanyFilter.INSTANCE);
        } else {
            return Optional.empty();
        }
    }

}
