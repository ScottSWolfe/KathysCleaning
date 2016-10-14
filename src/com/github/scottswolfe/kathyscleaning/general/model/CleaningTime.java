package com.github.scottswolfe.kathyscleaning.general.model;

import java.time.LocalTime;

/**
 * Represents a cleaning time. Holds a beginning time and an ending time.
 */
public class CleaningTime {

/* FIELDS =================================================================== */

    /**
     * The time the cleaning was begun.
     */
    private LocalTime beginTime;
    
    /**
     * The time the cleaning was ended.
     */
    private LocalTime endTime;

    
    
    
    
/* CONSTRUCTORS ============================================================= */
        
    public CleaningTime(LocalTime begin, LocalTime end) {
        beginTime = begin;
        endTime = end;
    }
        
    public CleaningTime() {
        this(LocalTime.of(0, 0), LocalTime.of(0, 0));
    }
    
    
    
        
/* PUBLIC METHODS =========================================================== */
        
        
        
        

/* PRIVATE METHODS ========================================================== */
        
        
        
        
        
/* GETTERS/SETTERS ========================================================== */
        
    /**
     * @return the beginTime
     */
    public LocalTime getBeginTime() {
        return beginTime;
    }

    /**
     * @param beginTime the beginTime to set
     */
    public void setBeginTime(LocalTime beginTime) {
        this.beginTime = beginTime;
    }

    /**
     * @return the endTime
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

}
