package com.github.scottswolfe.kathyscleaning.covenant.controller;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantEntry;
import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.covenant.view.CovenantPanel;
import com.github.scottswolfe.kathyscleaning.enums.DayOfWeek;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.model.WorkTime;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.menu.view.ChooseWeekPanel;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;

public class CovenantControllerHelper implements ControllerHelper<CovenantPanel, CovenantModel> {

    @Override
    public CovenantModel readViewIntoModel(CovenantPanel view) {
        CovenantModel model = new CovenantModel();

        for (int i = 0; i < CovenantPanel.ROWS; i++) {

            final List<WorkTime> workTimes = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                workTimes.add(
                    WorkTime.from(
                        DayOfWeek.fromIndex(j),
                        view.getBeginTimeTextfield()[i][j].getText(),
                        view.getEndTimeTextfield()[i][j].getText()
                    )
                );
            }

            model.addEntry(CovenantEntry.from(
                view.getNameLabels()[i].getText(),
                workTimes
            ));
        }

        for (int i = 0; i < CovenantPanel.COLS; i++) {
            String amount = view.getEarnedTextfields()[i].getText();
            if (!amount.isEmpty()) {
                model.addAmountEarned(Double.parseDouble(amount));
            } else {
                model.addAmountEarned(0.0);
            }
        }

        return model;
    }

    @Override
    public void writeModelToView(CovenantModel model, CovenantPanel view) {

        final List<CovenantEntry> entries = model.getEntries();

        for (int row = 0; row < CovenantPanel.ROWS; row++) {

            final String workerName;
            final List<WorkTime> workTimes;
            if (row < entries.size()) {
                workerName = entries.get(row).getWorker();
                workTimes = entries.get(row).getWorkTimes();
            } else {
                workerName = "";
                workTimes = new ArrayList<>();
            }

            view.getNameLabels()[row].setText(workerName);
            for (int count = 0; count < workTimes.size(); count++) {
                view.getBeginTimeTextfield()[row][count].setText(workTimes.get(count).getBeginTime());
                view.getEndTimeTextfield()[row][count].setText(workTimes.get(count).getEndTime());
            }
        }

        for (int count = 0; count < model.getAmountsEarned().size(); count++) {
            final double amount = model.getAmountsEarned().get(count);
            if (amount != 0) {
                view.getEarnedTextfields()[count].setText(String.valueOf(amount));
            } else {
                view.getEarnedTextfields()[count].setText("");
            }
        }
    }

    @Override
    public void saveToFile(CovenantModel model, File file) {
        JsonMethods.saveToFileJSON(model, CovenantModel.class, file, Form.COVENANT.getNum());
    }

    @Override
    public CovenantModel loadFromFile(File file) {
        return (CovenantModel) JsonMethods.loadFromFileJSON(CovenantModel.class, file, Form.COVENANT.getNum());
    }

    @Override
    public void initializeForm(GeneralController<CovenantPanel, CovenantModel> controller) {

        CovenantListeners covListeners = new CovenantListeners();
        CovenantModel covModel = new CovenantModel();
        CovenantPanel covPanel = new CovenantPanel(covListeners);

        MainFrame<CovenantPanel, CovenantModel> mainFrame =
                new MainFrame<CovenantPanel, CovenantModel>(controller);

        covListeners.setCovModel(covModel);
        covListeners.setCovPanel(covPanel);
        covListeners.setController(controller);
        covPanel.setFrame(mainFrame);
        controller.setView(covPanel);

        controller.readFileAndWriteToView(GeneralController.TEMP_SAVE_FILE);

        // TODO temporary hack
        JScrollPane scrollPane = new JScrollPane(covPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainFrame.add(scrollPane);
        Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        mainFrame.setMaximizedBounds(effectiveScreenSize);

        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    @Override
    public void updateDate(CovenantPanel view) {
        ChooseWeekPanel.initializePanel(this, false);
    }

    @Override
    public void updateDateHelper() {
        // do nothing
    }

    @Override
    public void eliminateWindow(CovenantPanel view) {
        @SuppressWarnings("rawtypes")
        MainFrame frame = (MainFrame) SwingUtilities.getWindowAncestor(view);
        frame.eliminate();
    }

    /**
     * Saves the current Covenant workers into a save file
     *
     * TODO Needs refactoring
     *
     * @param panel
     * @param model
     */
    public static void saveChosenWorkers(CovenantPanel panel, CovenantModel model) {
        try {

            FileWriter fw = new FileWriter(Settings.COV_WORKER_SAVE.getPath());
            BufferedWriter bw = new BufferedWriter(fw);

            for (int i = 0; i < CovenantPanel.ROWS; i++) {
                bw.write(panel.getNameLabels()[i].getText());
                bw.newLine();
            }

            boolean match;
            if (model.getDwd().getWorkers() != null) {
            for (int i = 0; i < model.getDwd().size(); i++) {

                match = false;

                for (int j = 0; j<panel.getNameLabels().length; j++) {

                    if (model.getDwd().get(i).equals(panel.getNameLabels()[j].getText())) {
                        match = true;
                        break;
                    }

                }

                if (match == false) {
                    bw.write(model.getDwd().getName(i));
                    bw.newLine();
                }

            }}

            bw.close();

        }
        catch(Exception e2) {
            e2.printStackTrace();
        }
    }

    /**
     * Saves the amounts earned into a save file
     *
     * TODO Needs refactoring
     *
     * @param panel
     */
    public static void saveAmountsEarned(CovenantPanel panel) {
        try {

            FileWriter fw = new FileWriter(Settings.COVENANT_EARNED_SAVE_FILE);
            BufferedWriter bw = new BufferedWriter(fw);

            for (int i = 0; i < CovenantPanel.COLS; i++) {

                if (panel.getEarnedTextfields()[i].getText().length() > 0) {
                    bw.write(panel.getEarnedTextfields()[i].getText() );
                    bw.newLine();
                }
                else {
                    bw.write(0);
                    bw.newLine();
                }
            }
            bw.close();
        }
        catch(Exception e2) {
            e2.printStackTrace();
        }
    }

}
