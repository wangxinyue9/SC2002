package Assignment_1;
import java.util.*;

public abstract class User {
    private String userID;
    private String userPassword;
    private String name;

    private FilterSettings filtersettings = new FilterSettings();

    public User(String name, String userID, String userPassword){
        this.name = name; 
        this.userID = userID;
        this.userPassword = userPassword;
    }

    public void login(){
        // NEED TO RETHINK HOW TO DO THIS IN MAIN APP
        //right now, only checks if the password is correct
        Scanner scanner = new Scanner(System.in);
        System.out.print("password: ");
        String passwordInput;
        passwordInput = scanner.nextLine();
        if (passwordInput == userPassword){
            System.out.println("login successful");
        }
        else{
            System.out.println("incorrect password, login unsuccessful");
        }

    }

    public String getUserID(){
        return userID;
    }

    public String getUserPasswrd(){
        return userPassword;
    }

    public String getName(){
        return name;
    }

    public void changePassword(){
        System.out.println("Old password: ");
        Scanner scanner = new Scanner(System.in);
        String oldPassword = scanner.nextLine();
        if (oldPassword == userPassword){
            System.out.println("New password: ");
            String newPassword = scanner.nextLine();
            userPassword = newPassword;
        }
        else {
            System.out.println("Incorrect password. Failed to change password.");
        }
    }


    public abstract List<InternshipOpportunity> saveAndApplyFilterInternship(FilterSettings filtersettings);
}
