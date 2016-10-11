package com.github.scottswolfe.kathyscleaning.nextweek.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.nextweek.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.nextweek.view.NW_HousePanel;


public class NW_MoveUpListener implements ActionListener {

//  FIELDS
	
	NW_DayPanel day_panel;
	NW_HousePanel house_panel;
	WorkerList dwd;
	JFrame frame;
	
	
	
//  CONSTRUCTOR
	
	
	public NW_MoveUpListener(NW_DayPanel day_panel, NW_HousePanel house_panel, WorkerList dwd, JFrame frame) {
		this.day_panel = day_panel;
		this.house_panel = house_panel;
		this.dwd = dwd;
		this.frame = frame;
	}
	
	
//  LISTENER
	
	public void actionPerformed(ActionEvent e){
		
		// initializing variables
		int num = day_panel.house_panel.length;
		int index = -1;
		for(int i=0; i<day_panel.house_panel.length; i++){
			if (day_panel.house_panel[i] == house_panel){
				index = i;
			}
		}
		
		
		// only move up if it is not the first panel
		if ( index != 0) {
			
		
		
		//remove old panels
		for(int i=0; i<num; i++){
			day_panel.jsp_panel.remove(day_panel.house_panel[i]);
		}
		
		
		// creating new array of house panels for the day panel
		NW_HousePanel[] new_house_panel_array = new NW_HousePanel[num];
		
		
		// copying first set of panels that don't change
		for(int i=0; i<index-1; i++){
			new_house_panel_array[i] = day_panel.house_panel[i].copyPanel(); 
		}
		
		
		// Switching Panels
		new_house_panel_array[index-1] = day_panel.house_panel[index].copyPanel();
		new_house_panel_array[index] = day_panel.house_panel[index-1].copyPanel();
		 
		
		// copying second set of panels that don't move
		for(int i=index+1; i<num; i++){
			new_house_panel_array[i] = day_panel.house_panel[i].copyPanel();
		}
		
		
		// settting field in day_panel
		day_panel.house_panel = new_house_panel_array;
		day_panel.addFlexibleFocusListeners();
		
		//add new panels
		for(int i=0; i<num; i++){
			day_panel.jsp_panel.add(day_panel.house_panel[i], "wrap 6, growx");
		}
		
		
		day_panel.revalidate();
		day_panel.repaint();
		
		
		} // end if index != 0 
		
	}
	
}
