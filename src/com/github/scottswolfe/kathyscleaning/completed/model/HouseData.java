package com.github.scottswolfe.kathyscleaning.completed.model;

import java.util.ArrayList;
import java.util.List;

import com.github.scottswolfe.kathyscleaning.completed.view.HousePanel;
import com.github.scottswolfe.kathyscleaning.general.helper.SharedDataManager;
import com.github.scottswolfe.kathyscleaning.general.model.Worker;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;

public class HouseData {

    private String house_name;
    private double house_pay;
    private String time_begin;
    private String time_end;
    private WorkerList workers;
    private ExceptionData exception_data;

    public HouseData() {
        house_name = "";
        house_pay = 0.0;
        time_begin = "";
        time_end = "";
        workers = new WorkerList(SharedDataManager.getInstance().getAvailableWorkerNames());
        exception_data = new ExceptionData();
    }

    public boolean isException() {
        if (exception_data == null) {
            return false;
        }
        return exception_data.isException();
    }

    public void setHouseName(String house_name) {
        this.house_name = house_name;
    }

    public void setHousePay(double money_earned) {
        this.house_pay = money_earned;
    }

    public void setHousePay(String money_earned) {
        if(money_earned.isEmpty() || money_earned.equals("") ){
            this.house_pay = 0;
        }
        else {
            this.house_pay = Double.parseDouble(money_earned);
        }
    }

    public void setTimeBegin(int time_begin) {
        this.time_begin = String.valueOf(time_begin);
    }

    public void setTimeBegin(String time_begin){
        this.time_begin = time_begin;

    }

    public void setTimeEnd(int time_begin) {
        this.time_end = String.valueOf(time_end);
    }

    public void setTimeEnd(String time_begin){
        this.time_end = time_begin;
    }

    public void setSelectedWorkers(final List<String> selectedWorkers) {
        workers.getWorkers().forEach(worker -> worker.setSelected(selectedWorkers.contains(worker.getName())));
    }

    public WorkerList getWorkerList() {
        return workers;
    }

    public void setWorkerList(WorkerList workers) {
        this.workers = workers;
    }

    public void updateWorkersAndKeepExistingSelections(@Nonnull final List<String> workerNames) {
        workers = WorkerList.from(workerNames, workers.getSelectedWorkerNames());
    }

    public void setExceptionData(ExceptionData exception_data) {
        this.exception_data = exception_data;
    }

    public String getHouseName() {
        return house_name;
    }

    public double getHousePay() {
        return house_pay;
    }

    public String getTimeBegin() {
        return time_begin;
    }

    public String getTimeEnd() {
        return time_end;
    }

    public List<List<Pair<String, Boolean>>> getWorkerSelectionGrid() {
        int workersAddedCount = 0;
        final List<List<Pair<String, Boolean>>> listOfRows = new ArrayList<>();
        for (int row = 0; row < HousePanel.WORKER_SELECTION_ROW_COUNT; row++) {
            final List<Pair<String, Boolean>> listOfColumns = new ArrayList<>();
            for (int column = 0; column < HousePanel.WORKER_SELECTION_COLUMN_COUNT; column++) {
                final String workerName;
                final boolean isSelected;
                if (workersAddedCount < workers.size()) {
                    final Worker worker = workers.get(workersAddedCount);
                    workerName = worker.getName();
                    isSelected = worker.isSelected();
                    workersAddedCount++;
                } else {
                    workerName = "";
                    isSelected = false;
                }
                listOfColumns.add(Pair.of(workerName, isSelected));
            }
            listOfRows.add(listOfColumns);
        }
        return listOfRows;
    }

    public ExceptionData getExceptionData() {
        return exception_data;
    }
}
