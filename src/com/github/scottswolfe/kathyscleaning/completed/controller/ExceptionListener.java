package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.completed.view.ExceptionPanel;
import com.github.scottswolfe.kathyscleaning.completed.view.HousePanel;
import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;


public class ExceptionListener implements ActionListener {

    // FIELDS
	WorkerList dwd;
	JFrame frame;
	JFrame local_frame;
	HousePanel housePanel;
	
	// CONSTRUCTOR	
	public ExceptionListener(WorkerList dwd, JFrame frame, HousePanel housePanel) {
		this.dwd = dwd;
		this.frame = frame;
		this.housePanel = housePanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		local_frame = new JFrame();
		local_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		local_frame.addWindowListener( new FrameCloseListener(frame) );
		
		local_frame.add(new ExceptionPanel(local_frame, dwd, housePanel));
		local_frame.pack();
				
		StaticMethods.findSetLocation(local_frame);
		
		frame.setEnabled(false);
		local_frame.setVisible(true);
	}
}
