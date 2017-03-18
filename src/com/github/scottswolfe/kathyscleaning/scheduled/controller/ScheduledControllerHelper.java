package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.io.File;
import java.util.Calendar;

import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;

public class ScheduledControllerHelper 
                        implements ControllerHelper<TabbedPane, Data>{

    @Override
    public Data readViewIntoModel(TabbedPane view) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void writeModelToView(Data model, TabbedPane view) {
        // TODO Auto-generated method stub
        
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
        // TODO Auto-generated method stub
        
    }

}
