package com.github.scottswolfe.kathyscleaning.covenant.controller;

import java.io.File;
import java.util.Calendar;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.covenant.view.CovenantPanel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;

public class CovenantControllerHelper
                    implements ControllerHelper<CovenantPanel, Data> {

    @Override
    public Data readViewIntoModel(CovenantPanel view) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void writeModelToView(Data model, CovenantPanel view) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void saveToFile(Data model, File file) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Data loadFromFile(File file) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Initializes a window that contains the Covenant Form
     * 
     * TODO temporary parameters...
     * @param oldFrame
     * @param date
     * @param mode
     * @param wk
     */
    public static void initializeForm(JFrame oldFrame, Calendar date, int mode, int wk) {
        oldFrame.setVisible(false); 
        oldFrame.dispose();
        
        CovenantPanel covPanel = new CovenantPanel(
                new WorkerList(WorkerList.COVENANT_WORKERS), date, mode, wk);
        CovenantModel covModel = new CovenantModel(
                new WorkerList(WorkerList.COVENANT_WORKERS), date, mode, wk);
        
        MainFrame<CovenantPanel, Data> menuFrame = new MainFrame<>(covPanel.getController());

        covPanel.setFrame(menuFrame);
        covPanel.getController().setCovModel(covModel);
        covPanel.getFrame().setResizable(false);
        covPanel.getFrame().pack();
        covPanel.getFrame().setLocationRelativeTo(null);
        covPanel.getFrame().setVisible(true);
    }

}
