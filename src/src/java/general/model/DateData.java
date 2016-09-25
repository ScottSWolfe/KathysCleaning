package src.java.general.model;

public class DateData {

	
	String month;
	String day;
	String year;
	

	
	
	public DateData(String month, String day, String year){
		
		this.month = month;
		this.day = day;
		this.year = year;
		
	}
	
	
	public DateData(String month, int day, int year){
		
		this.month = month;
		this.day = String.valueOf(day);
		this.year = String.valueOf(year);
		
	}
	
	
}
