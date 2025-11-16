package internship_management_system.filter;

import internship_management_system.enums.InternshipLevel;
import internship_management_system.enums.InternshipOpportunityStatus;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

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

    // Getters
    public Optional<HashSet<String>> getPreferredMajors() {
        return preferredMajors;
    }
    public Optional<LocalDate> getClosesAfter() {
        return closesAfter;
    }
    public Optional<LocalDate> getClosesBefore() {
        return closesBefore;
    }
    public Optional<HashSet<String>> getCompanies() {
        return companies;
    }
    public boolean isShowLevel(InternshipLevel level) {
        return showLevel[level.ordinal()];
    }
    public boolean isShowStatus(InternshipOpportunityStatus status) {
        return showStatus[status.ordinal()];
    }
    public boolean isShowVisible() {
        return showVisible;   
    }
    public boolean isShowHidden() {
        return showHidden;
    }
    public boolean isShowUnopened() {
        return showUnopened;
    }
    public boolean isShowClosed() {
        return showClosed;
    }

    public void addPreferredMajor(String major) {
        if(!this.preferredMajors.isPresent()) {
            this.preferredMajors = Optional.of(new HashSet<>());
        }
        this.preferredMajors.get().add(major);  
    }
    public void setClosesAfter(LocalDate date) {
        this.closesAfter = Optional.of(date);
    }
    public void setClosesBefore(LocalDate date) {
        this.closesBefore = Optional.of(date);
    }
    public void addCompany(String company) {
        if(!this.companies.isPresent()) {
            this.companies = Optional.of(new HashSet<>());
        }
        this.companies.get().add(company);
    }
    public void toggleShowLevel(InternshipLevel level) {
        this.showLevel[level.ordinal()] = !this.showLevel[level.ordinal()];
    }
    public void toggleShowStatus(InternshipOpportunityStatus status) {
        this.showStatus[status.ordinal()] = !this.showStatus[status.ordinal()];
    }
    public void toggleShowVisible() {
        this.showVisible = !this.showVisible;
    }
    public void toggleShowHidden() {
        this.showHidden = !this.showHidden;
    }
    public void toggleShowUnopened() {
        this.showUnopened = !this.showUnopened;
    }
    public void toggleShowClosed() {
        this.showClosed = !this.showClosed;
    }

    public void removePreferredMajor(String major) {
        if(this.preferredMajors.isPresent()) {
            this.preferredMajors.get().remove(major);
        }
    }
    public void removeCompany(String company) {
        if(this.companies.isPresent()) {
            this.companies.get().remove(company);
        }
    }

    public void resetPreferredMajors() {
        this.preferredMajors = Optional.empty();
    }
    public void resetCompanies() {
        this.companies = Optional.empty();
    }
    public void resetClosesAfter() {
        this.closesAfter = Optional.empty();
    }
    public void resetClosesBefore() {
        this.closesBefore = Optional.empty();
    }
}