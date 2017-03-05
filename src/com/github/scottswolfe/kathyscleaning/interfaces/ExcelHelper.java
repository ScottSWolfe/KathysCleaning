package com.github.scottswolfe.kathyscleaning.interfaces;

public interface ExcelHelper {

    /**
     * Writes the given model into the excel template
     * 
     * @param model the model to write into the excel template
     */
    public void writeModelToExcel(Object model);
    
}
