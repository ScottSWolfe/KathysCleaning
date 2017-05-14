package com.github.scottswolfe.kathyscleaning.completed.model;

import java.util.Calendar;

import com.github.scottswolfe.kathyscleaning.utility.CalendarMethods;

public class CompletedModel {

	public DayData[] dayData;
	
	public CompletedModel() {
	    dayData = new DayData[5];
	    for (int i = 0; i < 5; i++) {
	        dayData[i] = new DayData();
	    }
	    setDates(CalendarMethods.getFirstDayOfWeek());
	}
	
	public DayData[] getDay() {
		return dayData;
	}	
	
	public void setDayData(DayData[] dayData) {
	    this.dayData = dayData;
	}
	
	public void setDates(Calendar firstDayOfWeek) {
	    Calendar date = (Calendar) firstDayOfWeek.clone();
        for (int i = 0; i < dayData.length; i++) {
            dayData[i].setDate(date);
            date.add(Calendar.DATE, 1);
        }
	}
	
}
