package internship_management_system.filter;

import internship_management_system.enums.InternshipLevel;
import internship_management_system.enums.InternshipOpportunityStatus;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

/**
 * Class representing filter settings for internship opportunities
 */
public class InternshipOpportunityFilterSettings {

    private Optional<HashSet<String>> preferredMajors;
    private Optional<LocalDate> closesAfter, closesBefore;
    private Optional<HashSet<String>> companies;
    private final boolean[] showLevel;
    private final boolean[] showStatus;
    private boolean showVisible;
    private boolean showHidden;
    private boolean showUnopened;
    private boolean showClosed;

    /**
     * Constructor for InternshipOpportunityFilterSettings
     */
    public InternshipOpportunityFilterSettings() {
        this.preferredMajors = Optional.empty();
        this.closesAfter = Optional.empty();
        this.closesBefore = Optional.empty();
        this.companies = Optional.empty();
        this.showLevel = new boolean[]{true, true, true}; // basic, intermediate, advanced
        this.showStatus = new boolean[]{true, true, true, true}; // pending, approved, rejected, filled
        this.showVisible = true;
        this.showHidden = true;
        this.showUnopened = true;
        this.showClosed = true;
    }

    /**
     * @return the preferredMajors
     */
    public Optional<HashSet<String>> getPreferredMajors() {
        return preferredMajors;
    }

    /**
     * @return the closesAfter
     */
    public Optional<LocalDate> getClosesAfter() {
        return closesAfter;
    }

    /**
     * @return the closesBefore
     */
    public Optional<LocalDate> getClosesBefore() {
        return closesBefore;
    }

    /**
     * @return the companies to filter by
     */
    public Optional<HashSet<String>> getCompanies() {
        return companies;
    }

    /**
     * @param level The internship level to check
     * @return whether to show this level
     */
    public boolean isShowLevel(InternshipLevel level) {
        return showLevel[level.ordinal()];
    }

    /**
     * @param status The internship opportunity status to check
     * @return whether to show this status
     */
    public boolean isShowStatus(InternshipOpportunityStatus status) {
        return showStatus[status.ordinal()];
    }

    /**
     * @return whether to show visible opportunities
     */
    public boolean isShowVisible() {
        return showVisible;
    }

    /**
     * @return whether to show hidden opportunities
     */
    public boolean isShowHidden() {
        return showHidden;
    }

    /**
     * @return whether to show unopened opportunities
     */
    public boolean isShowUnopened() {
        return showUnopened;
    }

    /**
     * @return whether to show closed opportunities
     */
    public boolean isShowClosed() {
        return showClosed;
    }

    /**
     * Add a preferred major to filter by
     *
     * @param major The preferred major to add
     */
    public void addPreferredMajor(String major) {
        if (!this.preferredMajors.isPresent()) {
            this.preferredMajors = Optional.of(new HashSet<>());
        }
        this.preferredMajors.get().add(major);
    }

    /**
     * @param date The date to set as closesAfter
     */
    public void setClosesAfter(LocalDate date) {
        this.closesAfter = Optional.of(date);
    }

    /**
     * @param date The date to set as closesBefore
     */
    public void setClosesBefore(LocalDate date) {
        this.closesBefore = Optional.of(date);
    }

    /**
     * Add a company to filter by
     *
     * @param company The company to add
     */
    public void addCompany(String company) {
        if (!this.companies.isPresent()) {
            this.companies = Optional.of(new HashSet<>());
        }
        this.companies.get().add(company);
    }

    /**
     * Toggle whether to show a specific internship level
     *
     * @param level The internship level to toggle
     */
    public void toggleShowLevel(InternshipLevel level) {
        this.showLevel[level.ordinal()] = !this.showLevel[level.ordinal()];
    }

    /**
     * Toggle whether to show a specific internship opportunity status
     *
     * @param status The internship opportunity status to toggle
     */
    public void toggleShowStatus(InternshipOpportunityStatus status) {
        this.showStatus[status.ordinal()] = !this.showStatus[status.ordinal()];
    }

    /**
     * Toggle whether to show visible opportunities
     */
    public void toggleShowVisible() {
        this.showVisible = !this.showVisible;
    }

    /**
     * Toggle whether to show hidden opportunities
     */
    public void toggleShowHidden() {
        this.showHidden = !this.showHidden;
    }

    /**
     * Toggle whether to show unopened opportunities
     */
    public void toggleShowUnopened() {
        this.showUnopened = !this.showUnopened;
    }

    /**
     * Toggle whether to show closed opportunities
     */
    public void toggleShowClosed() {
        this.showClosed = !this.showClosed;
    }

    /**
     * Remove a preferred major from filter by
     *
     * @param major The preferred major to remove
     */
    public void removePreferredMajor(String major) {
        if (this.preferredMajors.isPresent()) {
            this.preferredMajors.get().remove(major);
        }
    }

    /**
     * Remove a company from filter by
     *
     * @param company The company to remove
     */
    public void removeCompany(String company) {
        if (this.companies.isPresent()) {
            this.companies.get().remove(company);
        }
    }

    /**
     * Reset preferred majors filter
     */
    public void resetPreferredMajors() {
        this.preferredMajors = Optional.empty();
    }

    /**
     * Reset companies filter
     */
    public void resetCompanies() {
        this.companies = Optional.empty();
    }

    /**
     * Reset closesAfter filter
     */
    public void resetClosesAfter() {
        this.closesAfter = Optional.empty();
    }

    /**
     * Reset closesBefore filter
     */
    public void resetClosesBefore() {
        this.closesBefore = Optional.empty();
    }
}
