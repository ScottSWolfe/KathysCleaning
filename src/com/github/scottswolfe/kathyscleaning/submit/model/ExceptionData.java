package com.github.scottswolfe.kathyscleaning.submit.model;

import com.github.scottswolfe.kathyscleaning.submit.view.ExceptionPanel;

public class ExceptionData {

//  FIELDS
	
	public String[] worker_name;
	public String[] time_begin;
	public String[] time_end;
	
	public boolean edited = false;
	
	
//  CONSTRUCTOR
	
	public ExceptionData( String[] worker_name, String[] time_begin, String[] time_end ) {
		this.worker_name = worker_name;
		this.time_begin = time_begin;
		this.time_end = time_end;
		
		edited = true;
	}
	
	public ExceptionData(){
		worker_name = new String[ExceptionPanel.EXCEPTION_ROWS];
		for (int i=0; i<ExceptionPanel.EXCEPTION_ROWS; i++) {
			worker_name[i] = "";
		}
		
		time_begin = new String[ExceptionPanel.EXCEPTION_ROWS];
		for (int i=0; i<ExceptionPanel.EXCEPTION_ROWS; i++) {
			time_begin[i] = "";
		}
		
		time_end = new String[ExceptionPanel.EXCEPTION_ROWS];
		for (int i=0; i<ExceptionPanel.EXCEPTION_ROWS; i++) {
			time_end[i] = "";
		}
				
	}
	
	
//  METHODS
	
	public void setWorkers( String[] s ){
		
		worker_name = s;
	}
	
	
	public void setTimeBegin( String[] s ){
		
		time_begin = s;
	}
	
	
	public void setTimeEnd( String[] s ){
		
		time_end = s;
	}
	
	/*
	private String setTime( String s ){
		
		char[] ch = s.toCharArray();
		char[] temp = new char[ch.length - 1];
		String time;
		int shift = 0;
		 
		// removing ':'
		for(int i=0; i<ch.length; i++ ){
			Character k = ch[i];
			if (!k.isDigit(k)){
				shift++;
			}
			else{
				temp[i-shift]=ch[i];
			}
		}
		time = new String(temp);
		
		return time;
	}
	*/
	
	public ExceptionData getExceptionData(){
		
		return this;
	}
}
