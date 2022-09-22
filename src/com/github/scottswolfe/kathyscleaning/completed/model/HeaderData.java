package com.github.scottswolfe.kathyscleaning.completed.model;

import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;

public class HeaderData {

    WorkerList workers;

    public HeaderData() {
        workers = new WorkerList(GlobalData.getInstance().getDefaultWorkerNames());
    }

    public void setWorkers(WorkerList workers) {
        this.workers = workers;
    }

    public WorkerList getWorkers() {
        return workers;
    }
}
