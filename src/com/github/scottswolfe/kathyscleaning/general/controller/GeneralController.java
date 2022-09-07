package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.SwingUtilities;

import com.github.scottswolfe.kathyscleaning.lbc.controller.LBCControllerHelper;
import com.github.scottswolfe.kathyscleaning.lbc.controller.LBCExcelHelper;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.scottswolfe.kathyscleaning.completed.controller.CompletedControllerHelper;
import com.github.scottswolfe.kathyscleaning.completed.controller.CompletedExcelHelper;
import com.github.scottswolfe.kathyscleaning.covenant.controller.CovenantControllerHelper;
import com.github.scottswolfe.kathyscleaning.covenant.controller.CovenantExcelHelper;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.interfaces.ExcelHelper;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.ScheduledControllerHelper;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.ScheduledExcelHelper;
import com.github.scottswolfe.kathyscleaning.weekend.controller.WeekendControllerHelper;
import com.github.scottswolfe.kathyscleaning.weekend.controller.WeekendExcelHelper;

/**
 * Controller for each form
 */
public class GeneralController<View, Model> implements Controller<View, Model> {

    /**
     * The view this controller controls
     */
    private View view;

    /**
     * The model this controller controls
     */
    private Model model;

    /**
     * The helper for this controller
     */
    private ControllerHelper<View, Model> helper;

    /**
     * The excel helper for this controller
     */
    private ExcelHelper<Model> excelHelper;

    /**
     * True if currently loading file into view; false otherwise
     */
    private boolean openingFile;

    /**
     * The type of form this controller controls
     */
    Form form;



/* CLASS VARIABLES ========================================================== */

    /**
     * Current Save File
     */
    public static final File TEMP_SAVE_FILE =
            new File(System.getProperty("user.dir") +
                    "\\save\\temp\\currentSave");



/* CONSTRUCTORS ============================================================= */

    @SuppressWarnings("unchecked")
    public GeneralController(Form type) {
        form = type;
        if (type == Form.COMPLETED) {
            helper = (ControllerHelper<View, Model>) new CompletedControllerHelper();
            excelHelper = (ExcelHelper<Model>) new CompletedExcelHelper();
        } else if (type == Form.COVENANT) {
            helper = (ControllerHelper<View, Model>) new CovenantControllerHelper();
            excelHelper = (ExcelHelper<Model>) new CovenantExcelHelper();
        } else if (type == Form.LBC) {
            helper = (ControllerHelper<View, Model>) new LBCControllerHelper();
            excelHelper = (ExcelHelper<Model>) new LBCExcelHelper();
        } else if (type == Form.WEEKEND) {
            helper = (ControllerHelper<View, Model>) new WeekendControllerHelper();
            excelHelper = (ExcelHelper<Model>) new WeekendExcelHelper();
        } else if (type == Form.SCHEDULED) {
            helper = (ControllerHelper<View, Model>) new ScheduledControllerHelper();
            excelHelper = (ExcelHelper<Model>) new ScheduledExcelHelper();
        } else {
            throw new RuntimeException("unexpected Form type");
        }
    }



/* PUBLIC METHODS =========================================================== */

    @Override
    public void readInputAndWriteToFile(File file) {
        helper.readInputAndWriteToFileHook();
        model = helper.readViewIntoModel(view);
        helper.saveToFile(model, TEMP_SAVE_FILE);
        SessionModel.save(TEMP_SAVE_FILE);
        if (!TEMP_SAVE_FILE.equals(file) && file != null) {
            try {
                Files.copy(TEMP_SAVE_FILE.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void readFileAndWriteToView(File file) {
        model = helper.loadFromFile(file);
        helper.writeModelToView(model, view);
    }

    @Override
    public void initializeForm() {
        helper.initializeForm(this);
    }

    @Override
    public void writeModelToExcel(XSSFWorkbook wb) {
        excelHelper.writeModelToExcel(model, wb);
    }

    @Override
    public void updateDate() {
        helper.updateDate(view);
    }

    @Override
    public void eliminateWindow() {
        helper.eliminateWindow(view);
    }

    @Override
    public Form getFormType() {
        return form;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setTitleText() {
        @SuppressWarnings("rawtypes")
        MainFrame frame = (MainFrame) SwingUtilities.getWindowAncestor((Component) view);
        frame.setTitleText(this);
    }


    /**
     * @return true if currently loading file into view; false otherwise
     */
    public boolean isOpeningFile() {
        return openingFile;
    }



// GETTERS/SETTERS ---------------------------------------------------------- */

    @Override
    public void setView(View obj) {
        this.view = (View) obj;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void setModel(Model obj) {
        this.model = (Model) obj;
    }

    @Override
    public Model getModel() {
        return model;
    }

    public void setOpeningFile(boolean opening) {
        openingFile = opening;
    }

    public void setControllerHelper(ControllerHelper<View, Model>
                                                                 helper) {
        this.helper = helper;
    }

    public void setExcelHelper(ExcelHelper<Model> excelHelper) {
        this.excelHelper = excelHelper;
    }

}
