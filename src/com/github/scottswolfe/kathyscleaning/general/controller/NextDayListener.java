package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;



public class NextDayListener implements ActionListener {
		
	// Fields
	TabbedPane tp;
	JFrame frame;
	
	
	
	// Constructor
	public NextDayListener(TabbedPane tp, JFrame frame ) {
		
		this.frame = frame;
		this.tp = tp;		
	}
	
	
	// Listener
	public void actionPerformed(ActionEvent e) {
		
		
		// if current tab is not the last tab
		if(tp.getSelectedIndex() < tp.getTabCount() - 1 ) {
			 
			//move to next tab
			tp.setSelectedIndex( tp.getSelectedIndex() + 1 );	
		}
				
	}
	
}

