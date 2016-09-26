package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class MainWindowListener implements WindowListener {
	
	

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {

		String[] abc = {"Cancel", "Confirm"};
		int i = JOptionPane.showOptionDialog(new JFrame(), "<html>Are you sure you want to close the program?<br>All work will be lost.",
									 null, JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, abc, 0);
		if (i==1) {
			System.exit(0);
			
			// TODO: make it so main menu opens back up instead of completely ending the program
		}
		
		
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
    
