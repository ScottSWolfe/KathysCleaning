package com.github.scottswolfe.kathyscleaning.completed.model;

import java.util.Calendar;

import com.github.scottswolfe.kathyscleaning.utility.CalendarMethods;

public class CompletedModel {

	public DayData[] dayData;
	
	public CompletedModel() {
	    dayData = new DayData[5];
	    Calendar date = CalendarMethods.getFirstDayOfWeek();
	    for (int i = 0; i < 5; i++) {
	        dayData[i] = new DayData();
	        dayData[i].setDate(date);
	        date.add(Calendar.DATE, 1);
	    }
	}
	
	public DayData[] getDay() {
		return dayData;
	}	
	
	public void setDayData(DayData[] dayData) {
	    this.dayData = dayData;
	}
	
}
