package com.github.scottswolfe.kathyscleaning.scheduled.model;

import com.github.scottswolfe.kathyscleaning.general.helper.SharedDataManager;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;

public class NW_HouseData {

    private String house_name;
    private WorkerList workers;

    public NW_HouseData() {
        house_name = "";
        workers = new WorkerList(SharedDataManager.getInstance().getAvailableWorkerNames());
    }

    public String getHouseName() {
        return house_name;
    }

    public void setHouseName(String house_name) {
        this.house_name = house_name;
    }

    public WorkerList getWorkerList() {
        return workers;
    }

    public void setWorkerList(WorkerList workers) {
        this.workers = workers;
    }

    public WorkerList getWorkers() {
        return workers;
    }
}
