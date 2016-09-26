package com.github.scottswolfe.kathyscleaning.nextweek.controller;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.general.model.DefaultWorkerData;
import com.github.scottswolfe.kathyscleaning.nextweek.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.nextweek.view.NW_HousePanel;
import com.github.scottswolfe.kathyscleaning.submit.view.DayPanel;


public class NW_DeleteHouseListener implements ActionListener {

	
//  FIELDS
	
	NW_DayPanel day_panel;
	NW_HousePanel house_panel;
	DefaultWorkerData dwd;
	JFrame frame;	
	
	
//  CONSTRUCTOR
	
	public NW_DeleteHouseListener(NW_DayPanel day_panel, NW_HousePanel house_panel, DefaultWorkerData dwd, JFrame frame) {
		this.day_panel = day_panel;
		this.house_panel = house_panel;
		this.dwd = dwd;
		this.frame = frame;
	}
	
	
	
//  LISTENER
	
	public void actionPerformed(ActionEvent e){
		
		// for resize at end of method
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
		NW_HousePanel[] new_house_panel_array = new NW_HousePanel[num-1];
		
		
		//copying panels that don't change index
		for(int i=0; i<index; i++){
			new_house_panel_array[i] = day_panel.house_panel[i].copyPanel(); 
		}
		
				
		// copying panels moved up one index
		for(int i=index; i<num-1; i++){
			new_house_panel_array[i] = day_panel.house_panel[i+1].copyPanel();
		}
		
		
		// setting house_panel field in day_panel
		day_panel.house_panel = new_house_panel_array;
		day_panel.addFlexibleFocusListeners();
		
		//add new panels
		for(int i=0; i<num-1; i++){
			day_panel.jsp_panel.add(day_panel.house_panel[i], new String("wrap " + DayPanel.PANEL_PADDING + ", growx") );
		}
		
		
		// Resizing frame		
		Dimension newSize = new Dimension( frame.getWidth(), frame.getHeight() );
		int jsp_constant = 0; // this is difference between size of jsp (scrollpane) and jsp_panel (jpanel on scrollpane)
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
		int extra_space = (frame.getLocation().y + newSize.height) - (int) effectiveScreenSize.getHeight();
		if ( frame.getLocation().y + newSize.height > effectiveScreenSize.getHeight() ) {
			newSize.setSize( frame.getWidth(), frame.getHeight() - extra_space );
		}
		
		
		frame.setSize(newSize);
		frame.revalidate();
		frame.repaint();
		
		
		} // end if statement that only allows for deleting if more than one house panel remains
		
	}
		
}
