package internship_management_system.ui.screens.filters;

import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import internship_management_system.users.User;

import java.time.LocalDate;
import java.util.Optional;

import internship_management_system.filter.InternshipOpportunityFilterSettings;
import internship_management_system.ui.IO;

/**
 * Screen to edit internship opportunity "closes before" filter
 */
public class EditOpportunityClosesBefore implements Screen {
    public final static EditOpportunityClosesBefore INSTANCE = new EditOpportunityClosesBefore();

    /**
     * Private constructor to enforce singleton pattern
     */
    private EditOpportunityClosesBefore() {}
    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent()) {
            IO.exitWithError("User not logged in");
        }
        IO.clearConsole();
        printTitle("Edit Internship Opportunity 'Closes Before' Filter", uiState);

        User user  = uiState.getCurrentUser().get();
        InternshipOpportunityFilterSettings settings = user.getOpportunityFilterSettings();

        System.out.print("Current Value: ");
        if(settings.getClosesBefore().isPresent()) {
            System.out.println(settings.getClosesBefore().get());
        } else {
            System.out.println("N\\A");
        }

        System.out.println("Enter new 'Closes Before' date (YYYY-MM-DD), * to reset or empty to cancel:");
        String input = IO.getScanner().nextLine().trim();
        if(input.equals("*")) {
            settings.resetClosesBefore();
        } else if(!input.isEmpty()) {
            try {
                LocalDate date = LocalDate.parse(input);
                if(settings.getClosesAfter().isPresent() && settings.getClosesAfter().get().isAfter(date)) {
                    System.out.println("Closes before date cannot be before closes after date.");
                    System.out.println("Press Enter to continue...");
                    IO.getScanner().nextLine();
                }
                else {
                    settings.setClosesBefore(date);
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                System.out.println("Press Enter to continue...");
                IO.getScanner().nextLine();
            }
        }

        return Optional.empty();
        
    }
}
