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
    
    /**
     * Whether the worker is selected or not
     */
    private boolean isSelected;
    
    
/* CONSTRUCTORS ============================================================= */
    
    public Worker() {
        name = "";
        isSelected = false;
    }    
    
    public Worker(String name) {
        this.name = name;
        isSelected = false;
    }
    
    public Worker(String name, boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;
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

    /**
     * @return the isSelected
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * @param isSelected the isSelected to set
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    
}
