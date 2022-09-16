package com.github.scottswolfe.kathyscleaning.scheduled.model;

import java.util.List;

public class NW_Data {

    public NW_DayData[] dayData;

    public NW_Data() {
        dayData = new NW_DayData[5];
        for (int i = 0; i < 5; i++) {
            dayData[i] = new NW_DayData();
        }
    }

    public void setDayData(NW_DayData[] dayData) {
        this.dayData = dayData;
    }

    public NW_DayData[] getDay() {
        return dayData;
    }

    public void setWorkers(List<List<String>> workerNames) {
        for (NW_DayData day : dayData) {
            day.setWorkers(workerNames);
        }
    }
}
