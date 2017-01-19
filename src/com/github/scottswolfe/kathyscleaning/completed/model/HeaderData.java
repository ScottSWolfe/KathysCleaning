package com.github.scottswolfe.kathyscleaning.completed.model;

import java.util.Calendar;

import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;

public class HeaderData {

	
//  FIELDS
	
	String week;
	int weekSelected;
	WorkerList dwd;
	private Calendar date;
	
	
//  CONSTRUCTOR
	
	public HeaderData() {
		
	}
	
	

// METHODS
	/*
	public boolean[] isSelected(){
		
		for(int i=0; i < dwd.getDefaultWorkers().length; i++){
			
			
			
		}
	}
	*/
	
	public void setWeek(String week) {
		this.week = week;
	}
	
	public void setDWD(WorkerList dwd) {
		this.dwd = dwd;
	}
	
	
	public String getWeek() {
		return week;
	}
	
	public WorkerList getDWD() {
		return dwd;
	}
	
	public void setDate(Calendar date) {
	    this.date = date;
	}
	
	public Calendar getDate() {
	    return date;
	}
	
	public void setWeekSelected(int weekSelected) {
        this.weekSelected = weekSelected;
    }
    
    public int getWeekSelected() {
        return weekSelected;
    }
}
