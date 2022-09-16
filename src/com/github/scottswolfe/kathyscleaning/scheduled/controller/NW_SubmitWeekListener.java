package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.helper.ExcelMethods;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.scheduled.view.ScheduledTabbedPane;

public class NW_SubmitWeekListener implements ActionListener {

    GeneralController<ScheduledTabbedPane, NW_Data> controller;

    public NW_SubmitWeekListener(final GeneralController<ScheduledTabbedPane, NW_Data> controller) {
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e){
        ExcelMethods.chooseFileAndGenerateExcelDoc(controller);
    }
}
