package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.io.File;

import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;

public class ScheduledControllerHelper {

/* CLASS VARIABLES ========================================================== */
   
    /**
     * Current Scheduled Houses Save File
     */
    public static final File CURRENT_SCHEDULED_DATA =
            new File(System.getProperty("user.dir") +
                    "\\save\\current\\CurrentScheduledHouseData.txt");

    

/* PUBLIC METHODS =========================================================== */
    
    /**
     * Saves the given data object to current file in JSON format
     * 
     * @param data the data to be saved
     */
    public static void saveToFileJSON(NW_Data data) {
        JsonMethods.saveToFileJSON(data, NW_Data.class, CURRENT_SCHEDULED_DATA);
    }
    
    /**
     * Returns a data object retried from the current JSON file
     * 
     * @param data the data to be saved
     */
    public static NW_Data loadFromFileJSON() {
        return (NW_Data) JsonMethods
                .loadFromFileJSON(NW_Data.class, CURRENT_SCHEDULED_DATA);
    }
    
    /**
     * Reads User's input from the Completed GUI into a NW_Data object 
     * 
     * @param data the NW_Data object to read the data into
     * @param tp the TabbedPane containing the user input
     * @return the new NW_Data object
     */
    public static NW_Data readUserInput(TabbedPane tp) {
        // TODO implement method
        return null;
    }
    
    public static void writeDataToView(NW_Data data, TabbedPane tp) {
        // TODO implement this method
    }



}
