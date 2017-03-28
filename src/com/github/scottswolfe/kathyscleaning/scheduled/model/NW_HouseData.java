package com.github.scottswolfe.kathyscleaning.scheduled.model;


public class NW_HouseData {

	
	// FIELDS
	private String house_name;
	private String[] selected_workers;
	
	
	
	// CONSTRUCTORS
	public NW_HouseData() {
	    house_name = "";
	    selected_workers = new String[3];
	    for (int i = 0; i < 3; i++) {
	        selected_workers[i] = "";
	    }
	}
	
		
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
