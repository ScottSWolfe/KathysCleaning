package src.java.submit.model;

import java.io.File;
import java.util.Calendar;


public class Data {

	
//  FIELDS
	
	public DayData[] dayData;
	CovenantData covData;
	
	//String day;
	//String month;
	//String year;
	
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
	
	/*
	public void setDate( String month, String day, String year ) {
		this.month = month;
		this.day = day;
		this.year = year;
	}
	
	public String[] getDate () {
		
		String[] s = new String[3];
		
		s[0] = month;
		s[1] = day;
		s[2] = year;
		
		return s;
	}
	*/

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
