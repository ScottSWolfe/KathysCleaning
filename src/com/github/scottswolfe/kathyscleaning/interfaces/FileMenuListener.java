package com.github.scottswolfe.kathyscleaning.interfaces;

public interface FileMenuListener {

/* PUBLIC METHODS =========================================================== */
    
    /**
     * This method is triggered when the user selects save in the file menu
     */
    public void menuItemSave();
    
    /**
     * This method is triggered when the user selects save as in the file menu
     */
    public void menuItemSaveAs();
    
    /**
     * This method is triggered when the user selects open in the file menu
     */
    public void menuItemOpen();
    
    
}
