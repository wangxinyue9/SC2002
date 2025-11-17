package internship_management_system.ui.screens.filters;

import internship_management_system.ui.Screen;
import internship_management_system.ui.IO;

/**
 * Specialized screen for toggleable lists.
 */
public interface FilterSettingsToggleableListScreen extends Screen {
    /**
     * Handles the toggling of a list of items for filter settings.
     * @param name The name of the list being edited
     * @param items The items in the list
     * @param currentValue current boolean values for each item
     * @param toggle array of Runnables to toggle each item
     * @param reset Runnable to reset the list
     * @return returns true if the screen should be shown again, false to go back
     */
    default boolean handleToggleableList(String name, String[] items, boolean[] currentValue, Runnable[] toggle, Runnable reset) {
        if(currentValue.length != items.length || toggle.length != items.length) {
            IO.exitWithError("mismatched toggleable list lengths");
        }
        System.out.println("Current " + name + " Filter:");
        for (int i = 0; i < items.length; i++) {
            String status = currentValue[i] ? "[X]" : "[ ]";
            System.out.printf("%d. %s %s\n", i + 1, status, items[i]);
        }

        System.out.println("");
        System.out.println("Type the number to toggle the level");
        System.out.println("Type * to reset");
        System.out.println("Type 0 to go back");

        System.out.print("Your input: ");
        String input = IO.getScanner().nextLine().trim();
        switch (input) {
            case "*" -> reset.run();
            case "0" -> {
                return false;
            }
            default -> {
                try {
                    int i = Integer.parseInt(input);
                    i--;
                    if (i >= 0 && i < items.length) {
                        toggle[i].run();
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        return true;
    }
}
