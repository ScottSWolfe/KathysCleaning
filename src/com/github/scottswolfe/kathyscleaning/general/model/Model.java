package com.github.scottswolfe.kathyscleaning.general.model;

/**
 * Root class for entire project model.
 */
public class Model {

/* FIELDS =================================================================== */
    
    /**
     * The completed cleanings for this week.
     */
    private CompletedItinerary completedItinerary;
    
    /**
     * The schedule for next week.
     */
    private ScheduledItinerary scheduledItinerary;
    
    
    
    
/* CONSTRUCTORS ============================================================= */
    
    public Model() {
        completedItinerary = new CompletedItinerary();
        scheduledItinerary = new ScheduledItinerary();
    }
        
    
        
        
/* PUBLIC METHODS =========================================================== */
        
        
        
        

/* PRIVATE METHODS ========================================================== */
        
        
        
        
        
/* GETTERS/SETTERS ========================================================== */
        
    /**
     * @return the scheduledItinerary
     */
    public ScheduledItinerary getScheduledItinerary() {
        return scheduledItinerary;
    }

    /**
     * @param scheduledItinerary the scheduledItinerary to set
     */
    public void setScheduledItinerary(ScheduledItinerary scheduledItinerary) {
        this.scheduledItinerary = scheduledItinerary;
    }
        
    /**
     * @return the completedItinerary
     */
    public CompletedItinerary getCompletedItinerary() {
        return completedItinerary;
    }

    /**
     * @param completedItinerary the completedItinerary to set
     */
    public void setCompletedItinerary(CompletedItinerary completedItinerary) {
        this.completedItinerary = completedItinerary;
    }  

}
