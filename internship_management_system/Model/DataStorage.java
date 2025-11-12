package internship_management_system.Model;

import internship_management_system.enums.InternshipLevel;
import internship_management_system.filter.InternshipApplicationFilterSettings;
import internship_management_system.filter.InternshipOpportunityFilterSettings;
import internship_management_system.internships.*;
import internship_management_system.users.*;

import java.time.LocalDate;
import java.util.*;

public class DataStorage
{
    private static final List<Student> students = new ArrayList<>();
    private static final List<CompanyRepresentative> companyReps = new ArrayList<>();
    private static final List<CareerCentreStaff> careerCenterStaffs = new ArrayList<>(); // Edited 
    private static final List<InternshipOpportunity> internshipOpportunities = new ArrayList<>(); // Edited
    private static final List<InternshipApplication> internshipApplications = new ArrayList<>();

    // --- Add Methods ---
    public static void addStudent(Student s) { students.add(s); }
    public static void addCompanyRep(CompanyRepresentative cr) { companyReps.add(cr); }
    public static void addInternship(InternshipOpportunity i) { internshipOpportunities.add(i); } // Edited

    // Edits made by jarif
    public static Student newStudent(String userID, String name, int yearOfStudy, String major) {
        Student s = new Student(students.size()+1, userID, name, yearOfStudy, major);
        students.add(s);
        return s;
    }
    public static CompanyRepresentative newCompanyRep(String userID, String name, String companyName, String department, String position) {
        CompanyRepresentative c = new CompanyRepresentative(companyReps.size()+1, userID, name, companyName, department, position);
        companyReps.add(c);
        return c;
    }
    public static CareerCentreStaff newCareerCentreStaff(String userID, String name, String role, String department, String email) {
        CareerCentreStaff c =  new CareerCentreStaff(careerCenterStaffs.size()+1, userID, name, role, department, email);
        careerCenterStaffs.add(c);
        return c;
    }
    public static InternshipOpportunity newInternshipOpportunity(String title, String description, String preferredMajor, InternshipLevel level, LocalDate openingDate, LocalDate closingDate, CompanyRepresentative companyRep, int numOfSlots, boolean visibility) {
        InternshipOpportunity i = new InternshipOpportunity(internshipOpportunities.size()+1, title, description, preferredMajor, level, openingDate, closingDate, companyRep, numOfSlots, visibility);
        internshipOpportunities.add(i);
        return i;
    }
    public static InternshipApplication newInternshipApplication(InternshipOpportunity opportunity, Student student) {
        InternshipApplication i = new InternshipApplication(internshipApplications.size()+1, opportunity, student);
        internshipApplications.add(i);
        return i;
    }

    // --- Getters ---
    public static List<Student> getStudents() { return students; }
    public static List<CompanyRepresentative> getCompanyReps() { return companyReps; }
    // public static List<InternshipOpportunity> getInternships() { return internshipOpportunities; } //edited
    public static List<InternshipOpportunity> getAllInternshipOpportunities() { return internshipOpportunities; }
    public static List<InternshipApplication> getAllInternshipApplications() { return internshipApplications; }
    public static List<InternshipOpportunity> getInternshipOpportunities(InternshipOpportunityFilterSettings filter) {
        return internshipOpportunities.stream().filter(o -> o.matchesFilter(filter)).toList();
    }
    public static List<InternshipApplication> getInternshipApplications(InternshipApplicationFilterSettings filter) {
        return internshipApplications.stream().filter(a -> a.matchesFilter(filter)).toList();
    }
    public static List<InternshipApplication> getInternshipApplicationsByStudent(Student student, InternshipApplicationFilterSettings filter) {
        return internshipApplications.stream()
                .filter(a -> a.getStudent().equals(student) && a.matchesFilter(filter))
                .toList();
    }
    public static Optional<InternshipApplication> getInternshipApplicationForAnOpportunity(InternshipOpportunity opportunity, Student student) {
        for(InternshipApplication application: internshipApplications) {
            if(application.getOpportunity().equals(opportunity) && application.getStudent().equals(student)) {
                return Optional.of(application);
            }
        }
        return Optional.empty();
    }
    public static Optional<User> getUserByUserID(String userID) {
        for(Student s: students) if(s.getUserID().equals(userID)) return Optional.of(s);
        for(CompanyRepresentative c: companyReps) if(c.getUserID().equals(userID)) return Optional.of(c);
        for(CareerCentreStaff c: careerCenterStaffs) if(c.getUserID().equals(userID)) return Optional.of(c);
        return Optional.empty();
    } 

    // --- Search Helpers using Streams ---
    public static Optional<InternshipOpportunity> findInternshipById(int id)
    {
        return internshipOpportunities.stream()                 //edited
                          .filter(i -> i.getId() == id)
                          .findFirst();
    }

    public static Optional<CompanyRepresentative> findCompanyRepById(String id)
    {
        return companyReps.stream()
                          .filter(r -> r.getUserID().equalsIgnoreCase(id))
                          .findFirst();
    }
}