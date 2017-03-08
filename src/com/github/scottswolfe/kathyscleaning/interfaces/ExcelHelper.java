package com.github.scottswolfe.kathyscleaning.interfaces;

import java.io.File;

public interface ExcelHelper {

    /**
     * Writes the given model into the excel template
     * 
     * @param model the model to write into the excel template
     * @param file the excel template file
     */
    public void writeModelToExcel(Object model, File file);
    
}
