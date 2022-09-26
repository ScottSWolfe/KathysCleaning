package com.github.scottswolfe.kathyscleaning.weekend.controller;

import java.io.File;
import java.util.List;

import com.github.scottswolfe.kathyscleaning.enums.SaveType;
import com.github.scottswolfe.kathyscleaning.general.controller.FormController;
import com.github.scottswolfe.kathyscleaning.general.helper.SharedDataManager;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendEntry;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendModel;
import com.github.scottswolfe.kathyscleaning.weekend.view.WeekendPanel;
import com.github.scottswolfe.kathyscleaning.weekend.view.WeekendPanel.JobPanel;

public class WeekendControllerHelper implements ControllerHelper<WeekendPanel, WeekendModel> {

    private final SharedDataManager sharedDataManager;

    public static WeekendControllerHelper from() {
        return new WeekendControllerHelper();
    }

    private WeekendControllerHelper() {
        sharedDataManager = SharedDataManager.getInstance();
    }

    @Override
    public WeekendModel initializeModel() {
        return new WeekendModel();
    }

    @Override
    public WeekendPanel initializeView(FormController<WeekendPanel, WeekendModel> controller, MainFrame<WeekendPanel, WeekendModel> parentFrame) {
        final WeekendPanel wp = new WeekendPanel(controller, sharedDataManager.getWeekendStartDay());
        final MainFrame<WeekendPanel, WeekendModel> weekendFrame = new MainFrame<>(controller);
        wp.setFrame(weekendFrame);
        return wp;
    }

    @Override
    public WeekendModel readViewIntoModel(WeekendPanel view) {

        WeekendModel model = new WeekendModel();
        WeekendEntry entry;
        JobPanel jp;

        for (int i = 0; i < WeekendPanel.MAX_JOB_COUNT; i++) {
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

        view.setDate(sharedDataManager.getWeekendStartDay());

        List<WeekendEntry> entries = model.getEntries();
        JobPanel jp;
        int i = 0;
        for (WeekendEntry entry : entries) {

            if (i == WeekendPanel.MAX_JOB_COUNT) {
                // todo: log or notify user about this situation
                break;
            }

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
    public void saveModelToFile(WeekendModel model, File file) {
        JsonMethods.saveToFileJSON(model, WeekendModel.class, file, SaveType.WEEKEND);
    }

    @Override
    public WeekendModel loadFromFile(File file) {
        return JsonMethods.loadFromFileJSON(WeekendModel.class, file, SaveType.WEEKEND);
    }

    @Override
    public void updateWorkersOnModel(final WeekendModel weekendModel, final List<List<String>> workerNames) {
        // do nothing
    }
}
