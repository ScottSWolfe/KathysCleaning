package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.io.File;
import java.util.List;

import javax.annotation.Nonnull;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.github.scottswolfe.kathyscleaning.completed.controller.CompletedControllerHelper;
import com.github.scottswolfe.kathyscleaning.covenant.controller.CovenantControllerHelper;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.lbc.controller.LBCControllerHelper;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.ScheduledControllerHelper;
import com.github.scottswolfe.kathyscleaning.utility.SaveFileManager;
import com.github.scottswolfe.kathyscleaning.weekend.controller.WeekendControllerHelper;

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
     * The scroll pane containing the view that this controller controls
     */
    private final JScrollPane scrollPane;

    /**
     * The helper for this controller
     */
    private final ControllerHelper<View, Model> controllerHelper;

    /**
     * The type of form this controller controls
     */
    private final Form form;

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

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(view);
        scrollPane.setBorder(null);
        parentFrame.add(scrollPane);
    }

    public void readViewAndWriteToTemporaryFile() {
        controllerHelper.readViewAndWriteToFileHook(view);
        model = controllerHelper.readViewIntoModel(view);
        controllerHelper.saveModelToFile(model, SaveFileManager.TEMP_SAVE_FILE);
        SessionModel.save();
    }

    public void readTemporaryFileAndWriteToView() {
        readFileAndWriteToView(SaveFileManager.TEMP_SAVE_FILE);
    }

    public void readFileAndWriteToView(File file) {
        model = readFile(file);
        writeModelToView(model);
    }

    public Model readFile(@Nonnull final File file) {
        return controllerHelper.loadFromFile(file);
    }

    public void writeModelToView(@Nonnull final Model model) {
        this.model = model;
        controllerHelper.writeModelToView(model, view);
    }

    public void launchForm() {
        refreshWindow();
        setTitleText();
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

    public void refreshWindow() {
        // We need to revalidate and pack before changing anything because this may be the first
        // time the window is handled and could have a size of 0x0.
        parentFrame.revalidate();
        parentFrame.pack();

        // We need to pack the frame again after possibly updating a scroll bar policy since it may
        // have added or removed a scrollbar. We need to update the scroll bar policies and pack
        // twice because adding a scrollbar could potentially cause the window to be larger than
        // the screen in the other dimension.
        updateScrollBarPolicies();
        parentFrame.pack();
        updateScrollBarPolicies();
        parentFrame.pack();

        limitFrameSizeToScreenSize();

        parentFrame.repaint();
    }

    private void updateScrollBarPolicies() {
        final Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        final boolean isFrameWiderThanScreen = parentFrame.getWidth() > effectiveScreenSize.getWidth();
        final boolean isFrameTallerThanScreen = parentFrame.getHeight() > effectiveScreenSize.getHeight();

        if (isFrameWiderThanScreen) {
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        } else {
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        }

        if (isFrameTallerThanScreen) {
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        } else {
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        }
    }

    private void limitFrameSizeToScreenSize() {
        final Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        final boolean isFrameWiderThanScreen = parentFrame.getWidth() > effectiveScreenSize.getWidth();
        final boolean isFrameTallerThanScreen = parentFrame.getHeight() > effectiveScreenSize.getHeight();
        final double newWidth = isFrameWiderThanScreen ? effectiveScreenSize.getWidth() : parentFrame.getWidth();
        final double newHeight = isFrameTallerThanScreen ? effectiveScreenSize.getHeight() : parentFrame.getHeight();
        parentFrame.setSize(new Dimension((int) newWidth, (int) newHeight));
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
