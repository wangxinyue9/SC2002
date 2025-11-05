package internship_management_system.users;
import java.util.*;
import java.util.stream.Collectors;

public class CareerCentreStaff extends User {
    private String role;
    private String department;
    private String email;
    private Scanner sc = new Scanner(System.in);

    public CareerCentreStaff(String id, String name, String password,
                             String role, String department, String email) {
        super(id, name, password);
        this.role = role;
        this.department = department;
        this.email = email;
    }

    // Login menu
    @Override
    public void login() {
        int option;
        do {
            System.out.println("\nCareer Centre Staff Menu");
            System.out.println("1. Approve / Reject Company Representatives");
            System.out.println("2. Approve / Reject Internship Opportunities");
            System.out.println("3. Approve / Reject Student Withdrawal Requests");
            System.out.println("4. Generate Comprehensive Internship Report");
            System.out.println("5. Logout");
            System.out.print("Select an option: ");

            while (!sc.hasNextInt()) {
                System.out.print("Please enter a valid number: ");
                sc.next();
            }
            option = sc.nextInt();
            sc.nextLine();

            switch (option) {
                case 1 -> approveCompanyRepresentatives();
                case 2 -> approveRejectInternships();
                case 3 -> approveRejectWithdrawals();
                case 4 -> displayComprehensiveReport();
                case 5 -> System.out.println("Logging out...");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (option != 5);
    }

    // Company representative approval
    public void approveCompanyRepresentatives() {
        List<CompanyRepresentative> allReps = getAllCompanyRepresentatives();
        List<CompanyRepresentative> pendingReps = allReps.stream()
                .filter(rep -> !rep.isApproved())
                .collect(Collectors.toList());

        if (pendingReps.isEmpty()) {
            System.out.println("No pending company representatives to review.");
            return;
        }

        System.out.println("\nPending Company Representatives:");
        for (int i = 0; i < pendingReps.size(); i++) {
            CompanyRepresentative rep = pendingReps.get(i);
            System.out.printf("%d. %s (%s - %s)%n",
                    i + 1, rep.getName(), rep.getCompanyName(), rep.getDepartment());
        }

        System.out.print("Select a representative (0 to cancel): ");
        int choice = sc.nextInt();
        sc.nextLine();
        if (choice <= 0 || choice > pendingReps.size()) return;

        CompanyRepresentative selected = pendingReps.get(choice - 1);
        System.out.print("Approve (A) or Reject (R): ");
        String decision = sc.nextLine().trim().toUpperCase();

        if (decision.equals("A")) {
            selected.setApproved(true);
            System.out.println(selected.getName() + " has been approved.");
        } else if (decision.equals("R")) {
            selected.setApproved(false);
            System.out.println(selected.getName() + " has been rejected.");
        } else {
            System.out.println("Invalid input.");
        }
    }

    // Internship opportunity approval
    public void approveRejectInternships() {
        List<InternshipOpportunity> pendingList = InternshipOpportunity.getOpportunitiesList("").stream()
                .filter(op -> op.getStatus() == InternshipOpportunityStatus.PENDING)
                .collect(Collectors.toList());

        if (pendingList.isEmpty()) {
            System.out.println("No pending internship opportunities for review.");
            return;
        }

        System.out.println("\nPending Internship Opportunities:");
        for (int i = 0; i < pendingList.size(); i++) {
            InternshipOpportunity op = pendingList.get(i);
            System.out.printf("%d. %s (%s)%n", i + 1, op.getTitle(), op.getCompanyRep());
        }

        System.out.print("Select an internship (0 to cancel): ");
        int choice = sc.nextInt();
        sc.nextLine();
        if (choice <= 0 || choice > pendingList.size()) return;

        InternshipOpportunity selected = pendingList.get(choice - 1);
        System.out.print("Approve (A) or Reject (R): ");
        String decision = sc.nextLine().trim().toUpperCase();

        if (decision.equals("A")) {
            selected.setStatus(InternshipOpportunityStatus.APPROVED);
            if (!selected.getVisibility()) selected.toggleVisibility();
            System.out.println(selected.getTitle() + " approved and made visible to students.");
        } else if (decision.equals("R")) {
            selected.setStatus(InternshipOpportunityStatus.REJECTED);
            System.out.println(selected.getTitle() + " rejected.");
        } else {
            System.out.println("Invalid input.");
        }
    }

    // Internship withdrawal approval
    public void approveRejectWithdrawals() {
        List<InternshipApplication> pendingWithdrawals = InternshipApplication.getApplicationList("").stream()
                .filter(app -> app.getWithdrawStatus() == WithdrawStatus.PENDING)
                .collect(Collectors.toList());

        if (pendingWithdrawals.isEmpty()) {
            System.out.println("No pending withdrawal requests.");
            return;
        }

        System.out.println("\nPending Withdrawal Requests:");
        for (int i = 0; i < pendingWithdrawals.size(); i++) {
            InternshipApplication app = pendingWithdrawals.get(i);
            System.out.printf("%d. %s withdrawing from %s%n",
                    i + 1, app.getStudent(), app.getOpportunity().getTitle());
        }

        System.out.print("Select a withdrawal request (0 to cancel): ");
        int choice = sc.nextInt();
        sc.nextLine();
        if (choice <= 0 || choice > pendingWithdrawals.size()) return;

        InternshipApplication selected = pendingWithdrawals.get(choice - 1);
        System.out.print("Approve (A) or Reject (R): ");
        String decision = sc.nextLine().trim().toUpperCase();

        if (decision.equals("A")) {
            selected.confirmWithdraw(true);
            System.out.println("Withdrawal approved.");
        } else if (decision.equals("R")) {
            selected.confirmWithdraw(false);
            System.out.println("Withdrawal rejected.");
        } else {
            System.out.println("Invalid input.");
        }
    }

    // Display comprehensive internship report (alphabetical by default)
    public void displayComprehensiveReport() {
        FilterSettings filtersettings = new FilterSettings(); // default filters
        List<InternshipOpportunity> opportunities = applyFilterSettings(filtersettings);

        if (opportunities == null || opportunities.isEmpty()) {
            System.out.println("No internship opportunities found.");
            return;
        }

        System.out.println("\nComprehensive Internship Report");
        System.out.printf("%-5s | %-25s | %-15s | %-10s | %-12s | %-10s%n",
                "ID", "Title", "Major", "Level", "Status", "Slots");
        for (InternshipOpportunity io : opportunities) {
            System.out.printf("%-5d | %-25s | %-15s | %-10s | %-12s | %-10d%n",
                    io.getId(), io.getTitle(), io.getPreferredMajor(),
                    io.getInternshipLevel(), io.getStatus(), io.getNumOfRemainingSlots());
        }
    }

    // Apply filters (for now just default alphabetical order)
    @Override
    public List<InternshipOpportunity> applyFilterSettings(FilterSettings filtersettings) {
        return InternshipOpportunity.getOpportunitiesList("").stream()
                .sorted(Comparator.comparing(InternshipOpportunity::getTitle))
                .collect(Collectors.toList());
    }
}

