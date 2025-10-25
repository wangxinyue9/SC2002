package Assignment_1;
import java.util.*;

public class Student extends User {
    
    int yearOfStudy;
    String major;
    List<InternshipApplication> appliedInternships;
    List<InternshipApplication> successfulInternships; //not used right now
    boolean acceptedSomeOffer; 


    public Student(String userID, String password, String name, int yearOfStudy, String major){
        super(name, userID, password);

        this.yearOfStudy = yearOfStudy;
        this.major = major;
        appliedInternships = new ArrayList<>();
        successfulInternships = new ArrayList<>();
        acceptedSomeOffer = false;
        //should probably edit the filtersettings here too
    }

    @Override
    public List<InternshipOpportunity> saveAndApplyFilterInternship(FilterSettings filtersettings) {
        //first apply the filter settings 
        //then display the internships matching the filters 
        //option for student to change the filters 
        //when filters change, it auto saves in filter settings
        return null;
    }

    public void applyForInternship(InternshipOpportunity internship){
        // Prevent duplicate application based on the student's applied list
        for (InternshipApplication app : appliedInternships) {
            if (app.getOpportunity().equals(internship)) {
                throw new Error("Already applied to this internship");
            }
        }

        // Create a new application via the opportunity API
        internship.newApplication(getUserID()); // maybe should use the student object directly

        // Locate the newly created application from the global list
        InternshipApplication created = null;
        List<InternshipApplication> all = InternshipApplication.getApplicationList("");
        if (all != null) {
            for (InternshipApplication app : all) {
                if (app.getStudent().equals(getUserID()) && app.getOpportunity().equals(internship)) {
                    if (created == null || app.getId() > created.getId()) {
                        created = app; // pick the latest matching application
                    }
                }
            }
        }

        if (created == null) {
            throw new Error("Failed to locate newly created application");
        }

        if (appliedInternships == null) {
            appliedInternships = new ArrayList<>();
        }
        appliedInternships.add(created);
    }

    public void viewInternshipStatus(InternshipApplication internship){
        System.out.println("The application status is: " + internship.getStatus());
    }

    public void acceptInternship(InternshipApplication internship){
        internship.confirmPlacement(true);
        toggleAcceptedSomeOffer();
    } 

    public void  requestWithdrawInternship(InternshipApplication internship){
        internship.requestWithdraw();
    }

    public void withdrawRequestToWithdraw (InternshipApplication internship){
        if (internship.getWithdrawStatus() != WithdrawStatus.PENDING) {
            throw new Error("No pending withdraw request to cancel");
        }
        // Use confirmWithdraw(false) to mark the pending request as rejected (i.e., canceled by student)
        internship.confirmWithdraw(false);
    }

    public void removeAppliedInternship(InternshipApplication internshipApplication){
        appliedInternships.remove(internshipApplication);
        //once the withdraw request is approved by the career staff, call this to remove from the applied list. 
    }

    public void toggleAcceptedSomeOffer(){
        acceptedSomeOffer = true;
    }

}

// User

// Rethink login flow and move logic to main app 
// Improve login beyond simple password check 


// Student

// Initialize/configure filter settings for a student in the constructor.
// Implement saveAndApplyFilterInternship to:
// Apply current filters,
// Display matching internships,
// Allow changes to filters,
// Auto-save updated filters
// Consider refactoring application API to use Student object instead of userID when calling newApplication
// Decide how to use or remove successfulInternships since it’s “not used right now.” 
// Ensure external flow (e.g., staff approval) calls removeAppliedInternship after withdraw approval to keep applied list in sync

