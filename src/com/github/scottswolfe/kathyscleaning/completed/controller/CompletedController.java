package com.github.scottswolfe.kathyscleaning.completed.controller;

import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.completed.model.HouseData;
import com.github.scottswolfe.kathyscleaning.completed.view.HousePanel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;

public class CompletedController
        extends GeneralController<TabbedPane, CompletedModel> {

    public CompletedController() {
        super(Form.COMPLETED);
    }

    public void setExceptionDataForHouse(int dayIndex, int houseIndex, ExceptionData newData) {
        CompletedModel model = this.getModel();
        HouseData houseData = model.getDay()[dayIndex].getHouseData()[houseIndex];
        houseData.setExceptionData(newData);
        
        TabbedPane view = this.getView();
        HousePanel housePanel = view.day_panel[dayIndex].house_panel[houseIndex];
        housePanel.setExceptionButtonColor(houseData.isException());
    }
    
}
