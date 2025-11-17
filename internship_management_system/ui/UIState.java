package internship_management_system.ui;

import java.util.Optional;

import internship_management_system.internships.InternshipApplication;
import internship_management_system.internships.InternshipOpportunity;
import internship_management_system.users.User;

/**
 * Stores the state of the UI such as current user, current internship opportunity, and current application.
 */
public class UIState {
    Optional<User> currentUser;
    Optional<InternshipOpportunity> currentOpportunity;
    Optional<InternshipApplication> currentApplication;

    /**
     * Constructor to initialize the UI state with empty optionals.
     */
    public UIState() {
        this.currentUser = Optional.empty();
        this.currentOpportunity = Optional.empty();
        this.currentApplication = Optional.empty();
    }

    /**
     * Get the current user.
     * @return Optional of the current user.
     */
    public Optional<User> getCurrentUser() {
        return currentUser;
    }
    /**
     * Set the current user.
     * @param user The user to set as current.
     */
    public void setCurrentUser(Optional<User> user) {
        this.currentUser = user;
    }

    /**
     * Get the current internship opportunity.
     * @return Optional of the current internship opportunity.
     */
    public Optional<InternshipOpportunity> getCurrentOpportunity() {
        return currentOpportunity;
    }
    /**
     * Set the current internship opportunity.
     * @param opportunity The internship opportunity to set as current.
     */
    public void setCurrentOpportunity(Optional<InternshipOpportunity> opportunity) {
        this.currentOpportunity = opportunity;
    }

    /**
     * Get the current internship application.
     * @return Optional of the current internship application.
     */
    public Optional<InternshipApplication> getCurrentApplication() {
        return currentApplication;
    }

    /**
     * Set the current internship application.
     * @param application The internship application to set as current.
     */
    public void setCurrentApplication(Optional<InternshipApplication> application) {
        this.currentApplication = application;
    }
}