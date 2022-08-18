package com.github.scottswolfe.kathyscleaning.lbc.controller;

import com.github.scottswolfe.kathyscleaning.enums.DayOfWeek;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkTime;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCEntry;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCModel;
import com.github.scottswolfe.kathyscleaning.lbc.view.LBCPanel;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.menu.view.ChooseWeekPanel;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;

public class LBCControllerHelper implements ControllerHelper<LBCPanel, LBCModel> {

    @Override
    public LBCModel readViewIntoModel(LBCPanel view) {
        LBCModel model = new LBCModel();
        LBCEntry entry;
        for (int i = 0; i < LBCPanel.ROWS; i++) {
            entry = new LBCEntry();
            entry.setWorker(view.getNameLabels()[i].getText());
            for (int j = 0; j < LBCPanel.COLS; j++) {
                WorkTime workTime = WorkTime.from(
                    DayOfWeek.fromIndex(j),
                    view.getBeginTimeTextfield()[i][j].getText(),
                    view.getEndTimeTextfield()[i][j].getText()
                );
                entry.addWorkTime(workTime);
            }
            model.addEntry(entry);
        }
        for (int i = 0; i < LBCPanel.COLS; i++) {
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
    public void writeModelToView(LBCModel model, LBCPanel view) {
        Iterator<LBCEntry> entries = model.entryIterator();
        LBCEntry entry;
        int row = 0;
        int col;

        // TODO temporary hack for when model is empty
        if (!entries.hasNext()) {
            for (int i = 0; i < LBCPanel.ROWS; i++) {
                entry = new LBCEntry();
                entry.setWorker(view.getNameLabels()[i].getText());
                for (int j = 0; j < LBCPanel.COLS; j++) {
                    final String beginTime = "";
                    final String endTime = "";
                    final WorkTime workTime = WorkTime.from(
                        DayOfWeek.fromIndex(j),
                        beginTime,
                        endTime
                    );
                    entry.addWorkTime(workTime);
                }
                model.addEntry(entry);
            }
            for (int i = 0; i < LBCPanel.COLS; i++) {
                model.addAmountEarned(0.0);
            }
            entries = model.entryIterator();
        }
        // end temporary hack

        while(entries.hasNext() && row < LBCPanel.ROWS) {
            entry = entries.next();
            view.getNameLabels()[row].setText(entry.getWorker());
            List<WorkTime> times = entry.getWorkTimes();
            col = 0;
            for (WorkTime workTime : times) {
                view.getBeginTimeTextfield()[row][col].setText(workTime.getBeginTime());
                view.getEndTimeTextfield()[row][col].setText(workTime.getEndTime());
                col++;
            }
            row++;
        }
        int i = 0;
        for (Double amount : model.getAmountsEarned()) {
            if (amount != 0) {
                view.getEarnedTextfields()[i].setText(String.valueOf(amount));
            } else {
                view.getEarnedTextfields()[i].setText("");
            }
            i++;
        }
    }

    @Override
    public void saveToFile(LBCModel model, File file) {
        JsonMethods.saveToFileJSON(model, LBCModel.class, file, Form.LBC.getNum());
    }

    @Override
    public LBCModel loadFromFile(File file) {
        return (LBCModel) JsonMethods.loadFromFileJSON(LBCModel.class, file, Form.LBC.getNum());
    }

    @Override
    public void initializeForm(GeneralController<LBCPanel, LBCModel> controller) {

        LBCListeners lbcListeners = new LBCListeners();
        LBCModel lbcModel = new LBCModel(
                new WorkerList(WorkerList.LBC_WORKERS), SessionModel.getCompletedStartDay(), 0, 0); // TODO remove 0, 0...
        LBCPanel lbcPanel = new LBCPanel(lbcListeners);

        MainFrame<LBCPanel, LBCModel> mainFrame =
                new MainFrame<LBCPanel, LBCModel>(controller);

        lbcListeners.setLbcModel(lbcModel);
        lbcListeners.setLbcPanel(lbcPanel);
        lbcListeners.setController(controller);
        lbcPanel.setFrame(mainFrame);
        controller.setView(lbcPanel);

        controller.readFileAndWriteToView(GeneralController.TEMP_SAVE_FILE);

        // TODO temporary hack
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
        ChooseWeekPanel.initializePanel(this, false);
    }

    @Override
    public void updateDateHelper() {
        // do nothing
    }

    @Override
    public void eliminateWindow(LBCPanel view) {
        @SuppressWarnings("rawtypes")
        MainFrame frame = (MainFrame) SwingUtilities.getWindowAncestor(view);
        frame.eliminate();
    }

    /**
     * Saves the current LBC workers into a save file
     *
     * TODO Needs refactoring
     *
     * @param panel
     * @param model
     */
    public static void saveChosenWorkers(LBCPanel panel, LBCModel model) {
        try {

            FileWriter fw = new FileWriter(Settings.LBC_WORKER_SAVE.getPath());
            BufferedWriter bw = new BufferedWriter(fw);

            for (int i = 0; i < LBCPanel.ROWS; i++) {
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
    public static void saveAmountsEarned(LBCPanel panel) {
        try {

            FileWriter fw = new FileWriter(Settings.LBC_EARNED_SAVE_FILE);
            BufferedWriter bw = new BufferedWriter(fw);

            for (int i = 0; i < LBCPanel.COLS; i++) {

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
