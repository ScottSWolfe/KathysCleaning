package com.github.scottswolfe.kathyscleaning.scheduled.model;

import com.github.scottswolfe.kathyscleaning.completed.model.DayData;

public class NW_Data {

	public NW_DayData[] dayData;
	NW_CovenantData covenant;
	public DayData[] completedDayData;
	
	public NW_Data(NW_DayData[] dayData, NW_CovenantData covenant, BeginExceptionData bed) {
		this.dayData = dayData;
		this.covenant = covenant;
	}
	
	public NW_Data(){
		dayData = new NW_DayData[5];
		completedDayData = new DayData[5];
		for (int i = 0; i < 5; i++) {
		    dayData[i] = new NW_DayData();
		    completedDayData[i] = new DayData();
		}
		covenant = new NW_CovenantData();
	}
	
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
