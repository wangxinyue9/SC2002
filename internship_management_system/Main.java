package internship_management_system;

import internship_management_system.Model.DataStorage;
import internship_management_system.enums.InternshipApplicationStatus;
import internship_management_system.enums.InternshipLevel;
import internship_management_system.enums.InternshipOpportunityStatus;
import internship_management_system.enums.PlacementConfirmationStatus;
import internship_management_system.enums.WithdrawStatus;
import internship_management_system.enums.CompanyRepresentativeStatus;
import internship_management_system.filter.InternshipApplicationFilterSettings;
import internship_management_system.filter.InternshipOpportunityFilterSettings;
import internship_management_system.internships.InternshipApplication;
import internship_management_system.internships.InternshipOpportunity;
import internship_management_system.users.CareerCentreStaff;
import internship_management_system.users.CompanyRepresentative;
import internship_management_system.users.Student;
import internship_management_system.users.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {

    static Scanner sc;
    static Optional<User> currentUser;
    static Optional<InternshipOpportunity> currentOpportunity;
    static Optional<InternshipApplication> currentApplication;

    static interface Screen {

        void screen();
    };
    static Screen nextScreen;

    public static void main(String[] args) {
        sc = new Scanner(System.in);
        currentUser = Optional.empty();
        currentOpportunity = Optional.empty();
        currentApplication = Optional.empty();

        if (args.length != 3) {
            System.err.println("Usage: ims <student-file.csv> <company-rep-file.csv> career-staff-file.csv>");
            System.exit(1);
        }
        loadDataFromCSV(args[0], args[1], args[2]);

        nextScreen = Main::loginScreen;
        while (true) {
            nextScreen.screen();
        }
    }

    static void clearScreen() {
        try {
            if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (final IOException | InterruptedException e) {
            System.err.println(e);
            exit();
        }
    }

    static void exit() {
        sc.close();
        System.exit(0);
    }

    static void loadDataFromCSV(String studentFile, String companyRepFile, String careerStaffFile) {
        try (Scanner sc2 = new Scanner(new File(studentFile))) {
            boolean firstLine = true;
            while (sc2.hasNextLine()) {
                String line = sc2.nextLine();
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] lineData = line.split(",");
                DataStorage.newStudent(lineData[0], lineData[1], Integer.parseInt(lineData[3]), lineData[2]); // StudentID,Name,Major,Year,Email
                // FIXME: student email is unused
            }
            sc2.close();
        } catch (FileNotFoundException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.printf("Failed to read student data from file \"%s\"\n", studentFile);
            System.exit(1);
        }

        try (Scanner sc2 = new Scanner(new File(companyRepFile))) {
            boolean firstLine = true;
            while (sc2.hasNextLine()) {
                String line = sc2.nextLine();
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] lineData = line.split(",");
                DataStorage.newCompanyRep(lineData[0], lineData[1], lineData[2], lineData[3], lineData[4]); // CompanyRepID,Name,CompanyName,Department,Position,Email,Status
                // FIXME: status is unused
            }
            sc2.close();
        } catch (FileNotFoundException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.printf("Failed to read company representative data from file \"%s\"\n", companyRepFile);
            System.exit(1);
        }

        try (Scanner sc2 = new Scanner(new File(careerStaffFile))) {
            boolean firstLine = true;
            while (sc2.hasNextLine()) {
                String line = sc2.nextLine();
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] lineData = line.split(",");
                DataStorage.newCareerCentreStaff(lineData[0], lineData[1], lineData[2], lineData[3], lineData[4]); // StaffID,Name,Role,Department,Email
            }
            sc2.close();
        } catch (FileNotFoundException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.printf("Failed to read career center staff data from file \"%s\"\n", careerStaffFile);
            System.exit(1);
        }
    }

    static void printTitle(String currentPage) {
        System.out.printf("Internship Management System - %s\n", currentPage);
        if (currentUser.isPresent()) {
            System.out.printf("Logged in as %s (%s)\n", currentUser.get().getName(), currentUser.get().getUserID());
        }
        System.out.println("");
    }

    static void loginScreen() {
        clearScreen();
        printTitle("Login / Register");

        if (currentUser.isPresent()) {
            throw new Error("Someone already logged in");
        }
        System.out.println("1. Log in");
        System.out.println("2. Register as a Company Representative");
        System.out.println("e. Exit");
        System.out.print("Please choose an option: ");
        String option = sc.nextLine().trim().toLowerCase();

        switch (option) {
            case "1" -> loginExistingUser();
            case "2" -> nextScreen = Main::companyRepRegisterScreen;
            case "e" -> exit();
            default -> nextScreen = Main::loginScreen;
        }
    }

    static void loginExistingUser() {
        clearScreen();
        printTitle("Login");

        System.out.print("Username: ");
        String username = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine();
        Optional<User> u = DataStorage.getUserByUserID(username);
        if (!u.isPresent() || !u.get().getUserPassword().equals(password)) {
            System.out.println("Invalid username or password");
            System.out.print("Press Enter to try again...");
            sc.nextLine();
            nextScreen = Main::loginScreen;
            return;

        }
        currentUser = u;
        if (u.get() instanceof Student) {
            nextScreen = Main::studentHomeScreen; 
        }else if (u.get() instanceof CompanyRepresentative) {
            nextScreen = Main::companyRepHomeScreen; 
        }else {
            nextScreen = Main::careerCenterStaffHomeScreen;
        }
    }

    static void companyRepRegisterScreen() {
        clearScreen();
        printTitle("Register as Company Representative");

        System.out.print("Choose a user ID: ");
        String userID = sc.nextLine().trim();
        if (userID.isEmpty()) {
            System.out.println("User ID cannot be empty.");
            System.out.print("Press Enter to try again...");
            sc.nextLine();
            nextScreen = Main::companyRepRegisterScreen;
            return;
        }
        if (DataStorage.getUserByUserID(userID).isPresent()) {
            System.out.printf("User ID \"%s\" is already taken.%n", userID);
            System.out.print("Press Enter to try again...");
            sc.nextLine();
            nextScreen = Main::companyRepRegisterScreen;
            return;
        }

        System.out.print("Full name: ");
        String name = sc.nextLine().trim();
        System.out.print("Company name: ");
        String companyName = sc.nextLine().trim();
        System.out.print("Department: ");
        String department = sc.nextLine().trim();
        System.out.print("Position: ");
        String position = sc.nextLine().trim();
        if (name.isEmpty() || companyName.isEmpty() || department.isEmpty() || position.isEmpty()) {
            System.out.println("All fields are required.");
            System.out.print("Press Enter to try again...");
            sc.nextLine();
            nextScreen = Main::companyRepRegisterScreen;
            return;
        }

        System.out.print("Create a password (min 6 characters): ");
        String password = sc.nextLine();
        if (password.length() < 6) {
            System.out.println("Password must be at least 6 characters long.");
            System.out.print("Press Enter to try again...");
            sc.nextLine();
            nextScreen = Main::companyRepRegisterScreen;
            return;
        }

        CompanyRepresentative newRep = DataStorage.newCompanyRep(userID, name, companyName, department, position);
        newRep.changePassword(password);
        newRep.setStatus(CompanyRepresentativeStatus.PENDING);

        System.out.println("\nRegistration submitted. A Career Centre Staff must approve your account before you can log in.");
        System.out.print("Press Enter to return to login...");
        sc.nextLine();
        nextScreen = Main::loginScreen;
    }

    static void changePasswordScreen() {
        clearScreen();
        printTitle("Change Password");

        System.out.print("Old password: ");
        String oldPassword = sc.nextLine();
        System.out.print("New password: ");
        String newPassword = sc.nextLine();

        boolean done = false;
        if (!currentUser.get().getUserPassword().equals(oldPassword)) {
            System.out.println("Old password is incorrect");
        } else if (newPassword.length() < 6) {
            System.out.println("Password must be at least 6 characters long");
        } else {
            currentUser.get().changePassword(newPassword);
            done = true;
        }

        if (!done) {
            System.out.print("Try again? [Y/n] ");
            String nxt = sc.nextLine().trim();
            if (nxt.isEmpty() || nxt.toLowerCase().equals("y")) {
                nextScreen = Main::changePasswordScreen;
                return;
            }
        }

        if (currentUser.get() instanceof Student) {
            nextScreen = Main::studentHomeScreen; 
        }else if (currentUser.get() instanceof CompanyRepresentative) {
            nextScreen = Main::companyRepHomeScreen; 
        }else {
            nextScreen = Main::careerCenterStaffHomeScreen;
        }
    }

    static void studentHomeScreen() {
        if (!(currentUser.get() instanceof Student)) {
            throw new Error("Current user is not a student");
        }
        clearScreen();
        printTitle("Student Home");

        String operations[] = {
            "Change password",
            "Edit internship opportunity filter settings",
            "Edit internship application filter settings",
            "Show all internship opportunities",
            "Show my internship applications",};
        for (int i = 0; i < operations.length; i++) {
            System.out.printf("%d. %s\n", i + 1, operations[i]);
        }
        System.out.println("0. Logout");
        System.out.println("e. Exit");

        System.out.print("Please choose an operation: ");
        String op = sc.nextLine().trim();

        switch (op) {
            case "1" ->
                nextScreen = Main::changePasswordScreen;
            case "0" -> {
                currentUser = Optional.empty();
                nextScreen = Main::loginScreen;
            }
            case "2" -> {
                nextScreen = Main::editOpportunityFilterSettingsScreen;
            }
            case "3" -> {
                nextScreen = Main::editApplicationFilterSettingsScreen;
            }
            case "4" -> {
                nextScreen = Main::internshipOpportunitiesScreen;
            }
            case "e" ->
                exit();
            default -> {
                nextScreen = Main::studentHomeScreen;
            }
        }
    }

    static void companyRepHomeScreen() {
        if (!(currentUser.get() instanceof CompanyRepresentative)) {
            throw new Error("Current user is not a company representative");
        }
        clearScreen();
        printTitle("Company Representative Home");

        if (((CompanyRepresentative) currentUser.get()).getStatus() != CompanyRepresentativeStatus.APPROVED) {
            if (((CompanyRepresentative) currentUser.get()).getStatus() == CompanyRepresentativeStatus.REJECTED) {
                System.out.println("Your application to be a Company Representative has been rejected by the Career Center Staff.");
            } else {
                System.out.println("Your account is still pending approval by the Career Center Staff.");
            }
            System.out.println("Please contact the Career Center for more information.");
            System.out.println("Press Enter to return to login screen...");
            sc.nextLine();
            currentUser = Optional.empty();
            nextScreen = Main::loginScreen;
            return;
        }

        String operations[] = {
            "Change password",
            "Edit internship opportunity filter settings",
            "Edit internship application filter settings",
            "Manage internship opportunities",};
        for (int i = 0; i < operations.length; i++) {
            System.out.printf("%d. %s\n", i + 1, operations[i]);
        }
        System.out.println("0. Logout");
        System.out.println("e. Exit");

        System.out.print("Please choose an operation: ");
        String op = sc.nextLine().trim();

        switch (op) {
            case "1" ->
                changePasswordScreen();
            case "2" ->
                nextScreen = Main::editOpportunityFilterSettingsScreen;
            case "3" ->
                nextScreen = Main::editApplicationFilterSettingsScreen;
            case "4" ->
                nextScreen = Main::internshipOpportunitiesScreen;
            case "0" -> {
                currentUser = Optional.empty();
                nextScreen = Main::loginScreen;
            }
            case "e" ->
                exit();
            default -> {
                nextScreen = Main::companyRepHomeScreen;
            }
        }
    }

    static void careerCenterStaffHomeScreen() {
        if (!(currentUser.get() instanceof internship_management_system.users.CareerCentreStaff)) {
            throw new Error("Current user is not a career centre staff");
        }
        clearScreen();
        printTitle("Career Centre Staff Home");

        String operations[] = {
            "Change password",
            "Edit internship opportunity filter settings",
            "Edit internship application filter settings",
            "Manage company representative applications",
            "Manage internship opportunities",
        };
        for (int i = 0; i < operations.length; i++) {
            System.out.printf("%d. %s\n", i + 1, operations[i]);
        }
        System.out.println("0. Logout");
        System.out.println("e. Exit");

        System.out.print("Please choose an operation: ");
        String op = sc.nextLine().trim();

        switch (op) {
            case "1" ->
                nextScreen = Main::changePasswordScreen;
            case "2" ->
                nextScreen = Main::editOpportunityFilterSettingsScreen;
            case "3" ->
                nextScreen = Main::editApplicationFilterSettingsScreen;
            case "4" ->
                nextScreen = Main::CCSManageCRApplicationsScreen;
            case "5" ->
                nextScreen = Main::internshipOpportunitiesScreen;
            case "0" -> {
                currentUser = Optional.empty();
                nextScreen = Main::loginScreen;
            }
            case "e" ->
                exit();
            default -> {
                nextScreen = Main::careerCenterStaffHomeScreen;
            }
        }
    }

    static void editOpportunityFilterSettingsScreen() {
        if (!currentUser.isPresent()) {
            throw new Error("No user is logged in");
        }
        clearScreen();
        printTitle("Edit Internship Opportunity Filter Settings");

        ArrayList<String> operations = new ArrayList<>();
        ArrayList<String> currentValue = new ArrayList<>();
        ArrayList<Screen> operationFunctions = new ArrayList<>();

        User user = currentUser.get();
        InternshipOpportunityFilterSettings settings = user.getOpportunityFilterSettings();

        if (!(user instanceof Student)) {
            operations.add("Preferred Majors");
            if (settings.getPreferredMajors().isPresent()) {
                currentValue.add("[" + String.join(", ", settings.getPreferredMajors().get()) + "]");
            } else {
                currentValue.add("ALL");
            }
            operationFunctions.add(Main::editOFSPreferredMajorsScreen);
        }
        if (!(user instanceof Student) || ((Student) user).getYearOfStudy() >= 2) {
            operations.add("Level");
            ArrayList<String> levels = new ArrayList<>();
            if (settings.isShowLevel(InternshipLevel.BASIC)) {
                levels.add("Basic");
            }
            if (settings.isShowLevel(InternshipLevel.INTERMEDIATE)) {
                levels.add("Intermediate");
            }
            if (settings.isShowLevel(InternshipLevel.ADVANCED)) {
                levels.add("Advanced");
            }
            if (levels.size() == 3) {
                currentValue.add("ALL");
            } else {
                currentValue.add("[" + String.join(", ", levels) + "]");
            }
            operationFunctions.add(Main::editOFSLevel);
        }

        operations.add("Status");
        ArrayList<String> statuses = new ArrayList<>();
        if (settings.isShowStatus(InternshipOpportunityStatus.PENDING)) {
            statuses.add("Pending");
        }
        if (settings.isShowStatus(InternshipOpportunityStatus.APPROVED)) {
            if (user instanceof Student) {
                statuses.add("Unfilled"); 
            }else {
                statuses.add("Approved");
            }
        }
        if (settings.isShowStatus(InternshipOpportunityStatus.REJECTED)) {
            statuses.add("Rejected");
        }
        if (settings.isShowStatus(InternshipOpportunityStatus.FILLED)) {
            statuses.add("Filled");
        }
        if (statuses.size() == (user instanceof Student ? 2 : 4)) {
            currentValue.add("ALL");
        } else {
            currentValue.add("[" + String.join(", ", statuses) + "]");
        }
        operationFunctions.add(Main::editOFSStatus);

        if (!(user instanceof Student)) {
            operations.add("Visibility");
            ArrayList<String> visibilities = new ArrayList<>();
            if (settings.isShowVisible()) {
                visibilities.add("Visible");
            }
            if (settings.isShowInvisible()) {
                visibilities.add("Invisible");
            }
            if (visibilities.size() == 2) {
                currentValue.add("ALL");
            } else {
                currentValue.add("[" + String.join(", ", visibilities) + "]");
            }
            operationFunctions.add(Main::editOFSVisibility);
        }

        if (!(user instanceof CompanyRepresentative)) {
            operations.add("Companies");
            if (settings.getCompanies().isPresent()) {
                currentValue.add("[" + String.join(", ", settings.getCompanies().get()) + "]");
            } else {
                currentValue.add("ALL");
            }
            operationFunctions.add(Main::editOFSCompanies);
        }

        if (!(user instanceof Student)) {
            operations.add("Opening After");
            if (settings.getOpensAfter().isPresent()) {
                currentValue.add(settings.getOpensAfter().get().toString());
            } else {
                currentValue.add("NA");
            }
            operationFunctions.add(Main::editOFSOpeningAfter);
        }
        operations.add("Closing Before");
        if (settings.getClosesBefore().isPresent()) {
            currentValue.add(settings.getClosesBefore().get().toString());
        } else {
            currentValue.add("NA");
        }
        operationFunctions.add(Main::editOFSClosingBefore);

        for (int i = 0; i < operations.size(); i++) {
            System.out.printf("%d. %s: %s\n", i + 1, operations.get(i), currentValue.get(i));
        }
        System.out.println("0. Go back");
        System.out.println("e. Exit");
        System.out.print("Please choose an operation: ");
        String op = sc.nextLine().trim();
        if (op.equals("e")) {
            exit();
        }
        try {
            int opi = Integer.parseInt(op);
            if (opi == 0) {
                if (user instanceof Student) {
                    nextScreen = Main::studentHomeScreen; 
                }else if (user instanceof CompanyRepresentative) {
                    nextScreen = Main::companyRepHomeScreen; 
                }else {
                    nextScreen = Main::careerCenterStaffHomeScreen;
                }
                return;
            }
            if (opi < 1 || opi > operationFunctions.size()) {
                nextScreen = Main::editOpportunityFilterSettingsScreen;
            } else {
                nextScreen = operationFunctions.get(opi - 1);
            }

        } catch (NumberFormatException e) {
            nextScreen = Main::editOpportunityFilterSettingsScreen;
        }
    }

    static void editOFSPreferredMajorsScreen() {
        clearScreen();
        printTitle("Edit Internship Opportunity Filter Settings - Preferred Majors");

        User user = currentUser.get();
        InternshipOpportunityFilterSettings settings = user.getOpportunityFilterSettings();

        System.out.println("Current Preferred Majors Filter: ");
        ArrayList<String> majors = new ArrayList<>();
        if (settings.getPreferredMajors().isPresent()) {
            majors.addAll(settings.getPreferredMajors().get());
            for (int i = 0; i < majors.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, majors.get(i));
            }
        } else {
            System.out.println("ALL");
        }
        System.out.println("To add a major, type '+ <major>'");
        System.out.println("To remove a major, type '- <major-number-in-list>'");
        System.out.println("Type * to reset");
        System.out.println("Type 0 to go back");

        System.out.print("Your input: ");
        String input = sc.nextLine().trim();
        if (input.startsWith("+ ")) {
            String majorToAdd = input.substring(2).trim();
            settings.addPreferredMajor(majorToAdd);
        } else if (input.startsWith("- ")) {
            try {
                int i = Integer.parseInt(input.substring(2).trim());
                i--;
                if (i >= 0 && i < majors.size()) {
                    settings.removePreferredMajor(majors.get(i));
                }
            } catch (NumberFormatException e) {
            }
        } else if (input.equals("*")) {
            settings.resetPreferredMajors();
        } else if (input.equals("0")) {
            nextScreen = Main::editOpportunityFilterSettingsScreen;
            return;
        }
        nextScreen = Main::editOFSPreferredMajorsScreen;
    }

    static void editOFSLevel() {
        clearScreen();
        printTitle("Edit Internship Opportunity Filter Settings - Level");

        User user = currentUser.get();
        InternshipOpportunityFilterSettings settings = user.getOpportunityFilterSettings();
        System.out.println("Current Level Filter: ");
        System.out.println("1. Basic: " + (settings.isShowLevel(InternshipLevel.BASIC) ? "\u2713" : "X"));
        System.out.println("2. Intermediate: " + (settings.isShowLevel(InternshipLevel.INTERMEDIATE) ? "\u2713" : "X"));
        System.out.println("3. Advanced: " + (settings.isShowLevel(InternshipLevel.ADVANCED) ? "\u2713" : "X"));
        System.out.println("Type the number to toggle the level");
        System.out.println("Type 4 to reset");
        System.out.println("Type 0 to go back");
        System.out.print("Your input: ");
        int i = sc.nextInt();
        sc.nextLine(); // consume newline
        switch (i) {
            case 1 ->
                settings.toggleShowLevel(InternshipLevel.BASIC);
            case 2 ->
                settings.toggleShowLevel(InternshipLevel.INTERMEDIATE);
            case 3 ->
                settings.toggleShowLevel(InternshipLevel.ADVANCED);
            case 4 -> {
                if (!settings.isShowLevel(InternshipLevel.BASIC)) {
                    settings.toggleShowLevel(InternshipLevel.BASIC);
                }
                if (!settings.isShowLevel(InternshipLevel.INTERMEDIATE)) {
                    settings.toggleShowLevel(InternshipLevel.INTERMEDIATE);
                }
                if (!settings.isShowLevel(InternshipLevel.ADVANCED)) {
                    settings.toggleShowLevel(InternshipLevel.ADVANCED);
                }
            }
            case 0 -> {
                nextScreen = Main::editOpportunityFilterSettingsScreen;
                return;
            }
            default -> {
            }
        }
        nextScreen = Main::editOFSLevel;
    }

    static void editOFSStatus() {
        clearScreen();
        printTitle("Edit Internship Opportunity Filter Settings - Status");

        User user = currentUser.get();
        InternshipOpportunityFilterSettings settings = user.getOpportunityFilterSettings();
        System.out.println("Current Status Filter: ");
        if (user instanceof Student) {
            System.out.println("1. Unfilled: " + (settings.isShowStatus(InternshipOpportunityStatus.APPROVED) ? "\u2713" : "X"));
            System.out.println("2. Filled: " + (settings.isShowStatus(InternshipOpportunityStatus.FILLED) ? "\u2713" : "X"));
        } else {
            System.out.println("1. Pending: " + (settings.isShowStatus(InternshipOpportunityStatus.PENDING) ? "\u2713" : "X"));
            System.out.println("2. Approved: " + (settings.isShowStatus(InternshipOpportunityStatus.APPROVED) ? "\u2713" : "X"));
            System.out.println("3. Rejected: " + (settings.isShowStatus(InternshipOpportunityStatus.REJECTED) ? "\u2713" : "X"));
            System.out.println("4. Filled: " + (settings.isShowStatus(InternshipOpportunityStatus.FILLED) ? "\u2713" : "X"));
        }
        System.out.println("Type the number to toggle the status");
        if (user instanceof Student) {
            System.out.println("Type 3 to reset");
        } else {
            System.out.println("Type 5 to reset");
        }
        System.out.println("Type 0 to go back");
        System.out.print("Your input: ");
        int i = sc.nextInt();
        sc.nextLine(); // consume newline

        if (user instanceof Student) {
            switch (i) {
                case 1 ->
                    settings.toggleShowStatus(InternshipOpportunityStatus.APPROVED);
                case 2 ->
                    settings.toggleShowStatus(InternshipOpportunityStatus.FILLED);
                case 3 -> {
                    if (!settings.isShowStatus(InternshipOpportunityStatus.APPROVED)) {
                        settings.toggleShowStatus(InternshipOpportunityStatus.APPROVED);
                    }
                    if (settings.isShowStatus(InternshipOpportunityStatus.FILLED)) {
                        settings.toggleShowStatus(InternshipOpportunityStatus.FILLED);
                    }
                }
                case 0 -> {
                    nextScreen = Main::editOpportunityFilterSettingsScreen;
                    return;
                }
                default -> {
                }
            }
            nextScreen = Main::editOFSStatus;
        } else {
            switch (i) {
                case 1 ->
                    settings.toggleShowStatus(InternshipOpportunityStatus.PENDING);
                case 2 ->
                    settings.toggleShowStatus(InternshipOpportunityStatus.APPROVED);
                case 3 ->
                    settings.toggleShowStatus(InternshipOpportunityStatus.REJECTED);
                case 4 ->
                    settings.toggleShowStatus(InternshipOpportunityStatus.FILLED);
                case 5 -> {
                    if (!settings.isShowStatus(InternshipOpportunityStatus.PENDING)) {
                        settings.toggleShowStatus(InternshipOpportunityStatus.PENDING);
                    }
                    if (!settings.isShowStatus(InternshipOpportunityStatus.APPROVED)) {
                        settings.toggleShowStatus(InternshipOpportunityStatus.APPROVED);
                    }
                    if (!settings.isShowStatus(InternshipOpportunityStatus.REJECTED)) {
                        settings.toggleShowStatus(InternshipOpportunityStatus.REJECTED);
                    }
                    if (!settings.isShowStatus(InternshipOpportunityStatus.FILLED)) {
                        settings.toggleShowStatus(InternshipOpportunityStatus.FILLED);
                    }
                }
                case 0 -> {
                    nextScreen = Main::editOpportunityFilterSettingsScreen;
                    return;
                }
                default -> {
                }
            }
            nextScreen = Main::editOFSStatus;
        }
    }

    static void editOFSVisibility() {
        clearScreen();
        printTitle("Edit Internship Opportunity Filter Settings - Visibility");

        User user = currentUser.get();
        InternshipOpportunityFilterSettings settings = user.getOpportunityFilterSettings();
        System.out.println("Current Visibility Filter: ");
        System.out.println("1. Visible: " + (settings.isShowVisible() ? "\u2713" : "X"));
        System.out.println("2. Invisible: " + (settings.isShowInvisible() ? "\u2713" : "X"));
        System.out.println("Type the number to toggle the visibility");
        System.out.println("Type 3 to reset");
        System.out.println("Type 0 to go back");
        System.out.print("Your input: ");
        int i = sc.nextInt();
        sc.nextLine(); // consume newline
        switch (i) {
            case 1 ->
                settings.toggleShowVisible();
            case 2 ->
                settings.toggleShowInvisible();
            case 3 -> {
                if (!settings.isShowVisible()) {
                    settings.toggleShowVisible();
                }
                if (!settings.isShowInvisible()) {
                    settings.toggleShowInvisible();
                }
            }
            case 0 -> {
                nextScreen = Main::editOpportunityFilterSettingsScreen;
                return;
            }
            default -> {
            }
        }
        nextScreen = Main::editOFSVisibility;
    }

    static void editOFSCompanies() {
        clearScreen();
        printTitle("Edit Internship Opportunity Filter Settings - Companies");

        User user = currentUser.get();
        InternshipOpportunityFilterSettings settings = user.getOpportunityFilterSettings();

        System.out.println("Current Companies Filter: ");
        ArrayList<String> companies = new ArrayList<>();
        if (settings.getCompanies().isPresent()) {
            companies.addAll(settings.getCompanies().get());
            for (int i = 0; i < companies.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, companies.get(i));
            }
        } else {
            System.out.println("ALL");
        }
        System.out.println("To add a company, type '+ <company>'");
        System.out.println("To remove a company, type '- <company-number-in-list>'");
        System.out.println("Type * to reset");
        System.out.println("Type 0 to go back");

        System.out.print("Your input: ");
        String input = sc.nextLine().trim();
        if (input.startsWith("+ ")) {
            String companyToAdd = input.substring(2).trim();
            settings.addCompany(companyToAdd);
        } else if (input.startsWith("- ")) {
            try {
                int i = Integer.parseInt(input.substring(2).trim());
                i--;
                if (i >= 0 && i < companies.size()) {
                    settings.removeCompany(companies.get(i));
                }
            } catch (NumberFormatException e) {
            }
        } else if (input.equals("*")) {
            settings.resetCompanies();
        } else if (input.equals("0")) {
            nextScreen = Main::editOpportunityFilterSettingsScreen;
            return;
        }
        nextScreen = Main::editOFSCompanies;
    }

    static void editOFSOpeningAfter() {
        clearScreen();
        printTitle("Edit Internship Opportunity Filter Settings - Opening After");

        User user = currentUser.get();
        InternshipOpportunityFilterSettings settings = user.getOpportunityFilterSettings();

        System.out.println("Current Opens After Filter: ");
        if (settings.getOpensAfter().isPresent()) {
            System.out.println(settings.getOpensAfter().get());
        } else {
            System.out.println("N\\A");
        }
        System.out.print("Enter a date in YYYY-MM-DD format. To reset, type '*'. To go back, type '0': ");
        String input = sc.nextLine().trim();
        switch (input) {
            case "*" -> {
                settings.resetOpensAfter();
            }
            case "0" -> {
            }
            default -> {
                try {
                    settings.setOpensAfter(LocalDate.parse(input));
                } catch (Exception e) {
                    System.err.println("Invalid date format");
                    System.out.print("Press any button to continue...");
                    sc.nextLine();
                }
            }
        }
        nextScreen = Main::editOpportunityFilterSettingsScreen;
    }

    static void editOFSClosingBefore() {
        clearScreen();
        printTitle("Edit Internship Opportunity Filter Settings - Closing Before");

        User user = currentUser.get();
        InternshipOpportunityFilterSettings settings = user.getOpportunityFilterSettings();

        System.out.println("Current Closes Before Filter: ");
        if (settings.getClosesBefore().isPresent()) {
            System.out.println(settings.getClosesBefore().get());
        } else {
            System.out.println("N\\A");
        }
        System.out.print("Enter a date in YYYY-MM-DD format. To reset, type '*'. To go back, type '0': ");
        String input = sc.nextLine().trim();
        switch (input) {
            case "*" -> {
                settings.resetClosesBefore();
            }
            case "0" -> {
            }
            default -> {
                try {
                    settings.setClosesBefore(LocalDate.parse(input));
                } catch (Exception e) {
                    System.err.println("Invalid date format");
                    System.out.print("Press any button to continue...");
                    sc.nextLine();
                }
            }
        }
        nextScreen = Main::editOpportunityFilterSettingsScreen;
    }

    static void editApplicationFilterSettingsScreen() {
        if (!currentUser.isPresent()) {
            throw new Error("No user is logged in");
        }
        clearScreen();
        printTitle("Edit Internship Application Filter Settings");

        ArrayList<String> operations = new ArrayList<>();
        ArrayList<String> currentValue = new ArrayList<>();
        ArrayList<Screen> operationFunctions = new ArrayList<>();

        User user = currentUser.get();
        InternshipApplicationFilterSettings settings = user.getApplicationFilterSettings();

        operations.add("Apply opportunity filters");
        currentValue.add(settings.getOpportunityFilters().isPresent() ? "\u2713" : "X");
        operationFunctions.add(Main::editApplicationFilterSettingsScreen);

        operations.add("Status");
        ArrayList<String> statuses = new ArrayList<>();
        if (settings.isShowStatus(InternshipApplicationStatus.PENDING)) {
            statuses.add("Pending");
        }
        if (settings.isShowStatus(InternshipApplicationStatus.SUCCESSFUL)) {
            statuses.add("Successful");
        }
        if (settings.isShowStatus(InternshipApplicationStatus.UNSUCCESSFUL)) {
            statuses.add("Unsuccessful");
        }
        if (statuses.size() == 3) {
            currentValue.add("ALL");
        } else {
            currentValue.add("[" + String.join(", ", statuses) + "]");
        }
        operationFunctions.add(Main::editAFSStatus);

        operations.add("Withdraw Status");
        ArrayList<String> withdrawStatuses = new ArrayList<>();
        if (settings.isShowWithdrawStatus(WithdrawStatus.NOT_REQUESTED)) {
            withdrawStatuses.add("Not requested");
        }
        if (settings.isShowWithdrawStatus(WithdrawStatus.PENDING)) {
            withdrawStatuses.add("Pending");
        }
        if (settings.isShowWithdrawStatus(WithdrawStatus.APPROVED)) {
            withdrawStatuses.add("Approved");
        }
        if (settings.isShowWithdrawStatus(WithdrawStatus.REJECTED)) {
            withdrawStatuses.add("Rejected");
        }
        if (withdrawStatuses.size() == 4) {
            currentValue.add("ALL");
        } else {
            currentValue.add("[" + String.join(", ", withdrawStatuses) + "]");
        }
        operationFunctions.add(Main::editAFSWithdrawStatus);

        operations.add("Placement Confirmation Status");
        ArrayList<String> placementConfirmationStatuses = new ArrayList<>();
        if (settings.isShowPlacementConfirmationStatus(PlacementConfirmationStatus.PENDING)) {
            placementConfirmationStatuses.add("Pending");
        }
        if (settings.isShowPlacementConfirmationStatus(PlacementConfirmationStatus.ACCEPTED)) {
            placementConfirmationStatuses.add("Accepted");
        }
        if (settings.isShowPlacementConfirmationStatus(PlacementConfirmationStatus.REJECTED)) {
            placementConfirmationStatuses.add("Rejected");
        }
        if (placementConfirmationStatuses.size() == 3) {
            currentValue.add("ALL");
        } else {
            currentValue.add("[" + String.join(", ", placementConfirmationStatuses) + "]");
        }
        operationFunctions.add(Main::editAFSPlacementConfirmationStatus);

        for (int i = 0; i < operations.size(); i++) {
            System.out.printf("%d. %s: %s\n", i + 1, operations.get(i), currentValue.get(i));
        }
        System.out.println("0. Go back");
        System.out.println("e. Exit");
        System.out.print("Please choose an operation: ");
        String op = sc.nextLine().trim();
        if (op.equals("e")) {
            exit();
        }

        try {
            int opi = Integer.parseInt(op);
            if (opi == 0) {
                if (user instanceof Student) {
                    nextScreen = Main::studentHomeScreen; 
                }else if (user instanceof CompanyRepresentative) {
                    nextScreen = Main::companyRepHomeScreen; 
                }else {
                    nextScreen = Main::careerCenterStaffHomeScreen;
                }
                return;
            }
            if (opi < 1 || opi > operationFunctions.size()) {
                nextScreen = Main::editApplicationFilterSettingsScreen;
            } else {
                if (opi == 1) {
                    if (settings.getOpportunityFilters().isPresent()) {
                        settings.setOpportunityFilters(Optional.empty());
                    } else {
                        settings.setOpportunityFilters(Optional.of(user.getOpportunityFilterSettings()));
                    }
                    nextScreen = Main::editApplicationFilterSettingsScreen;
                } else {
                    nextScreen = operationFunctions.get(opi - 1);
                }
            }

        } catch (NumberFormatException e) {
            nextScreen = Main::editApplicationFilterSettingsScreen;
        }
    }

    static void editAFSStatus() {
        clearScreen();
        printTitle("Edit Internship Application Filter Settings - Status");

        User user = currentUser.get();
        InternshipApplicationFilterSettings settings = user.getApplicationFilterSettings();

        System.out.println("Current Status Filter: ");
        System.out.println("1. PENDING: " + (settings.isShowStatus(InternshipApplicationStatus.PENDING) ? "\u2713" : "X"));
        System.out.println("2. SUCCESSFUL: " + (settings.isShowStatus(InternshipApplicationStatus.SUCCESSFUL) ? "\u2713" : "X"));
        System.out.println("3. UNSUCCESSFUL: " + (settings.isShowStatus(InternshipApplicationStatus.UNSUCCESSFUL) ? "\u2713" : "X"));
        System.out.println("Type the number to toggle the status");
        System.out.println("Type 4 to reset");
        System.out.println("Type 0 to go back");
        System.out.print("Your input: ");
        int i = sc.nextInt();
        sc.nextLine(); // consume newline
        switch (i) {
            case 1 ->
                settings.toggleShowStatus(InternshipApplicationStatus.PENDING);
            case 2 ->
                settings.toggleShowStatus(InternshipApplicationStatus.SUCCESSFUL);
            case 3 ->
                settings.toggleShowStatus(InternshipApplicationStatus.UNSUCCESSFUL);
            case 4 -> {
                if (!settings.isShowStatus(InternshipApplicationStatus.PENDING)) {
                    settings.toggleShowStatus(InternshipApplicationStatus.PENDING);
                }
                if (!settings.isShowStatus(InternshipApplicationStatus.SUCCESSFUL)) {
                    settings.toggleShowStatus(InternshipApplicationStatus.SUCCESSFUL);
                }
                if (!settings.isShowStatus(InternshipApplicationStatus.UNSUCCESSFUL)) {
                    settings.toggleShowStatus(InternshipApplicationStatus.UNSUCCESSFUL);
                }
            }
            case 0 -> {
                nextScreen = Main::editApplicationFilterSettingsScreen;
            }
        }
    }

    static void editAFSWithdrawStatus() {
        clearScreen();
        printTitle("Edit Internship Application Filter Settings - Withdraw Status");

        User user = currentUser.get();
        InternshipApplicationFilterSettings settings = user.getApplicationFilterSettings();

        System.out.println("Current Withdraw Status Filter: ");
        System.out.println("1. Not requested: " + (settings.isShowWithdrawStatus(WithdrawStatus.NOT_REQUESTED) ? "\u2713" : "X"));
        System.out.println("2. Pending: " + (settings.isShowWithdrawStatus(WithdrawStatus.PENDING) ? "\u2713" : "X"));
        System.out.println("3. Approved: " + (settings.isShowWithdrawStatus(WithdrawStatus.APPROVED) ? "\u2713" : "X"));
        System.out.println("4. Rejected: " + (settings.isShowWithdrawStatus(WithdrawStatus.REJECTED) ? "\u2713" : "X"));
        System.out.println("Type the number to toggle the withdraw status");
        System.out.println("Type 5 to reset");
        System.out.println("Type 0 to go back");
        System.out.print("Your input: ");
        int i = sc.nextInt();
        sc.nextLine(); // consume newline
        switch (i) {
            case 1 ->
                settings.toggleShowWithdrawStatus(WithdrawStatus.NOT_REQUESTED);
            case 2 ->
                settings.toggleShowWithdrawStatus(WithdrawStatus.PENDING);
            case 3 ->
                settings.toggleShowWithdrawStatus(WithdrawStatus.APPROVED);
            case 4 ->
                settings.toggleShowWithdrawStatus(WithdrawStatus.REJECTED);
            case 5 -> {
                if (!settings.isShowWithdrawStatus(WithdrawStatus.NOT_REQUESTED)) {
                    settings.toggleShowWithdrawStatus(WithdrawStatus.NOT_REQUESTED);
                }
                if (!settings.isShowWithdrawStatus(WithdrawStatus.PENDING)) {
                    settings.toggleShowWithdrawStatus(WithdrawStatus.PENDING);
                }
                if (!settings.isShowWithdrawStatus(WithdrawStatus.APPROVED)) {
                    settings.toggleShowWithdrawStatus(WithdrawStatus.APPROVED);
                }
                if (!settings.isShowWithdrawStatus(WithdrawStatus.REJECTED)) {
                    settings.toggleShowWithdrawStatus(WithdrawStatus.REJECTED);
                }
            }
            case 0 -> {
                nextScreen = Main::editApplicationFilterSettingsScreen;
            }
        }
    }

    static void editAFSPlacementConfirmationStatus() {
        clearScreen();
        printTitle("Edit Internship Application Filter Settings - Placement Confirmation Status");

        User user = currentUser.get();
        InternshipApplicationFilterSettings settings = user.getApplicationFilterSettings();

        System.out.println("Current Placement Confirmation Status Filter: ");
        System.out.println("1. Pending: " + (settings.isShowPlacementConfirmationStatus(PlacementConfirmationStatus.PENDING) ? "\u2713" : "X"));
        System.out.println("2. Accepted: " + (settings.isShowPlacementConfirmationStatus(PlacementConfirmationStatus.ACCEPTED) ? "\u2713" : "X"));
        System.out.println("3. Rejected: " + (settings.isShowPlacementConfirmationStatus(PlacementConfirmationStatus.REJECTED) ? "\u2713" : "X"));
        System.out.println("Type the number to toggle the placement confirmation status");
        System.out.println("Type 4 to reset");
        System.out.println("Type 0 to go back");
        System.out.print("Your input: ");
        int i = sc.nextInt();
        sc.nextLine(); // consume newline
        switch (i) {
            case 1 ->
                settings.toggleShowPlacementConfirmationStatus(PlacementConfirmationStatus.PENDING);
            case 2 ->
                settings.toggleShowPlacementConfirmationStatus(PlacementConfirmationStatus.ACCEPTED);
            case 3 ->
                settings.toggleShowPlacementConfirmationStatus(PlacementConfirmationStatus.REJECTED);
            case 4 -> {
                if (!settings.isShowPlacementConfirmationStatus(PlacementConfirmationStatus.PENDING)) {
                    settings.toggleShowPlacementConfirmationStatus(PlacementConfirmationStatus.PENDING);
                }
                if (!settings.isShowPlacementConfirmationStatus(PlacementConfirmationStatus.ACCEPTED)) {
                    settings.toggleShowPlacementConfirmationStatus(PlacementConfirmationStatus.ACCEPTED);
                }
                if (!settings.isShowPlacementConfirmationStatus(PlacementConfirmationStatus.REJECTED)) {
                    settings.toggleShowPlacementConfirmationStatus(PlacementConfirmationStatus.REJECTED);
                }
            }
            case 0 -> {
                nextScreen = Main::editApplicationFilterSettingsScreen;
            }
        }
    }

    static void CCSManageCRApplicationsScreen() {
        if (!currentUser.isPresent() || !(currentUser.get() instanceof internship_management_system.users.CareerCentreStaff)) {
            throw new Error("Current user is not a career centre staff");
        }
        clearScreen();
        printTitle("Manage Company Representative Applications");
        ArrayList<CompanyRepresentative> pendingCRs = DataStorage.getCompanyReps().stream()
                .filter(cr -> cr.getStatus() == CompanyRepresentativeStatus.PENDING)
                .collect(Collectors.toCollection(ArrayList::new));

        if (pendingCRs.isEmpty()) {
            System.out.println("No pending company representative applications.");
            System.out.print("Press any button to go back...");
            sc.nextLine();
            nextScreen = Main::careerCenterStaffHomeScreen;
            return;
        }

        for (int i = 0; i < pendingCRs.size(); i++) {
            CompanyRepresentative cr = pendingCRs.get(i);
            System.out.printf("%d. %s (Company: %s, Department: %s, Position: %s)\n", i + 1, cr.getName(), cr.getCompanyName(), cr.getDepartment(), cr.getPosition());
        }
        System.out.println("0. Go back");
        System.out.print("Please choose a company representative to review: ");
        String op = sc.nextLine().trim();
        try {
            int opi = Integer.parseInt(op);
            if (opi == 0) {
                nextScreen = Main::careerCenterStaffHomeScreen;
                return;
            }
            if (opi < 1 || opi > pendingCRs.size()) {
                nextScreen = Main::CCSManageCRApplicationsScreen;
            } else {
                CompanyRepresentative cr = pendingCRs.get(opi - 1);
                System.out.print("Decision? [C]ancel / [A]pprove / [R]eject: ");
                String decision = sc.nextLine().trim().toUpperCase();
                switch (decision) {
                    case "A" -> {
                        cr.setStatus(CompanyRepresentativeStatus.APPROVED);
                    }
                    case "R" -> {
                        cr.setStatus(CompanyRepresentativeStatus.REJECTED);
                    }
                    default -> {
                    }
                }
                nextScreen = Main::CCSManageCRApplicationsScreen;
            }
        } catch (NumberFormatException e) {
            nextScreen = Main::CCSManageCRApplicationsScreen;
        }
    }

    static void internshipOpportunitiesScreen() {
        if (!currentUser.isPresent()) {
            throw new Error("No user is logged in");
        }
        clearScreen();
        if(currentUser.get() instanceof Student) printTitle("View Internship Opportunities");
        else printTitle("Manage Internship Opportunities");

        User user = currentUser.get();
        ArrayList<InternshipOpportunity> opportunities = new ArrayList<>(DataStorage.getInternshipOpportunities(user.getOpportunityFilterSettings()));

        if (opportunities.isEmpty()) {
            System.out.println("No internship opportunities found.");
        } else {
            System.out.println("Internship Opportunities:");
            for (int i = 0; i < opportunities.size(); i++) {
                System.out.println((i+1)+"\t-----------\n"+opportunities.get(i).toString());
                if (i + 1 < opportunities.size()) {
                    System.out.println("--------------------");
                }
            }
        }
        System.out.print("");
        if(user instanceof CompanyRepresentative) System.out.print("Type internship opportunity ID to manage, '+' to create new or 0 to go back: ");
        else if(user instanceof Student) System.out.print("Type internship opportunity ID to view, or 0 to go back: ");
        else System.out.print("Type internship opportunity ID to manage, or 0 to go back: ");
        String input = sc.nextLine().trim();
        if (input.equals("0")) {
            if(user instanceof Student) {
                nextScreen = Main::studentHomeScreen;
            } else if (user instanceof CompanyRepresentative) {
                nextScreen = Main::companyRepHomeScreen;
            } else {
                nextScreen = Main::careerCenterStaffHomeScreen;
            }
            return;
        }
        if (input.equals("+")) {
            if(!(user instanceof CompanyRepresentative)) {
                nextScreen = Main::internshipOpportunitiesScreen;
                return;
            }
            CompanyRepresentative cr = (CompanyRepresentative) user;
            if (DataStorage.getAllInternshipOpportunities().stream().filter(io -> io.getCompanyRep().getCompanyName().equals(cr.getCompanyName())).count() >= 5) {
                System.out.println("You have reached the maximum number of internship opportunities (5) for your company.");
                System.out.print("Press any button to continue...");
                sc.nextLine();
            }
            nextScreen = Main::CRCreateInternshipOpportunityScreen;
        } else {
            try {
                int i = Integer.parseInt(input);
                if(i < 1 || i > opportunities.size()) {
                    nextScreen = Main::internshipOpportunitiesScreen;
                    return;
                }
                currentOpportunity = Optional.of(opportunities.get(i - 1));
                nextScreen = Main::internshipOpportunityScreen;

            } catch (NumberFormatException e) {
                nextScreen = Main::internshipOpportunitiesScreen;
            }
        }

    }

    static void CRCreateInternshipOpportunityScreen() {
        if (!currentUser.isPresent() || !(currentUser.get() instanceof CompanyRepresentative)) {
            throw new Error("Current user is not a company representative");
        }
        clearScreen();
        printTitle("Create Internship Opportunity");

        System.out.print("Internship title: ");
        String title = sc.nextLine().trim();

        System.out.print("Internship description: ");
        String description = sc.nextLine().trim();

        System.out.print("Preferred major: ");
        String preferredMajor = sc.nextLine().trim();

        System.out.print("Internship level? [B]asic / [I]ntermediate / [A]dvanced: ");
        InternshipLevel level;
        switch (sc.nextLine().trim().toUpperCase()) {
            case "B" ->
                level = InternshipLevel.BASIC;
            case "I" ->
                level = InternshipLevel.INTERMEDIATE;
            case "A" ->
                level = InternshipLevel.ADVANCED;
            default -> {
                System.out.println("Invalid level input.");
                System.out.print("Press any button to continue...");
                sc.nextLine();
                nextScreen = Main::CRCreateInternshipOpportunityScreen;
                return;
            }
        }

        System.out.print("Opening date (YYYY-MM-DD): ");
        LocalDate openingDate;
        try {
            openingDate = LocalDate.parse(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            System.out.print("Press any button to continue...");
            sc.nextLine();
            nextScreen = Main::CRCreateInternshipOpportunityScreen;
            return;
        }

        System.out.print("Closing date (YYYY-MM-DD): ");
        LocalDate closingDate;
        try {
            closingDate = LocalDate.parse(sc.nextLine().trim());
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            System.out.print("Press any button to continue...");
            sc.nextLine();
            nextScreen = Main::CRCreateInternshipOpportunityScreen;
            return;
        }

        System.out.print("Number of slots: ");
        int numOfSlots = sc.nextInt();
        sc.nextLine(); // consume newline
        if (numOfSlots <= 0 || numOfSlots > 10) {
            System.out.println("Invalid number of slots. Must be between 1 and 10.");
            System.out.print("Press any button to continue...");
            sc.nextLine();
            nextScreen = Main::CRCreateInternshipOpportunityScreen;
            return;
        }

        System.out.print("Visible to students? [Y/n] ");
        String visibilityInput = sc.nextLine().trim().toLowerCase();
        boolean isVisible = visibilityInput.equals("y") || visibilityInput.equals("");

        InternshipOpportunity io = DataStorage.newInternshipOpportunity(
                title,
                description,
                preferredMajor,
                level,
                openingDate,
                closingDate,
                (CompanyRepresentative) currentUser.get(),
                numOfSlots,
                isVisible
        );

        System.out.println("Internship opportunity created with ID: " + io.getId());
        System.out.print("Press any button to continue...");
        sc.nextLine();
        currentOpportunity = Optional.of(io);
        nextScreen = Main::internshipOpportunityScreen;
    }

    static void internshipOpportunityScreen() {
        if(!currentOpportunity.isPresent())
            throw new Error("No internship opportunity selected");
        if (!currentUser.isPresent()) {
            throw new Error("No user logged in");
        }
        clearScreen();
        printTitle("Internship Opportunity");

        InternshipOpportunity io = currentOpportunity.get();
        System.out.println(io);
        System.out.println("");

        ArrayList<String> operations = new ArrayList<>();
        ArrayList<Runnable> operationFunctions = new ArrayList<>();
        // FIXME: don't use runnable

        if(currentUser.get() instanceof Student) {
            // TODO: allow to reply if other application is withdrawn
            Student student = (Student) currentUser.get();
            if(!io.matchesFilter(student.getOpportunityFilterSettings())) throw new Error("Current internship opportunity does not match student's filter settings");
            Optional<InternshipApplication> application = DataStorage.getInternshipApplicationForAnOpportunity(io, student);
            if(application.isPresent()) {
                operations.add("View your application");
                operationFunctions.add(() -> {
                    currentApplication = application;
                    nextScreen = Main::internshipApplicationScreen;
                });
            }
            else if(io.getStatus() == InternshipOpportunityStatus.APPROVED) {
                operations.add("Apply for this internship");
                operationFunctions.add(() -> {
                    InternshipApplication newApplication = DataStorage.newInternshipApplication(io, student);
                    currentApplication = Optional.of(newApplication);
                    nextScreen = Main::internshipApplicationScreen;
                });
            }
        }
        if(currentUser.get() instanceof CompanyRepresentative) {
            CompanyRepresentative cr = (CompanyRepresentative) currentUser.get();
            if(!io.getCompanyRep().equals(cr)) throw new Error("Current internship opportunity does not belong to the current company representative");
            
            if(io.getStatus() == InternshipOpportunityStatus.REJECTED) {
                System.out.println("This internship opportunity has been rejected. You cannot modify it.");
            }
            else {
                operations.add("See all applications for this internship");
                operationFunctions.add(() -> {
                    // TODO: To be implemented
                    nextScreen = Main::internshipOpportunityScreen;
                });

                operations.add("Toggle visibility: " + (io.getVisibility() ? "\u2713" : "X"));
                operationFunctions.add(() -> {
                    io.toggleVisibility();
                    nextScreen = Main::internshipOpportunityScreen;
                });
            }
        }
        if(currentUser.get() instanceof CareerCentreStaff) {
            CareerCentreStaff ccs = (CareerCentreStaff) currentUser.get();
            if(io.getStatus() == InternshipOpportunityStatus.PENDING) {
                operations.add("Approve or reject internship opportunity");
                operationFunctions.add(() -> {
                    System.out.print("Decision? [C]ancel / [A]pprove / [R]eject: ");
                    String decision = sc.nextLine().trim().toUpperCase();
                    switch (decision) {
                        case "A" -> {
                            io.setStatus(InternshipOpportunityStatus.APPROVED);
                        }
                        case "R" -> {
                            io.setStatus(InternshipOpportunityStatus.REJECTED);
                        }
                        default -> {}
                    }
                    nextScreen = Main::internshipOpportunityScreen;
                });
            }
            else if(io.getStatus() == InternshipOpportunityStatus.REJECTED) {
                System.out.println("This internship opportunity has been rejected.");
            }
            else {
                operations.add("See all applications for this internship");
                operationFunctions.add(() -> {
                    // TODO: To be implemented
                    nextScreen = Main::internshipOpportunityScreen;
                });
            }
        }

        for(int i = 0; i < operations.size(); i++) {
            System.out.printf("%d. %s\n", i+1, operations.get(i));
        }
        System.out.println("0. Go back");
        System.out.print("Please choose an operation: ");
        int op = sc.nextInt();
        sc.nextLine(); // consume newline
        if(op == 0) {
            // FIXME: eh need a path of screens
            nextScreen = Main::internshipOpportunitiesScreen;
        }
        else if(op < 1 || op > operationFunctions.size()) {
            nextScreen = Main::internshipOpportunityScreen;
        }
        else {
            operationFunctions.get(op - 1).run();
        }
    }

    static void internshipApplicationScreen() {
        if(!currentApplication.isPresent())
            throw new Error("No internship application selected");
        if(!currentUser.isPresent())
            throw new Error("No user logged in");

        InternshipApplication application = currentApplication.get();
        clearScreen();
        printTitle("Internship Application");

        System.out.printf("Application ID: %d\n", application.getId());
        System.out.printf("Student: %s (%s)\n",
                application.getStudent().getName(),
                application.getStudent().getUserID());
        System.out.printf("Internship: %s\n", application.getOpportunity().getTitle());
        System.out.printf("Status: %s\n", application.getStatus());
        System.out.printf("Withdraw status: %s\n", application.getWithdrawStatus());
        System.out.printf("Placement confirmation: %s\n\n", application.getPlacementConfirmationStatus());
        System.out.println("Opportunity details:");
        System.out.println(application.getOpportunity());
        System.out.println("");

        ArrayList<String> operations = new ArrayList<>();
        ArrayList<Runnable> operationFunctions = new ArrayList<>();

        if(currentUser.get() instanceof Student) {
            Student student = (Student) currentUser.get();
            if(!application.getStudent().equals(student))
                throw new Error("Student cannot view another student's application");

            if(application.getWithdrawStatus() == WithdrawStatus.NOT_REQUESTED
                    && application.getStatus() != InternshipApplicationStatus.UNSUCCESSFUL) {
                operations.add("Request to withdraw application");
                operationFunctions.add(() -> {
                    try {
                        application.requestWithdraw();
                        System.out.println("Withdraw request submitted. Await approval from Career Centre Staff.");
                    } catch (Error e) {
                        System.out.printf("Failed to submit withdraw request: %s%n", e.getMessage());
                    }
                    System.out.print("Press Enter to continue...");
                    sc.nextLine();
                    nextScreen = Main::internshipApplicationScreen;
                });
            } else if(application.getWithdrawStatus() == WithdrawStatus.PENDING) {
                operations.add("Cancel withdraw request");
                operationFunctions.add(() -> {
                    application.confirmWithdraw(false);
                    System.out.println("Withdraw request cancelled.");
                    System.out.print("Press Enter to continue...");
                    sc.nextLine();
                    nextScreen = Main::internshipApplicationScreen;
                });
            }

            if(application.getStatus() == InternshipApplicationStatus.SUCCESSFUL
                    && application.getWithdrawStatus() != WithdrawStatus.APPROVED) {
                if(application.getPlacementConfirmationStatus() == PlacementConfirmationStatus.PENDING) {
                    operations.add("Accept offer");
                    operationFunctions.add(() -> {
                        try {
                            application.confirmPlacement(true);
                            System.out.println("Offer accepted.");
                        } catch (Error e) {
                            System.out.printf("Unable to accept offer: %s%n", e.getMessage());
                        }
                        System.out.print("Press Enter to continue...");
                        sc.nextLine();
                        nextScreen = Main::internshipApplicationScreen;
                    });

                    operations.add("Reject offer");
                    operationFunctions.add(() -> {
                        application.confirmPlacement(false);
                        System.out.println("Offer rejected.");
                        System.out.print("Press Enter to continue...");
                        sc.nextLine();
                        nextScreen = Main::internshipApplicationScreen;
                    });
                }
            }
        } else if(currentUser.get() instanceof CompanyRepresentative) {
            CompanyRepresentative cr = (CompanyRepresentative) currentUser.get();
            if(!application.getOpportunity().getCompanyRep().equals(cr))
                throw new Error("Company Representative cannot manage other companies' applications");

            if(application.getStatus() == InternshipApplicationStatus.PENDING
                    && application.getWithdrawStatus() != WithdrawStatus.APPROVED) {
                operations.add("Offer internship to student");
                operationFunctions.add(() -> {
                    try {
                        application.finalizeApplicationStatus(true);
                        System.out.println("Application marked successful.");
                    } catch (Error e) {
                        System.out.printf("Unable to offer internship: %s%n", e.getMessage());
                    }
                    System.out.print("Press Enter to continue...");
                    sc.nextLine();
                    nextScreen = Main::internshipApplicationScreen;
                });

                operations.add("Reject application");
                operationFunctions.add(() -> {
                    try {
                        application.finalizeApplicationStatus(false);
                        System.out.println("Application rejected.");
                    } catch (Error e) {
                        System.out.printf("Unable to reject application: %s%n", e.getMessage());
                    }
                    System.out.print("Press Enter to continue...");
                    sc.nextLine();
                    nextScreen = Main::internshipApplicationScreen;
                });
            }
        } else if(currentUser.get() instanceof CareerCentreStaff) {
            if(application.getWithdrawStatus() == WithdrawStatus.PENDING) {
                operations.add("Approve withdraw request");
                operationFunctions.add(() -> {
                    application.confirmWithdraw(true);
                    System.out.println("Withdrawal approved.");
                    System.out.print("Press Enter to continue...");
                    sc.nextLine();
                    nextScreen = Main::internshipApplicationScreen;
                });

                operations.add("Reject withdraw request");
                operationFunctions.add(() -> {
                    application.confirmWithdraw(false);
                    System.out.println("Withdrawal rejected.");
                    System.out.print("Press Enter to continue...");
                    sc.nextLine();
                    nextScreen = Main::internshipApplicationScreen;
                });
            }
        }

        for(int i = 0; i < operations.size(); i++) {
            System.out.printf("%d. %s\n", i+1, operations.get(i));
        }
        System.out.println("0. Go back");
        System.out.print("Please choose an operation: ");
        String input = sc.nextLine().trim();

        Screen backScreen;
        if(currentOpportunity.isPresent()) {
            backScreen = Main::internshipOpportunityScreen;
        } else if(currentUser.get() instanceof Student) {
            backScreen = Main::studentHomeScreen;
        } else if(currentUser.get() instanceof CompanyRepresentative) {
            backScreen = Main::companyRepHomeScreen;
        } else {
            backScreen = Main::careerCenterStaffHomeScreen;
        }

        if(input.equals("0")) {
            nextScreen = backScreen;
            return;
        }
        try {
            int op = Integer.parseInt(input);
            if(op < 1 || op > operationFunctions.size()) {
                nextScreen = Main::internshipApplicationScreen;
            } else {
                operationFunctions.get(op - 1).run();
            }
        } catch (NumberFormatException e) {
            nextScreen = Main::internshipApplicationScreen;
        }
    }


}
