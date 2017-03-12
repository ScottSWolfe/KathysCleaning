package com.github.scottswolfe.kathyscleaning.completed.model;

import java.io.File;
import java.util.Calendar;


public class Data {

	
//  FIELDS
	
	public DayData[] dayData;
	CovenantData covData;	
	public Calendar date;
	public static File new_file;
	
	
//  CONSTRUCTOR
	
	public Data(DayData[] dayData, CovenantData covData) {
		
		this.dayData = dayData;
		this.covData = covData;
		
	}
	
	public Data(){
		
	}
	
	
	
//  METHODS

	public void setDate ( Calendar date ) {
		this.date = date;
	}
	
	public Calendar getDate () {
		return date;
	}
	
	public static void setFile( File f ) {
		new_file = f;
	}
	
	public static File getFile() {
		return new_file;
	}
	
	public void setDayData(DayData[] dayData){
		this.dayData = dayData;
	}
	
	public void setCovData(CovenantData covData) {
		this.covData = covData;
	}
	
	
	public DayData[] getDay() {
		return dayData;
	}
	
	public CovenantData getCovData() {
		return covData;
	}
	
	
}
