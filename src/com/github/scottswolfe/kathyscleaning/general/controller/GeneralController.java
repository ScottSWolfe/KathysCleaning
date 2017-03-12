package com.github.scottswolfe.kathyscleaning.general.controller;

import java.io.File;
import java.util.Calendar;

import com.github.scottswolfe.kathyscleaning.completed.controller.CompletedControllerHelper;
import com.github.scottswolfe.kathyscleaning.completed.controller.CompletedExcelHelper;
import com.github.scottswolfe.kathyscleaning.covenant.controller.CovenantControllerHelper;
import com.github.scottswolfe.kathyscleaning.covenant.controller.CovenantExcelHelper;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.interfaces.ExcelHelper;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.ScheduledControllerHelper;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.ScheduledExcelHelper;
import com.github.scottswolfe.kathyscleaning.weekend.controller.WeekendControllerHelper;

/**
 * Controller for each form
 */
public class GeneralController<ViewObject, ModelObject>
             implements Controller<ViewObject, ModelObject> {

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
    private ControllerHelper<ViewObject, ModelObject> helper;
    
    /**
     * The excel helper for this controller
     */
    private ExcelHelper<ModelObject> excelHelper;
    
    /**
     * True if currently loading file into view; false otherwise
     */
    private boolean openingFile;
    
    
    
/* CLASS VARIABLES ========================================================== */

    /**
     * Current Save File
     */
    public static final File TEMP_SAVE_FILE =
            new File(System.getProperty("user.dir") +
                    "\\save\\temp\\currentSave");
    
    /**
     * Save File for Default Fill-in
     */
    public static final File DEFAULT_FILL_IN_SAVE_FILE =
            new File(System.getProperty("user.dir") +
                    "\\save\\default\\defaultSave");
    
    
 
/* CONSTRUCTORS ============================================================= */
    
    @SuppressWarnings("unchecked")
    public GeneralController(Form type) {
        if (type == Form.COMPLETED) {
            helper = (ControllerHelper<ViewObject, ModelObject>) new CompletedControllerHelper();
            excelHelper = (ExcelHelper<ModelObject>) new CompletedExcelHelper();
        } else if (type == Form.COVENANT) {
            helper = (ControllerHelper<ViewObject, ModelObject>) new CovenantControllerHelper();
            excelHelper = (ExcelHelper<ModelObject>) new CovenantExcelHelper();
        } else if (type == Form.WEEKEND) {
            helper = (ControllerHelper<ViewObject, ModelObject>) new WeekendControllerHelper();
            excelHelper = (ExcelHelper<ModelObject>) new WeekendControllerHelper();
        } else if (type == Form.SCHEDULED) {
            helper = (ControllerHelper<ViewObject, ModelObject>) new ScheduledControllerHelper();
            excelHelper = (ExcelHelper<ModelObject>) new ScheduledExcelHelper();
        } else {
            throw new RuntimeException("unexpected Form type");
        }
    }
    
    
    
/* PUBLIC METHODS =========================================================== */
        
    @Override
    public void readInputAndWriteToFile(File file) {
        model = helper.readViewIntoModel(view);
        helper.saveToFile(model, TEMP_SAVE_FILE);
        if (!TEMP_SAVE_FILE.equals(file) && file != null) {
            helper.saveToFile(model, file);
        }
    }
    
    @Override
    public void readFileAndWriteToView(File file) {
        model = helper.loadFromFile(file);
        helper.writeModelToView(model, view);
        if (!TEMP_SAVE_FILE.equals(file)) {
            helper.saveToFile(model, TEMP_SAVE_FILE);
        }
    }
    
    @Override
    public void initializeForm(Calendar date, int mode, int wk) {
        helper.initializeForm(date, mode, wk);
    }
    
    @Override
    public void writeModelToExcel(File excelFile) {
        excelHelper.writeModelToExcel(model, excelFile);
    }
    
    /**
     * @return true if currently loading file into view; false otherwise
     */
    public boolean isOpeningFile() {
        return openingFile;
    }
    
    
    
// GETTERS/SETTERS ---------------------------------------------------------- */
    
    @Override
    public void setView(ViewObject obj) {
        this.view = (ViewObject) obj;
    }
    
    @Override
    public ViewObject getView() {
        return view;
    }
    
    @Override
    public void setModel(ModelObject obj) {
        this.model = (ModelObject) obj;
    }
    
    @Override
    public ModelObject getModel() {
        return model;
    }
    
    public void setOpeningFile(boolean opening) {
        openingFile = opening;
    }
    
    public void setControllerHelper(ControllerHelper<ViewObject, ModelObject>
                                                                 helper) {
        this.helper = helper;
    }
      
    public void setExcelHelper(ExcelHelper<ModelObject> excelHelper) {
        this.excelHelper = excelHelper;
    }
    
}
