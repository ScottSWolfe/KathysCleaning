package src.java.nextweek.model;

import src.java.general.model.DefaultWorkerData;

public class NoteData {
	
	// FIELDS
	public String[] name_box_data;
	public String[] note_field_data;
	DefaultWorkerData dwd;
	
	// CONSTRUCTOR
	public NoteData ( String[] name_box_data, String[] note_field_data, DefaultWorkerData dwd ) {
		this.name_box_data = name_box_data;
		this.note_field_data = note_field_data;
		this.dwd = dwd;
	}
	
	public NoteData ( int num_notes ) {
		
		/*
		String[] name_box_data = new String[num_notes];
		String[] note_field_data = new String[num_notes];
		
		for (int i=0; i<num_notes; i++) {
			name_box_data[i] = "";
			note_field_data[i] = "";
		}
		*/
		
	}
	
	// GETTERS & SETTERS
	public String[] getNameBoxData () {
		return name_box_data;
	}
	public void setNameBoxData ( String[] name_data ) {
		name_box_data = name_data;
	}
	
	public String[] getNoteFieldData () {
		return note_field_data;
	}
	public void setNoteFieldData ( String[] note_data ) {
		note_field_data = note_data;
	}
	
	
}
