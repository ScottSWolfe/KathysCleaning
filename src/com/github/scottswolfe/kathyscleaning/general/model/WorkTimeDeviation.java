package com.github.scottswolfe.kathyscleaning.general.model;

import java.util.HashMap;

/**
 * Contains data of workers' beginning and ending times of working that differ
 * from the rest of the crew.
 */
public class WorkTimeDeviation {

/* FIELDS =================================================================== */
    
    /**
     * A map containing workers and their cleaning times.
     */
    private HashMap<Worker, CleaningTime> map;
    
    
    
    
/* CONSTRUCTORS ============================================================= */
        
   public WorkTimeDeviation(HashMap<Worker, CleaningTime> map) {
       this.map = map;
   }

   
   
        
/* PUBLIC METHODS =========================================================== */
        
        
        
        

/* PRIVATE METHODS ========================================================== */
        
        
        
        
        
/* GETTERS/SETTERS ========================================================== */
   
   /**
    * @return the map
    */
   public HashMap<Worker, CleaningTime> getMap() {
       return map;
   }

   /**
    * @param map the map to set
    */
   public void setMap(HashMap<Worker, CleaningTime> map) {
       this.map = map;
   }
         
}
