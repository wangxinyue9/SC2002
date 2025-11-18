package internship_management_system.ui.screens.company_representative;

import internship_management_system.enums.InternshipLevel;
import internship_management_system.enums.InternshipOpportunityStatus;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.users.CompanyRepresentative;
import internship_management_system.internships.InternshipOpportunity;
import internship_management_system.ui.IO;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Screen for Company Representatives to edit internship opportunities
 */
public class CREditOpportunity implements Screen {

    public static final CREditOpportunity INSTANCE = new CREditOpportunity();

    /**
     * Private constructor for singleton pattern
     */
    private CREditOpportunity() {
    }

    @Override
    public Optional<Screen> show(UIState uiState) {
        if (uiState.getCurrentUser().isEmpty() || !(uiState.getCurrentUser().get() instanceof CompanyRepresentative)) {
            IO.exitWithError("Not logged in as Company Representative");
        }
        if (uiState.getCurrentOpportunity().isEmpty()) {
            IO.exitWithError("No opportunity selected to edit");
        }

        InternshipOpportunity opp = uiState.getCurrentOpportunity().get();
        if (opp.getStatus() != InternshipOpportunityStatus.PENDING) {
            IO.exitWithError("Opportunity editing isn't allowed after getting approved/rejected");
        }

        IO.clearConsole();
        printTitle("Edit Internship Opportunity", uiState);
        System.out.println(opp);
        System.out.println("");

        System.out.print("Do you want to edit this opportunity? [Y/n]: ");
        String choice = IO.getScanner().nextLine().trim().toLowerCase();
        if (!choice.isEmpty() && !choice.equals("y")) {
            return Optional.empty();
        }

        System.out.print("New title (leave empty to keep current): ");
        String newTitle = IO.getScanner().nextLine().trim();
        System.out.print("New description (leave empty to keep current): ");
        String newDescription = IO.getScanner().nextLine().trim();
        System.out.print("New preferred major (leave empty to keep current): ");
        String newPreferredMajor = IO.getScanner().nextLine().trim();
        System.out.print("New internship level? [B]asic / [I]ntermediate / [A]dvanced (leave empty to keep current): ");
        InternshipLevel newLevel = null;
        String levelInput = IO.getScanner().nextLine().trim().toLowerCase();
        switch (levelInput) {
            case "b" ->
                newLevel = InternshipLevel.BASIC;
            case "i" ->
                newLevel = InternshipLevel.INTERMEDIATE;
            case "a" ->
                newLevel = InternshipLevel.ADVANCED;
        }

        System.out.print("New opening date (YYYY-MM-DD) (leave empty to keep current): ");
        LocalDate newOpeningDate = null;
        String newOpeningDateStr = IO.getScanner().nextLine().trim();
        try {
            if (!newOpeningDateStr.isEmpty()) {
                newOpeningDate = LocalDate.parse(newOpeningDateStr);
            }
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            System.out.print("Press any button to continue...");
            IO.getScanner().nextLine();
            return Optional.of(INSTANCE);
        }

        System.out.print("New closing date (YYYY-MM-DD) (leave empty to keep current): ");
        LocalDate newClosingDate = null;
        String newClosingDateStr = IO.getScanner().nextLine().trim();
        try {
            if (!newClosingDateStr.isEmpty()) {
                newClosingDate = LocalDate.parse(newClosingDateStr);
            }
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            System.out.print("Press any button to continue...");
            IO.getScanner().nextLine();
            return Optional.of(INSTANCE);
        }

        System.out.print("New number of slots (leave empty to keep current): ");
        String newNumOfSlotsStr = IO.getScanner().nextLine().trim();
        int newNumOfSlots = -1;
        try {
            if (!newNumOfSlotsStr.isEmpty()) {
                newNumOfSlots = Integer.parseInt(newNumOfSlotsStr);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
            System.out.print("Press any button to continue...");
            IO.getScanner().nextLine();
            return Optional.of(INSTANCE);
        }

        opp.editOpportunity(newTitle, newDescription, newPreferredMajor, newLevel, newOpeningDate, newClosingDate, newNumOfSlots);
        return Optional.empty();
    }
}
