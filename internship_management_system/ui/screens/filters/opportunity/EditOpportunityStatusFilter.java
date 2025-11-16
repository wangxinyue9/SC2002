package internship_management_system.ui.screens.filters.opportunity;
import internship_management_system.enums.InternshipOpportunityStatus;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.ui.screens.filters.FilterSettingsToggleableListScreen;
import internship_management_system.users.User;
import internship_management_system.filter.InternshipOpportunityFilterSettings;
import internship_management_system.ui.IO;
import internship_management_system.users.Student;
import java.util.Optional;

/**
 * Screen to edit internship opportunity status filter settings
 */
public class EditOpportunityStatusFilter implements FilterSettingsToggleableListScreen {
    public static final EditOpportunityStatusFilter INSTANCE = new EditOpportunityStatusFilter();

    /**
     * Private constructor to enforce singleton pattern
     */
    private EditOpportunityStatusFilter() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent()) {
            IO.exitWithError("User not logged in");
        }
        IO.clearConsole();
        printTitle("Opportunity Filter Setting - Status Filter", uiState);

        User user = uiState.getCurrentUser().get();
        InternshipOpportunityFilterSettings settings = user.getOpportunityFilterSettings();

        boolean stay;

        if(user instanceof Student) {
            stay = handleToggleableList("Status", 
                new String[]{"Not filled", "Filled"},
                new boolean[]{
                    settings.isShowStatus(InternshipOpportunityStatus.APPROVED),
                    settings.isShowStatus(InternshipOpportunityStatus.FILLED)
                },
                new Runnable[]{
                    
                    () -> settings.toggleShowStatus(InternshipOpportunityStatus.APPROVED),
                    () -> settings.toggleShowStatus(InternshipOpportunityStatus.FILLED)
                },
                () -> {
                    if(!settings.isShowStatus(InternshipOpportunityStatus.APPROVED)) settings.toggleShowStatus(InternshipOpportunityStatus.APPROVED);
                    if(settings.isShowStatus(InternshipOpportunityStatus.FILLED)) settings.toggleShowStatus(InternshipOpportunityStatus.FILLED);
                }
            );
        }

        else {
            stay = handleToggleableList("Status",
                new String[]{"Pending", "Approved", "Rejected", "Filled"},
                new boolean[]{
                    settings.isShowStatus(InternshipOpportunityStatus.PENDING),
                    settings.isShowStatus(InternshipOpportunityStatus.APPROVED),
                    settings.isShowStatus(InternshipOpportunityStatus.REJECTED),
                    settings.isShowStatus(InternshipOpportunityStatus.FILLED)
                },
                new Runnable[]{
                    () -> settings.toggleShowStatus(InternshipOpportunityStatus.PENDING),
                    () -> settings.toggleShowStatus(InternshipOpportunityStatus.APPROVED),
                    () -> settings.toggleShowStatus(InternshipOpportunityStatus.REJECTED),
                    () -> settings.toggleShowStatus(InternshipOpportunityStatus.FILLED)
                },
                () -> {
                    if(!settings.isShowStatus(InternshipOpportunityStatus.PENDING)) settings.toggleShowStatus(InternshipOpportunityStatus.PENDING);
                    if(!settings.isShowStatus(InternshipOpportunityStatus.APPROVED)) settings.toggleShowStatus(InternshipOpportunityStatus.APPROVED);
                    if(!settings.isShowStatus(InternshipOpportunityStatus.REJECTED)) settings.toggleShowStatus(InternshipOpportunityStatus.REJECTED);
                    if(!settings.isShowStatus(InternshipOpportunityStatus.FILLED)) settings.toggleShowStatus(InternshipOpportunityStatus.FILLED);
                }
            );
        }

        if (stay) {
            return Optional.of(EditOpportunityStatusFilter.INSTANCE);
        } else {
            return Optional.empty();
        }
    }

}
