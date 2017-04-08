package com.github.scottswolfe.kathyscleaning.weekend.controller;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import javax.swing.SwingUtilities;

import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.menu.view.ChooseWeekPanel;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendEntry;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendModel;
import com.github.scottswolfe.kathyscleaning.weekend.view.WeekendPanel;
import com.github.scottswolfe.kathyscleaning.weekend.view.WeekendPanel.JobPanel;

public class WeekendControllerHelper
        implements ControllerHelper<WeekendPanel, WeekendModel> {

    WeekendPanel panel;
    
    @Override
    public WeekendModel readViewIntoModel(WeekendPanel view) {

        WeekendModel model = new WeekendModel();
        WeekendEntry entry;
        JobPanel jp;
        
        for (int i = 0; i < WeekendPanel.NUM_JOB_PANELS; i++) {
            entry = new WeekendEntry();
            jp = view.jp[i];
            
            entry.setWorkedIsChecked(jp.worked_checkbox.isSelected());
            entry.setCustomer(String.valueOf(jp.customer_combobox.getSelectedItem()));
            if (!jp.jobpaid_field.getText().isEmpty()) {
                entry.setAmountReceived(Double.parseDouble(jp.jobpaid_field.getText()));
            } else {
                entry.setAmountReceived(0.0);
            }
            entry.setEmployee(String.valueOf(jp.employee_combobox.getSelectedItem()));
            if (!jp.workerpaid_field.getText().isEmpty()) {
                entry.setAmountPaid(Double.parseDouble(jp.workerpaid_field.getText()));
            } else {
                entry.setAmountPaid(0.0);
            }
            
            model.addEntry(entry);
        }
        return model;
    }

    @Override
    public void writeModelToView(WeekendModel model, WeekendPanel view) {
        
        view.date = model.getDate();
        
        List<WeekendEntry> entries = model.getEntries();
        JobPanel jp;
        int i = 0;
        for (WeekendEntry entry : entries) {
            jp = view.jp[i];
            jp.worked_checkbox.setSelected(entry.isWorkedIsChecked());
            jp.customer_combobox.setSelectedItem(entry.getCustomer());
            if (entry.getAmountReceived() != 0.0) {
                jp.jobpaid_field.setText(String.valueOf(entry.getAmountReceived()));
            }
            jp.employee_combobox.setSelectedItem(entry.getEmployee());
            if (entry.getAmountPaid() != 0.0) {
                jp.workerpaid_field.setText(String.valueOf(entry.getAmountPaid()));
            }
            i++;
        }
    }

    @Override
    public void saveToFile(WeekendModel model, File file) {
        JsonMethods.saveToFileJSON(model, WeekendModel.class, file, Form.WEEKEND.getNum());
    }

    @Override
    public WeekendModel loadFromFile(File file) {
        return (WeekendModel) JsonMethods.loadFromFileJSON(WeekendModel.class, file, Form.WEEKEND.getNum());
    }

    @Override
    public void initializeForm(GeneralController<WeekendPanel, WeekendModel> controller) {
        
        WeekendPanel wp = new WeekendPanel(controller, 0, 0); // TODO remove 0, 0
        MainFrame<WeekendPanel, WeekendModel> weekendFrame = new MainFrame<>(controller);
        
        controller.setView(wp);
        wp.setFrame(weekendFrame);          
        
        controller.readFileAndWriteToView(GeneralController.TEMP_SAVE_FILE);

        weekendFrame.add(wp);
        weekendFrame.pack();
        weekendFrame.setLocationRelativeTo(null);
        weekendFrame.setVisible(true);
    }
    
    @Override
    public void updateDate(WeekendPanel wp) {
        this.panel = wp;
        ChooseWeekPanel.initializePanel(this, false);
    }
    
    @Override
    public void updateDateHelper() {
        Calendar date = SessionModel.getCompletedStartDay();
        String s = new String( "Week of " +
                ( Integer.parseInt(String.valueOf(date.get(Calendar.MONTH)))+1 ) +
                "/" + date.get(Calendar.DATE) + "/" + date.get(Calendar.YEAR) );
        panel.date_label.setText(s);
    }
    
    @Override
    public void eliminateWindow(WeekendPanel view) {
        @SuppressWarnings("rawtypes")
        MainFrame frame = (MainFrame) SwingUtilities.getWindowAncestor(view);
        frame.eliminate();
    }
    
}
