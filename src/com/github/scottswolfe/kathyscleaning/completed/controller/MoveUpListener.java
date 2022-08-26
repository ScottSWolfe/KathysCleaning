package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.completed.view.HousePanel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_HousePanel;


public class MoveUpListener implements ActionListener {

//  FIELDS

	DayPanel day_panel;
	NW_DayPanel nw_day_panel;
	HousePanel house_panel;
	NW_HousePanel nw_house_panel;
	WorkerList dwd;
	JFrame frame;
	TabbedPane tp;



//  CONSTRUCTOR

	public MoveUpListener(DayPanel day_panel, HousePanel house_panel, WorkerList dwd, JFrame frame, TabbedPane tp) {
		this.day_panel = day_panel;
		this.house_panel = house_panel;
		this.dwd = dwd;
		this.frame = frame;
		this.tp = tp;
	}

	public MoveUpListener(NW_DayPanel day_panel, NW_HousePanel house_panel, WorkerList dwd, JFrame frame, TabbedPane tp) {
		this.nw_day_panel = day_panel;
		this.nw_house_panel = house_panel;
		this.dwd = dwd;
		this.frame = frame;
		this.tp = tp;
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
		HousePanel[] new_house_panel_array = new HousePanel[num];


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


		// setting field in day_panel
		day_panel.house_panel = new_house_panel_array;

		//add new panels
		for(int i=0; i<num; i++){
			day_panel.jsp_panel.add(day_panel.house_panel[i], "wrap 6, growx");
		}

		day_panel.revalidate();
		day_panel.repaint();


		} // end if index != 0

	}

}
