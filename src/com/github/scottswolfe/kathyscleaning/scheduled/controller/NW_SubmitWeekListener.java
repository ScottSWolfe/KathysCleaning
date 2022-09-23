package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.github.scottswolfe.kathyscleaning.general.controller.FormController;
import com.github.scottswolfe.kathyscleaning.general.helper.ExcelMethods;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.scheduled.view.ScheduledTabbedPane;

public class NW_SubmitWeekListener implements ActionListener {

    FormController<ScheduledTabbedPane, NW_Data> controller;

    public NW_SubmitWeekListener(final FormController<ScheduledTabbedPane, NW_Data> controller) {
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e){
        ExcelMethods.chooseFileAndGenerateExcelDoc();
    }
}
