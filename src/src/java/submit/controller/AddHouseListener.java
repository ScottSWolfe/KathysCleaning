package src.java.submit.controller;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import src.java.general.model.DefaultWorkerData;
import src.java.general.view.TabbedPane;
import src.java.submit.view.DayPanel;
import src.java.submit.view.HousePanel;


public class AddHouseListener implements ActionListener {

//  FIELDS
	
	DayPanel day_panel;
	HousePanel house_panel;
	DefaultWorkerData dwd;
	JFrame frame;
	TabbedPane tp;
	
	
	
//  CONSTRUCTOR
	
	public AddHouseListener(DayPanel day_panel, HousePanel house_panel, DefaultWorkerData dwd, JFrame frame, TabbedPane tp) {
		this.day_panel = day_panel;
		this.house_panel = house_panel;
		this.dwd = dwd;
		this.frame = frame;
		this.tp = tp;
	}
	
	
	
//  LISTENER
	
	public void actionPerformed(ActionEvent e){
		
		if (day_panel.house_panel.length < 5) {
		
		// info for resizing at end of method
		int panel_height = day_panel.house_panel[0].getHeight() + DayPanel.PANEL_PADDING;
		
		
		// initializing variables
		int num_houses = day_panel.house_panel.length;
		int index = -1;
		for(int i=0; i<num_houses; i++){
			if (day_panel.house_panel[i] == house_panel){
				index = i;
			}
		}
		
		
		//remove old panels
		for(int i=0; i<num_houses; i++){
			day_panel.jsp_panel.remove(day_panel.house_panel[i]);
		}
		
		
		// creating new array of house panels for the day panel
		HousePanel[] new_house_panel = new HousePanel[ num_houses + 1 ];
		
		
		// copy panels that don't change
		for(int i=0; i<index+1; i++){
			new_house_panel[i] = day_panel.house_panel[i].copyPanel(); 
		}
		
		
		// creating new panel
		new_house_panel[index+1] = new HousePanel(new String("House " + (index + 2) ), dwd, day_panel, frame, tp);
		
		
		// copying panels moved down one index
		for(int i=index+2; i<num_houses + 1; i++){
			new_house_panel[i] = day_panel.house_panel[i-1].copyPanel();
		}
		
		
		// setting day_panel's house_panel field to new array
		day_panel.house_panel = new_house_panel;
		day_panel.addFlexibleFocusListeners();
		
		
		//add new panels
		for(int i=0; i<num_houses+1; i++){
			day_panel.jsp_panel.add(day_panel.house_panel[i], new String("wrap " + DayPanel.PANEL_PADDING + ", growx") );
		}


		// Resizing frame
		//frame.revalidate();
		//frame.pack();
		
		Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		
		Dimension newSize = new Dimension( frame.getWidth(), frame.getHeight() );
		int base = frame.getHeight() - day_panel.jsp.getHeight();
		int remaining_space = (int) effectiveScreenSize.getHeight() - (int) frame.getLocation().getY() - frame.getHeight();
		int remaining_from_base = (int) effectiveScreenSize.getHeight() - (int) frame.getLocation().getY() - base;
		int jsp_constant = 6; // this is difference between size of jsp (scrollpane) and jsp_panel (jpanel on scrollpane)
		int full_jsp_panel_height = day_panel.jsp_panel.getHeight() + jsp_constant;
		int jsp_difference = day_panel.jsp.getHeight() - full_jsp_panel_height;		
		
		
		// if all house panels are visible and room to extend full panel: extend by one panel
		if ( jsp_difference >= 0  &&  remaining_space - panel_height >= 0 ) {
			newSize.setSize(frame.getWidth(), frame.getHeight() + panel_height);
		}
		
		// if all house panels are visible and less than one panel to extend: extend as much as possible
		else if ( jsp_difference >= 0  &&  ( remaining_space > 0 && remaining_space < panel_height ) ) {
			newSize.setSize(frame.getWidth(), frame.getHeight() + remaining_space);
		}
		
		// if any house panels are hidden on scroll pane: extend until no hidden panels or to bottom of screen
		else if ( jsp_difference < 0 ) {
			newSize.setSize( frame.getWidth(), base + Math.min( remaining_from_base, full_jsp_panel_height) );
		}
				
		frame.setSize(newSize);
		frame.revalidate();
		frame.repaint();
		
		} // end if length < 5
		
	}
	
}
