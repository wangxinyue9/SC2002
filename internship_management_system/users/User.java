package internship_management_system.users;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import internship_management_system.internships.InternshipOpportunity;
import internship_management_system.enums.InternshipOpportunityStatus;
import internship_management_system.filter.InternshipOpportunityFilterSettings;
import internship_management_system.enums.InternshipLevel;
import internship_management_system.*;
import internship_management_system.filter.InternshipApplicationFilterSettings;
import internship_management_system.internships.InternshipApplication;


public abstract class User {
    private int id; // Edited 
    private String userID;
    private String userPassword;
    private String name;
    private final InternshipOpportunityFilterSettings opportunityFilterSettings; // Edited
    private final InternshipApplicationFilterSettings applicationFilterSettings;

    // private FilterSettings filtersettings = new FilterSettings.Builder().sortBy(FilterSettings.SortBy.ALPHA).build();


    public User(int id, String name, String userID){ // Edited
        this.id = id; //
        this.name = name; 
        this.userID = userID;
        this.userPassword = "password";
        this.opportunityFilterSettings = new InternshipOpportunityFilterSettings();
        this.applicationFilterSettings = new InternshipApplicationFilterSettings();
        this.applicationFilterSettings.setOpportunityFilters(Optional.of(this.opportunityFilterSettings));
    }

    /*public void login(){
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

    }*/

    public String getUserID(){
        return userID;
    }

    public String getUserPassword(){
        return userPassword;
    }

    public String getName(){
        return name;
    }

    public void changePassword(String password) {
        this.userPassword = password;
    }
    public InternshipOpportunityFilterSettings getOpportunityFilterSettings() {
        return this.opportunityFilterSettings;
    }
    public InternshipApplicationFilterSettings getApplicationFilterSettings() {
        return this.applicationFilterSettings;
    }

    /*public void changePassword(){
        System.out.println("Old password: ");
        Scanner scanner = new Scanner(System.in);
        String oldPassword = scanner.nextLine();
        if (oldPassword.equals(userPassword)){
            System.out.println("New password: ");
            String newPassword = scanner.nextLine();
            userPassword = newPassword;
        }
        else {
            System.out.println("Incorrect password. Failed to change password.");
        }
    }*/


