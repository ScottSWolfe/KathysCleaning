package com.github.scottswolfe.kathyscleaning.general.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents next week's schedule.
 */
public class ScheduledItinerary extends Itinerary {
    
/* FIELDS =================================================================== */
    
    private List<ScheduledCleaning> scheduledCleanings;
    
    
    
    
/* CONSTRUCTORS ============================================================= */
        
    public ScheduledItinerary() {
        scheduledCleanings = new ArrayList<>();
    }
 
    
    
        
/* PUBLIC METHODS =========================================================== */
        
        
        
        

/* PRIVATE METHODS ========================================================== */
        
        
       
    
        
/* GETTERS/SETTERS ========================================================== */
        
    /**
     * @return the scheduledCleanings
     */
    public List<ScheduledCleaning> getScheduledCleanings() {
        return scheduledCleanings;
    }

    /**
     * @param scheduledCleanings the scheduledCleanings to set
     */
    public void setScheduledCleanings(List<ScheduledCleaning> scheduledCleanings) {
        this.scheduledCleanings = scheduledCleanings;
    }

}
