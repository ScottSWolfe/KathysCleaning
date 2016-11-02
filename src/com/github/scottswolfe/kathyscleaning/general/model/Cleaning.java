package com.github.scottswolfe.kathyscleaning.general.model;

import java.util.ArrayList;
import java.util.List;

/*
 * Abstract base class for completed and scheduled cleanings.
 */
public abstract class Cleaning {

    
    
/* FIELDS =================================================================== */
    
    /**
     * Unique name for this cleaning.
     */
    private String name;

    /**
     * The employees who work this cleaning.
     */
    private List<Worker> workers;

    
    
    
/* CONSTRUCTOR ============================================================== */

    /**
     * No-arg Constructor.
     */
    public Cleaning() {
        this("unknown", new ArrayList<Worker>());
    }
    
    /**
     * Constructor for typical use.
     */
    public Cleaning(String name, ArrayList<Worker> workers) {
        this.name = name;
        this.workers = workers;
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
    
    /**
     * @return the workers
     */
    public List<Worker> getWorkers() {
        return workers;
    }

    /**
     * @param workers the workers to set
     */
    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    } 

}
