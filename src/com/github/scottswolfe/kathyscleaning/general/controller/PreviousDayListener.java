package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;


	public class PreviousDayListener implements ActionListener {
		
	// fields
	TabbedPane tp;
	JFrame frame;
		
	// constructor
	public PreviousDayListener(TabbedPane tp, JFrame frame) {
			
		this.tp = tp;
		this.frame = frame;
			
	}
	
	// previous_day button is selected
	public void actionPerformed(ActionEvent e) {
			
			
		// if the first tab is not the current tab
		if(tp.getSelectedIndex() > 0) {
			
			// changing tab
			tp.setSelectedIndex( tp.getSelectedIndex() - 1 );
			
		}		
				
	}
	
}
	
	
	
	
