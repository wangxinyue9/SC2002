package Assignment_1;  // keep this line because your other files use it

import java.time.LocalDate;
import java.util.*;

public final class FilterSettings {
    public enum SortBy { ALPHA, CLOSE_DATE_ASC, CLOSE_DATE_DESC, OPEN_DATE_ASC, OPEN_DATE_DESC }

    private final Set<InternshipStatus> statuses;
    private final Set<InternshipLevel> levels;
    private final Set<String> majors;
    private final LocalDate closingBefore;
    private final LocalDate closingAfter;
    private final Boolean visibleOnly; // true=public only; false=all; null=unspecified
    private final SortBy sortBy;

    // Builder pattern (so other members can easily set filters)
    public static class Builder {
        private Set<InternshipStatus> statuses = new HashSet<>();
        private Set<InternshipLevel> levels = new HashSet<>();
        private Set<String> majors = new HashSet<>();
        private LocalDate closingBefore;
        private LocalDate closingAfter;
        private Boolean visibleOnly = null;
        private SortBy sortBy = SortBy.ALPHA;

        public Builder addStatus(InternshipStatus s){ statuses.add(s); return this; }
        public Builder addLevel(InternshipLevel l){ levels.add(l); return this; }
        public Builder addMajor(String m){ majors.add(m.toUpperCase()); return this; }
        public Builder closingBefore(LocalDate d){ closingBefore = d; return this; }
        public Builder closingAfter(LocalDate d){ closingAfter = d; return this; }
        public Builder visibleOnly(Boolean b){ visibleOnly = b; return this; }
        public Builder sortBy(SortBy s){ sortBy = s; return this; }

        public FilterSettings build(){ return new FilterSettings(this); }
    }

    private FilterSettings(Builder b){
        this.statuses = Set.copyOf(b.statuses);
        this.levels = Set.copyOf(b.levels);
        this.majors = Set.copyOf(b.majors);
        this.closingBefore = b.closingBefore;
        this.closingAfter = b.closingAfter;
        this.visibleOnly = b.visibleOnly;
        this.sortBy = b.sortBy;
    }

    // getters (used by other teams)
    public Set<InternshipStatus> statuses(){ return statuses; }
    public Set<InternshipLevel> levels(){ return levels; }
    public Set<String> majors(){ return majors; }
    public LocalDate closingBefore(){ return closingBefore; }
    public LocalDate closingAfter(){ return closingAfter; }
    public Boolean visibleOnly(){ return visibleOnly; }
    public SortBy sortBy(){ return sortBy; }
}