package com.github.scottswolfe.kathyscleaning.completed.model;

import com.github.scottswolfe.kathyscleaning.general.helper.SharedDataManager;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;

import javax.annotation.Nonnull;
import java.util.List;

public class HeaderData {

    WorkerList workers;

    public HeaderData() {
        workers = new WorkerList(SharedDataManager.getInstance().getAvailableWorkerNames());
    }

    public void setWorkers(WorkerList workers) {
        this.workers = workers;
    }

    public void updateWorkersAndKeepExistingSelections(@Nonnull final List<String> workerNames) {
        workers = WorkerList.from(workerNames, workers.getSelectedWorkerNames());
    }

    public WorkerList getWorkers() {
        return workers;
    }
}
