package com.github.scottswolfe.kathyscleaning.general.model;

import java.util.ArrayList;

/**
 * Represents a message to a worker or to several workers.
 */
public class Note {

/* FIELDS =================================================================== */
    
    /**
     * The workers who will receive this note.
     */
    private ArrayList<Worker> workers;
    
    /**
     * The message the workers will receive.
     */
    private String message;
    
    
    
    
/* CONSTRUCTORS ============================================================= */
    
    public Note(ArrayList<Worker> workers, String message) {
        this.workers = workers;
        this.message = message;
    }
    
    public Note(Worker worker, String message) {
        ArrayList<Worker> workers = new ArrayList<Worker>();
        workers.add(worker);
        this.workers = workers;
        this.message = message;
    }
        
        
    
    
/* PUBLIC METHODS =========================================================== */
        
        
        
        

/* PRIVATE METHODS ========================================================== */
        
        
       
    
        
/* GETTERS/SETTERS ========================================================== */
        
    /**
     * @return the workers
     */
    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    /**
     * @param workers the workers to set
     */
    public void setWorkers(ArrayList<Worker> workers) {
        this.workers = workers;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
