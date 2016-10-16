package com.github.scottswolfe.kathyscleaning.scheduled.model;

public class NW_CovenantDayData {

	
	// FIELDS
	CovJobWorker[] cjw;
	
	
	// CONSTRUCTOR
	
	
	// METHODS
	
	
	// INNER CLASS
	
	public class CovJobWorker {
		
		
		// FIELDS
		private String name;
		private String job;
		private String note;
		
		
		// METHODS
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getJob() {
			return job;
		}
		public void setJob(String job) {
			this.job = job;
		}
		public String getNote() {
			return note;
		}
		public void setNote(String note) {
			this.note = note;
		}
		
	}
	
}
