package com.github.scottswolfe.kathyscleaning.scheduled.model;

import java.util.ArrayList;
import java.util.List;

import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;

public class NW_HouseData {

    private String house_name;
    private List<String> selected_workers;
    private WorkerList workers;

    public NW_HouseData() {
        house_name = "";
        selected_workers = new ArrayList<>();
        workers = new WorkerList(GlobalData.getInstance().getDefaultWorkerNames());
    }

    public void setHouseName(String house_name) {
        this.house_name = house_name;
    }

    public void setSelectedWorkers(List<String> selected_workers) {
        this.selected_workers = selected_workers;
    }

    public List<String> getSelectedWorkers() {
        return selected_workers;
    }

    public void setWorkerList(WorkerList workers) {
        this.workers = workers;
    }

    public WorkerList getWorkers() {
        return workers;
    }
}
