package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.Component;
import java.awt.Window;
import java.io.File;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.github.scottswolfe.kathyscleaning.lbc.controller.LBCControllerHelper;
import com.github.scottswolfe.kathyscleaning.lbc.controller.LBCExcelHelper;

import com.github.scottswolfe.kathyscleaning.completed.controller.CompletedControllerHelper;
import com.github.scottswolfe.kathyscleaning.completed.controller.CompletedExcelHelper;
import com.github.scottswolfe.kathyscleaning.covenant.controller.CovenantControllerHelper;
import com.github.scottswolfe.kathyscleaning.covenant.controller.CovenantExcelHelper;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.interfaces.ExcelHelper;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.ScheduledControllerHelper;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.ScheduledExcelHelper;
import com.github.scottswolfe.kathyscleaning.weekend.controller.WeekendControllerHelper;
import com.github.scottswolfe.kathyscleaning.weekend.controller.WeekendExcelHelper;

/**
 * Controller for each form
 */
public class FormController<View extends JComponent, Model> {

    /**
     * The view this controller controls
     */
    private final View view;

    /**
     * The model this controller controls
     */
    private Model model;

    /**
     * The frame that contains the view
     */
    private final MainFrame<View, Model> parentFrame;

    /**
     * The helper for this controller
     */
    private final ControllerHelper<View, Model> controllerHelper;

    /**
     * The type of form this controller controls
     */
    private final Form form;

    /**
     * Current Save File
     */
    public static final File TEMP_SAVE_FILE = new File(System.getProperty("user.dir") + "\\save\\temp\\currentSave");

    public static FormController<?, ?> from(final Form type) {
        return new FormController<>(type);
    }

    private FormController(final Form type) {
        form = type;

        if (type == Form.COMPLETED) {
            controllerHelper = (ControllerHelper<View, Model>) new CompletedControllerHelper();
        } else if (type == Form.COVENANT) {
            controllerHelper = (ControllerHelper<View, Model>) new CovenantControllerHelper();
        } else if (type == Form.LBC) {
            controllerHelper = (ControllerHelper<View, Model>) new LBCControllerHelper();
        } else if (type == Form.WEEKEND) {
            controllerHelper = (ControllerHelper<View, Model>) new WeekendControllerHelper();
        } else if (type == Form.SCHEDULED) {
            controllerHelper = (ControllerHelper<View, Model>) new ScheduledControllerHelper();
        } else {
            throw new RuntimeException("Unexpected Form type: " + type);
        }

        parentFrame = new MainFrame<>(this);
        model = controllerHelper.initializeModel();
        view = controllerHelper.initializeView(this, parentFrame);

        parentFrame.add(view);
    }

    public void readInputAndWriteToFile() {
        controllerHelper.readViewAndWriteToFileHook(view);
        model = controllerHelper.readViewIntoModel(view);
        controllerHelper.saveModelToFile(model, TEMP_SAVE_FILE);
        SessionModel.save();
    }

    public void readFileAndWriteToView(File file) {
        model = controllerHelper.loadFromFile(file);
        controllerHelper.writeModelToView(model, view);
    }

    public void launchForm() {
        parentFrame.revalidate();
        parentFrame.pack();
        parentFrame.repaint();
        parentFrame.setLocationRelativeTo(null);
        parentFrame.setVisible(true);
    }

    public void updateDate() {
        controllerHelper.updateDate(this, view);
    }

    public void updateWorkers(final List<List<String>> workerNames) {
        controllerHelper.updateWorkersOnModel(model, workerNames);
        controllerHelper.writeModelToView(model, view);
    }

    public void hideWindow() {
        Window window = SwingUtilities.getWindowAncestor(view);
        window.setVisible(false);
    }

    public Form getFormType() {
        return form;
    }

    public void setTitleText() {
        final MainFrame<View, Model> frame = (MainFrame<View, Model>) SwingUtilities.getWindowAncestor((Component) view);
        frame.setTitleText(this);
    }

    public View getView() {
        return view;
    }

    public JFrame getParentFrame() {
        return parentFrame;
    }
}
