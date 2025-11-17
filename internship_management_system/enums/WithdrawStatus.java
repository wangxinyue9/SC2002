package internship_management_system.enums;

/**
 * Enum representing the withdraw status
 */
public enum WithdrawStatus {
    /** Withdraw request has not been made by the student */
    NOT_REQUESTED,
    /** Withdraw request has been made and is pending approval */
    PENDING,
    /** Withdraw request has been approved by career centre staff */
    APPROVED,
    /** Withdraw request has been rejected by career centre staff */
    REJECTED
}

