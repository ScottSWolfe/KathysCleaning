package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.scheduled.model.BeginExceptionEntry;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_ExceptionPanel;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;


public class NW_ExceptionListener implements ActionListener {
	
	// FIELDS
	NW_DayPanel dp;
	WorkerList dwd;
	JFrame mainframe;
	
	
	// CONSTRUCTOR
	public NW_ExceptionListener( NW_DayPanel dp, WorkerList dwd, JFrame frame ){
		this.dp = dp;
		this.dwd = dwd;
		this.mainframe = frame;
	}
	
	
	// LISTENER METHOD
	
	/**
	 *  This fires when the "Exception" button is pressed on the beginPanel.
	 *	This button opens up the ExceptionPanel that allows the user
	 *	to enter exceptions for employee meeting locations and times.
	 *	This method will also check certain conditions in order to 
	 *	facilitate the user's selections by supplying certain choices
	 *	if certain conditions are met.
	 */
	public void actionPerformed(ActionEvent e) {

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		frame.addWindowListener(new FrameCloseListener(mainframe));
		
		NW_ExceptionPanel panel = new NW_ExceptionPanel(frame, dp, dwd);
		
		BeginExceptionEntry entry;
        for (int i=0; i < dp.getNumBED(); i++) {
            entry = dp.getExceptionData().get(i);
            panel.setEmployee_combobox_text(entry.getName(), i);
            panel.setTime_textfield_text(entry.getTime(), i);
            panel.setLocation_combobox_text(entry.getMeetLocation(), i);
            panel.setNote_textfield_text(entry.getNote(), i);
        }
						
		frame.add(panel);
		frame.pack();
		StaticMethods.findSetLocation(frame);
		frame.setVisible(true);
		
		mainframe.setEnabled(false);
	}

}
