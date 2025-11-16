package internship_management_system.ui;

import java.util.Optional;

/**
 * Interface representing a screen in the UI
 */

public interface Screen {
    /**
     * Print the title of the current screen and user information (if logged in)
     * @param currentPage Current screen title
     * @param uiState The current UI state
     */
    default void printTitle(String currentPage, UIState uiState) {
        System.out.printf("Internship Management System - %s\n", currentPage);
        if (uiState.getCurrentUser().isPresent()) {
            System.out.printf("Logged in as %s (%s)\n", uiState.getCurrentUser().get().getName(), uiState.getCurrentUser().get().getUserID());
        }
        System.out.println("");
    }

    /**
     * Show the current screen
     * @return An Optional containing the next Screen to display, or empty to exit
     */
    Optional<Screen> show(UIState uiState);
};