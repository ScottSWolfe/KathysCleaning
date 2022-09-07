package com.github.scottswolfe.kathyscleaning.lbc.controller;

import com.github.scottswolfe.kathyscleaning.enums.DayOfWeek;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.controller.SubmitFormListener;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCDay;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCHeader;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCModel;
import com.github.scottswolfe.kathyscleaning.lbc.view.LBCPanel;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.util.Map;
import java.util.stream.Collectors;

public class LBCControllerHelper implements ControllerHelper<LBCPanel, LBCModel> {

    @Override
    public LBCModel readViewIntoModel(final LBCPanel view) {

        final LBCHeader lbcHeader = LBCHeader.from(view.getHeaderWorkerSelectionGrid());

        final Map<DayOfWeek, LBCDay> lbcDays = view.getLBCDayPanels().stream()
            .map(lbcDayPanel -> LBCDay.from(
                lbcDayPanel.getDayOfWeek(),
                lbcDayPanel.getAmountEarned(),
                lbcDayPanel.getBeginTime(),
                lbcDayPanel.getEndTime(),
                lbcDayPanel.getWorkerSelectionGrid(),
                lbcDayPanel.getExceptionData()
            ))
            .collect(Collectors.toMap(LBCDay::getDayOfWeek, lbcDay -> lbcDay));

        return LBCModel.from(lbcHeader, lbcDays);
    }

    @Override
    public void writeModelToView(final LBCModel model, final LBCPanel view) {

        view.setHeaderWorkerSelectionGrid(model.getLbcHeader().getWorkerSelectionGrid());

        view.getLBCDayPanels().forEach(lbcDayPanel -> {

            final LBCDay lbcDay = model.getLbcDays().get(lbcDayPanel.getDayOfWeek());
            if (lbcDay == null) {
                throw new RuntimeException("LBCDay should not be null. Day of week: " + lbcDayPanel.getDayOfWeek());
            }

            lbcDayPanel.setAmountEarned(lbcDay.getAmountEarned());
            lbcDayPanel.setBeginTime((lbcDay.getBeginTime()));
            lbcDayPanel.setEndTime(lbcDay.getEndTime());
            lbcDayPanel.setWorkers(lbcDay.getWorkerSelectionGrid());
            lbcDayPanel.setExceptionData(lbcDay.getExceptionData());
        });
    }

    @Override
    public void saveToFile(LBCModel model, File file) {
        JsonMethods.saveToFileJSON(model, LBCModel.class, file, Form.LBC.getNum());
    }

    @Override
    public LBCModel loadFromFile(File file) {
        final LBCModel model = (LBCModel) JsonMethods.loadFromFileJSON(LBCModel.class, file, Form.LBC.getNum());

        if (model.getLbcHeader() == null && model.getLbcDays() == null) {
            return LBCModel.from();
        }

        return model;
    }

    @Override
    public void initializeForm(GeneralController<LBCPanel, LBCModel> controller) {

        final MainFrame<LBCPanel, LBCModel> mainFrame = new MainFrame<LBCPanel, LBCModel>(controller);

        final LBCPanel lbcPanel = LBCPanel.from(
            SubmitFormListener.from(controller),
            new FrameCloseListener(mainFrame)
        );

        controller.setView(lbcPanel);

        controller.readFileAndWriteToView(GeneralController.TEMP_SAVE_FILE);

        JScrollPane scrollPane = new JScrollPane(lbcPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainFrame.add(scrollPane);
        Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        mainFrame.setMaximizedBounds(effectiveScreenSize);

        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    @Override
    public void updateDate(LBCPanel view) {
    }

    @Override
    public void updateDateHelper() {
    }

    @Override
    public void eliminateWindow(LBCPanel view) {
        final JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(view);
        frame.setVisible(false);
        frame.dispose();
    }

    /**
     * Saves the current LBC workers into a save file
     *
     * @param panel
     * @param model
     */
    public static void saveChosenWorkers(LBCPanel panel, LBCModel model) {
    }

    /**
     * Saves the amounts earned into a save file
     *
     * @param panel
     */
    public static void saveAmountsEarned(LBCPanel panel) {
    }
}
