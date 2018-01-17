package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_HousePanel;


public class NW_DeleteHouseListener implements ActionListener {

	
//  FIELDS
	
	NW_DayPanel day_panel;
	NW_HousePanel house_panel;
	WorkerList dwd;
	JFrame frame;	
	
	
//  CONSTRUCTOR
	
	public NW_DeleteHouseListener(NW_DayPanel day_panel, NW_HousePanel house_panel, WorkerList dwd, JFrame frame) {
		this.day_panel = day_panel;
		this.house_panel = house_panel;
		this.dwd = dwd;
		this.frame = frame;
	}
	
	
	
//  LISTENER
	
	public void actionPerformed(ActionEvent e){
		
	    if (day_panel.getNumHousePanels() <= 1) {
	        return;
	    }
	    
		// for resize at end of method
		int panel_height = day_panel.house_panels.get(0).getHeight() + DayPanel.PANEL_PADDING;
		
		// initializing variables
		int num = day_panel.getNumHousePanels();
		int index = -1;
		for(int i = 0; i < num; i++){
			if (day_panel.house_panels.get(i) == house_panel) {
				index = i;
			}
		}
		
		//remove old panels
		for(int i=0; i<num; i++){
			day_panel.jsp_panel.remove(day_panel.house_panels.get(i));
		}		
		
		// delete house panel
		day_panel.house_panels.remove(index);
		
		// resetting focus listeners
		day_panel.addFlexibleFocusListeners();
		
		// add panels back to scroll pane
        for(NW_HousePanel house_panel : day_panel.house_panels) {
            day_panel.jsp_panel.add(house_panel, new String("wrap " + DayPanel.PANEL_PADDING + ", growx") );
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
	}
		
}
