package Assignment_1;  // keep this line because your other files use it

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FilterSettings {
    public enum Level { BASIC, INTERMEDIATE, ADVANCED }
    public enum Status { PENDING, APPROVED, REJECTED, FILLED }

    private Set<Status> statuses = new HashSet<>();
    private Set<Level> levels = new HashSet<>();
    private Set<String> majors = new HashSet<>();
    private LocalDate closingBefore;
    private LocalDate closingAfter;
    private boolean visibleOnly = true;

    public FilterSettings() {}

    // Example setters
    public void addStatus(Status s) { statuses.add(s); }
    public void addLevel(Level l) { levels.add(l); }
    public void addMajor(String m) { majors.add(m.toUpperCase()); }
    public void setClosingBefore(LocalDate d) { closingBefore = d; }
    public void setClosingAfter(LocalDate d) { closingAfter = d; }
    public void setVisibleOnly(boolean b) { visibleOnly = b; }

    // Example filter logic (works with a list of Internship objects)
    public List<Internship> apply(List<Internship> internships) {
        return internships.stream()
            .filter(i -> statuses.isEmpty() || statuses.contains(i.getStatus()))
            .filter(i -> levels.isEmpty() || levels.contains(i.getLevel()))
            .filter(i -> majors.isEmpty() || majors.contains(i.getPreferredMajor().toUpperCase()))
            .filter(i -> closingBefore == null || !i.getCloseDate().isAfter(closingBefore))
            .filter(i -> closingAfter == null || !i.getCloseDate().isBefore(closingAfter))
            .filter(i -> !visibleOnly || i.isVisible())
            .sorted(Comparator.comparing(Internship::getCloseDate))
            .collect(Collectors.toList());
    }
}
