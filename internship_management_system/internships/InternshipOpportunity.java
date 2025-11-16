package internship_management_system.internships;

import internship_management_system.Model.DataStorage;
import internship_management_system.enums.InternshipLevel;
import internship_management_system.enums.InternshipOpportunityStatus;
import internship_management_system.enums.WithdrawStatus;
import internship_management_system.filter.InternshipOpportunityFilterSettings;
import internship_management_system.users.CompanyRepresentative;
import internship_management_system.users.Student;

import java.time.LocalDate;

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

    // private static ArrayList<InternshipOpportunity> internshipOpportunities;

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

    /*public static int addNewOpportunity(String title, String description, String preferredMajor, InternshipLevel level, LocalDate openingDate, LocalDate closingDate, CompanyRepresentative companyRep, int numOfSlots, boolean visibility) {
        int id = internshipOpportunities.size();
        internshipOpportunities.add(new InternshipOpportunity(id, title, description, preferredMajor, level, openingDate, closingDate, companyRep, numOfSlots, visibility));
        return id;
    }*/

    /*public static ArrayList<InternshipOpportunity> getOpportunitiesList(String filter) {
        // TODO: Do filtering here
        return internshipOpportunities;
    }*/

    @Override
    public String toString() {
        String lvl, statusString;
        lvl = switch (this.level) {
            case BASIC -> "basic";
            case INTERMEDIATE -> "intermediate";
            default -> "advanced";
        };
        statusString = switch (this.status) {
            case PENDING -> "pending";
            case APPROVED -> "approved";
            case REJECTED -> "rejected";
            default -> "filled";
        };

        return this.id + "\n" + this.title + "\n" + this.description + "\n"
            + "Company: " + this.companyRep.getCompanyName() + "\tSlots: " + numOfSlots + "\n"
            + "Preferred Major: " + this.preferredMajor + "\tLevel: " + lvl + "\tStatus: " + statusString + "\n"
            + "Opening Date: " + openingDate + "\tClosing Date: " + closingDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPreferredMajor() {
        return preferredMajor;
    }

    public InternshipLevel getInternshipLevel() {
        return level;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public InternshipOpportunityStatus getStatus() {
        return status;
    }

    public CompanyRepresentative getCompanyRep() {
        return companyRep;
    }

    public int getNumOfSlots() {
        return numOfSlots;
    }

    public int getNumOfRemainingSlots() {
        return numOfRemainingSlots;
    }

    public boolean getVisibility() {
        return visibility;
    }

    public void setStatus(InternshipOpportunityStatus status) {
        this.status = status;
    }

    public void toggleVisibility() {
        this.visibility = !this.visibility;
    }

    public void newApplication(Student student) {
        if(!student.getMajor().equals(this.preferredMajor) || (this.level != InternshipLevel.BASIC && student.getYearOfStudy() <= 2)) {
            throw new Error("Student is not eligible for this internship");
        }
        if(student.hasAcceptedSomeOffer()) {
            throw new Error("Student has already accepted some offer");
        }
        for(InternshipApplication application: DataStorage.getAllInternshipApplications()) {
            if(application.getStudent().equals(student) && application.getOpportunity().equals(this) && application.getWithdrawStatus() != WithdrawStatus.APPROVED) {
                throw new Error("Student has already applied for this internship");
            }
        }
        if (!this.visibility) {
            throw new Error("Internship opportunity is not visible");
        }
        if (this.openingDate.isAfter(LocalDate.now())) {
            throw new Error("Internship opportunity is not open yet");
        }
        if (this.closingDate.isBefore(LocalDate.now())) {
            throw new Error("Internship opportunity is closed");
        }
        if (this.status != InternshipOpportunityStatus.APPROVED) {
            throw new Error("Internship opportunity is not approved yet or filled");
        }

        DataStorage.newInternshipApplication(this, student);
    }

    void freeUpASlot() { // It will only be used by other classes (i.e. by classes in the same package, not ui). So, shouldn't be public
        numOfRemainingSlots++;
        if (numOfRemainingSlots == 1) {
            this.status = InternshipOpportunityStatus.APPROVED;
        }
    }

    void takeUpASlot() {
        numOfRemainingSlots--;
        if (numOfRemainingSlots == 0) {
            this.status = InternshipOpportunityStatus.FILLED;
        }
    }

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
        if (this.closingDate.isBefore(LocalDate.now()) && !filter.isShowClosed()) {
            return false;
        }
        return true;
    }
}
