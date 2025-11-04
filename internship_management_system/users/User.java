package Assignment_1;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import internship_management_system.internships.InternshipOpportunity;
import internship_management_system.enums.InternshipOpportunityStatus;
import internship_management_system.enums.InternshipLevel;


public abstract class User {
    private String userID;
    private String userPassword;
    private String name;

    private FilterSettings filtersettings = new FilterSettings.Builder().sortBy(FilterSettings.SortBy.ALPHA).build();

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
        if (oldPassword.equals(userPassword)){
            System.out.println("New password: ");
            String newPassword = scanner.nextLine();
            userPassword = newPassword;
        }
        else {
            System.out.println("Incorrect password. Failed to change password.");
        }
    }


    public List<InternshipOpportunity> applyFilterSettings(FilterSettings filtersettings){
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

    // Interactive editor entrypoint (role-aware via capability map)
    public void setFilterSettings() {
        this.filtersettings = editFilterSettingsInteractive(this.filtersettings, getFilterEditCapabilities());
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

    protected FilterSettings editFilterSettingsInteractive(FilterSettings current, Map<String, Boolean> caps) {
        Scanner scanner = new Scanner(System.in);
        FilterSettings.Builder b = new FilterSettings.Builder();

        // 1) Opportunity Status
        if (caps.getOrDefault("opportunityStatus", true)) {
            System.out.println("Change status filter?");
            System.out.println("0) No change");
            System.out.println("1) No restriction (clear)");
            InternshipOpportunityStatus[] stVals = InternshipOpportunityStatus.values();
            for (int i = 0; i < stVals.length; i++) { System.out.println((i + 2) + ") " + stVals[i]); }
            System.out.print("Enter choice: ");
            int statusChoice = promptInt(scanner, 0, stVals.length + 1);
            switch (statusChoice) {
                case 0 -> { for (InternshipOpportunityStatus s : current.statuses()) { b.addStatus(s); } }
                case 1 -> { }
                default -> { int sel = statusChoice - 2; if (sel >= 0 && sel < stVals.length) { b.addStatus(stVals[sel]); } }
            }
        } else { for (InternshipOpportunityStatus s : current.statuses()) { b.addStatus(s); } }

        // 1a) Application Statuses (removed)\n
        // 2) Levels
        if (caps.getOrDefault("level", true)) {
            System.out.println("Change level filter?");
            System.out.println("0) No change");
            System.out.println("1) No restriction (clear)");
            InternshipLevel[] lvVals = InternshipLevel.values();
            for (int i = 0; i < lvVals.length; i++) { System.out.println((i + 2) + ") " + lvVals[i]); }
            System.out.print("Enter choice: ");
            int levelChoice = promptInt(scanner, 0, lvVals.length + 1);
            switch (levelChoice) { case 0 -> { for (InternshipLevel l : current.levels()) { b.addLevel(l); } } case 1 -> { } default -> { int sel = levelChoice - 2; if (sel >= 0 && sel < lvVals.length) { b.addLevel(lvVals[sel]); } } }
        } else { for (InternshipLevel l : current.levels()) { b.addLevel(l); } }

        // 3) Majors
        if (caps.getOrDefault("majors", true)) {
            System.out.println("Change majors filter?");
            System.out.println("0) No change");
            System.out.println("1) No restriction (clear)");
            System.out.println("2) Enter majors (comma-separated)");
            System.out.print("Enter choice: ");
            int majorChoice = promptInt(scanner, 0, 2);
            switch (majorChoice) { case 0 -> { for (String m : current.majors()) { b.addMajor(m); } } case 1 -> { } case 2 -> { System.out.print("Majors (comma-separated): "); String line = scanner.nextLine(); if (line != null && !line.isBlank()) { for (String p : line.split(",")) { String mj = p.trim(); if (!mj.isEmpty()) { b.addMajor(mj); } } } } default -> { } }
        } else { for (String m : current.majors()) { b.addMajor(m); } }

        // 4) Visibility
        if (caps.getOrDefault("visibility", true)) {
            System.out.println("Change visibility filter?");
            System.out.println("0) No change");
            System.out.println("1) Only public opportunities");
            System.out.println("2) No restriction");
            System.out.print("Enter choice: ");
            int visChoice = promptInt(scanner, 0, 2);
            switch (visChoice) { case 0 -> b.visibleOnly(current.visibleOnly()); case 1 -> b.visibleOnly(true); case 2 -> b.visibleOnly(null); default -> { } }
        } else { b.visibleOnly(current.visibleOnly()); }

        // 5) Closing dates
        if (caps.getOrDefault("closingDates", true)) {
            System.out.println("Change closingBefore date?");
            System.out.println("0) No change");
            System.out.println("1) Clear");
            System.out.println("2) Set date (yyyy-MM-dd)");
            System.out.print("Enter choice: ");
            int beforeChoice = promptInt(scanner, 0, 2);
            switch (beforeChoice) { case 0 -> { if (current.closingBefore() != null) { b.closingBefore(current.closingBefore()); } } case 1 -> { } case 2 -> { System.out.print("Enter date (yyyy-MM-dd): "); String s = scanner.nextLine().trim(); try { b.closingBefore(LocalDate.parse(s)); } catch (Exception ignored) {} } default -> { } }

            System.out.println("Change closingAfter date?");
            System.out.println("0) No change");
            System.out.println("1) Clear");
            System.out.println("2) Set date (yyyy-MM-dd)");
            System.out.print("Enter choice: ");
            int afterChoice = promptInt(scanner, 0, 2);
            switch (afterChoice) { case 0 -> { if (current.closingAfter() != null) { b.closingAfter(current.closingAfter()); } } case 1 -> { } case 2 -> { System.out.print("Enter date (yyyy-MM-dd): "); String s = scanner.nextLine().trim(); try { b.closingAfter(LocalDate.parse(s)); } catch (Exception ignored) {} } default -> { } }
        } else {
            if (current.closingBefore() != null) { b.closingBefore(current.closingBefore()); }
            if (current.closingAfter() != null) { b.closingAfter(current.closingAfter()); }
        }

        // 6) Sort
        if (caps.getOrDefault("sort", true)) {
            System.out.println("Change sort order?");
            System.out.println("0) No change");
            FilterSettings.SortBy[] sorts = FilterSettings.SortBy.values();
            for (int i = 0; i < sorts.length; i++) { System.out.println((i + 1) + ") " + sorts[i]); }
            System.out.print("Enter choice: ");
            int sortChoice = promptInt(scanner, 0, sorts.length);
            if (sortChoice == 0) { b.sortBy(current.sortBy()); } else { int sel = sortChoice - 1; if (sel >= 0 && sel < sorts.length) { b.sortBy(sorts[sel]); } }
        } else { b.sortBy(current.sortBy()); }

        return b.build();
    }

    private static int promptInt(Scanner scanner, int minInclusive, int maxInclusive) {
        while (true) {
            if (!scanner.hasNextInt()) {
                System.out.print("Please enter a valid number: ");
                scanner.next();
                continue;
            }
            int val = scanner.nextInt();
            scanner.nextLine(); // consume newline
            if (val < minInclusive || val > maxInclusive) {
                System.out.print("Please enter a number between " + minInclusive + " and " + maxInclusive + ": ");
                continue;
            }
            return val;
        }
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
    }
}


