package internship_management_system.internships;

import internship_management_system.enums.InternshipLevel;
import internship_management_system.enums.InternshipOpportunityStatus;
import internship_management_system.filter.InternshipOpportunityFilterSettings;
import internship_management_system.users.CompanyRepresentative;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Class representing an internship opportunity
 */
public class InternshipOpportunity {

    private final int id;
    private final String title, description, preferredMajor;
    private final InternshipLevel level;
    private final LocalDate openingDate, closingDate;
    private InternshipOpportunityStatus status;
    private final CompanyRepresentative companyRep;
    private final int numOfSlots;
    private int numOfRemainingSlots;
    private boolean visibility;

    /**
     * Constructor for InternshipOpportunity
     *
     * @param id The ID of the internship opportunity, also the index in the
     * DataStorage list
     * @param title The title of the internship opportunity
     * @param description The description of the internship opportunity
     * @param preferredMajor The preferred major for the internship opportunity
     * @param level The level of the internship opportunity
     * @param openingDate The opening date of the internship opportunity
     * @param closingDate The closing date of the internship opportunity
     * @param companyRep The company representative associated with the
     * internship opportunity
     * @param numOfSlots The number of slots available for the internship
     * opportunity
     * @param visibility The visibility status of the internship opportunity
     */
    public InternshipOpportunity(int id, String title, String description, String preferredMajor, InternshipLevel level, LocalDate openingDate, LocalDate closingDate, CompanyRepresentative companyRep, int numOfSlots, boolean visibility) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.preferredMajor = preferredMajor;
        this.level = level;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.status = InternshipOpportunityStatus.PENDING;
        this.companyRep = companyRep;
        this.numOfSlots = numOfSlots;
        this.numOfRemainingSlots = numOfSlots;
        this.visibility = visibility;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String lvl, statusString;
        lvl = switch (this.level) {
            case BASIC ->
                "basic";
            case INTERMEDIATE ->
                "intermediate";
            default ->
                "advanced";
        };
        statusString = switch (this.status) {
            case PENDING ->
                "PENDING";
            case APPROVED ->
                "APPROVED";
            case REJECTED ->
                "REJECTED";
            default ->
                "FILLED";
        };

        ArrayList<String> extra = new ArrayList<>();
        if (this.status != InternshipOpportunityStatus.APPROVED) {
            extra.add("[" + statusString + "]");
        }
        if (!this.visibility) {
            extra.add("[HIDDEN]");
        }
        if (this.openingDate.isAfter(LocalDate.now())) {
            extra.add("[NOT OPENED]");
        }
        if (this.closingDate.isBefore(LocalDate.now())) {
            extra.add("[CLOSED]");
        }
        String extraInfo = String.join(" ", extra);

        return "(id = " + this.id + ") " + this.title + "\n"
                + this.description + "\n"
                + "Company: " + this.companyRep.getCompanyName() + "\tSlots: " + numOfSlots + "\n"
                + "Preferred Major: " + this.preferredMajor + "\tLevel: " + lvl + "\n"
                + "Opening Date: " + openingDate + "\tClosing Date: " + closingDate
                + (extraInfo.isEmpty() ? "" : "\n" + extraInfo);
    }

    /**
     * @return the id of the internship opportunity
     */
    public int getId() {
        return id;
    }

    /**
     * @return the title of the internship opportunity
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the description of the internship opportunity
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the preferred major for the internship opportunity
     */
    public String getPreferredMajor() {
        return preferredMajor;
    }

    /**
     * @return the level of the internship opportunity
     */
    public InternshipLevel getInternshipLevel() {
        return level;
    }

    /**
     * @return the opening date of the internship opportunity
     */
    public LocalDate getOpeningDate() {
        return openingDate;
    }

    /**
     * @return the closing date of the internship opportunity
     */
    public LocalDate getClosingDate() {
        return closingDate;
    }

    /**
     * @return the status of the internship opportunity
     */
    public InternshipOpportunityStatus getStatus() {
        return status;
    }

    /**
     * @return the company representative associated with the internship
     * opportunity
     */
    public CompanyRepresentative getCompanyRep() {
        return companyRep;
    }

    /**
     * @return the number of slots available for the internship opportunity
     */
    public int getNumOfSlots() {
        return numOfSlots;
    }

    /**
     * @return the number of remaining slots for the internship opportunity
     */
    public int getNumOfRemainingSlots() {
        return numOfRemainingSlots;
    }

    /**
     * @return the visibility status of the internship opportunity
     */
    public boolean getVisibility() {
        return visibility;
    }

    /**
     * @param status The new status of the internship opportunity
     */
    public void setStatus(InternshipOpportunityStatus status) {
        this.status = status;
    }

    /**
     * Toggle the visibility status of the internship opportunity
     */
    public void toggleVisibility() {
        this.visibility = !this.visibility;
    }

    /**
     * Free up a slot for the internship opportunity Only used internally
     */
    void freeUpASlot() {
        numOfRemainingSlots++;
        if (numOfRemainingSlots == 1) {
            this.status = InternshipOpportunityStatus.APPROVED;
        }
    }

    /**
     * Take up a slot for the internship opportunity Only used internally
     */
    void takeUpASlot() {
        numOfRemainingSlots--;
        if (numOfRemainingSlots == 0) {
            this.status = InternshipOpportunityStatus.FILLED;
        }
    }

    /**
     * Check if the internship opportunity matches the given filter settings
     *
     * @param filter The filter settings to match against
     * @return true if the internship opportunity matches the filter, false
     * otherwise
     */
    public boolean matchesFilter(InternshipOpportunityFilterSettings filter) {
        if (filter.getPreferredMajors().isPresent() && !filter.getPreferredMajors().get().contains(this.preferredMajor)) {
            return false;
        }
        if (filter.getClosesAfter().isPresent() && this.closingDate.isBefore(filter.getClosesAfter().get())) {
            return false;
        }
        if (filter.getClosesBefore().isPresent() && this.closingDate.isAfter(filter.getClosesBefore().get())) {
            return false;
        }
        if (filter.getCompanies().isPresent() && !filter.getCompanies().get().contains(this.companyRep.getCompanyName())) {
            return false;
        }
        if (!filter.isShowLevel(this.level)) {
            return false;
        }
        if (!filter.isShowStatus(this.status)) {
            return false;
        }
        if (this.visibility && !filter.isShowVisible()) {
            return false;
        }
        if (!this.visibility && !filter.isShowHidden()) {
            return false;
        }
        if (this.openingDate.isAfter(LocalDate.now()) && !filter.isShowUnopened()) {
            return false;
        }
        return !(this.closingDate.isBefore(LocalDate.now()) && !filter.isShowClosed());
    }
}
