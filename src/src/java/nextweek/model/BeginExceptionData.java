package src.java.nextweek.model;


public class BeginExceptionData {
	// this is an object class that contains the data for one employee's meeting exception
	
	// FIELDS
	private String name;
	private String location;
	private String time;
	private String note;
	

	
	// CONSTRUCTOR
	public BeginExceptionData(String name, String location, String time, String note) {
		super();
		this.name = name;
		this.location = location;
		this.time = time;
		this.note = note;
	}
	
	public BeginExceptionData() {
		
	}
	
	
	
	// PRIVATE METHODS
	
	
	
	// PUBLIC METHODS
	
	public boolean doesExist() {
	
		boolean exist = false;
		if (name != null) {
			exist = true;
		}
		
		return exist;
	}
	
	
	// BASIC GETTERS and SETTERS
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
