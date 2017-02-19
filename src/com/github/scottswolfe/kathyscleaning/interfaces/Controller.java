package com.github.scottswolfe.kathyscleaning.interfaces;

/**
 * A general controller interface for all controllers in this project.
 */
public interface Controller {

/* PUBLIC METHODS =========================================================== */
    
    public void readInputAndWriteToFile();
    
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
