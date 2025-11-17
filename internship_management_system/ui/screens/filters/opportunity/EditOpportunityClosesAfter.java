package internship_management_system.ui.screens.filters.opportunity;

import internship_management_system.filter.InternshipOpportunityFilterSettings;
import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;
import java.util.Optional;
import internship_management_system.ui.IO;
import internship_management_system.users.User;
import java.time.LocalDate;

/**
 * Screen to edit internship opportunity "closes after" filter
 */

public class EditOpportunityClosesAfter implements Screen {
    public final static EditOpportunityClosesAfter INSTANCE = new EditOpportunityClosesAfter();

    /**
     * Private constructor to enforce singleton pattern
     */
    private EditOpportunityClosesAfter() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent()) {
            IO.exitWithError("User not logged in");
        }
        IO.clearConsole();
        printTitle("Opportunity Filter Setting - 'Closes After' Filter", uiState);

        User user  = uiState.getCurrentUser().get();
        InternshipOpportunityFilterSettings settings = user.getOpportunityFilterSettings();
        
        System.out.print("Current Value: ");
        if(settings.getClosesAfter().isPresent()) {
            System.out.println(settings.getClosesAfter().get());
        } else {
            System.out.println("N\\A");
        }

        System.out.println("Enter new 'Closes After' date (YYYY-MM-DD), * to reset or empty to cancel:");
        String input = IO.getScanner().nextLine().trim();
        if(input.equals("*")) {
            settings.resetClosesAfter();
        } else if(!input.isEmpty()) {
            try {
                LocalDate date = LocalDate.parse(input);
                if(settings.getClosesBefore().isPresent() && settings.getClosesBefore().get().isBefore(date)) {
                    System.out.println("Closes after date cannot be after closes before date.");
                    System.out.print("Try again? [Y/n] ");
                    String resp = IO.getScanner().nextLine().trim();
                    if(resp.isEmpty() || resp.equalsIgnoreCase("y")) {
                        return Optional.of(INSTANCE);
                    }
                }
                else {
                    settings.setClosesAfter(date);
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                System.out.print("Try again? [Y/n] ");
                String resp = IO.getScanner().nextLine().trim();
                if(resp.isEmpty() || resp.equalsIgnoreCase("y")) {
                    return Optional.of(INSTANCE);
                }
            }
        }

        return Optional.empty();
    }
}
