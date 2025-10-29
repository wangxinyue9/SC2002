package internship_management_system.Model;

import internship_management_system.users.*;
import java.util.*;

public class DataStorage
{
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<CompanyRepresentative> companyReps = new ArrayList<>();

    // --- Add Methods ---
    public void addStudent(Student s) { students.add(s); }
    public void addTeacher(CompanyRepresentative cr) { companyReps.add(cr); }

    // --- Getters ---
    public ArrayList<Student> getStudents() { return students; }
    public ArrayList<CompanyRepresentative> getCompanyReps() { return companyReps; }
}