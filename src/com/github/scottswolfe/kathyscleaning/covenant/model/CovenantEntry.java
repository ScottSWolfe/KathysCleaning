package com.github.scottswolfe.kathyscleaning.covenant.model;

import java.util.ArrayList;
import java.util.List;

import com.github.scottswolfe.kathyscleaning.covenant.view.CovenantPanel;
import com.github.scottswolfe.kathyscleaning.enums.DayOfWeek;
import com.github.scottswolfe.kathyscleaning.general.model.WorkTime;

public class CovenantEntry {

    private String worker;
    List<WorkTime> workTimes;

    public static CovenantEntry from() {
        return new CovenantEntry("", createEmptyWorkTimeList());
    }

    public static CovenantEntry from(final String workerName) {
        return new CovenantEntry(workerName, createEmptyWorkTimeList());
    }

    public static CovenantEntry from(final String workerName, final List<WorkTime> workTimes) {
        return new CovenantEntry(workerName, workTimes);
    }

    private CovenantEntry(final String workerName, final List<WorkTime> workTimes) {
        this.worker = workerName;
        this.workTimes = workTimes;
    }

    private static List<WorkTime> createEmptyWorkTimeList() {
        final List<WorkTime> times = new ArrayList<>();
        for (int count = 0; count < CovenantPanel.COLS; count++) {
            times.add(WorkTime.from(DayOfWeek.fromName(CovenantPanel.day[count]), "", ""));
        }
        return times;
    }

    /**
     * @return the worker
     */
    public String getWorker() {
        return worker;
    }

    /**
     * @param worker the worker to set
     */
    public void setWorker(String worker) {
        this.worker = worker;
    }

    public List<WorkTime> getWorkTimes() {
        return workTimes;
    }
}
