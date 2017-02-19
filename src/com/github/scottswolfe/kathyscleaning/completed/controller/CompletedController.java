package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.io.File;

import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;

/**
 * Controller for the completed houses panel
 */
public class CompletedController implements Controller/*, Savable*/ {

/* INSTANCE VARIABLES ======================================================= */
    
    /**
     * The view this controller controls
     */
    private TabbedPane tp;
    
    /**
     * The model this controller controls
     */
    private Data data;
    
    
 
/* PUBLIC METHODS =========================================================== */
    
    @Override
    public void readInputAndWriteToFile() {
        data = CompletedControllerHelper.readUserInput(tp);
        //CompletedControllerHelper.saveToFileJSON(data);
        JsonMethods.saveToFileJSON(data, Data.class, new File(System.getProperty("user.dir") +
                    "\\save\\current\\CurrentCompletedHouseData.txt"));
    }
    
    @Override
    public void readFileAndWriteToView() {
        //data = CompletedControllerHelper.loadFromFileJSON();
        data = (Data) JsonMethods.loadFromFileJSON(Data.class, new File(System.getProperty("user.dir") +
                    "\\save\\current\\CurrentCompletedHouseData.txt"));
        CompletedControllerHelper.writeDataToView(data, tp);
    }
    
    /**
     * Reads the user's input into the Data object
     */
    //public void readUserInput() {
    //    data = CompletedControllerHelper.readUserInput(tp);
    //}
    
    /**
     * Writes Data to the excel template
     */
    public void writeDataToExcel() {
        File file = Settings.getExcelTemplateFile();
        CompletedExcelHelper helper = new CompletedExcelHelper(data, file);
        helper.writeDataToExcel();
    }
/*    
    @Override
    public void saveToFile() {
        data = readUserInput();
        CompletedControllerHelper.saveToFileJSON(data);
    }

    @Override
    public void loadFromFile() {
        data = CompletedControllerHelper.loadFromFileJSON();
        // TODO implement a clearView() method ???
        writeDataToView();
    }
  */  

    
    
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
    
    
    
}
