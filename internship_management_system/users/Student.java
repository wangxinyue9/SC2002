package internship_management_system.users;

import internship_management_system.enums.InternshipOpportunityStatus;
import internship_management_system.enums.InternshipLevel;

/**
 * Student user class
 */
public class Student extends User {

    private final int yearOfStudy;
    private final String major;
    private final String email;

    /**
     * Constructor for Student
     *
     * @param id Student's internal ID, also refers to index in DataStorage
     * @param userID Student's login ID
     * @param name Student's full name
     * @param yearOfStudy Student's current year of study
     * @param major Student's major
     * @param email Student's email address
     */
    public Student(int id, String userID, String name, int yearOfStudy, String major, String email) {
        super(id, name, userID);

        this.yearOfStudy = yearOfStudy;
        this.major = major;
        this.email = email;

        super.getOpportunityFilterSettings().toggleShowHidden();
        super.getOpportunityFilterSettings().toggleShowUnopened();
        super.getOpportunityFilterSettings().toggleShowClosed();
        super.getOpportunityFilterSettings().addPreferredMajor(major);
        if (yearOfStudy <= 2) {
            super.getOpportunityFilterSettings().toggleShowLevel(InternshipLevel.INTERMEDIATE);
            super.getOpportunityFilterSettings().toggleShowLevel(InternshipLevel.ADVANCED);
        }
        super.getOpportunityFilterSettings().toggleShowStatus(InternshipOpportunityStatus.PENDING);
        super.getOpportunityFilterSettings().toggleShowStatus(InternshipOpportunityStatus.REJECTED);
        super.getOpportunityFilterSettings().toggleShowStatus(InternshipOpportunityStatus.FILLED);
        super.getOpportunityFilterSettings().toggleShowHidden();
    }

    /**
     * @return Student's major
     */
    public String getMajor() {
        return major;
    }

    /**
     * @return Student's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return Student's year of study
     */
    public int getYearOfStudy() {
        return yearOfStudy;
    }
}