    /*public List<InternshipOpportunity> applyFilterSettings(FilterSettings filtersettings){
        // get all opportunities
        List<InternshipOpportunity> all = internship_management_system.internships.InternshipOpportunity.getOpportunitiesList("");
        if (all == null) all = new ArrayList<>();

        // read filters
        Set<InternshipOpportunityStatus> statuses = filtersettings.statuses();
        Set<InternshipLevel> levels = filtersettings.levels();
        Set<String> majors = filtersettings.majors();
        LocalDate closingBefore = filtersettings.closingBefore();
        LocalDate closingAfter = filtersettings.closingAfter();
        Boolean visibleOnly = filtersettings.visibleOnly();

        // apply filters
        List<InternshipOpportunity> filtered = new ArrayList<>();
        for (InternshipOpportunity opportunity : all) {
            boolean matches = true;
            if (matches && statuses != null && !statuses.isEmpty()) {
                matches = statuses.contains(opportunity.getStatus());
            }
            if (matches && levels != null && !levels.isEmpty()) {
                matches = levels.contains(opportunity.getInternshipLevel());
            }
            if (matches && majors != null && !majors.isEmpty()) {
                String pref = opportunity.getPreferredMajor();
                pref = pref == null ? null : pref.toUpperCase();
                matches = majors.contains(pref);
            }
            if (matches && Boolean.TRUE.equals(visibleOnly)) {
                matches = opportunity.getVisibility();
            }
            if (matches && closingBefore != null && opportunity.getClosingDate() != null) {
                matches = opportunity.getClosingDate().isBefore(closingBefore);
            }
            if (matches && closingAfter != null && opportunity.getClosingDate() != null) {
                matches = opportunity.getClosingDate().isAfter(closingAfter);
            }
            if (matches) {
                filtered.add(opportunity);
            }
        }

        // sort
        Comparator<InternshipOpportunity> byTitle = Comparator.comparing(InternshipOpportunity::getTitle, Comparator.nullsLast(String::compareToIgnoreCase));
        Comparator<InternshipOpportunity> byOpen = Comparator.comparing(InternshipOpportunity::getOpeningDate, Comparator.nullsLast(LocalDate::compareTo));
        Comparator<InternshipOpportunity> byClose = Comparator.comparing(InternshipOpportunity::getClosingDate, Comparator.nullsLast(LocalDate::compareTo));
        Comparator<InternshipOpportunity> cmp;
        switch (filtersettings.sortBy()) {
            case OPEN_DATE_ASC: {
                cmp = byOpen.thenComparing(byTitle);
                break;
            }
            case OPEN_DATE_DESC: {
                cmp = byOpen.reversed().thenComparing(byTitle);
                break;
            }
            case CLOSE_DATE_ASC: {
                cmp = byClose.thenComparing(byTitle);
                break;
            }
            case CLOSE_DATE_DESC: {
                cmp = byClose.reversed().thenComparing(byTitle);
                break;
            }
            case ALPHA:
            default: {
                cmp = byTitle;
                break;
            }
        }
        filtered.sort(cmp);//prompt user for change in filter


        return filtered;
    }

    public void setFilterSettings(FilterSettings newSettings) {
        if (newSettings == null) {
            this.filtersettings = new FilterSettings.Builder()
                    .sortBy(FilterSettings.SortBy.ALPHA)
                    .build();
        } else {
            this.filtersettings = newSettings;
        }
    }

    public FilterSettings getFilterSettings() {
        return this.filtersettings;
    }

    public List<InternshipOpportunity> applyFilterSettings() {
        return applyFilterSettings(this.filtersettings);
    }


    // Inputs for opportunity statuses:
    // - null: keep current statuses unchanged
    // - empty set: clear filter (no restriction; show all statuses)
    // - non-empty set: filter to exactly these statuses
    public void setOpportunityStatuses(java.util.Set<internship_management_system.enums.InternshipOpportunityStatus> newStatuses) {
        if (!getFilterEditCapabilities().getOrDefault("opportunityStatus", true)) {
            throw new IllegalStateException("Editing opportunity status is not permitted for this user");
        }
        FilterSettings current = this.filtersettings;
        FilterSettings.Builder b = new FilterSettings.Builder();
        java.util.Set<internship_management_system.enums.InternshipOpportunityStatus> finalStatuses = (newStatuses != null) ? newStatuses : current.statuses();
        if (finalStatuses != null) {
            for (internship_management_system.enums.InternshipOpportunityStatus s : finalStatuses) { b.addStatus(s); }
        }
        if (current.levels() != null) { for (internship_management_system.enums.InternshipLevel l : current.levels()) { b.addLevel(l); } }
        if (current.majors() != null) { for (String m : current.majors()) { b.addMajor(m); } }
        if (current.closingBefore() != null) { b.closingBefore(current.closingBefore()); }
        if (current.closingAfter() != null) { b.closingAfter(current.closingAfter()); }
        b.visibleOnly(current.visibleOnly());
        b.sortBy(current.sortBy());
        this.filtersettings = b.build();
    }

    // Inputs for levels:
    // - null: keep current levels unchanged
    // - empty set: clear filter (no restriction)
    // - non-empty set: filter to exactly these levels
    public void setLevels(java.util.Set<internship_management_system.enums.InternshipLevel> newLevels) {
        if (!getFilterEditCapabilities().getOrDefault("level", true)) {
            throw new IllegalStateException("Editing level is not permitted for this user");
        }
        FilterSettings current = this.filtersettings;
        FilterSettings.Builder b = new FilterSettings.Builder();
        if (current.statuses() != null) { for (internship_management_system.enums.InternshipOpportunityStatus s : current.statuses()) { b.addStatus(s); } }
        java.util.Set<internship_management_system.enums.InternshipLevel> finalLevels = (newLevels != null) ? newLevels : current.levels();
        if (finalLevels != null) { for (internship_management_system.enums.InternshipLevel l : finalLevels) { b.addLevel(l); } }
        if (current.majors() != null) { for (String m : current.majors()) { b.addMajor(m); } }
        if (current.closingBefore() != null) { b.closingBefore(current.closingBefore()); }
        if (current.closingAfter() != null) { b.closingAfter(current.closingAfter()); }
        b.visibleOnly(current.visibleOnly());
        b.sortBy(current.sortBy());
        this.filtersettings = b.build();
    }

    // Inputs for majors:
    // - null: keep current majors unchanged
    // - empty set: clear filter (no restriction)
    // - non-empty set: filter to exactly these majors (case-insensitive; normalized by builder)
    public void setMajors(java.util.Set<String> newMajors) {
        if (!getFilterEditCapabilities().getOrDefault("majors", true)) {
            throw new IllegalStateException("Editing majors is not permitted for this user");
        }
        FilterSettings current = this.filtersettings;
        FilterSettings.Builder b = new FilterSettings.Builder();
        if (current.statuses() != null) { for (internship_management_system.enums.InternshipOpportunityStatus s : current.statuses()) { b.addStatus(s); } }
        if (current.levels() != null) { for (internship_management_system.enums.InternshipLevel l : current.levels()) { b.addLevel(l); } }
        java.util.Set<String> finalMajors = (newMajors != null) ? newMajors : current.majors();
        if (finalMajors != null) { for (String m : finalMajors) { b.addMajor(m); } }
        if (current.closingBefore() != null) { b.closingBefore(current.closingBefore()); }
        if (current.closingAfter() != null) { b.closingAfter(current.closingAfter()); }
        b.visibleOnly(current.visibleOnly());
        b.sortBy(current.sortBy());
        this.filtersettings = b.build();
    }

    // Inputs for visibility (tri-state):
    // - null: keep current visibility unchanged
    // - true: only public/visible opportunities
    // - false: allow all (no visibility restriction)
    public void setVisibility(java.lang.Boolean visibleOnly) {
        if (!getFilterEditCapabilities().getOrDefault("visibility", true)) {
            throw new IllegalStateException("Editing visibility is not permitted for this user");
        }
        FilterSettings current = this.filtersettings;
        FilterSettings.Builder b = new FilterSettings.Builder();
        if (current.statuses() != null) { for (internship_management_system.enums.InternshipOpportunityStatus s : current.statuses()) { b.addStatus(s); } }
        if (current.levels() != null) { for (internship_management_system.enums.InternshipLevel l : current.levels()) { b.addLevel(l); } }
        if (current.majors() != null) { for (String m : current.majors()) { b.addMajor(m); } }
        if (current.closingBefore() != null) { b.closingBefore(current.closingBefore()); }
        if (current.closingAfter() != null) { b.closingAfter(current.closingAfter()); }
        b.visibleOnly(visibleOnly);
        b.sortBy(current.sortBy());
        this.filtersettings = b.build();
    }

    // Inputs for closingBefore:
    // - null: clear closingBefore (no upper bound)
    // - LocalDate: set closingBefore to the provided date
    public void setClosingBefore(java.time.LocalDate before) {
        if (!getFilterEditCapabilities().getOrDefault("closingDates", true)) {
            throw new IllegalStateException("Editing closing dates is not permitted for this user");
        }
        FilterSettings current = this.filtersettings;
        FilterSettings.Builder b = new FilterSettings.Builder();
        if (current.statuses() != null) { for (internship_management_system.enums.InternshipOpportunityStatus s : current.statuses()) { b.addStatus(s); } }
        if (current.levels() != null) { for (internship_management_system.enums.InternshipLevel l : current.levels()) { b.addLevel(l); } }
        if (current.majors() != null) { for (String m : current.majors()) { b.addMajor(m); } }
        if (before != null) { b.closingBefore(before); }
        if (current.closingAfter() != null) { b.closingAfter(current.closingAfter()); }
        b.visibleOnly(current.visibleOnly());
        b.sortBy(current.sortBy());
        this.filtersettings = b.build();
    }

    // Inputs for closingAfter:
    // - null: clear closingAfter (no lower bound)
    // - LocalDate: set closingAfter to the provided date
    public void setClosingAfter(java.time.LocalDate after) {
        if (!getFilterEditCapabilities().getOrDefault("closingDates", true)) {
            throw new IllegalStateException("Editing closing dates is not permitted for this user");
        }
        FilterSettings current = this.filtersettings;
        FilterSettings.Builder b = new FilterSettings.Builder();
        if (current.statuses() != null) { for (internship_management_system.enums.InternshipOpportunityStatus s : current.statuses()) { b.addStatus(s); } }
        if (current.levels() != null) { for (internship_management_system.enums.InternshipLevel l : current.levels()) { b.addLevel(l); } }
        if (current.majors() != null) { for (String m : current.majors()) { b.addMajor(m); } }
        if (current.closingBefore() != null) { b.closingBefore(current.closingBefore()); }
        if (after != null) { b.closingAfter(after); }
        b.visibleOnly(current.visibleOnly());
        b.sortBy(current.sortBy());
        this.filtersettings = b.build();
    }

    // Inputs for sortBy:
    // - null: keep current sort order
    // - SortBy: set the sorting strategy
    public void setSortBy(FilterSettings.SortBy sortBy) {
        if (!getFilterEditCapabilities().getOrDefault("sort", true)) {
            throw new IllegalStateException("Editing sort is not permitted for this user");
        }
        FilterSettings current = this.filtersettings;
        FilterSettings.Builder b = new FilterSettings.Builder();
        if (current.statuses() != null) { for (internship_management_system.enums.InternshipOpportunityStatus s : current.statuses()) { b.addStatus(s); } }
        if (current.levels() != null) { for (internship_management_system.enums.InternshipLevel l : current.levels()) { b.addLevel(l); } }
        if (current.majors() != null) { for (String m : current.majors()) { b.addMajor(m); } }
        if (current.closingBefore() != null) { b.closingBefore(current.closingBefore()); }
        if (current.closingAfter() != null) { b.closingAfter(current.closingAfter()); }
        b.visibleOnly(current.visibleOnly());
        if (sortBy != null) { b.sortBy(sortBy); } else { b.sortBy(current.sortBy()); }
        this.filtersettings = b.build();
    }

    // Default capabilities: all editable. Subclasses override to restrict.
    protected Map<String, Boolean> getFilterEditCapabilities() {
        Map<String, Boolean> caps = new HashMap<>();
        caps.put("opportunityStatus", true);
                caps.put("level", true);
        caps.put("majors", true);
        caps.put("visibility", true);
        caps.put("closingDates", true);
        caps.put("sort", true);
        return caps;
    }

    protected void printOpportunitiesTable(List<InternshipOpportunity> opportunities) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fmt = "%-4s | %-25s | %-12s | %-8s | %-10s | %-10s | %-9s | %-7s | %-5s\n";
        System.out.printf(fmt, "ID", "Title", "Level", "Major", "Open", "Close", "Status", "Public", "Left");
        System.out.println("----+---------------------------+--------------+----------+------------+------------+-----------+---------+------");
        for (InternshipOpportunity opportunity : opportunities) {
            String open = (opportunity.getOpeningDate() == null) ? "" : df.format(opportunity.getOpeningDate());
            String close = (opportunity.getClosingDate() == null) ? "" : df.format(opportunity.getClosingDate());
            System.out.printf(
                    fmt,
                    opportunity.getId(),
                    String.valueOf(opportunity.getTitle()),
                    String.valueOf(opportunity.getInternshipLevel()),
                    String.valueOf(opportunity.getPreferredMajor()),
                    open,
                    close,
                    String.valueOf(opportunity.getStatus()),
                    opportunity.getVisibility() ? "Yes" : "No",
                    opportunity.getNumOfRemainingSlots()
            );
        }
    }*/
}
