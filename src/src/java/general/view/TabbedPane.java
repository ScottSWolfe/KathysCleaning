package src.java.general.view;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import src.java.nextweek.view.NW_DayPanel;
import src.java.submit.model.DayData;
import src.java.submit.view.DayPanel;


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
