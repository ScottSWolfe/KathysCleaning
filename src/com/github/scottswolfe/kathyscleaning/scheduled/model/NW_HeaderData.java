package com.github.scottswolfe.kathyscleaning.scheduled.model;

import com.github.scottswolfe.kathyscleaning.general.helper.SharedDataManager;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;

public class NW_HeaderData {

    WorkerList dwd;

    public NW_HeaderData() {
        dwd = new WorkerList(SharedDataManager.getInstance().getAvailableWorkerNames());
    }

    public void setDWD(WorkerList dwd) {
        this.dwd = dwd;
    }

    public WorkerList getDWD() {
        return dwd;
    }
}
