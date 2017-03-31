package com.github.scottswolfe.kathyscleaning.weekend.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;

public class WeekendModel {

    private Calendar date;
    private List<WeekendEntry> entries;
    
    public WeekendModel() {
        entries = new ArrayList<WeekendEntry>();
        date = SessionModel.getCompletedStartDay();
        date.add(Calendar.DATE, -1);
    }

    /**
     * @return the entries
     */
    public List<WeekendEntry> getEntries() {
        return entries;
    }

    /**
     * @param entries the entries to set
     */
    public void addEntry(WeekendEntry entry) {
        entries.add(entry);
    }

    /**
     * @return the date
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Calendar date) {
        this.date = date;
    }
    
}
