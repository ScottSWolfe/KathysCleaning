package com.github.scottswolfe.kathyscleaning.weekend.model;

import java.util.ArrayList;
import java.util.List;

public class WeekendModel {

    private List<WeekendEntry> entries;

    public WeekendModel() {
        entries = new ArrayList<>();
    }

    /**
     * @return the entries
     */
    public List<WeekendEntry> getEntries() {
        return entries;
    }

    /**
     * @param entry the entry to add
     */
    public void addEntry(WeekendEntry entry) {
        entries.add(entry);
    }
}
