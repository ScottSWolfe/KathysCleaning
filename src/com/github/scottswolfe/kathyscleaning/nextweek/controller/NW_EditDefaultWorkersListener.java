package com.github.scottswolfe.kathyscleaning.nextweek.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.controller.StaticMethods;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.nextweek.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.nextweek.view.NW_EditDefaultWorkersPanel;


public class NW_EditDefaultWorkersListener implements ActionListener {


//  FIELDS
	
	WorkerList dwd;
	NW_DayPanel day_panel;
	JFrame container_frame;
	
	
	
	
//  CONSTRUCTOR
	
	public NW_EditDefaultWorkersListener(WorkerList dwd, NW_DayPanel day_panel, JFrame container_frame) {
		this.dwd = dwd;
		this.day_panel = day_panel;
		this.container_frame = container_frame;
	}
	
	
	
	
	
//  LISTENER
	
	public void actionPerformed(ActionEvent e){
		
		JFrame frame = new JFrame();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.addWindowListener( new FrameCloseListener( container_frame ));
		container_frame.setEnabled(false);
		
		try {
			frame.add( new NW_EditDefaultWorkersPanel( day_panel.header_panel.dwp, frame, day_panel ) );
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		frame.pack();
		
		StaticMethods.findSetLocation(frame);
		
		frame.setVisible(true);
		
	}
	
}
