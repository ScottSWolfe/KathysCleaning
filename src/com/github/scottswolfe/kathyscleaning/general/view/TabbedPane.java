package com.github.scottswolfe.kathyscleaning.general.view;


import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.github.scottswolfe.kathyscleaning.nextweek.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.submit.model.DayData;
import com.github.scottswolfe.kathyscleaning.submit.view.DayPanel;


@SuppressWarnings("serial")
public class TabbedPane extends JTabbedPane {

//  FIELDS
	
	public DayPanel[] day_panel;
	// OR
	public NW_DayPanel[] nw_day_panel;
	
	DayData[] day_data;
	JScrollPane[] jsp;
	
	public int previous_tab;
	
//  CONSTRUCTOR
	
	/*
	public TabbedPane(DayPanel[] day_panel, JScrollPane[] jsp) {
		
		this.day_panel = day_panel;
		this.jsp = jsp;
	}
	*/
	
//  METHODS
	
	public void changePreviousTab(int index){
		this.previous_tab = index;
	}
	
	
	
}
