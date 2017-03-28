package com.github.scottswolfe.kathyscleaning.scheduled.model;

import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;

public class NW_HeaderData {

	
//  FIELDS
	
	String week;
	WorkerList dwd;
	
	
	
//  CONSTRUCTOR
	
	public NW_HeaderData() {
	    week = "";
	    dwd = new WorkerList();
	}
	
	public NW_HeaderData(String week, WorkerList dwd) {
		
		this.week = week;
		this.dwd = dwd;
		
	}
		
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
	
	
}
