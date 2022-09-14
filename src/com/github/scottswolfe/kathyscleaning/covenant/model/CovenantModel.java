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

    /**
     * Container for the default workers.
     */
    private WorkerList dwd;

    public CovenantModel() {
        dwd = new WorkerList(GlobalData.getInstance().getDefaultWorkerNames());

        covenantEntries = new ArrayList<>();
        for (int row = 0; row < CovenantPanel.ROWS; row++) {
            if (row < dwd.size()) {
                covenantEntries.add(CovenantEntry.from(dwd.getName(row)));
            } else {
                covenantEntries.add(CovenantEntry.from());
            }
        }

        amountsEarned = new ArrayList<>();
        this.dwd = new WorkerList(GlobalData.getInstance().getDefaultWorkerNames());
    }

    public void addEntry(CovenantEntry entry) {
        covenantEntries.add(entry);
    }

    public void addAmountEarned(Double amount) {
        amountsEarned.add(amount);
    }

    public List<Double> getAmountsEarned() {
        return amountsEarned;
    }

    public List<CovenantEntry> getEntries() {
        return covenantEntries;
    }

    /**
     * @return the dwd
     */
    public WorkerList getDwd() {
        return dwd;
    }

    /**
     * @param dwd the dwd to set
     */
    public void setDwd(WorkerList dwd) {
        this.dwd = dwd;
    }
}
