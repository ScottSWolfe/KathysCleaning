package com.github.scottswolfe.kathyscleaning.weekend.controller;

import java.io.File;

import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.weekend.view.WeekendPanel;

public class WeekendControllerHelper
        implements ControllerHelper<WeekendPanel, Data> {

    @Override
    public Data readViewIntoModel(WeekendPanel view) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void writeModelToView(Data model, WeekendPanel view) {
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

    
}
