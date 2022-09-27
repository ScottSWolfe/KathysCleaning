package com.github.scottswolfe.kathyscleaning.scheduled.model;

import com.github.scottswolfe.kathyscleaning.general.helper.SharedDataManager;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.lbc.view.LBCPanel;
import com.github.scottswolfe.kathyscleaning.utility.WorkerSelectionGridHelper;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduledLBCData {

    private final String meetTime;
    private final List<List<Pair<String, Boolean>>> workerSelectionGrid;
    private final ScheduledLBCExceptions scheduledLBCExceptions;

    public static ScheduledLBCData from() {
        return new ScheduledLBCData(
            "",
            new WorkerList(SharedDataManager.getInstance().getAvailableWorkerNames())
                .getAsWorkerSelectionGrid(
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

    public void updateWorkersAndKeepSelections(@Nonnull final List<String> workerNames) {
        WorkerSelectionGridHelper.getInstance().updateWorkersAndKeepSelections(workerSelectionGrid, workerNames);
    }

    public ScheduledLBCExceptions getScheduledLBCExceptions() {
        return scheduledLBCExceptions;
    }

    private static List<List<Pair<String, Boolean>>> getWorkerSelectionGridFromNames(final List<List<String>> workerNames) {
        return workerNames.stream()
            .map(workerNamesInRow -> workerNamesInRow.stream()
                .map(workerName -> Pair.of(workerName, false))
                .collect(Collectors.toList())
            )
            .collect(Collectors.toList());
    }
}
