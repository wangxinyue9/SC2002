import java.util.ArrayList;

/**
 *
 * @author jarif
 */
public class InternshipApplication {
    private final int id;
    private final InternshipOpportunity opportunity;
    private final String student; // TODO: Change type to Student later
    private InternshipApplicationStatus status;
    private WithdrawStatus withdrawStatus;
    private PlacementConfirmationStatus placementConfirmationStatus;

    private static ArrayList<InternshipApplication> internshipApplications;

    public InternshipApplication(int id, InternshipOpportunity opportunity, String student) {
        this.id = id;
        this.opportunity = opportunity;
        this.student = student;
        this.status = InternshipApplicationStatus.PENDING;
        this.withdrawStatus = WithdrawStatus.NOT_REQUESTED;
        this.placementConfirmationStatus = PlacementConfirmationStatus.PENDING;
    }

    public static int addNewOpportunity(InternshipOpportunity opportunity, String student) {
        int id = internshipApplications.size();
        internshipApplications.add(new InternshipApplication(id, opportunity, student));
        return id;
    }

    public static ArrayList<InternshipApplication> getApplicationList(String filter) {
        // TODO: Filter here
        return internshipApplications;
    }

    public int getId() {
        return id;
    }
    public InternshipOpportunity getOpportunity() {
        return opportunity;
    }
    public String getStudent() {
        return student;
    }
    public InternshipApplicationStatus getStatus() {
        return status;
    }
    public WithdrawStatus getWithdrawStatus() {
        return  withdrawStatus;
    }
    
    public void finalizeApplicationStatus(boolean offerInternship) { // Only used by CompanyRep
        if(this.status != InternshipApplicationStatus.PENDING) throw new Error("Application status already finalized");
        if(this.withdrawStatus == WithdrawStatus.APPROVED) throw new Error("Application already withdrawn");
        // ?? if(this.opportunity.getStatus() == InternshipOpportunityStatus.FILLED) throw new Error("Internship opportunity already filled");
        if(offerInternship) this.status = InternshipApplicationStatus.SUCCESSFUL;
        else this.status = InternshipApplicationStatus.UNSUCCESSFUL;
    }
    public void confirmPlacement(boolean accept) {
        if(this.withdrawStatus == WithdrawStatus.APPROVED) throw new Error("Application already withdrawn");
        if(this.status != InternshipApplicationStatus.SUCCESSFUL) throw new Error("Internship hasn't been offered");
        if(!accept) {
            this.placementConfirmationStatus = PlacementConfirmationStatus.REJECTED;
        }
        else {
            if(this.opportunity.getStatus() == InternshipOpportunityStatus.FILLED) throw new Error("Internship opportunity already filled"); // Sorry bro you didn't accept in time 
            // TODO: CHECK IF THE STUDENT HAS ALREADY ACCEPTED SOME OTHER OFFER
            // if(this.student.acceptedSomeOffer()) throw ...
            this.placementConfirmationStatus = PlacementConfirmationStatus.ACCEPTED;
            // TODO: toggle acceptedSomeOffer
            // this.student.toggleAcceptedSomeOffer();
            this.opportunity.takeUpASlot();
            internshipApplications.forEach((application) -> {
                if(application.getStudent().equals(this.student) && !application.equals(this)) {
                    application.confirmWithdraw(true);
                }
            });
        }
    }

    public void requestWithdraw() {
        if(this.withdrawStatus != WithdrawStatus.NOT_REQUESTED) throw new Error("Already requested");
        this.withdrawStatus = WithdrawStatus.PENDING;
    }
    public void confirmWithdraw(boolean approve) {
        if(approve) {
            this.withdrawStatus = WithdrawStatus.APPROVED;
            if(this.status == InternshipApplicationStatus.SUCCESSFUL && this.placementConfirmationStatus == PlacementConfirmationStatus.ACCEPTED) {
                // TODO: toggle acceptedSomeOffer
                // this.student.toggleAcceptedSomeOffer();
                this.opportunity.freeUpASlot();
            }
        }
        else {
            this.withdrawStatus = WithdrawStatus.REJECTED;
        }
    }
}
