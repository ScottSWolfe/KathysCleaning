package com.github.scottswolfe.kathyscleaning.scheduled.model;

import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.lbc.view.LBCPanel;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ScheduledLBCData {

    private final String meetTime;
    private final List<List<Pair<String, Boolean>>> workerSelectionGrid;
    private final ScheduledLBCExceptions scheduledLBCExceptions;

    public static ScheduledLBCData from() {
        return new ScheduledLBCData(
            "",
            new WorkerList(WorkerList.LBC_WORKERS).getAsWorkerSelectionGrid(
                LBCPanel.LBC_WORKER_ROW_COUNT, LBCPanel.LBC_WORKER_COLUMN_COUNT
            ),
            ScheduledLBCExceptions.from()
        );
    }

    public static ScheduledLBCData from(
        final String meetTime,
        final List<List<Pair<String, Boolean>>> workerSelectionGrid,
        final ScheduledLBCExceptions scheduledLBCExceptions
    ) {
        return new ScheduledLBCData(meetTime, workerSelectionGrid, scheduledLBCExceptions);
    }

    private ScheduledLBCData(
        final String meetTime,
        final List<List<Pair<String, Boolean>>> workerSelectionGrid,
        final ScheduledLBCExceptions scheduledLBCExceptions
    ) {
        this.meetTime = meetTime;
        this.workerSelectionGrid = workerSelectionGrid;
        this.scheduledLBCExceptions = scheduledLBCExceptions;
    }

    public String getMeetTime() {
        return meetTime;
    }

    public List<List<Pair<String, Boolean>>> getWorkerSelectionGrid() {
        return workerSelectionGrid;
    }

    public ScheduledLBCExceptions getScheduledLBCExceptions() {
        return scheduledLBCExceptions;
    }
}
