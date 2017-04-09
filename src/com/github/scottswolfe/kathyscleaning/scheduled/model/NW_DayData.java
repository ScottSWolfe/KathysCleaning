package com.github.scottswolfe.kathyscleaning.scheduled.model;

import java.util.ArrayList;
import java.util.List;

import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_HeaderPanel;

public class NW_DayData {
	
	NW_HeaderData headerData;
	
	public NW_HouseData[] houseData;
	public List<BeginExceptionData> beginExceptionList;
	public String meet_location;
	public String meet_time;
	
	public WorkerList cov_worker;
	
	public NoteData dayNoteData;
	public NoteData covNoteData;
	
	
	// OR
	
	List<WorkerSchedule> ws;
	
	
	
//  CONSTRUCTOR
	
	public NW_DayData() {
		headerData = new NW_HeaderData();
		houseData = new NW_HouseData[3];
		for (int i = 0; i < 3; i++) {
		    houseData[i] = new NW_HouseData();
		}
		beginExceptionList = new ArrayList<>();
		meet_location = "";
		meet_time = "";
		cov_worker = new WorkerList();
		covNoteData = new NoteData();
		ws = new ArrayList<>();
	}
	
	public NW_DayData(NW_HeaderData headerData, NW_HouseData[] houseData, List<BeginExceptionData> bed) {
		this.headerData = headerData;
		this.houseData = houseData;
		this.beginExceptionList = bed;
	}
	
	
		
//  METHODS
	
	public int getNumWorkSchedules(){		
		if (ws != null) {
			return ws.size();
		}
		else {
			return 0;
		}
	}

	public void setWorkerSchedule(List<WorkerSchedule> ws) {
		this.ws = ws;
	}
	
	public List<WorkerSchedule> getWorkerSchedule() {
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
	
	public List<BeginExceptionData> getBeginExceptionData() {
		return beginExceptionList;
	}

	public void setBeginExceptionData(List<BeginExceptionData> bed) {
		this.beginExceptionList = bed;
	}
	
}
