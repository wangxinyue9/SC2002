package internship_management_system.filter;

import javax.swing.text.html.Option;

import internship_management_system.enums.InternshipApplicationStatus;
import internship_management_system.enums.WithdrawStatus;
import internship_management_system.enums.PlacementConfirmationStatus;
import java.util.Optional;

public class InternshipApplicationFilterSettings {
    private Optional<InternshipOpportunityFilterSettings> opportunityFilters;
    private final boolean[] showStatus;
    private final boolean[] showWithdrawStatus;
    private final boolean[] showPlacementConfirmationStatus;

    public InternshipApplicationFilterSettings() {
        this.opportunityFilters = Optional.empty();
        this.showStatus = new boolean[]{true, true, true}; // pending, successful, unsuccessful
        this.showWithdrawStatus = new boolean[]{true, true, true, true}; // not requested, pending, approved, rejected
        this.showPlacementConfirmationStatus = new boolean[]{true, true, true}; // pending, accepted, rejected
    }

    // Getters
    public Optional<InternshipOpportunityFilterSettings> getOpportunityFilters() {
        return opportunityFilters;
    }
    public boolean isShowStatus(InternshipApplicationStatus status) {
        return showStatus[status.ordinal()];
    }
    public boolean isShowWithdrawStatus(WithdrawStatus status) {
        return showWithdrawStatus[status.ordinal()];
    }
    public boolean isShowPlacementConfirmationStatus(PlacementConfirmationStatus status) {
        return showPlacementConfirmationStatus[status.ordinal()];
    }
    // Setters
    public void setOpportunityFilters(Optional<InternshipOpportunityFilterSettings> filters) {
        this.opportunityFilters = filters;
    }
    public void toggleShowStatus(InternshipApplicationStatus status) {
        this.showStatus[status.ordinal()] = !this.showStatus[status.ordinal()];
    }
    public void toggleShowWithdrawStatus(WithdrawStatus status) {
        this.showWithdrawStatus[status.ordinal()] = !this.showWithdrawStatus[status.ordinal()];
    }
    public void toggleShowPlacementConfirmationStatus(PlacementConfirmationStatus status) {
        this.showPlacementConfirmationStatus[status.ordinal()] = !this.showPlacementConfirmationStatus[status.ordinal()];
    }

}
