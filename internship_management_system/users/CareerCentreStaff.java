package internship_management_system.users;

/**
 * Class representing a career centre staff user
 */
public class CareerCentreStaff extends User {
    private final String department;
    private final String email;

    public CareerCentreStaff(int id, String userID, String name, String department, String email) {
        super(id, name, userID);
        this.department = department;
        this.email = email;
    }

    /**
     * @return Department of the career centre staff
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @return Email of the career centre staff
     */
    public String getEmail() {
        return email;
    }
}