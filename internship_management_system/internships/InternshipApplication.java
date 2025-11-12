package internship_management_system.internships;

import internship_management_system.Model.DataStorage;
import internship_management_system.enums.InternshipApplicationStatus;
import internship_management_system.enums.InternshipOpportunityStatus;
import internship_management_system.enums.PlacementConfirmationStatus;
import internship_management_system.enums.WithdrawStatus;
import internship_management_system.filter.InternshipApplicationFilterSettings;
import internship_management_system.users.Student;

public class InternshipApplication {

    private final int id;
    private final InternshipOpportunity opportunity;
    private final Student student;
    private InternshipApplicationStatus status;
    private WithdrawStatus withdrawStatus;
    private PlacementConfirmationStatus placementConfirmationStatus;

    // private static ArrayList<InternshipApplication> internshipApplications;

    public InternshipApplication(int id, InternshipOpportunity opportunity, Student student) {
        this.id = id;
        this.opportunity = opportunity;
        this.student = student;
        this.status = InternshipApplicationStatus.PENDING;
        this.withdrawStatus = WithdrawStatus.NOT_REQUESTED;
        this.placementConfirmationStatus = PlacementConfirmationStatus.PENDING;
    }

    /*public static int addNewOpportunity(InternshipOpportunity opportunity, Student student) {
        int id = internshipApplications.size();
        internshipApplications.add(new InternshipApplication(id, opportunity, student));
        return id;
    }

    public static ArrayList<InternshipApplication> getApplicationList(String filter) {
        // TODO: Filter here
        return internshipApplications;
    }*/

    public int getId() {
        return id;
    }

    public InternshipOpportunity getOpportunity() {
        return opportunity;
    }

    public Student getStudent() {
        return student;
    }

    public InternshipApplicationStatus getStatus() {
        return status;
    }

    public WithdrawStatus getWithdrawStatus() {
        return withdrawStatus;
    }

    public void finalizeApplicationStatus(boolean offerInternship) { // Only used by CompanyRep
        if (this.status != InternshipApplicationStatus.PENDING) {
            throw new Error("Application status already finalized");
        }
        if (this.withdrawStatus == WithdrawStatus.APPROVED) {
            throw new Error("Application already withdrawn");
        }
        // ?? if(this.opportunity.getStatus() == InternshipOpportunityStatus.FILLED) throw new Error("Internship opportunity already filled");
        if (offerInternship) {
            this.status = InternshipApplicationStatus.SUCCESSFUL;
        } else {
            this.status = InternshipApplicationStatus.UNSUCCESSFUL;
        }
    }

    public void confirmPlacement(boolean accept) {
        if (this.withdrawStatus == WithdrawStatus.APPROVED) {
            throw new Error("Application already withdrawn");
        }
        if (this.status != InternshipApplicationStatus.SUCCESSFUL) {
            throw new Error("Internship hasn't been offered");
        }
        if (!accept) {
            this.placementConfirmationStatus = PlacementConfirmationStatus.REJECTED;
        } else {
            if (this.opportunity.getStatus() == InternshipOpportunityStatus.FILLED) {
                throw new Error("Internship opportunity already filled"); // Sorry bro you didn't accept in time
            }
            // No need to check if the student has already accepted some offer since, when a student accepts some offer, all of his other offers are automatically withdrawn
            this.placementConfirmationStatus = PlacementConfirmationStatus.ACCEPTED;
            this.student.toggleAcceptedSomeOffer(); // Maybe do in student class?
            this.opportunity.takeUpASlot();
            DataStorage.getAllInternshipApplications().forEach((application) -> {
                if (application.getStudent().equals(this.student) && !application.equals(this)) {
                    application.confirmWithdraw(true);
                }
            });
        }
    }

    public void requestWithdraw() {
        if (this.withdrawStatus != WithdrawStatus.NOT_REQUESTED) {
            throw new Error("Already requested");
        }
        this.withdrawStatus = WithdrawStatus.PENDING;
    }

    public void confirmWithdraw(boolean approve) {
        if (approve) {
            this.withdrawStatus = WithdrawStatus.APPROVED;
            if (this.status == InternshipApplicationStatus.SUCCESSFUL && this.placementConfirmationStatus == PlacementConfirmationStatus.ACCEPTED) {
                this.student.toggleAcceptedSomeOffer(); // Maybe do in student class?
                this.opportunity.freeUpASlot();
            }
        } else {
            this.withdrawStatus = WithdrawStatus.REJECTED;
        }
    }

    public boolean matchesFilter(InternshipApplicationFilterSettings filter) {
        if (!filter.isShowStatus(this.status)) {
            return false;
        }
        if (!filter.isShowWithdrawStatus(this.withdrawStatus)) {
            return false;
        }
        if (!filter.isShowPlacementConfirmationStatus(this.placementConfirmationStatus)) {
            return false;
        }
        if (filter.getOpportunityFilters().isPresent()) {
            return this.opportunity.matchesFilter(filter.getOpportunityFilters().get());
        }
        return true;
    }
}
