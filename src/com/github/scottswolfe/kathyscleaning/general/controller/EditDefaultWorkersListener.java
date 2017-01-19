package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.completed.view.EditDefaultWorkersPanel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;


public class EditDefaultWorkersListener implements ActionListener {


//  FIELDS
	
	WorkerList dwd;
	DayPanel day_panel;
	JFrame container_frame;
	

	
//  CONSTRUCTOR
	
	public EditDefaultWorkersListener(WorkerList dwd, DayPanel day_panel, JFrame container_frame ) {
		this.dwd = dwd;
		this.day_panel = day_panel;
		this.container_frame = container_frame;
	}
	
	
	
//  LISTENER
	
	public void actionPerformed(ActionEvent e){
		
		JFrame frame = new JFrame();
		frame.setResizable(false);
		frame.addWindowListener( new FrameCloseListener(container_frame) );
		
		try {
			frame.add( new EditDefaultWorkersPanel( day_panel.header_panel.dwp, frame, day_panel ) );
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		frame.pack();
		
		StaticMethods.findSetLocation( frame );
		
		container_frame.setEnabled(false);
		frame.setVisible(true);
		
	}
	
}
