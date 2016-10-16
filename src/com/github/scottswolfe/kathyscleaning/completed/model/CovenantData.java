package com.github.scottswolfe.kathyscleaning.completed.model;


public class CovenantData {

	
//  FIELDS
	
	String[] worker;  // think about creating a worker class??
	int[][] time_begin;
	int[][] time_end;
	
	
	
//  CONSTRUCTOR
	
	public CovenantData(String[] worker, int[][] time_begin, int[][] time_end) {
		
		this.worker = worker;
		this.time_begin = time_begin;
		this.time_end = time_end;
		
	}
	
	
	
//  METHODS
	
	public void setWorker(String[] worker) {
		this.worker = worker;
	}
	
	public void setTimeBegin(int[][] time_begin) {
		this.time_begin = time_begin;
	}
	
	public void setTimeEnd(int[][] time_end) {
		this.time_end = time_end;
	}
	
	
	public String[] getWorker() {
		return worker;
	}
	
	public int[][] getTimeBegin() {
		return time_begin;
	}
	
	public int[][] getTimeEnd() {
		return time_end;
	}
	
	
}
