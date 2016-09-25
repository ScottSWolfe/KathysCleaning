package src.java.general.view;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import src.java.nextweek.view.NW_DayPanel;
import src.java.submit.model.DayData;


@SuppressWarnings("serial")
public class NW_TabbedPane extends JTabbedPane {

//  FIELDS
	
	NW_DayPanel[] day_panel;
	DayData[] day_data;
	JScrollPane[] jsp;
	
	int previous_tab;
	
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
