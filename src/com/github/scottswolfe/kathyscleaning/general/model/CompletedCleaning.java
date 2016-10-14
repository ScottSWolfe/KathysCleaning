package com.github.scottswolfe.kathyscleaning.general.model;

import java.util.ArrayList;

public class CompletedCleaning extends Cleaning {

/* FIELDS =================================================================== */
    
    /**
     * Amount of money paid for this completed cleaning.
     */
    private double amountPaid;
    
    /**
     * The time period for this cleaning.
     */
    private CleaningTime cleaningTime;
    
    /**
     * The work time deviations for workers who worked a different time. 
     */
    private ArrayList<WorkTimeDeviation> timeDeviations;
    
    
    
    
/* CONSTRUCTORS ============================================================= */
        
    public CompletedCleaning(String name, ArrayList<Worker> workers,
            double amountPaid, CleaningTime time) {
        super(name, workers);
        this.amountPaid = amountPaid;
        cleaningTime = time;
    }

    public CompletedCleaning() {
        super();
        amountPaid = 0;
        cleaningTime = new CleaningTime();
    }

        
        
    
    
/* PUBLIC METHODS =========================================================== */
        
        
        
        

/* PRIVATE METHODS ========================================================== */
        
        
        
        
        
/* GETTERS/SETTERS ========================================================== */
        
    /**
     * @return the amountPaid
     */
    public double getAmountPaid() {
        return amountPaid;
    }

    /**
     * @param amountPaid the amountPaid to set
     */
    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    /**
     * @return the cleaningTime
     */
    public CleaningTime getCleaningTime() {
        return cleaningTime;
    }

    /**
     * @param cleaningTime the cleaningTime to set
     */
    public void setCleaningTime(CleaningTime cleaningTime) {
        this.cleaningTime = cleaningTime;
    }

    /**
     * @return the timeDeviations
     */
    public ArrayList<WorkTimeDeviation> getTimeDeviations() {
        return timeDeviations;
    }

    /**
     * @param timeDeviations the timeDeviations to set
     */
    public void setTimeDeviations(ArrayList<WorkTimeDeviation> timeDeviations) {
        this.timeDeviations = timeDeviations;
    }
    
}
