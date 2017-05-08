package com.github.scottswolfe.kathyscleaning.completed.model;

import java.util.ArrayList;
import java.util.List;


public class ExceptionData {

/* INSTANCE VARIABLES ======================================================= */
	
    private List<ExceptionEntry> entries; 

    
	
/* CONSTRUCTORS ============================================================= */
	
	public ExceptionData(List<ExceptionEntry> entries) {
	    this.entries = entries;
	}
	
	public ExceptionData() {
	    entries = new ArrayList<>();
	}
	
	
	
/* PUBLIC METHODS =========================================================== */
	
	/**
	 * Checks if an exception entry exists
	 * 
	 * @return return true if there is an exception entry, false otherwise
	 */
	public boolean isException() {
	    if (entries.isEmpty()) {
	        return false;
	    }
	    for (ExceptionEntry entry : entries) {
	        if (!entry.getWorker_name().equals("") || 
	                !entry.getTime_begin().equals("") ||
	                !entry.getTime_end().equals("")) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public List<ExceptionEntry> getEntries() {
	    return entries;
	}
	
	public ExceptionEntry getEntry(int index) {
	    return entries.get(index);
	}
	
	public void setEntries(List<ExceptionEntry> entries) {
	    this.entries = entries;
	}
	
}
