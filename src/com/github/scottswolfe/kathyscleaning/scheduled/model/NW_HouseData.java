package com.github.scottswolfe.kathyscleaning.scheduled.model;

import java.util.ArrayList;
import java.util.List;

public class NW_HouseData {

	
	// FIELDS
	private String house_name;
	private List<String> selected_workers;
	
	
	// CONSTRUCTORS
	public NW_HouseData() {
	    house_name = "";
	    selected_workers = new ArrayList<>();
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
	
}
