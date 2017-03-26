package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;


public class FrameCloseListener implements WindowListener {

	
	// FIELDS
	
	JFrame container_frame;
	
	
	// CONSTRUCTOR
	
	public FrameCloseListener( JFrame container_frame ) {
		this.container_frame = container_frame;
	}
	
	// ACTIONS
	
	
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {

		container_frame.setEnabled(true);
		container_frame.toFront();
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
        // TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
