package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.github.scottswolfe.kathyscleaning.completed.controller.CompletedControllerHelper;
import com.github.scottswolfe.kathyscleaning.covenant.controller.CovenantControllerHelper;
import com.github.scottswolfe.kathyscleaning.enums.Form;
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
     * The type of form this controller controls
     */
    private final Form form;

    /**
     * The view this controller controls
     */
    private View view;

    /**
     * The model this controller controls
     */
    private Model model;

    /**
     * The frame that contains the view
     */
    private MainFrame<View, Model> parentFrame;

    /**
     * The scroll pane containing the view that this controller controls
     */
    private JScrollPane scrollPane;

    /**
     * The helper for this controller
     */
    private final ControllerHelper<View, Model> controllerHelper;

    public static FormController<?, ?> from(final Form type) {
        return new FormController<>(type);
    }

    private FormController(final Form type) {
        form = type;

        if (type == Form.COMPLETED) {
            controllerHelper = (ControllerHelper<View, Model>) CompletedControllerHelper.from();
        } else if (type == Form.COVENANT) {
            controllerHelper = (ControllerHelper<View, Model>) new CovenantControllerHelper();
        } else if (type == Form.LBC) {
            controllerHelper = (ControllerHelper<View, Model>) new LBCControllerHelper();
        } else if (type == Form.WEEKEND) {
            controllerHelper = (ControllerHelper<View, Model>) WeekendControllerHelper.from();
        } else if (type == Form.SCHEDULED) {
            controllerHelper = (ControllerHelper<View, Model>) ScheduledControllerHelper.from();
        } else {
            throw new RuntimeException("Unexpected Form type: " + type);
        }

        model = controllerHelper.initializeModel();
        initializeView();
    }

    private void initializeView() {
        view = controllerHelper.initializeView();
        scrollPane = initializeScrollPane(view);
        parentFrame = new MainFrame<>(this);
        parentFrame.add(scrollPane);
    }

    private JScrollPane initializeScrollPane(@Nonnull final View view) {
        final JScrollPane newScrollPane = new JScrollPane();
        newScrollPane.setViewportView(view);
        newScrollPane.setBorder(null);
        return newScrollPane;
    }

    public void writeViewToTemporarySaveFile() {
        controllerHelper.readViewAndWriteToFileHook(view);
        model = controllerHelper.readViewIntoModel(view);
        controllerHelper.saveModelToFile(model, SaveFileManager.TEMP_SAVE_FILE);
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

    public void launchForm(@Nonnull final Optional<Point> centerPoint) {
        refreshWindow();
        setTitleText();
        setWindowLocation(centerPoint);
        parentFrame.setVisible(true);
    }

    public void updateDate() {
        // Writing the model to view is sufficient to update the date because the date data is now
        // only stored in the SharedDataModel which should be updated before calling this method.
        controllerHelper.writeModelToView(model, view);
    }

    public void updateWorkers(@Nonnull final List<String> workerNames) {
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

    private void setWindowLocation(@Nonnull final Optional<Point> centerPoint) {
        if (centerPoint.isPresent()) {
            setWindowLocation(centerPoint.get());
        } else {
            setToDefaultWindowLocation();
        }
    }

    private void setWindowLocation(@Nonnull final Point centerPoint) {
        final Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

        final int topLeftX = Math.max(centerPoint.x - (parentFrame.getWidth() / 2), 0);
        final int xScreenOverlap = (topLeftX + parentFrame.getWidth()) - (int) effectiveScreenSize.getWidth();
        final int adjustedTopLeftX = xScreenOverlap > 0 ? Math.max(topLeftX - xScreenOverlap, 0) : topLeftX;

        final int topLeftY = Math.max(centerPoint.y - (parentFrame.getHeight() / 2), 0);
        final int yScreenOverlap = (topLeftY + parentFrame.getHeight()) - (int) effectiveScreenSize.getHeight();
        final int adjustedTopLeftY = yScreenOverlap > 0 ? Math.max(topLeftY - yScreenOverlap, 0) : topLeftY;

        parentFrame.setLocation(adjustedTopLeftX, adjustedTopLeftY);
    }

    /**
     * The default window location is centered on the x-axis. On the y-axis 1/4 of the non-window
     * screen is above the window and 3/4 is below the window.
     */
    private void setToDefaultWindowLocation() {
        final Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

        final int centerX = (int) effectiveScreenSize.getCenterX();
        final int topLeftX = centerX - (parentFrame.getWidth() / 2);
        final int xScreenOverlap = (topLeftX + parentFrame.getWidth()) - (int) effectiveScreenSize.getWidth();
        final int adjustedTopLeftX = xScreenOverlap > 0 ? Math.max(topLeftX - xScreenOverlap, 0) : topLeftX;

        final int yDiff = effectiveScreenSize.height - parentFrame.getHeight();
        final int topLeftY = yDiff / 4;
        final int yScreenOverlap = (topLeftY + parentFrame.getHeight()) - (int) effectiveScreenSize.getHeight();
        final int adjustedTopLeftY = yScreenOverlap > 0 ? Math.max(topLeftY - yScreenOverlap, 0) : topLeftY;

        parentFrame.setLocation(adjustedTopLeftX, adjustedTopLeftY);
    }

    public void hideWindow() {
        parentFrame.setVisible(false);
    }

    public void freezeWindow() {
        parentFrame.setEnabled(false);
    }

    public void unfreezeWindow() {
        parentFrame.toFront();
        parentFrame.setEnabled(true);
    }

    public void recreateView() {
        initializeView();
        controllerHelper.writeModelToView(model, view);
    }

    public Point getCenterPoint() {
        return new Point(
            parentFrame.getWidth() / 2 + parentFrame.getX(),
            parentFrame.getHeight() / 2 + parentFrame.getY()
        );
    }

    public Form getFormType() {
        return form;
    }

    public void setTitleText() {
        parentFrame.setTitleText(this);
    }

    public View getView() {
        return view;
    }

    public Model getModel() {
        return model;
    }

    public JFrame getFrame() {
        return parentFrame;
    }
}
