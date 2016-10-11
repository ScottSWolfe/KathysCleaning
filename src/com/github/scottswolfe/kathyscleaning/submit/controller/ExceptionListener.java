package com.github.scottswolfe.kathyscleaning.submit.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.controller.StaticMethods;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.submit.view.ExceptionPanel;
import com.github.scottswolfe.kathyscleaning.submit.view.HousePanel;


public class ExceptionListener implements ActionListener {

//  FIELDS
	
	HousePanel house_panel;
	WorkerList dwd;
	JFrame frame;
	JFrame local_frame;
	
	
//  CONSTRUCTOR	
	
	public ExceptionListener( HousePanel house_panel, WorkerList dwd, JFrame frame ) {
		
		this.house_panel = house_panel;
		this.dwd = dwd;
		this.frame = frame;
		//this.exception_data = house_panel.exceptionData;
		
	}
	
	
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		local_frame = new JFrame();
		local_frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		local_frame.addWindowListener( new FrameCloseListener(frame) );
		
		local_frame.add( new ExceptionPanel( local_frame, dwd, house_panel ) );
		local_frame.pack();
				
		StaticMethods.findSetLocation( local_frame );
		
		frame.setEnabled(false);
		local_frame.setVisible(true);
		
		
	}

	
	
}
