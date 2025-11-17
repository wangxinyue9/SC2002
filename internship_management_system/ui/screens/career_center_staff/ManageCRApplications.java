package internship_management_system.ui.screens.career_center_staff;

import internship_management_system.ui.Screen;
import internship_management_system.ui.UIState;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import internship_management_system.enums.CompanyRepresentativeStatus;
import internship_management_system.model.DataStorage;
import internship_management_system.ui.IO;
import internship_management_system.users.CareerCentreStaff;
import internship_management_system.users.CompanyRepresentative;

/**
 * Screen for managing Company Representative applications by Career Center Staff.
 */
public class ManageCRApplications implements Screen {
    public static final ManageCRApplications INSTANCE = new ManageCRApplications();

    /*
     * Private constructor to enforce singleton pattern.
     */
    private ManageCRApplications() {}

    @Override
    public Optional<Screen> show(UIState uiState) {
        if(!uiState.getCurrentUser().isPresent() || !(uiState.getCurrentUser().get() instanceof  CareerCentreStaff)) {
            IO.exitWithError("Not logged in as a career center staff.");
        }

        IO.clearConsole();
        printTitle("Manage Company Representative Applications", uiState);

        ArrayList<CompanyRepresentative> pendingCRs = DataStorage.getCompanyReps().stream()
                .filter(cr -> cr.getStatus() == CompanyRepresentativeStatus.PENDING)
                .collect(Collectors.toCollection(ArrayList::new));

        if (pendingCRs.isEmpty()) {
            System.out.println("No pending company representative applications.");
            System.out.print("Press any button to go back...");
            IO.getScanner().nextLine();
            return Optional.empty();
        }

        for (int i = 0; i < pendingCRs.size(); i++) {
            CompanyRepresentative cr = pendingCRs.get(i);
            System.out.printf("%d. %s (Company: %s, Department: %s, Position: %s)\n", i + 1, cr.getName(), cr.getCompanyName(), cr.getDepartment(), cr.getPosition());
        }
        System.out.println("0. Go back");
        System.out.print("Please choose a company representative to review: ");
        String op = IO.getScanner().nextLine().trim();
        try {
            int opi = Integer.parseInt(op);
            if (opi == 0) {
                return Optional.empty();
            }
            if (opi < 1 || opi > pendingCRs.size()) {
                return Optional.of(ManageCRApplications.INSTANCE);
            } else {
                CompanyRepresentative cr = pendingCRs.get(opi - 1);
                System.out.print("Decision? [C]ancel / [A]pprove / [R]eject: ");
                String decision = IO.getScanner().nextLine().trim().toUpperCase();
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
                return Optional.of(ManageCRApplications.INSTANCE);
            }
        } catch (NumberFormatException e) {
            return Optional.of(ManageCRApplications.INSTANCE);
        }
    }
}