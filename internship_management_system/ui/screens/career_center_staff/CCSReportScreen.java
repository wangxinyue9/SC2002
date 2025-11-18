package internship_management_system.ui.screens.career_center_staff;

import internship_management_system.internships.InternshipApplication;
import internship_management_system.internships.InternshipOpportunity;
import internship_management_system.model.DataStorage;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.ui.IO;
import internship_management_system.users.CareerCentreStaff;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Screen to generate reports for Career Center Staff
 */
public class CCSReportScreen implements Screen {
    public static final CCSReportScreen INSTANCE = new CCSReportScreen();

    /**
     * Private constructor for singleton pattern
     */
    private CCSReportScreen() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(uiState.getCurrentUser().isEmpty() || !(uiState.getCurrentUser().get() instanceof CareerCentreStaff)) {
            IO.exitWithError("Not logged in as Career Centre Staff");
        }

        IO.clearConsole();
        printTitle("Comprehensive Report", uiState);

        System.out.println("Please note this report is generated according to your filter settings. For a complete report, please reset filters in the Filter Settings screen.");
        System.out.println();

        CareerCentreStaff staff = (CareerCentreStaff) uiState.getCurrentUser().get();

        ArrayList<InternshipOpportunity> opportunities = new ArrayList<>(DataStorage.getInternshipOpportunities(staff.getOpportunityFilterSettings()));
        ArrayList<InternshipApplication> applications = new ArrayList<>(DataStorage.getInternshipApplications(staff.getApplicationFilterSettings()));

        int pendingOpportunities = 0;
        int approvedOpportunities = 0;
        int rejectedOpportunities = 0;
        int filledOpportunities = 0;
        for(InternshipOpportunity opportunity : opportunities) {
            switch(opportunity.getStatus()) {
                case PENDING -> pendingOpportunities++;
                case APPROVED -> approvedOpportunities++;
                case REJECTED -> rejectedOpportunities++;
                case FILLED -> filledOpportunities++;
            }
        }

        System.out.printf("Total Internship Opportunities: %d%n", opportunities.size());
        System.out.printf(" - Pending: %d%n", pendingOpportunities);
        System.out.printf(" - Approved: %d%n", approvedOpportunities);
        System.out.printf(" - Rejected: %d%n", rejectedOpportunities);
        System.out.printf(" - Filled: %d%n", filledOpportunities);
        System.out.println();

        int pendingApplications = 0;
        int applicationsOffered = 0;
        int applicationsRejected = 0;
        for(InternshipApplication application : applications) {
            switch(application.getStatus()) {
                case PENDING -> pendingApplications++;
                case SUCCESSFUL -> applicationsOffered++;
                case UNSUCCESSFUL -> applicationsRejected++;
            }
        }

        System.out.printf("Total Internship Applications: %d%n", applications.size());
        System.out.printf(" - Pending: %d%n", pendingApplications);
        System.out.printf(" - Offered: %d%n", applicationsOffered);
        System.out.printf(" - Rejected: %d%n", applicationsRejected);
        System.out.println();

        System.out.println("");

        for(InternshipOpportunity opportunity : opportunities) {
            System.out.println(opportunity);
            System.out.println("-------------------------");
            DataStorage.getInternshipApplications(opportunity, staff.getApplicationFilterSettings())
                .forEach(application -> System.out.println("\t"+application));
            System.out.println("-------------------------");
            System.out.println();
        }

        System.out.print("Press Enter to go back...");
        IO.getScanner().nextLine();
        return Optional.empty();
    }
}
