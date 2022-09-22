package com.github.scottswolfe.kathyscleaning.completed.model;

import java.util.List;

public class CompletedModel {

	public DayData[] dayData;

	public CompletedModel() {
	    dayData = new DayData[5];
	    for (int i = 0; i < 5; i++) {
	        dayData[i] = new DayData();
	    }
	}

	public DayData[] getDay() {
		return dayData;
	}

	public void setDayData(DayData[] dayData) {
	    this.dayData = dayData;
	}

    public void setWorkers(List<List<String>> workerNames) {
        for (DayData day : dayData) {
            day.setWorkers(workerNames);
        }
    }
}
