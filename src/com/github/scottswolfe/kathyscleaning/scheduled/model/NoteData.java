package com.github.scottswolfe.kathyscleaning.scheduled.model;

import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_NotePanel;

public class NoteData {
	
	// FIELDS
	public String[] name_box_data;
	public String[] note_field_data;
	WorkerList dwd;
	
	// CONSTRUCTOR
	public NoteData() {
	    name_box_data = new String[NW_NotePanel.ROWS];
	    note_field_data = new String[NW_NotePanel.ROWS];
	    for (int i = 0; i < NW_NotePanel.ROWS; i++) {
	        name_box_data[i] = "";
	        note_field_data[i] = "";
	        dwd = new WorkerList();
	    }
	}
	
	public NoteData ( String[] name_box_data, String[] note_field_data, WorkerList dwd ) {
		this.name_box_data = name_box_data;
		this.note_field_data = note_field_data;
		this.dwd = dwd;
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
