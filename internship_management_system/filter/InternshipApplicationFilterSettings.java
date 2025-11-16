package internship_management_system.filter;

import internship_management_system.enums.InternshipApplicationStatus;
import internship_management_system.enums.WithdrawStatus;
import internship_management_system.enums.PlacementConfirmationStatus;


/**
 * Filter settings for internship applications
 */
public class InternshipApplicationFilterSettings {
    private final InternshipOpportunityFilterSettings opportunityFilters;
    private boolean useOpportunityFilters;
    private final boolean[] showStatus;
    private final boolean[] showWithdrawStatus;
    private final boolean[] showPlacementConfirmationStatus;

    /**
     * Constructor initializing default filter settings
     */
    public InternshipApplicationFilterSettings(InternshipOpportunityFilterSettings opportunityFilters) {
        this.opportunityFilters = opportunityFilters;
        this.useOpportunityFilters = false;
        this.showStatus = new boolean[]{true, true, true}; // pending, successful, unsuccessful
        this.showWithdrawStatus = new boolean[]{true, true, true, true}; // not requested, pending, approved, rejected
        this.showPlacementConfirmationStatus = new boolean[]{true, true, true}; // pending, accepted, rejected
    }

    /**
     * @return the opportunityFilters
     */
    public InternshipOpportunityFilterSettings getOpportunityFilters() {
        return opportunityFilters;
    }

    /**
     * @param status application status enum
     * @return whether to show applications with the given status
     */
    public boolean isShowStatus(InternshipApplicationStatus status) {
        return showStatus[status.ordinal()];
    }
    /**
     * @param status withdraw status enum
     * @return whether to show applications with the given withdraw status
     */
    public boolean isShowWithdrawStatus(WithdrawStatus status) {
        return showWithdrawStatus[status.ordinal()];
    }
    /**
     * @param status placement confirmation status enum
     * @return whether to show applications with the given placement confirmation status
     */
    public boolean isShowPlacementConfirmationStatus(PlacementConfirmationStatus status) {
        return showPlacementConfirmationStatus[status.ordinal()];
    }

    /**
     * @return use opportunity filters?
     */
    public boolean isUseOpportunityFilters() {
        return useOpportunityFilters;
    }
    
    /**
     * Toggles the use opportunity filters setting
     */
    public void toggleUseOpportunityFilters() {
        this.useOpportunityFilters = !this.useOpportunityFilters;
    }
    /**
    * Toggles the show status for the given application status
    * @param status application status enum
    */
    public void toggleShowStatus(InternshipApplicationStatus status) {
        this.showStatus[status.ordinal()] = !this.showStatus[status.ordinal()];
    }
    /**
     * Toggles the show withdraw status for the given withdraw status
     * @param status withdraw status enum
     */
    public void toggleShowWithdrawStatus(WithdrawStatus status) {
        this.showWithdrawStatus[status.ordinal()] = !this.showWithdrawStatus[status.ordinal()];
    }
    /**
     * Toggles the show placement confirmation status for the given placement confirmation status
     * @param status placement confirmation status enum
     */
    public void toggleShowPlacementConfirmationStatus(PlacementConfirmationStatus status) {
        this.showPlacementConfirmationStatus[status.ordinal()] = !this.showPlacementConfirmationStatus[status.ordinal()];
    }

}
