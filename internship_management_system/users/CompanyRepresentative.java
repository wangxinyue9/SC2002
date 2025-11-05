package internship_management_system.users;

import internship_management_system.*;
import internship_management_system.enums.*;
import internship_management_system.internships.*;

import java.util.*;
import java.time.LocalDate;

public class CompanyRepresentative extends User
{
    private String companyName;
    private String department;
    private String position;
    private boolean approved = false;

    public CompanyRepresentative(String id, String name, String password,
                                 String companyName, String department, String position
                                )
    {
        super(id, name, password);

        this.companyName = companyName;
        this.department = department;
        this.position = position;
    }

    public void register()
    {

    }

    public void createInternship(int id, String title, String description, String major,
                                 InternshipLevel level, LocalDate openDate, LocalDate closeDate,
                                 String companyRep, int slots, boolean visibility
                                )
    {
        InternshipOpportunity internshipOpp = new InternshipOpportunity(id, title, description, major,
                                                                        level, openDate, closeDate,
                                                                        this.companyName, slots, visibility
                                                                       );
    }

    public void approveApplication(int studentId)
    {

    }

    public void rejectApplication(int studentId)
    {

    }

    public void toggleVisibility(int internshipId)
    {

    }


    public String getCompanyName() { return companyName; }

    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getDepartment() { return department; }

    public void setDepartment(String department) { this.department = department; }

    public String getPosition() { return position; }

    public void setPosition(String position) { this.position = position; }

    public boolean isApproved() { return approved; }

    public void setApproved(boolean approved) { this.approved = approved; }
}
