package com.github.scottswolfe.kathyscleaning.nextweek.model;

import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;

public class NW_HeaderData {

	
//  FIELDS
	
	String week;
	WorkerList dwd;
	
	
	
//  CONSTRUCTOR
	
	public NW_HeaderData(String week, WorkerList dwd) {
		
		this.week = week;
		this.dwd = dwd;
		
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
	
	
}
