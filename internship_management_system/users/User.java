package internship_management_system.users;

import internship_management_system.filter.InternshipOpportunityFilterSettings;
import internship_management_system.filter.InternshipApplicationFilterSettings;

/**
 * Abstract class which will be used as a base for different types of users
 */
public abstract class User {

    private final int id;
    private final String userID;
    private String userPassword;
    private final String name;
    private final InternshipOpportunityFilterSettings opportunityFilterSettings;
    private final InternshipApplicationFilterSettings applicationFilterSettings;

    /**
     * Constructor for User class
     *
     * @param id User's id numbered from 1 upwards, also refers to index in Data
     * Storage
     * @param name User's full name
     * @param userID User's unique user ID
     */
    public User(int id, String name, String userID) {
        this.id = id;
        this.name = name;
        this.userID = userID;
        this.userPassword = "password";
        this.opportunityFilterSettings = new InternshipOpportunityFilterSettings();
        this.applicationFilterSettings = new InternshipApplicationFilterSettings(this.opportunityFilterSettings);
    }

    /**
     * getter of user's internal ID
     *
     * @return user's internal ID
     */
    public int getId() {
        return id;
    }

    /**
     * getter of user's unique user ID
     *
     * @return user's unique user ID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Validates if the input password matches the user's password
     *
     * @param password input password to validate
     * @return true if password matches, false otherwise
     */
    public boolean validatePassword(String password) {
        return this.userPassword.equals(password);
    }

    /**
     * getter of user's full name
     *
     * @return user's full name
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the user's password to the new password
     *
     * @param password new password to set
     */
    public void changePassword(String password) {
        this.userPassword = password;
    }

    /**
     * getter of user's internship opportunity filter settings
     *
     * @return user's internship opportunity filter settings
     */
    public InternshipOpportunityFilterSettings getOpportunityFilterSettings() {
        return this.opportunityFilterSettings;
    }

    /**
     * getter of user's internship application filter settings
     *
     * @return user's internship application filter settings
     */
    public InternshipApplicationFilterSettings getApplicationFilterSettings() {
        return this.applicationFilterSettings;
    }

}
