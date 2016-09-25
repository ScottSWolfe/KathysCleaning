package src.java.submit.model;

import src.java.submit.view.HeaderPanel;


public class DayData {

	
//  FIELDS
	
	HeaderData headerData;
	public HouseData[] houseData;
	
	
	
//  CONSTRUCTOR
	
	public DayData() {
		
	}
	
	
	public DayData(HeaderData headerData, HouseData[] houseData) {
		this.headerData = headerData;
		this.houseData = houseData;
	}
	
	
	
	
	
//  METHODS
	
	public HeaderData getHeaderData( HeaderPanel header_panel ) {
		return header_panel.getHeaderData();
	}
	
	public void setHeader(HeaderData headerData) {
		this.headerData = headerData;
	}
	
	public HeaderData getHeaderData() {
		return headerData;
	}
	
	public void setHouseData(HouseData[] houseData) {
		this.houseData = houseData;
	}

	public HouseData[] getHouseData() {
		return houseData;
	}
	
	
}
