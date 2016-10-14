package com.github.scottswolfe.kathyscleaning.general.model;

/**
 * Represents a Kathy's Cleaning employee.
 */
public class Worker {

/* FIELDS =================================================================== */
    
    /**
     * The employee's unique identifying name.
     */
    private String name; 
    
    
    
    
/* CONSTRUCTORS ============================================================= */
    
    public Worker(String name) {
        this.name = name;
    }
    
    
    
    
/* PUBLIC METHODS =========================================================== */
    
    
    
    

/* PRIVATE METHODS ========================================================== */
    
    
    
    
    
/* GETTERS/SETTERS ========================================================== */
    
    /**
     * @return the worker's name.
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name The name being set.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    
}
