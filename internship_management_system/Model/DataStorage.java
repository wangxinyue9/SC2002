package internship_management_system.Model;

import internship_management_system.users.*;
import java.util.*;

public class DataStorage
{
    private static ArrayList<Student> students = new ArrayList<>();
    private static ArrayList<CompanyRepresentative> companyReps = new ArrayList<>();

    // --- Add Methods ---
    public static void addStudent(Student s) { students.add(s); }
    public static void addTeacher(CompanyRepresentative cr) { companyReps.add(cr); }

    // --- Getters ---
    public static ArrayList<Student> getStudents() { return students; }
    public static ArrayList<CompanyRepresentative> getCompanyReps() { return companyReps; }
}