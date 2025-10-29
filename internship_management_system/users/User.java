package internship_management_system.users;

import internship_management_system.FilterSettings;
import internship_management_system.internships.InternshipOpportunity;
import java.util.*;

public abstract class User
{
    private String id;
    private String name;
    private String password;
    private FilterSettings filter_settings = new FilterSettings();

    public User(String id, String name, String password)
    {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public void login()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Password: ");
        String passwordInput = scanner.nextLine();

        if (passwordInput.equals(password))
            System.out.println("Login Successful!");
        else
            System.out.println("Incorrect Password! Login Unsuccessful!");

    }

    public String getId() { return id; }

    public String getName(){ return name; }

    public String getPassword() { return password; }

    public void changePassword(String password) { this.password = password; }

    public abstract List<InternshipOpportunity> saveAndApplyFilterInternship(FilterSettings filtersettings);
}