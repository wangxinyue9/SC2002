package Assignment_1;
import java.util.*;

public class Student extends User {
    
    int yearOfStudy;
    String major;
    List<InternshipApplication> appliedInternships;
    List<InternshipApplication> successfulInternships;
    boolean acceptedSomeOffer; 


    public Student(String userID, String password, String name, int yearOfStudy, String major){
        super(name, userID, password);

        this.yearOfStudy = yearOfStudy;
        this.major = major;
        appliedInternships = new ArrayList<>();
        successfulInternships = new ArrayList<>();
        acceptedSomeOffer = false;
    }

    @Override
    public List<InternshipOpportunity> saveAndApplyFilterInternship(FilterSettings filtersettings) {
        return null;
    }

    public void applyForIinternship(InternshipOpportunity internship){

    }

    public void viewInternshipStatus(InternshipApplication internship){

    }

    public void acceptInternship(InternshipApplication internship){

    } 

    public void  requestWithdrawInternship(InternshipApplication internship){

    }

    public void withdrawRequestToWithdraw (InternshipApplication internship){

    }

    public void toggleAcceptedSomeOffer(){
        
    }

}


