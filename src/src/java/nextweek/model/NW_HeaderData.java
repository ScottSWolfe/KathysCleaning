package src.java.nextweek.model;

import src.java.general.model.DefaultWorkerData;

public class NW_HeaderData {

	
//  FIELDS
	
	String week;
	DefaultWorkerData dwd;
	
	
	
//  CONSTRUCTOR
	
	public NW_HeaderData(String week, DefaultWorkerData dwd) {
		
		this.week = week;
		this.dwd = dwd;
		
	}
	
	

// METHODS
	/*
	public boolean[] isSelected(){
		
		for(int i=0; i < dwd.default_workers.length; i++){
			
			
			
		}
	}
	*/
	
	public void setWeek(String week) {
		this.week = week;
	}
	
	public void setDWD(DefaultWorkerData dwd) {
		this.dwd = dwd;
	}
	
	
	public String getWeek() {
		return week;
	}
	
	public DefaultWorkerData getDWD() {
		return dwd;
	}
	
	
}
