package com.github.scottswolfe.kathyscleaning.lbc.model;

import com.github.scottswolfe.kathyscleaning.general.helper.SharedDataManager;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.lbc.view.LBCPanel;
import com.github.scottswolfe.kathyscleaning.utility.WorkerSelectionGridHelper;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;

public class LBCHeader {

    private final List<List<Pair<String, Boolean>>> workerSelectionGrid;

    public static LBCHeader from(final List<List<Pair<String, Boolean>>> workerSelectionGrid) {
        return new LBCHeader(workerSelectionGrid);
    }

    public static LBCHeader from() {
        return new LBCHeader(
            new WorkerList(SharedDataManager.getInstance().getAvailableWorkerNames()).getAsWorkerSelectionGrid(
                LBCPanel.LBC_WORKER_ROW_COUNT, LBCPanel.LBC_WORKER_COLUMN_COUNT
            )
        );
    }

    public static LBCHeader copyOf(@Nonnull final LBCHeader lbcHeader) {
        return new LBCHeader(lbcHeader.workerSelectionGrid);
    }

    private LBCHeader(final List<List<Pair<String, Boolean>>> workerSelectionGrid) {
        this.workerSelectionGrid = ImmutableList.copyOf(workerSelectionGrid);
    }

    public List<List<Pair<String, Boolean>>> getWorkerSelectionGrid() {
        return workerSelectionGrid;
    }

    public void setWorkers(final List<List<String>> workerNames) {
        for (int row = 0; row < workerSelectionGrid.size(); row++) {
            for (int column = 0; column < workerSelectionGrid.get(row).size(); column++) {
                final String workerName;
                if (!workerNames.isEmpty() && row < workerNames.size() && column < workerNames.get(row).size()) {
                    workerName = workerNames.get(row).get(column);
                } else {
                    workerName = "";
                }
                workerSelectionGrid.get(row).set(column, Pair.of(workerName, false));
            }
        }
    }

    public void updateWorkersAndKeepSelections(@Nonnull final List<String> workerNames) {
        WorkerSelectionGridHelper.getInstance().updateWorkersAndKeepSelections(workerSelectionGrid, workerNames);
    }
}
