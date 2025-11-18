package internship_management_system.internships;

import internship_management_system.enums.InternshipApplicationStatus;
import internship_management_system.enums.InternshipOpportunityStatus;
import internship_management_system.enums.PlacementConfirmationStatus;
import internship_management_system.enums.WithdrawStatus;
import internship_management_system.filter.InternshipApplicationFilterSettings;
import internship_management_system.model.DataStorage;
import internship_management_system.users.Student;
import java.util.ArrayList;

/**
 * Class representing an internship application
 */
public class InternshipApplication {

    private final int id;
    private final InternshipOpportunity opportunity;
    private final Student student;
    private InternshipApplicationStatus status;
    private WithdrawStatus withdrawStatus;
    private PlacementConfirmationStatus placementConfirmationStatus;

    /**
     * Constructor for InternshipApplication
     *
     * @param id The ID of the internship application, also the index in the
     * DataStorage list
     * @param opportunity The internship opportunity applied for
     * @param student The student who applied for the internship opportunity
     */
    public InternshipApplication(int id, InternshipOpportunity opportunity, Student student) {
        this.id = id;
        this.opportunity = opportunity;
        this.student = student;
        this.status = InternshipApplicationStatus.PENDING;
        this.withdrawStatus = WithdrawStatus.NOT_REQUESTED;
        this.placementConfirmationStatus = PlacementConfirmationStatus.PENDING;
    }

    /**
     * @return the ID of the internship application
     */
    public int getId() {
        return id;
    }

    /**
     * @return the internship opportunity applied for
     */
    public InternshipOpportunity getOpportunity() {
        return opportunity;
    }

    /**
     * @return the student who applied for the internship opportunity
     */
    public Student getStudent() {
        return student;
    }

    /**
     * @return the status of the internship application
     */
    public InternshipApplicationStatus getStatus() {
        return status;
    }

    /**
     * @return the withdraw status of the internship application
     */
    public WithdrawStatus getWithdrawStatus() {
        return withdrawStatus;
    }

    /**
     * @return the placement confirmation status of the internship application
     */
    public PlacementConfirmationStatus getPlacementConfirmationStatus() {
        return placementConfirmationStatus;
    }

    /**
     * Finalize the application status Only meant to be used by CompanyRep
     *
     * @param offerInternship whether to offer the internship
     */
    public void finalizeApplicationStatus(boolean offerInternship) {
        if (this.status != InternshipApplicationStatus.PENDING) {
            throw new Error("Application status already finalized");
        }
        if (this.withdrawStatus == WithdrawStatus.APPROVED) {
            throw new Error("Application already withdrawn");
        }
        if (this.opportunity.getStatus() == InternshipOpportunityStatus.FILLED) {
            throw new Error("Internship opportunity already filled");
        }
        if (offerInternship) {
            this.status = InternshipApplicationStatus.SUCCESSFUL;
        } else {
            this.status = InternshipApplicationStatus.UNSUCCESSFUL;
        }
    }

    /**
     * Confirm placement for the internship application meant to be used by
     * Student
     *
     * @param accept whether to accept the internship offer
     */
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
            this.placementConfirmationStatus = PlacementConfirmationStatus.ACCEPTED;
            this.opportunity.takeUpASlot();
            DataStorage.getInternshipApplications(this.student).forEach((application) -> {
                if (!application.equals(this)) {
                    application.confirmWithdraw(true);
                }
            });
        }
    }

    /**
     * Request to withdraw the internship application meant to be used by
     * Student
     */
    public void requestWithdraw() {
        if (this.withdrawStatus != WithdrawStatus.NOT_REQUESTED) {
            throw new Error("Already requested");
        }
        this.withdrawStatus = WithdrawStatus.PENDING;
    }

    /**
     * Confirm or reject the withdrawal request meant to be used by
     * CareerCentreStaff, but also to withdraw other offers when student accepts
     * one
     *
     * @param approve whether to approve the withdrawal request
     */
    public void confirmWithdraw(boolean approve) {
        if (approve) {
            this.withdrawStatus = WithdrawStatus.APPROVED;
            if (this.status == InternshipApplicationStatus.SUCCESSFUL && this.placementConfirmationStatus == PlacementConfirmationStatus.ACCEPTED) {
                this.opportunity.freeUpASlot();
            }
        } else {
            this.withdrawStatus = WithdrawStatus.REJECTED;
        }
    }

    /**
     * Check if the internship application matches the given filter settings
     *
     * @param filter The filter settings to match against
     * @return true if the internship application matches the filter, false
     * otherwise
     */
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
        if (filter.isUseOpportunityFilters()) {
            return this.opportunity.matchesFilter(filter.getOpportunityFilters());
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        ArrayList<String> extra = new ArrayList<>();
        if (status != InternshipApplicationStatus.PENDING) {
            if (status == InternshipApplicationStatus.SUCCESSFUL) {
                extra.add("[OFFERED]");
            } else {
                extra.add("[REJECTED]");
            }
        }
        if (withdrawStatus != WithdrawStatus.NOT_REQUESTED) {
            switch (withdrawStatus) {
                case APPROVED ->
                    extra.add("[WITHDRAWN]");
                case PENDING ->
                    extra.add("[WITHDRAW REQUEST PENDING]");
                default ->
                    extra.add("[WITHDRAW REQUEST REJECTED]");
            }
        }
        if (placementConfirmationStatus != PlacementConfirmationStatus.PENDING) {
            switch (placementConfirmationStatus) {
                case ACCEPTED ->
                    extra.add("[OFFER ACCEPTED]");
                case REJECTED ->
                    extra.add("[OFFER REJECTED]");
                default -> {
                }
            }
        }
        String extraStr = String.join(" ", extra);
        if (!extraStr.isEmpty()) {
            extraStr = "\n# " + extraStr;
        }
        return String.format(" (id=%d) %s (%s)'s application for (id=%d) %s%s", id, student.getName(), student.getUserID(), opportunity.getId(), opportunity.getTitle(), extraStr);
    }
}
