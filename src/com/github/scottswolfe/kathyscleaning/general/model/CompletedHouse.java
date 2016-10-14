package com.github.scottswolfe.kathyscleaning.general.model;

import java.util.ArrayList;

/**
 * @author Scott
 * Edited 9/23/2016
 * 
 * Represents a house that was cleaned previously. 
 */
public class CompletedHouse extends CompletedCleaning {
    
/* FIELDS =================================================================== */
    
    
    
    
/* CONSTRUCTORS ============================================================= */
    
    public CompletedHouse(String name, ArrayList<Worker> workers,
            double amountPaid, CleaningTime time) {
        super(name, workers, amountPaid, time);
    }
    
    public CompletedHouse() {
        super();
    }    

        
        

/* PUBLIC METHODS =========================================================== */
        
   
    
        
        
/* PRIVATE METHODS ========================================================== */
        
        
        
        
        
/* GETTERS/SETTERS ========================================================== */
        
        
        
        
   
}
