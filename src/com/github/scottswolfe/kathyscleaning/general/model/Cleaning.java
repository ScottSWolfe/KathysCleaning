package com.github.scottswolfe.kathyscleaning.general.model;

/*
 * Abstract base class for completed and scheduled cleanings.
 */
public abstract class Cleaning {

    
    
/* FIELDS =================================================================== */
    
    /**
     * Unique name for this cleaning.
     */
    private String name;


    
    
    
/* CONSTRUCTOR ============================================================== */

    /**
     * 
     */
    public Cleaning(String name) {
        this.name = name;
    }
    
    
    
    
/* GETTERS/SETTERS ========================================================== */
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
}
