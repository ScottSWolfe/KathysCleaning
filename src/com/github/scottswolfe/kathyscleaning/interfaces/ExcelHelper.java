package com.github.scottswolfe.kathyscleaning.interfaces;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface ExcelHelper<ModelObject> {

    /**
     * Writes the given model into the excel template
     * 
     * @param model the model to write into the excel template
     * @param file the excel template file
     */
    public void writeModelToExcel(ModelObject model, XSSFWorkbook wb);
    
}
