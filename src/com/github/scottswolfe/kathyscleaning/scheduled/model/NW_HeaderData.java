package com.github.scottswolfe.kathyscleaning.scheduled.model;

import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;

public class NW_HeaderData {

	
//  FIELDS
	
	String week;
	WorkerList dwd;
	
	
	
//  CONSTRUCTOR
	
	public NW_HeaderData() {
	    week = "";
	    dwd = new WorkerList(WorkerList.HOUSE_WORKERS);
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
