package com.github.scottswolfe.kathyscleaning.covenant.controller;

import java.io.File;

import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.covenant.view.CovenantPanel;
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

}
