package com.github.scottswolfe.kathyscleaning.completed.model;

public class CompletedModel {

	public DayData[] dayData;
	
	public CompletedModel(){
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
	
}
