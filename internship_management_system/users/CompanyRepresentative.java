package internship_management_system.users;

import internship_management_system.enums.*;

/**
 * Class representing a company representative user
 */
public class CompanyRepresentative extends User {

    private final String companyName;
    private final String department;
    private final String position;
    private CompanyRepresentativeStatus status;

    /**
     * Constructor for CompanyRepresentative
     *
     * @param id CompanyRepresentative's internal ID, also refers to index in
     * DataStorage
     * @param name CompanyRepresentative's full name
     * @param companyName Name of the company the representative works for
     * @param department Department of the company the representative works for
     * @param position Position of the representative in the company
     */
    public CompanyRepresentative(int id, String name,
            String companyName, String department, String position) {
        super(id, name, "CR" + id);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.status = CompanyRepresentativeStatus.PENDING;

        super.getOpportunityFilterSettings().addCompany(companyName);
    }

    /**
     * @return Company name of the representative
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @return Status of the company representative
     */
    public CompanyRepresentativeStatus getStatus() {
        return status;
    }

    /**
     * Set the status of the company representative
     * @param status New status to be set
     */
    public void setStatus(CompanyRepresentativeStatus status) {
        this.status = status;
    }

    /**
     * @return Department of the company representative
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @return Position of the company representative
     */
    public String getPosition() {
        return position;
    }
}
