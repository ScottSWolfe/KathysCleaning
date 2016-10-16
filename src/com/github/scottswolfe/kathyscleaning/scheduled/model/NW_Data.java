package com.github.scottswolfe.kathyscleaning.scheduled.model;

public class NW_Data {

	
//  FIELDS
	
	public NW_DayData[] dayData;
	NW_CovenantData covenant;
	
	
	
//  CONSTRUCTOR
	
	public NW_Data(NW_DayData[] dayData, NW_CovenantData covenant, BeginExceptionData bed) {
		this.dayData = dayData;
		this.covenant = covenant;
	}
	
	public NW_Data(){
		
	}
	
	
	
//  METHODS
	
	public void setDayData(NW_DayData[] dayData){
		this.dayData = dayData;
	}
	
	public void setCovenant(NW_CovenantData covenant) {
		this.covenant = covenant;
	}
	
	
	public NW_DayData[] getDay() {
		return dayData;
	}
	
	public NW_CovenantData getCovenant() {
		return covenant;
	}
	
	
}
