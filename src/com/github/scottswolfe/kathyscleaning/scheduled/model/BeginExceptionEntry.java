package com.github.scottswolfe.kathyscleaning.scheduled.model;

/**
 *  This contains the data for one employee's meeting exception.
 */
public class BeginExceptionEntry {
	
	// FIELDS
	private String name;
	private String location;
	private String time;
	private String note;
	
	// CONSTRUCTORS
	public BeginExceptionEntry(String name, String location, String time, String note) {
		this.name = name;
		this.location = location;
		this.time = time;
		this.note = note;
	}
	
	public BeginExceptionEntry() {
		name = "";
		location = "";
		time = "";
		note = "";
	}
	
	
	// PUBLIC METHODS
	
	/**
	 * Checks if the exception has all the necessary data to add into the excel
	 * document.
	 * 
	 * @return true if the entry has the necessary data, false otherwise.
	 */
	public boolean isException() {
		if (name != null && !name.equals("") &&
		        location != null && !location.equals("") &&
		        time != null && !time.equals("")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if the exception entry has any data.
	 * 
	 * @return true if every field is empty, false otherwise.
	 */
	public boolean isBlank() {
	    if (name != null && !name.equals("")) {
            return false;
        }
	    if (location != null && !location.equals("")) {
	        return false;
	    }
	    if (time != null && !time.equals("")) {
            return false;
        }
	    if (note != null && !note.equals("")) {
            return false;
        }
	    return true;
	}
	
	
	// GETTERS and SETTERS
	public String getName() {
		return name;
	}

	public void setName(String name) {
		if ( name != null && name.length() > 0 ) {
			this.name = name;
		}
		else{
			this.name = "";
		}
		
	}
	
	public String getMeetLocation() {
		return location;
	}
	
	public void setMeetLocation(String location) {
		if ( location != null && location.length() > 0 ) {
			this.location = location;
		}
		else{
			this.location = "";
		}
	}
	
	public String getTime() {
		return time;
	}
	
	public void setTime(String time) {
		if ( time != null && time.length() > 0 ) {
			this.time = time;
		}
		else{
			this.time = "";
		}
	}
	
	public String getNote() {
		return note;
	}
	
	public void setNote(String note) {
		if ( note != null && note.length() > 0 ) {
			this.note = note;
		}
		else{
			this.note = "";
		}
	}
	
}
