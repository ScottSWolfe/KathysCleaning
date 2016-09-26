package com.github.scottswolfe.kathyscleaning.nextweek.model;

import com.github.scottswolfe.kathyscleaning.nextweek.view.NW_HeaderPanel;

public class NW_DayData {

	
//  FIELDS
	
	NW_HeaderData headerData;
	
	public NW_HouseData[] houseData;
	public BeginExceptionData[] bed;
	public String meet_location;
	public String meet_time;
	
	public String[] cov_worker;
	
	public NoteData dayNoteData;
	public NoteData covNoteData;
	
	
	// OR
	
	WorkerSchedule[] ws;
	
	
	
//  CONSTRUCTOR
	
	public NW_DayData() {
		
	}
	
	
	public NW_DayData(NW_HeaderData headerData, NW_HouseData[] houseData, BeginExceptionData[] bed) {
		this.headerData = headerData;
		this.houseData = houseData;
		this.bed = bed;
	}
	
	
		
//  METHODS
	
	public int getNumWorkSchedules(){		
		if (ws != null) {
			return ws.length;
		}
		else {
			return 0;
		}
	}

	public void setWorkerSchedule( WorkerSchedule[] ws ) {
		this.ws = ws;
	}
	
	public WorkerSchedule[] getWorkerSchedule() {
		return ws;
	}
	
	public NW_HeaderData getHeaderData( NW_HeaderPanel header_panel ) {
		
		return header_panel.getHeaderData();
		
	}
	
	
	
	public void setHeader(NW_HeaderData headerData) {
		this.headerData = headerData;
	}
	
	public void setHouseData(NW_HouseData[] houseData) {
		this.houseData = houseData;
	}
	
	
	public NW_HeaderData getHeaderData() {
		return headerData;
	}
	
	public NW_HouseData[] getHouseData() {
		return houseData;
	}
	
	public BeginExceptionData[] getBeginExceptionData() {
		return bed;
	}

	public void setBeginExceptionData(BeginExceptionData[] bed) {
		this.bed = bed;
	}
	
}
