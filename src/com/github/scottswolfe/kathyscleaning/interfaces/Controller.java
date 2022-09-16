package com.github.scottswolfe.kathyscleaning.interfaces;

import java.io.File;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.scottswolfe.kathyscleaning.enums.Form;

import javax.swing.JFrame;

/**
 * A general controller interface for all controllers in this project.
 */
public interface Controller<ViewObject, ModelObject> {

/* PUBLIC METHODS =========================================================== */

    /**
     * Reads the user's input from the view and writes it to a save file
     */
    public void readInputAndWriteToFile();

    /**
     * Reads a save file and writes the data into the current view
     *
     * @param file the file to read from
     */
    public void readFileAndWriteToView(File file);

    /**
     * Writes the model to the Excel template and saves it as the given file
     *
     * @param excelFile the new excel file to be created
     */
    public void writeModelToExcel(XSSFWorkbook wb);

    /**
     * Creates the window that this controller controls
     */
    public void launchForm();

    /**
     * Updates the date shown
     */
    public void updateDate();

    /**
     * Updates the worker names on the form
     */
    void updateWorkers(final List<List<String>> workerNames);

    /**
     * Disposes of the frame for the form that the controller controls
     */
    public void hideWindow();

    /**
     * Returns the form type that the controller controls
     */
    public Form getFormType();

    /**
     * Sets the title for the frame of the current window
     */
    public void setTitleText();



/* GETTERS/SETTERS ========================================================== */

    /**
     * Returns the view object for the controller
     */
    public ViewObject getView();

    public JFrame getParentFrame();
}
