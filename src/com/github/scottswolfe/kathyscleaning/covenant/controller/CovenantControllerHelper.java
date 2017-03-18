package com.github.scottswolfe.kathyscleaning.covenant.controller;

import java.io.File;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantEntry;
import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.covenant.view.CovenantPanel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.model.WorkTime;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;

public class CovenantControllerHelper
                    implements ControllerHelper<CovenantPanel, CovenantModel> {

    @Override
    public CovenantModel readViewIntoModel(CovenantPanel view) {
        CovenantModel model = new CovenantModel();
        CovenantEntry entry;
        WorkTime times;
        for (int i = 0; i < CovenantPanel.ROWS; i++) {    
            entry = new CovenantEntry();
            entry.setWorker(view.getNameLabels()[i].getText());
            for (int j = 0; j < 5; j++) {
                times = new WorkTime();
                times.setBeginTime(view.getBeginTimeTextfield()[i][j].getText());
                times.setEndTime(view.getEndTimeTextfield()[i][j].getText());
                entry.addWorkTime(times);
            }
            model.addEntry(entry);
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
        Iterator<CovenantEntry> entries = model.entryIterator();
        CovenantEntry entry;
        int row = 0;
        int col;
        
        // TODO temporary hack for when model is empty
        if (!entries.hasNext()) {
            WorkTime times;
            for (int i = 0; i < CovenantPanel.ROWS; i++) {    
                entry = new CovenantEntry();
                entry.setWorker(view.getNameLabels()[i].getText());
                for (int j = 0; j < CovenantPanel.COLS; j++) {
                    times = new WorkTime();
                    times.setBeginTime("");
                    times.setEndTime("");
                    entry.addWorkTime(times);
                }
                model.addEntry(entry);
            }
            for (int i = 0; i < CovenantPanel.COLS; i++) {
                model.addAmountEarned(0.0);
            }
            entries = model.entryIterator();
        }
        // end temporary hack
        
        while(entries.hasNext() && row < CovenantPanel.ROWS) {
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
    public void saveToFile(CovenantModel model, File file) {
        JsonMethods.saveToFileJSON(model, CovenantModel.class, file, Form.COVENANT.getNum());
    }

    @Override
    public CovenantModel loadFromFile(File file) {
        return (CovenantModel) JsonMethods.loadFromFileJSON(CovenantModel.class, file, Form.COVENANT.getNum());
    }

    @Override
    public void initializeForm(Calendar date, int mode, int wk) {
        
        GeneralController<CovenantPanel, CovenantModel> controller =
                                    new GeneralController<>(Form.COVENANT);
        
        CovenantListeners covListeners = new CovenantListeners();
        CovenantModel covModel = new CovenantModel(
                new WorkerList(WorkerList.COVENANT_WORKERS), date, mode, wk);        
        CovenantPanel covPanel = new CovenantPanel(covListeners,
                new WorkerList(WorkerList.COVENANT_WORKERS), date, mode, wk);
  
        MainFrame<CovenantPanel, CovenantModel> mainFrame =
                new MainFrame<CovenantPanel, CovenantModel>(controller);
        
        covListeners.setCovModel(covModel);
        covListeners.setCovPanel(covPanel);
        covListeners.setController(controller);
        covPanel.setFrame(mainFrame);
        controller.setView(covPanel);
        
        mainFrame.add(covPanel);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

}
