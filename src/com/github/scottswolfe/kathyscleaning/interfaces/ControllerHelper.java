package com.github.scottswolfe.kathyscleaning.interfaces;

import java.io.File;

public interface ControllerHelper<ViewObject, ModelObject> {

    /**
     * Reads user's input in the view into a model object 
     * 
     * @param view the view containing the user's input
     * @return the model object
     */
    public ModelObject readViewIntoModel(ViewObject view);
    
    /**
     * Writes the given model into the given view
     * 
     * @param model the model object to be written into the view
     * @param view the view in which to write the model
     */
    public void writeModelToView(ModelObject model, ViewObject view);
    
    /**
     * Saves the given model to the given file
     * 
     * @param data the data to be saved
     */
    public void saveToFile(ModelObject model, File file);
    
    /**
     * Loads a model object from the given file
     * 
     * @param file the file from which to load the object
     */
    public ModelObject loadFromFile(File file);
     
}
