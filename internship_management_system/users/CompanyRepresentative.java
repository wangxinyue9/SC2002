package internship_management_system.users;

import internship_management_system.Model.DataStorage;
import internship_management_system.enums.*;
import internship_management_system.filter.InternshipOpportunityFilterSettings;
import internship_management_system.internships.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CompanyRepresentative extends User
{
    private String companyName;
    private String department;
    private String position;
    // private boolean approved;
    private CompanyRepresentativeStatus status; // Edited

    public CompanyRepresentative(int id, String userID, String name,
                                 String companyName, String department, String position) // Edited
    {
        super(id, name, userID);
        this.companyName = companyName;
        this.department = department;
        this.position = position;
        this.status = CompanyRepresentativeStatus.PENDING;

        super.getOpportunityFilterSettings().addCompany(companyName);
    }

    // --- Registration (Pending Approval by Career Center Staff) ---
    /*public void register()
    {
        DataStorage.addCompanyRep(this);
        System.out.printf("Registration submitted for %s (%s). Awaiting approval.%n", getName(), companyName);
    }

    // --- Internship Creation ---
    public Optional<InternshipOpportunity> createInternship(
            int id, String title, String description, String major,
            InternshipLevel level, LocalDate openDate, LocalDate closeDate,
            int slots, boolean visibility)
    {

        if (!approved)
        {
            System.out.println("You are not yet approved to create internships.");
            return Optional.empty();
        }

        long existing = DataStorage.getInternshipOpportunities("")
                                   .stream()
                                   .filter(i -> i.getCompanyRep().getCompanyName().equals(companyName))
                                   .count();

        if (existing >= 5)
        {
            System.out.println("Limit Reached: You can only create up to 5 internships!");
            return Optional.empty();
        }

        InternshipOpportunity internship = new InternshipOpportunity
                (
                    id, title, description, major, level,
                    openDate, closeDate, this, slots, visibility
                );

        DataStorage.addInternship(internship);
        System.out.println("Internship Created and Pending Career Center Approval.");
        return Optional.of(internship);
    }*/

    // --- Application Decisions ---
    /*public void approveApplication(int internshipId, String studentId)
    {
        DataStorage.findInternshipById(internshipId)
                   .ifPresentOrElse(i ->
                   {
                        i.approveApplicant(studentId);
                        System.out.printf("Student %s approved for internship %d.%n", studentId, internshipId);
                   },
                   () -> System.out.println("Internship not found."));
    }

    public void rejectApplication(int internshipId, String studentId)
    {
        DataStorage.findInternshipById(internshipId)
                .ifPresentOrElse(i ->
                {
                    i.rejectApplicant(studentId);
                    System.out.printf("Student %s rejected for internship %d.%n", studentId, internshipId);
                },
                () -> System.out.println("Internship not found."));
    }

    // --- Visibility Toggle ---
    public void toggleVisibility(int internshipId)
    {
        DataStorage.findInternshipById(internshipId)
                .ifPresentOrElse(i ->
                {
                    i.toggleVisibility();
                    System.out.printf("Internship %d visibility toggled to %s.%n", internshipId, i.getVisibility());
                },
                () -> System.out.println("Internship not found."));
    }

    // --- Filtering ---
    public List<InternshipOpportunity> saveAndApplyFilterInternship(FilterSettings filters)
    {
        // Example: Filter Logic using Streams
        return DataStorage.getInternships()
                          .stream()
                          .filter(i -> i.getCompanyRep().getCompanyName().equals(companyName))
                          .filter(i -> filters.matches(i))
                          .sorted(Comparator.comparing(InternshipOpportunity::getTitle))
                          .collect(Collectors.toList());
    }*/

    // --- Getters and Setters ---
    public String getCompanyName() { return companyName; }
    public CompanyRepresentativeStatus getStatus() { return status; }
    public void setStatus(CompanyRepresentativeStatus status) { this.status = status; }
    public String getDepartment() { return department; }
    public String getPosition() { return position; }
}