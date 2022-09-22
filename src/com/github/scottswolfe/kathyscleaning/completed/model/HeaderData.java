package com.github.scottswolfe.kathyscleaning.completed.model;

import java.util.Calendar;

import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.utility.CalendarMethods;

public class HeaderData {

    WorkerList workers;
    private Calendar date;

    public HeaderData() {
        workers = new WorkerList(GlobalData.getInstance().getDefaultWorkerNames());
        date = SessionModel.getCompletedStartDay();
    }

    public void setWorkers(WorkerList workers) {
        this.workers = workers;
    }

    public WorkerList getWorkers() {
        return workers;
    }

    public void setDate(Calendar date) {
        this.date = CalendarMethods.trim(date);
    }

    public Calendar getDate() {
        return CalendarMethods.copy(date);
    }
}
