
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author jarif
 */
public class InternshipOpportunity {
    private final int id;
    private final String title, description, preferredMajor;
    private final InternshipLevel level;
    private final LocalDate openingDate, closingDate;
    private InternshipOpportunityStatus status;
    private final String companyRep; // TODO: change later
    private final int numOfSlots;
    private int numOfRemainingSlots;
    private boolean visibility;

    private static ArrayList<InternshipOpportunity> internshipOpportunities;

    public InternshipOpportunity(int id, String title, String description, String preferredMajor, InternshipLevel level, LocalDate openingDate, LocalDate closingDate, String companyRep, int numOfSlots, boolean visibility) {
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

    public static int addNewOpportunity(String title, String description, String preferredMajor, InternshipLevel level, LocalDate openingDate, LocalDate closingDate, String companyRep, int numOfSlots, boolean visibility) {
        int id = internshipOpportunities.size();
        internshipOpportunities.add(new InternshipOpportunity(id, title, description, preferredMajor, level, openingDate, closingDate, companyRep, numOfSlots, visibility));
        return id;
    }

    public static ArrayList<InternshipOpportunity> getOpportunitiesList(String filter) {
        // TODO: Do filtering here
        return internshipOpportunities;
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
    public String getCompanyRep() {
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
    public void newApplication(String student) {
        // TODO: Check if the student is eligible
        // TODO: CHECK IF THE STUDENT HAS ALREADY ACCEPTED SOME OTHER APPLICATION
        // TODO: CHECK if student has already applied for the same opportunity
        if(!this.visibility) throw new Error("Internship opportunity is not visible");
        if(this.openingDate.isAfter(LocalDate.now())) throw new Error("Internship opportunity is not open yet");
        if(this.closingDate.isBefore(LocalDate.now())) throw new Error("Internship opportunity is closed");
        if(this.status != InternshipOpportunityStatus.APPROVED) throw new Error("Internship opportunity is not approved yet or filled");

        InternshipApplication.addNewOpportunity(this, student);
    }

    void freeUpASlot() { // It will only be used by other classes (i.e. by classes in the same package, not ui). So, shouldn't be public
        numOfRemainingSlots++;
        if(numOfRemainingSlots == 1) {
            this.status = InternshipOpportunityStatus.APPROVED;
        }
    }
    void takeUpASlot() {
        numOfRemainingSlots--;
        if(numOfRemainingSlots == 0) {
            this.status = InternshipOpportunityStatus.FILLED;
        }
    }
}
