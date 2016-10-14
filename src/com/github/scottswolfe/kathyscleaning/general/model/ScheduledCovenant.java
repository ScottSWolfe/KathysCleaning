package com.github.scottswolfe.kathyscleaning.general.model;

import java.util.ArrayList;

public class ScheduledCovenant extends ScheduledCleaning {
    
/* FIELDS =================================================================== */
    
    
    
    
/* CONSTRUCTORS ============================================================= */
        
    public ScheduledCovenant(String name, ArrayList<Worker> workers) {
        super(name, workers);
    }

    public ScheduledCovenant() {
        super("Covenant", new ArrayList<Worker>());
    }
        
        
/* PUBLIC METHODS =========================================================== */
        
        
        
        

/* PRIVATE METHODS ========================================================== */
        
        
        
        
        
/* GETTERS/SETTERS ========================================================== */
        
        
        
        

}
