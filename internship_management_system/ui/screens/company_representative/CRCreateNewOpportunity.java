package internship_management_system.ui.screens.company_representative;

import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.enums.CompanyRepresentativeStatus;
import internship_management_system.enums.InternshipLevel;
import internship_management_system.internships.InternshipOpportunity;
import internship_management_system.model.DataStorage;
import internship_management_system.ui.IO;
import internship_management_system.users.CompanyRepresentative;

import java.time.LocalDate;
import java.util.Optional;


/**
 * Screen for creating a new internship opportunity by Company Representatives.
 */
public class CRCreateNewOpportunity implements Screen {
    public static final CRCreateNewOpportunity INSTANCE = new CRCreateNewOpportunity();

    /*
     * Private constructor to enforce singleton pattern.
     */
    private CRCreateNewOpportunity() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent() || !(uiState.getCurrentUser().get() instanceof CompanyRepresentative)) {
            IO.exitWithError("Not logged in as a company representative");
        }
        CompanyRepresentative cr = (CompanyRepresentative) uiState.getCurrentUser().get();
        if(cr.getStatus() != CompanyRepresentativeStatus.APPROVED) {
            IO.exitWithError("Company representative is not approved to create internship opportunities.");
        }

        IO.clearConsole();
        printTitle("Create New Internship Opportunity", uiState);
        
        System.out.print("Internship title (enter empty title to go back): ");
        String title = IO.getScanner().nextLine().trim();
        if (title.isEmpty()) {
            System.out.print("Are you sure you want to go back? [Y/n] ");
            String confirm = IO.getScanner().nextLine().trim().toLowerCase();
            if (confirm.equals("y") || confirm.equals("")) {
                return Optional.empty();
            }
            else {
                return Optional.of(INSTANCE);
            }
        }

        System.out.print("Internship description: ");
        String description = IO.getScanner().nextLine().trim();

        System.out.print("Preferred major: ");
        String preferredMajor = IO.getScanner().nextLine().trim();

        System.out.print("Internship level? [B]asic / [I]ntermediate / [A]dvanced: ");
        InternshipLevel level;
        switch (IO.getScanner().nextLine().trim().toUpperCase()) {
            case "B" ->
                level = InternshipLevel.BASIC;
            case "I" ->
                level = InternshipLevel.INTERMEDIATE;
            case "A" ->
                level = InternshipLevel.ADVANCED;
            default -> {
                System.out.println("Invalid level input.");
                System.out.print("Press any button to continue...");
                IO.getScanner().nextLine();
                return Optional.empty();
            }
        }

        System.out.print("Opening date (YYYY-MM-DD): ");
        LocalDate openingDate;
        try {
            openingDate = LocalDate.parse(IO.getScanner().nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            System.out.print("Press any button to continue...");
            IO.getScanner().nextLine();
            return Optional.empty();
        }

        System.out.print("Closing date (YYYY-MM-DD): ");
        LocalDate closingDate;
        try {
            closingDate = LocalDate.parse(IO.getScanner().nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            System.out.print("Press any button to continue...");
            IO.getScanner().nextLine();
            return Optional.empty();
        }

        System.out.print("Number of slots: ");
        int numOfSlots = IO.getScanner().nextInt();
        IO.getScanner().nextLine(); // consume newline
        if (numOfSlots <= 0 || numOfSlots > 10) {
            System.out.println("Invalid number of slots. Must be between 1 and 10.");
            System.out.print("Press any button to continue...");
            IO.getScanner().nextLine();
            return Optional.empty();
        }

        System.out.print("Visible to students? [Y/n] ");
        String visibilityInput = IO.getScanner().nextLine().trim().toLowerCase();
        boolean isVisible = visibilityInput.equals("y") || visibilityInput.equals("");

        InternshipOpportunity io = DataStorage.newInternshipOpportunity(
                title,
                description,
                preferredMajor,
                level,
                openingDate,
                closingDate,
                (CompanyRepresentative) uiState.getCurrentUser().get(),
                numOfSlots,
                isVisible
        );

        System.out.println("Internship opportunity created with ID: " + io.getId());
        System.out.print("Press any button to continue...");
        IO.getScanner().nextLine();

        uiState.setCurrentOpportunity(Optional.of(io));
        return Optional.of(CROpportunityScreen.INSTANCE);
    }    
}
