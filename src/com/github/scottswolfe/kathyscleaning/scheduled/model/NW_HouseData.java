package com.github.scottswolfe.kathyscleaning.scheduled.model;


public class NW_HouseData {

	
	// FIELDS
	private String house_name;
	private String[] selected_workers;
	
	
	
	// CONSTRUCTORS
	
	
		
	// METHODS
	
	// Set House Name
	public void setHouseName(String house_name) {
		this.house_name = house_name;
	}
	
	// Set Selected Workers
	public void setSelectedWorkers( String[] selected_workers ){
		this.selected_workers = selected_workers;
	}
	
	
	public String getHouseName() {		
		return house_name;	
	}
	
	public String[] getSelectedWorkers() {
		return selected_workers;
	}
	
	
}
