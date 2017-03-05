package com.github.scottswolfe.kathyscleaning.general.controller;

import java.io.File;

import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.interfaces.ExcelHelper;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

@SuppressWarnings("unchecked")
/**
 * Controller for each form
 */
public class GeneralController<ViewObject, ModelObject> implements Controller {

/* INSTANCE VARIABLES ======================================================= */
    
    /**
     * The view this controller controls
     */
    private ViewObject view;
    
    /**
     * The model this controller controls
     */
    private ModelObject model;
    
    /**
     * The helper for this controller
     */
    private ControllerHelper helper;
    
    /**
     * The excel helper for this controller
     */
    private ExcelHelper excelHelper;
    
    
    
/* CLASS VARIABLES ========================================================== */

    /**
     * Current Save File
     */
    public static final File CURRENT_COMPLETED_DATA =
            new File(System.getProperty("user.dir") +
                    "\\save\\current\\currentSave");

    /**
     * Save File for Default Fill-in
     */
    public static final File DEFAULT_COMPLETED_DATA =
            new File(System.getProperty("user.dir") +
                    "\\save\\default\\defaultSave");
    
    
 
/* PUBLIC METHODS =========================================================== */
        
    @Override
    public void readInputAndWriteToFile() {
        model = (ModelObject) helper.readViewIntoModel(view);
        helper.saveToFile(model, Settings.currentSaveFile);
    }
    
    @Override
    public void readFileAndWriteToView() {
        model = (ModelObject) helper.loadFromFile(Settings.currentSaveFile);
        helper.writeModelToView(model, view);
    }
    
    /**
     * Writes Data to the excel template
     */
    public void writeModelToExcel() {
        excelHelper.writeModelToExcel(model);
    }
    
    /**
     * Writes model to the default settings file
     */
    public void readInputAndWriteToDefaultFile() {
        model = (ModelObject) helper.readViewIntoModel(view);
        helper.saveToFile(model, DEFAULT_COMPLETED_DATA);
    }
    
    
    
// GETTERS/SETTERS ---------------------------------------------------------- */
    
    @Override
    public void setView(Object obj) {
        this.view = (ViewObject) obj;
    }
    
    @Override
    public Object getView() {
        return view;
    }
    
    @Override
    public void setModel(Object obj) {
        this.model = (ModelObject) obj;
    }
    
    @Override
    public Object getModel() {
        return model;
    }
    
    public void setData(Object model) {
        this.model = (ModelObject) model;
    }
          
}
