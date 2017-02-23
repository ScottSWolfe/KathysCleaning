package com.github.scottswolfe.kathyscleaning.interfaces;

/**
 * A general controller interface for all controllers in this project.
 */
public interface Controller {

/* PUBLIC METHODS =========================================================== */
    
    /**
     * Reads the user's input from the view and writes it to a save file
     */
    public void readInputAndWriteToFile();
    
    /**
     * Reads a save file and writes the data into the current view
     */
    public void readFileAndWriteToView();
    
    
    
/* GETTERS/SETTERS ========================================================== */
    
    /**
     * Sets the view object for the controller
     * 
     * @param obj the view
     */
    public void setView(Object obj);
    
    /**
     * Returns the view object for the controller
     */
    public Object getView();
    
    /**
     * Sets the model object for the controller
     * 
     * @param obj the model
     */
    public void setModel(Object obj);
    
    /**
     * Returns the model object for the controller
     */
    public Object getModel();
}
