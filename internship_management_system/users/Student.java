package internship_management_system.users;

import Assignment_1.FilterSettings;
import internship_management_system.internships.InternshipApplication;
import internship_management_system.internships.InternshipOpportunity;
import internship_management_system.enums.WithdrawStatus;
import java.util.*;

public class Student extends User
{
    
    private int yearOfStudy;
    private String major;
    private List<InternshipApplication> appliedInternships;
    private List<InternshipApplication> successfulInternships; //not used right now
    private boolean acceptedSomeOffer;


    public Student(String id, String name, String password, int yearOfStudy, String major)
    {
        super(id, name, password);

        this.yearOfStudy = yearOfStudy;
        this.major = major;
        appliedInternships = new ArrayList<>();
        successfulInternships = new ArrayList<>();
        acceptedSomeOffer = false;        // Initialize student-specific default filter settings
        FilterSettings.Builder fb = new FilterSettings.Builder();
        // Only public opportunities
        fb.visibleOnly(true);
        // Status: APPROVED or FILLED
        fb.addStatus(internship_management_system.enums.InternshipOpportunityStatus.APPROVED);
        fb.addStatus(internship_management_system.enums.InternshipOpportunityStatus.FILLED);
        // Level by year of study
        if (this.yearOfStudy <= 2) {
            fb.addLevel(internship_management_system.enums.InternshipLevel.BASIC);
        } else {
            fb.addLevel(internship_management_system.enums.InternshipLevel.BASIC);
            fb.addLevel(internship_management_system.enums.InternshipLevel.INTERMEDIATE);
            fb.addLevel(internship_management_system.enums.InternshipLevel.ADVANCED);
        }
        // Major: student's own major
        if (this.major != null && !this.major.isEmpty()) {
            fb.addMajor(this.major);
        }
        // Persist on the user
        setFilterSettings(fb.build());
        
    }

    @Override
    public List<InternshipOpportunity> applyFilterSettings(FilterSettings filtersettings)
    {
        return super.applyFilterSettings(filtersettings);
    }

    public void applyForInternship(InternshipOpportunity internship)
    {
        // Prevent duplicate application based on the student's applied list
        for (InternshipApplication app : appliedInternships)
        {
            if (app.getOpportunity().equals(internship))
            {
                throw new Error("Already applied to this internship");
            }
        }

        // Create a new application via the opportunity API
        internship.newApplication(getId()); // maybe should use the student object directly

        // Locate the newly created application from the global list
        InternshipApplication created = null;
        List<InternshipApplication> all = InternshipApplication.getApplicationList("");
        if (all != null)
        {
            for (InternshipApplication app : all)
            {
                if (app.getStudent().equals(getId()) && app.getOpportunity().equals(internship))
                {
                    if (created == null || app.getId() > created.getId())
                    {
                        created = app; // pick the latest matching application
                    }
                }
            }
        }

        if (created == null)
        {
            throw new Error("Failed to locate newly created application");
        }

        if (appliedInternships == null)
        {
            appliedInternships = new ArrayList<>();
        }
        appliedInternships.add(created);
    }

    public void viewInternshipStatus(InternshipApplication internship)
    {
        System.out.println("The application status is: " + internship.getStatus());
    }

    public void acceptInternship(InternshipApplication internship)
    {
        internship.confirmPlacement(true);
        toggleAcceptedSomeOffer();
    } 

    public void requestWithdrawInternship(InternshipApplication internship){
        internship.requestWithdraw();
    }

    public void withdrawRequestToWithdraw (InternshipApplication internship)
    {
        if (internship.getWithdrawStatus() != WithdrawStatus.PENDING)
        {
            throw new Error("No pending withdraw request to cancel");
        }
        // Use confirmWithdraw(false) to mark the pending request as rejected (i.e., canceled by student)
        internship.confirmWithdraw(false);
    }

    public void removeAppliedInternship(InternshipApplication internshipApplication)
    {
        appliedInternships.remove(internshipApplication);
        //once the withdraw request is approved by the career staff, call this to remove from the applied list. 
    }

    public void toggleAcceptedSomeOffer(){
        acceptedSomeOffer = true;
    }
    @Override
    protected java.util.Map<String, Boolean> getFilterEditCapabilities() {
        java.util.Map<String, Boolean> caps = new java.util.HashMap<>();
        caps.put("opportunityStatus", true);
        caps.put("level", false);
        caps.put("majors", false);
        caps.put("visibility", false);
        caps.put("closingDates", true);
        caps.put("sort", true);
        return caps;
    }
}

// User

// Rethink login flow and move logic to main app 
// Improve login beyond simple password check 


// Student

// Initialize/configure filter settings for a student in the constructor.
// Implement applyFilterSettings to:
// Apply current filters,
// Display matching internships,
// Allow changes to filters,
// Auto-save updated filters
// Consider refactoring application API to use Student object instead of userID when calling newApplication
// Decide how to use or remove successfulInternships since it鈥檚 鈥渘ot used right now.鈥?
// Ensure external flow (e.g., staff approval) calls removeAppliedInternship after withdraw approval to keep applied list in sync





