package com.github.scottswolfe.kathyscleaning.covenant.controller;

import java.io.File;
import java.util.Calendar;

import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.covenant.view.CovenantPanel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
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

    @Override
    public void initializeForm(Calendar date, int mode, int wk) {
        
        GeneralController<CovenantPanel, Data> controller =
                                    new GeneralController<>(Form.COVENANT);
        
        CovenantListeners covListeners = new CovenantListeners();
        CovenantModel covModel = new CovenantModel(
                new WorkerList(WorkerList.COVENANT_WORKERS), date, mode, wk);        
        CovenantPanel covPanel = new CovenantPanel(covListeners,
                new WorkerList(WorkerList.COVENANT_WORKERS), date, mode, wk);
  
        MainFrame<CovenantPanel, Data> mainFrame =
                new MainFrame<CovenantPanel, Data>(controller);
        
        covListeners.setCovModel(covModel);
        covListeners.setCovPanel(covPanel);
        covPanel.setFrame(mainFrame);
        
        mainFrame.add(covPanel);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

}
