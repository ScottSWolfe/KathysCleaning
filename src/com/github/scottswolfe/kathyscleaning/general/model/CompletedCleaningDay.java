package com.github.scottswolfe.kathyscleaning.general.model;

import java.time.DayOfWeek;
import java.util.ArrayList;

public class CompletedCleaningDay extends CleaningDay {

/* FIELDS =================================================================== */

    private ArrayList<CompletedCleaning> completedCleanings;
    
    
    
    
/* CONSTRUCTORS ============================================================= */
        
    public CompletedCleaningDay(DayOfWeek day,
            ArrayList<CompletedCleaning> completedCleanings) {
        super(day);
        this.completedCleanings = completedCleanings;
    }


    
    
/* PUBLIC METHODS =========================================================== */
        
        
        
        

/* PRIVATE METHODS ========================================================== */
        
        
        
        
        
/* GETTERS/SETTERS ========================================================== */
        
    /**
     * @return the completedCleanings
     */
    public ArrayList<CompletedCleaning> getCompletedCleanings() {
        return completedCleanings;
    }

    /**
     * @param completedCleanings the completedCleanings to set
     */
    public void setCompletedCleanings(
            ArrayList<CompletedCleaning> completedCleanings) {
        this.completedCleanings = completedCleanings;
    }     

}
