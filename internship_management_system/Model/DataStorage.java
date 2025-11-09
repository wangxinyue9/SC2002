package internship_management_system.Model;

import internship_management_system.internships.*;
import internship_management_system.users.*;
import java.util.*;

public class DataStorage
{
    private static final List<Student> students = new ArrayList<>();
    private static final List<CompanyRepresentative> companyReps = new ArrayList<>();
    private static final List<InternshipOpportunity> internships = new ArrayList<>();

    // --- Add Methods ---
    public static void addStudent(Student s) { students.add(s); }
    public static void addCompanyRep(CompanyRepresentative cr) { companyReps.add(cr); }
    public static void addInternship(InternshipOpportunity i) { internships.add(i); }

    // --- Getters ---
    public static List<Student> getStudents() { return students; }
    public static List<CompanyRepresentative> getCompanyReps() { return companyReps; }
    public static List<InternshipOpportunity> getInternships() { return internships; }

    // --- Search Helpers using Streams ---
    public static Optional<InternshipOpportunity> findInternshipById(int id)
    {
        return internships.stream()
                          .filter(i -> i.getId() == id)
                          .findFirst();
    }

    public static Optional<CompanyRepresentative> findCompanyRepById(String id)
    {
        return companyReps.stream()
                          .filter(r -> r.getId().equalsIgnoreCase(id))
                          .findFirst();
    }
}