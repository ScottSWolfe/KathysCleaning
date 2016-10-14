package com.github.scottswolfe.kathyscleaning.general.model;

import java.time.DayOfWeek;
import java.util.ArrayList;

public class ScheduledCleaningDay extends CleaningDay {

/* FIELDS =================================================================== */

    private ArrayList<ScheduledCleaning> scheduledCleanings;
    
    
    
    
/* CONSTRUCTORS ============================================================= */
    
    public ScheduledCleaningDay(DayOfWeek day,
            ArrayList<ScheduledCleaning> scheduledCleanings) {
        super(day);
        this.scheduledCleanings = scheduledCleanings;
    }

    public ScheduledCleaningDay(DayOfWeek day) {
        super(day);
    }

        
        
    
    
    
/* PUBLIC METHODS =========================================================== */
        
        
        
        

/* PRIVATE METHODS ========================================================== */
        
        
        
        
        
/* GETTERS/SETTERS ========================================================== */
        
    /**
     * @return the scheduledCleanings
     */
    public ArrayList<ScheduledCleaning> getScheduledCleanings() {
        return scheduledCleanings;
    }

    /**
     * @param scheduledCleanings the scheduledCleanings to set
     */
    public void setScheduledCleanings(
            ArrayList<ScheduledCleaning> scheduledCleanings) {
        this.scheduledCleanings = scheduledCleanings;
    }

}
