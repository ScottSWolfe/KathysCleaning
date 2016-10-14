package com.github.scottswolfe.kathyscleaning.general.model;

import java.time.DayOfWeek;

/**
 * Abstract base class for completed and scheduled cleaning days.
 */
public abstract class CleaningDay {

/* FIELDS =================================================================== */
    
    /**
     * The day of the week for this CleaningDay.
     */
    DayOfWeek dayOfWeek;
    
    
    
/* CONSTRUCTORS ============================================================= */
     
    public CleaningDay(DayOfWeek day) {
        dayOfWeek = day;
    }



        
        
        
/* PUBLIC METHODS =========================================================== */
        
        
        
        

/* PRIVATE METHODS ========================================================== */
        
        
        
        
        
/* GETTERS/SETTERS ========================================================== */
        
    /**
     * @return the dayOfWeek
     */
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * @param dayOfWeek the dayOfWeek to set
     */
    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

}
