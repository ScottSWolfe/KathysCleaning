package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.io.File;

import com.github.scottswolfe.kathyscleaning.completed.controller.CompletedControllerHelper;
import com.github.scottswolfe.kathyscleaning.completed.controller.CompletedExcelHelper;
import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.persistance.Savable;

/**
 * Controller for the scheduled houses panel
 */
public class ScheduledController implements Controller {
   
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
        data = ScheduledControllerHelper.readUserInput(tp);
        ScheduledControllerHelper.saveToFileJSON(data);
    }
    
    @Override
    public void readFileAndWriteToView() {
        data = ScheduledControllerHelper.loadFromFileJSON();
        ScheduledControllerHelper.writeDataToView(data, tp);
    }
        
    /**
     * Writes Data to the excel template
     */
    public void writeDataToExcel() {
        File file = Settings.getExcelTemplateFile();
        ScheduledExcelHelper helper = new ScheduledExcelHelper(data, file);
        helper.writeDataToExcel();
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
    
    
    
}
