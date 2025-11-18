package internship_management_system.ui.screens;

import internship_management_system.filter.InternshipApplicationFilterSettings;
import internship_management_system.internships.InternshipApplication;
import internship_management_system.model.DataStorage;
import internship_management_system.ui.IO;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.ui.screens.career_center_staff.CCSApplicationScreen;
import internship_management_system.ui.screens.company_representative.CRApplicationScreen;
import internship_management_system.ui.screens.student.StudentApplicationScreen;
import internship_management_system.users.CareerCentreStaff;
import internship_management_system.users.CompanyRepresentative;
import internship_management_system.users.Student;
import internship_management_system.users.User;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Screen to display a list of internship applications.
 */
public class InternshipApplicationList implements Screen {

    public static final InternshipApplicationList INSTANCE = new InternshipApplicationList();

    /**
     * Private constructor to enforce singleton pattern.
     */
    private InternshipApplicationList() {
    }

    @Override
    public Optional<Screen> show(UIState uiState) {
        if (!uiState.getCurrentUser().isPresent()) {
            IO.exitWithError("User not logged in");
        }

        IO.clearConsole();
        printTitle("Internship Applications List", uiState);

        User user = uiState.getCurrentUser().get();

        ArrayList<InternshipApplication> applications;
        InternshipApplicationFilterSettings settings = user.getApplicationFilterSettings();
        if (user instanceof Student student) {
            applications = new ArrayList<>(DataStorage.getInternshipApplications(student, settings)); 
        }else if(user instanceof  CareerCentreStaff) {
            applications = new ArrayList<>(DataStorage.getInternshipApplications(settings));
        } else{
            CompanyRepresentative cr = (CompanyRepresentative) user;
            applications = DataStorage.getInternshipApplications(settings).stream()
                .filter(opp -> opp.getOpportunity().getCompanyRep().equals(cr))
                .collect(Collectors.toCollection(ArrayList::new));
        }

        if (applications.isEmpty()) {
            System.out.println("No internship application.");
        } else {
            System.out.println("Internship Applications:");
            System.out.println();
            for (int i = 0; i < applications.size(); i++) {
                InternshipApplication app = applications.get(i);
                System.out.println((i + 1) + ". " + app);
                System.out.println();
            }
        }

        System.out.print("");

        System.out.print("Type internship application row number (not id) to manage, or 0 to go back: ");

        String input = IO.getScanner().nextLine().trim();

        try {
            int choice = Integer.parseInt(input);
            if (choice == 0) {
                return Optional.empty();
            }
            if (choice < 1 || choice > applications.size()) {
                return Optional.of(INSTANCE);
            }
            if (user instanceof Student) {
                uiState.setCurrentApplication(Optional.of(applications.get(choice - 1)));
                return Optional.of(StudentApplicationScreen.INSTANCE);
            } else if (user instanceof CompanyRepresentative) {
                uiState.setCurrentApplication(Optional.of(applications.get(choice - 1)));
                return Optional.of(CRApplicationScreen.INSTANCE);
            } else {
                uiState.setCurrentApplication(Optional.of(applications.get(choice - 1)));
                return Optional.of(CCSApplicationScreen.INSTANCE);
            }
        } catch (NumberFormatException e) {
            return Optional.of(INSTANCE);
        }
    }
}
