package com.github.scottswolfe.kathyscleaning.scheduled.model;


public class NW_CovenantData {

	
//  FIELDS
	
	NW_CovenantDayData[] cdd;
	
	
	
//  CONSTRUCTOR
	
	public NW_CovenantData() {
		cdd = new NW_CovenantDayData[5];
		for (int i = 0; i < 5; i++) {
		    cdd[i] = new NW_CovenantDayData();
		}
	}
	
	
	
//  METHODS
	
	
	
	
}
