package com.github.scottswolfe.kathyscleaning.nextweek.model;


public class WorkerSchedule {

	
	// FIELDS
	String name;
	String location;
	public String time;
	public String[] house;
	public String[] note;
	String schedule;
	
	public String ex_note;
	public boolean ex_note_written = false;
	
	public boolean working_covenant = false;
	
	String covenant; // still needed??
	
	
	
	
	// CONSTRUCTOR
	public WorkerSchedule() {
		
	}


	// METHDOS
	
	
	// isEquivalent checks a string character by character to see if it matches the name of the worker schedule
	public boolean isEquivalent( String s ) {
		
		// putting new string into Character array
		Character[] c = null;
		if ( s != null) {
			c = new Character[s.length()];
			for ( int i=0; i<s.length(); i++) {
				c[i] = s.charAt(i);
			}
		}
		else {
			return false;
		}
		
		// putting name into Character array
		Character[] k = null;
		if ( name != null) {
			k = new Character[name.length()];
			for ( int i=0; i<name.length(); i++) {
				k[i] = name.charAt(i);
			}
		}
		else {
			return false;
		}
		
		
		// comparing the two arrays
		boolean done = false;
		int i=0;
		while ( !done ) {
			
			// if
			if (k.length <= i) {
				done = true;
			}
			else if ( c.length > i && c[i].equals(k[i]) ) {
				i++;
			}
			else {
				return false;
			}
			
		}
		
		return true;
		
	}
	
	
	// addHouse adds a house string onto the array of house strings that may or may not already exist
	public void addHouse( String add ) {
		
		String[] s;
		
		// declaring new string array; length determined by previous array
		if(house == null){
			s = new String[1];
		}
		else {
			s = new String[house.length + 1];
		}
		
		// copying strings from previous array and adding new string
		int index = 0;
		for (int i=0; i<s.length-1; i++) {
			s[i] = house[i];
			index++;
		}
		s[index] = add;
		
		// setting the new house variable
		setHouse(s);	
	}
	
	
	// addNote adds a note string onto the array of not strings that may or may not already exist
	public void addNote( String add ) {
		
		String[] s;
		
		// declaring new string array; length determined by previous array
		if(note == null){
			s = new String[1];
		}
		else {
			s = new String[note.length + 1];
		}
		
		// copying strings from previous array and adding new string
		int index = 0;
		for (int i=0; i<s.length-1; i++) {
			s[i] = note[i];
			index++;
		}
		s[index] = add;
		
		// setting the new note variable
		setNote(s);
	}
	
	
	public String createSchedule() {
	// this method puts all of an employee's schedule data into one string
		
		String s;
		
		String h = new String();
		for(int i=0; i<house.length; i++){
			h = new String( h + ", " + house[i]);
		}
		
		s = new String( time + " " + location + "..." + h + covenant + note );
		return s;
	}
	
	
	public String createExceptionSchedule() {
	// this method puts all of an employee's exception schedule data into one string

		String s;
		
		String h = new String();
		for(int i=0; i<house.length; i++){
			h = new String( h + ", " + house[i]);
		}
		
		s = new String( time + " " + location + "..." + h + covenant + note );
		return s;
	}
	
	
	
	
	
	// Basic Getters and Setters
	
	
	
	public void setSchedule( String s ) {
		schedule = s;
	}
	
	public String getSchedule() {
		return schedule;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getMeetLocation() {
		return location;
	}

	public void setMeetLocation(String location) {
		this.location = location;
	}


	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}


	public String[] getHouse() {
		return house;
	}

	public void setHouse(String[] house) {
		this.house = house;
	}


	public String getCovenant() {
		return covenant;
	}

	public void setCovenant(String covenant) {
		this.covenant = covenant;
	}


	public String[] getNote() {
		return note;
	}

	public void setNote(String[] note) {
		this.note = note;
	}
	
}
