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


public class DeleteHouseListener implements ActionListener {

	
//  FIELDS
	
	DayPanel day_panel;
	HousePanel house_panel;
	DefaultWorkerData dwd;
	JFrame frame;
	TabbedPane tp;
	
	
	
//  CONSTRUCTOR
	
	public DeleteHouseListener(DayPanel day_panel, HousePanel house_panel, DefaultWorkerData dwd, JFrame frame, TabbedPane tp) {
		this.day_panel = day_panel;
		this.house_panel = house_panel;
		this.dwd = dwd;
		this.frame = frame;
		this.tp = tp;
	}
	
	
	
//  LISTENER
	
	public void actionPerformed(ActionEvent e){
		
		// for resizing at end of method
		int panel_height = day_panel.house_panel[0].getHeight() + DayPanel.PANEL_PADDING;
		
		
		// initializing variables
		int num = day_panel.house_panel.length;
		int index = -1;
		for(int i=0; i<day_panel.house_panel.length; i++){
			if (day_panel.house_panel[i] == house_panel){
				index = i;
			}
		}
		
		
		// only delete house if there are more than one house panels remaining
		if (num > 1) {
		
		
		//remove old panels
		for(int i=0; i<num; i++){
			day_panel.jsp_panel.remove(day_panel.house_panel[i]);
		}
		
		
		// creating new array of house panels for the day panel
		HousePanel[] new_house_panel_array = new HousePanel[num-1];
		
		
		// copy panels whose index doesn't change
		for(int i=0; i<index; i++){
			new_house_panel_array[i] = day_panel.house_panel[i].copyPanel(); 
		}
		
				
		// copy panels moved up one index
		for(int i=index; i<num-1; i++){
			new_house_panel_array[i] = day_panel.house_panel[i+1].copyPanel();
		}
		
		
		// set house_panel field in the day_panel
		day_panel.house_panel = new_house_panel_array;
		day_panel.addFlexibleFocusListeners();
		
		
		// add new panels
		for(int i=0; i<num-1; i++){
			day_panel.jsp_panel.add(day_panel.house_panel[i], new String("wrap " + DayPanel.PANEL_PADDING + ", growx") );
		}
		
		
		// Resizing frame		
		Dimension newSize = new Dimension( frame.getWidth(), frame.getHeight() );
		int jsp_constant = 6; // this is difference between size of jsp (scrollpane) and jsp_panel (jpanel on scrollpane)
		int full_jsp_panel_height = day_panel.jsp_panel.getHeight() + jsp_constant;
		int jsp_difference = full_jsp_panel_height - day_panel.jsp.getHeight();
		
		
		// if one or more house panels are hidden on Scroll pane: don't change frame size
		if ( jsp_difference >= panel_height ) {
			// do nothing
		}
		else if ( jsp_difference < panel_height && jsp_difference > 0 ) {
			newSize.setSize( frame.getWidth(), frame.getHeight() - (panel_height - jsp_difference) );
		}
		else if ( jsp_difference <= 0 ) {
			newSize.setSize( frame.getWidth(), frame.getHeight() - panel_height );
		}
		

		// checking that frame is within the screen
		Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		//System.out.println("size: " + effectiveScreenSize.);
		int extra_space = (frame.getLocation().y + newSize.height) - (int) effectiveScreenSize.getHeight();
		if ( frame.getLocation().y + newSize.height > effectiveScreenSize.getHeight() ) {
			newSize.setSize( frame.getWidth(), frame.getHeight() - extra_space );
		}
		
		
		frame.setSize(newSize);
		frame.revalidate();
		frame.repaint();
		
		
		} // end if statement that only allows for deleting panel only if more than one house panel remains
		
	}
		
}
