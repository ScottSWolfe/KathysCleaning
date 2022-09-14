package com.github.scottswolfe.kathyscleaning.lbc.model;

import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.lbc.view.LBCPanel;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class LBCHeader {

    private final List<List<Pair<String, Boolean>>> workerSelectionGrid;

    public static LBCHeader from(final List<List<Pair<String, Boolean>>> workerSelectionGrid) {
        return new LBCHeader(workerSelectionGrid);
    }

    public static LBCHeader from() {
        return new LBCHeader(
            new WorkerList(GlobalData.getInstance().getDefaultWorkerNames()).getAsWorkerSelectionGrid(
                LBCPanel.LBC_WORKER_ROW_COUNT, LBCPanel.LBC_WORKER_COLUMN_COUNT
            )
        );
    }

    private LBCHeader(final List<List<Pair<String, Boolean>>> workerSelectionGrid) {
        this.workerSelectionGrid = workerSelectionGrid;
    }

    public List<List<Pair<String, Boolean>>> getWorkerSelectionGrid() {
        return workerSelectionGrid;
    }
}
