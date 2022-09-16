package com.github.scottswolfe.kathyscleaning.covenant.model;

import java.util.ArrayList;
import java.util.List;

import com.github.scottswolfe.kathyscleaning.covenant.view.CovenantPanel;
import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;

/**
 * Model for Covenant Academy data and workers.
 */
public class CovenantModel {

    List<CovenantEntry> covenantEntries;
    List<Double> amountsEarned;

    public static CovenantModel from() {
        final WorkerList workers = new WorkerList(GlobalData.getInstance().getDefaultWorkerNames());

        final List<CovenantEntry> covenantEntries = new ArrayList<>();
        for (int row = 0; row < CovenantPanel.ROWS; row++) {
            if (row < workers.size()) {
                covenantEntries.add(CovenantEntry.from(workers.getName(row)));
            } else {
                covenantEntries.add(CovenantEntry.from());
            }
        }

        final List<Double> amountsEarned = new ArrayList<>();
        for (int i = 0; i < CovenantPanel.COLS; i++) {
            amountsEarned.add(0.0);
        }

        return new CovenantModel(covenantEntries, amountsEarned);
    }

    public static CovenantModel from(
        final List<CovenantEntry> covenantEntries,
        final List<Double> amountsEarned
    ) {
        return new CovenantModel(covenantEntries, amountsEarned);
    }

    private CovenantModel(
        final List<CovenantEntry> covenantEntries,
        final List<Double> amountsEarned
    ) {
        this.covenantEntries = covenantEntries;
        this.amountsEarned = amountsEarned;
    }

    public List<Double> getAmountsEarned() {
        return amountsEarned;
    }

    public List<CovenantEntry> getEntries() {
        return covenantEntries;
    }

    public void setWorkers(final List<String> workerNames) {
        for (int count = 0; count < workerNames.size() && count < covenantEntries.size(); count++) {
            covenantEntries.get(count).setWorker(workerNames.get(count));
        }
    }
}
