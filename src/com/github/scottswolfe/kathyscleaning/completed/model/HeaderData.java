package com.github.scottswolfe.kathyscleaning.completed.model;

import java.util.Calendar;

import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;

public class HeaderData {

	WorkerList workers;
	private Calendar date;
	
	public HeaderData() {
		workers = new WorkerList();
		date = SessionModel.getCompletedStartDay();
	}
		
	public void setWorkers(WorkerList workers) {
		this.workers = workers;
	}
	
	public WorkerList getWorkers() {
		return workers;
	}
	
	public void setDate(Calendar date) {
	    this.date = date;
	}
	
	public Calendar getDate() {
	    return date;
	}
	
}
