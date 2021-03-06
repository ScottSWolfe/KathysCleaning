package com.github.scottswolfe.kathyscleaning.completed.model;

import java.util.ArrayList;
import java.util.List;

import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;

public class HouseData {

	private String house_name;
	private double house_pay;
	private String time_begin;
	private String time_end;
	private List<String> selected_workers;
	private WorkerList workers;
	private ExceptionData exception_data;
	
	public HouseData() {
	    house_name = "";
	    house_pay = 0.0;
	    time_begin = "";
	    time_end = "";
	    selected_workers = new ArrayList<>();
	    workers = new WorkerList(WorkerList.HOUSE_WORKERS);
	    exception_data = new ExceptionData();
	}
	
	public boolean isException() {
	    if (exception_data == null) {
	        return false;
	    }
	    return exception_data.isException();
	}
	
	public void setHouseName(String house_name) {
		this.house_name = house_name;
	}
	
	public void setHousePay(double money_earned) {
		this.house_pay = money_earned;
	}
	
	public void setHousePay(String money_earned) {
		if(money_earned.isEmpty() || money_earned.equals("") ){
			this.house_pay = 0;
		}
		else {
			this.house_pay = Double.parseDouble(money_earned);
		}
	}
	
	public void setTimeBegin(int time_begin) {
		this.time_begin = String.valueOf(time_begin);
	}
	
	public void setTimeBegin(String time_begin){
		this.time_begin = time_begin;
		
	}
	
	public void setTimeEnd(int time_begin) {
		this.time_end = String.valueOf(time_end);
	}
		
	public void setTimeEnd(String time_begin){
		this.time_end = time_begin;
	}
	
	public void setSelectedWorkers(List<String> selected_workers) {
		this.selected_workers = selected_workers;
	}
	
	public void setWorkerList(WorkerList workers) {
	    this.workers = workers;
	}
	
	public void setExceptionData(ExceptionData exception_data) {
		this.exception_data = exception_data;
	}
	
	public String getHouseName() {		
		return house_name;	
	}
	
	public double getHousePay() {
		return house_pay;
	}
	
	public String getTimeBegin() {
		return time_begin;
	}
	
	public String getTimeEnd() {
		return time_end;
	}
	
	public List<String> getSelectedWorkers() {
		return selected_workers;
	}
	
	public WorkerList getWorkerList() {
	    return workers;
	}
	
	public ExceptionData getExceptionData() {
		return exception_data;
	}
	
	// TODO move the logic for this to another class
	public double getHours(){
		double hours;
		if (!this.time_end.isEmpty() && !this.time_begin.isEmpty()) {
			int minutes = convertToMinutes( this.time_end ) - convertToMinutes( this.time_begin );
			hours = convertToHours( minutes );
		} else {
			hours = 0;
		}
		return hours;
	}
	
	private int convertToMinutes( String time ){
		
		char[] temp_ch = time.toCharArray();
		char[] ch = new char[ temp_ch.length - 1 ];
		int minutes;
		
		// remove the ':'
		int shift = 0;
		for(int i=0; i<temp_ch.length; i++){
			Character k = temp_ch[i];
			if(!Character.isDigit(k)){
				shift++;
			}
			else{
				ch[i-shift]=temp_ch[i];
			}
		}
		
		// TODO: **ERROR occurs for work times beginning before 10am and ending after 10am**
		// converting from hhmm to minutes
		if ( ch.length == 4) {
			minutes = (Character.getNumericValue(ch[0]) * 600 + Character.getNumericValue(ch[1]) * 60 + Character.getNumericValue(ch[2]) * 10 + Character.getNumericValue(ch[3]) );
		}
		// TODO: I would like to make this time calculation work more generally (eg if she started a house before 7am or 
		//        finished after 6pm)
		else if ( ch.length == 3) {
			if (Character.getNumericValue(ch[0]) <= 6) { 
				minutes =  (Character.getNumericValue(ch[0]) + 12) * 60 + Character.getNumericValue(ch[1]) * 10 + Character.getNumericValue(ch[2]);
			}
			else {
				minutes =  ((Character.getNumericValue(ch[0]) - 7)%12 + 7) * 60 + Character.getNumericValue(ch[1]) * 10 + Character.getNumericValue(ch[2]);
			}
		}
		else {
			minutes = 0;  // TODO throw some type of error message here??
		}
		
		return minutes;
	}
	
	private double convertToHours( int minutes ) {
		return ((double) minutes) / 60;
	}
	
}
