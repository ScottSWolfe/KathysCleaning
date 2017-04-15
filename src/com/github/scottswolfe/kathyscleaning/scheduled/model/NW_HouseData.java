package com.github.scottswolfe.kathyscleaning.scheduled.model;

import java.util.ArrayList;
import java.util.List;

import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;

public class NW_HouseData {

	
	// FIELDS
	private String house_name;
	private List<String> selected_workers;
	private WorkerList workers;
	
	
	// CONSTRUCTORS
	public NW_HouseData() {
	    house_name = "";
	    selected_workers = new ArrayList<>();
	    workers = new WorkerList(WorkerList.HOUSE_WORKERS);
	}
	
		
	// METHODS
	
	// Set House Name
	public void setHouseName(String house_name) {
		this.house_name = house_name;
	}
	
	// Set Selected Workers
	public void setSelectedWorkers(List<String> selected_workers) {
		this.selected_workers = selected_workers;
	}
	
	
	public String getHouseName() {		
		return house_name;	
	}
	
	public List<String> getSelectedWorkers() {
		return selected_workers;
	}
	
	public void setWorkerList(WorkerList workers) {
	    this.workers = workers;
	}
	
	public WorkerList getWorkers() {
	    return workers;
	}
	
}
