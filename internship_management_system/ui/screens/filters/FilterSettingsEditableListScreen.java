package internship_management_system.ui.screens.filters;

import internship_management_system.ui.Screen;
import java.util.ArrayList;
import java.util.Optional;
import internship_management_system.ui.IO;
import java.util.HashSet;
import java.util.function.Consumer;

/**
 * Specialized screen for editable lists.
 */

public interface FilterSettingsEditableListScreen extends Screen {
    /**
     * Handles the editing of a list of items for filter settings.
     * @param name The name of the list being edited
     * @param currentItems current items in the list
     * @param addItem Consumer to add an item
     * @param removeItem Consumer to remove an item
     * @param resetItems Runnable to reset the list
     * @return returns true if the screen should be shown again, false to go back
     */
    default boolean handleListEditing(String name, Optional<HashSet<String>> currentItems, Consumer<String> addItem, Consumer<String> removeItem, Runnable resetItems) {
        // TODO: encapsulation
        System.out.println("Current " + name + "(s):");
        ArrayList<String> itemsList = new ArrayList<>();
        if(currentItems.isPresent()) {
            itemsList.addAll(currentItems.get());
        } else {
            System.out.println("ALL");
        }
        for (int i = 0; i < itemsList.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, itemsList.get(i));
        }

        String lower_name = String.join("-", name.toLowerCase().split(" "));
        System.out.println("");
        System.out.printf("To add a %s, type '+ <%s>'\n", name.toLowerCase(), lower_name);
        System.out.printf("To remove a %s, type '- <%s-number-in-list>'\n", name.toLowerCase(), lower_name);
        System.out.println("Type * to reset");
        System.out.println("Type 0 to go back");

        System.out.print("Your input: ");
        String input = IO.getScanner().nextLine().trim();
        if (input.startsWith("+ ")) {
            String toAdd = input.substring(2).trim();
            addItem.accept(toAdd);
        } else if (input.startsWith("- ")) {
            try {
                int i = Integer.parseInt(input.substring(2).trim());
                i--;
                if (i >= 0 && i < itemsList.size()) {
                    removeItem.accept(itemsList.get(i));
                }
            } catch (NumberFormatException e) {
            }
        } else if (input.equals("*")) {
            resetItems.run();
        } else if (input.equals("0")) {
            return false;
        }
        return true;
    }
}