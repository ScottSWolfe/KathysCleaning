package com.github.scottswolfe.kathyscleaning.lbc.model;

import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.enums.DayOfWeek;
import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.lbc.view.LBCPanel;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class LBCDay {

    private final DayOfWeek dayOfWeek;
    private final String amountEarned;
    private final String beginTime;
    private final String endTime;
    private final List<List<Pair<String, Boolean>>> workerSelectionGrid;
    private final ExceptionData exceptionData;

    public static LBCDay from(
        final DayOfWeek dayOfWeek,
        final String amountEarned,
        final String beginTime,
        final String endTime,
        final List<List<Pair<String, Boolean>>> workerSelectionGrid,
        final ExceptionData exceptionData
    ) {
        return new LBCDay(
            dayOfWeek,
            amountEarned,
            beginTime,
            endTime,
            workerSelectionGrid,
            exceptionData
        );
    }

    public static LBCDay from(final DayOfWeek dayOfWeek) {
        return new LBCDay(
            dayOfWeek,
            "",
            "",
            "",
            new WorkerList(GlobalData.getInstance().getDefaultWorkerNames())
                .getAsWorkerSelectionGrid(
                    LBCPanel.LBC_WORKER_ROW_COUNT, LBCPanel.LBC_WORKER_COLUMN_COUNT
                ),
            new ExceptionData()
        );
    }

    private LBCDay(
        final DayOfWeek dayOfWeek,
        final String amountEarned,
        final String beginTime,
        final String endTime,
        final List<List<Pair<String, Boolean>>> workerSelectionGrid,
        final ExceptionData exceptionData
    ) {
        this.dayOfWeek = dayOfWeek;
        this.amountEarned = amountEarned;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.workerSelectionGrid = workerSelectionGrid;
        this.exceptionData = exceptionData;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public String getAmountEarned() {
        return amountEarned;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public List<List<Pair<String, Boolean>>> getWorkerSelectionGrid() {
        return workerSelectionGrid;
    }

    public ExceptionData getExceptionData() {
        return exceptionData;
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
}
