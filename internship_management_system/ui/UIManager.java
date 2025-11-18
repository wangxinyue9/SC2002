package internship_management_system.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;

import internship_management_system.model.DataStorage;
import internship_management_system.ui.screens.HomeScreen;
import java.util.ArrayList;

/**
 * Class to manage the UI
 */
public class UIManager {
    final UIState uiState;
    final ArrayList<Screen> screens;

    /**
     * instantiate UI Manager
     * @param studentFile Path to the student data file
     * @param careerStaffFile Path to career staff data file
     */
    public UIManager(String studentFile, String careerStaffFile) {
        this.uiState = new UIState();
        this.screens = new ArrayList<>();
        loadDataFromCSV(studentFile, careerStaffFile);
    }

    /**
     * Start the main application loop
     */
    public void run() {
        screens.add(HomeScreen.INSTANCE);
        while(!screens.isEmpty()) {
            Screen currentScreen = screens.get(screens.size() - 1);
            Optional<Screen> nextScreen;
            try {
                nextScreen = currentScreen.show(uiState);   
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e);
                System.exit(1);
                return;
            }
            if (nextScreen.isPresent()) {
                if (nextScreen.get().equals(currentScreen)) {
                    continue;
                }
                screens.add(nextScreen.get());
            } else {
                screens.remove(screens.size() - 1);
            }
        }
    }

    /**
     * Loads data from CSV files into the DataStorage
     * @param studentFile Path to the student data file
     * @param careerStaffFile Path to career staff data file
     */
    private void loadDataFromCSV(String studentFile, String careerStaffFile) {
        try (Scanner sc2 = new Scanner(new File(studentFile))) {
            boolean firstLine = true;
            while (sc2.hasNextLine()) {
                String line = sc2.nextLine();
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] lineData = line.split(",");
                DataStorage.newStudent(lineData[0], lineData[1], Integer.parseInt(lineData[3]), lineData[2], lineData[4]); // StudentID,Name,Major,Year,Email
            }
            sc2.close();
        } catch (FileNotFoundException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.printf("Failed to read student data from file \"%s\"\n", studentFile);
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
                DataStorage.newCareerCentreStaff(lineData[0], lineData[1], lineData[3], lineData[4]); // StaffID,Name,Role,Department,Email
            }
            sc2.close();
        } catch (FileNotFoundException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.printf("Failed to read career center staff data from file \"%s\"\n", careerStaffFile);
            System.exit(1);
        }
    }
}
