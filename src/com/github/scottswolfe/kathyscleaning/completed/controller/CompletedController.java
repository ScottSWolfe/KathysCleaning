package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.io.File;

import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

/**
 * Controller for the completed houses panel
 */
public class CompletedController implements Controller {

/* INSTANCE VARIABLES ======================================================= */
    
    /**
     * The view this controller controls
     */
    private TabbedPane tp;
    
    /**
     * The model this controller controls
     */
    private Data data;
    
    /**
     * True if currently loading file; false otherwise
     */
    private boolean openingFile; 
    
    
    
/* CLASS VARIABLES ========================================================== */

    /**
     * Current Completed Houses Save File
     */
    public static final File CURRENT_COMPLETED_DATA =
            new File(System.getProperty("user.dir") +
                    "\\save\\current\\CurrentCompletedHouseData.txt");

    /**
     * Current Completed Houses Save File
     */
    public static final File DEFAULT_COMPLETED_DATA =
            new File(System.getProperty("user.dir") +
                    "\\save\\default\\DefaultCompletedHouseData.txt");

    
 
/* PUBLIC METHODS =========================================================== */
    
    @Override
    public void readInputAndWriteToFile() {
        data = CompletedControllerHelper.readUserInput(tp);
        CompletedControllerHelper.saveToFileJSON(data, CURRENT_COMPLETED_DATA);
    }
    
    @Override
    public void readFileAndWriteToView() {
        data = CompletedControllerHelper
                            .loadFromFileJSON(CURRENT_COMPLETED_DATA);
        CompletedControllerHelper.writeDataToView(data, tp);
    }
    
    /**
     * Writes Data to the excel template
     */
    public void writeDataToExcel() {
        File file = Settings.getExcelTemplateFile();
        CompletedExcelHelper helper = new CompletedExcelHelper(data, file);
        helper.writeDataToExcel();
    }
    
    /**
     * Writes data to the default settings file
     */
    public void readInputAndWriteToDefaultFile() {
        data = CompletedControllerHelper.readUserInput(tp);
        CompletedControllerHelper.saveToFileJSON(data, DEFAULT_COMPLETED_DATA);
    }
    
    /**
     * Returns true if currently opening a file; false otherwise
     */
    public boolean isOpeningFile() {
        return openingFile;
    }
    
    
// GETTERS/SETTERS ---------------------------------------------------------- */
    
    @Override
    public void setView(Object obj) {
        this.tp = (TabbedPane) obj;
    }
    
    @Override
    public Object getView() {
        return tp;
    }
    
    @Override
    public void setModel(Object obj) {
        this.data = (Data) obj;
    }
    
    @Override
    public Object getModel() {
        return data;
    }
    
    public void setData(Data data) {
        this.data = data;
    }
    
    public Data getData() {
        return data;
    }
    
    public void setTabbedPane(TabbedPane tp) {
        this.tp = tp;
    }
    
    public TabbedPane getTabbedPane() {
        return tp;
    }
    
    public void setOpeningFile(boolean openingFile) {
        this.openingFile = openingFile;
    }
    
    
    
}
