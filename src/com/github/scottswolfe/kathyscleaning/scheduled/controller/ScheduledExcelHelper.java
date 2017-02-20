package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.io.File;

import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;

public class ScheduledExcelHelper {

/* INSTANCE VARIABLES ======================================================= */

    /**
     * The data to write to the excel file
     */
    NW_Data data;
    
    /**
     * The excel file to which to write the data
     */
    File file;

    
    
/* CONSTRUCTOR ============================================================== */

    public ScheduledExcelHelper(NW_Data data, File file) {
        this.data = data;
        this.file = file;
    }
    
    
    
/* PUBLIC METHODS =+========================================================= */

    /**
     * Writes the given NW_Data object to the given excel file.
     * 
     * @param data the NW_Data object to write to the excel file
     * @param file the Excel file to write the data object to
     */
    public void writeDataToExcel() {     
        // TODO implement the function
    }
    
    
    
}
