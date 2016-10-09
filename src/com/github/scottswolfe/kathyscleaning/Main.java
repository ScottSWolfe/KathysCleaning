package com.github.scottswolfe.kathyscleaning;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.menu.view.MenuPanel;


/**
 * This class contains the main method and launches the application.
 */
public class Main {

	public static void main(String[] args) {
	    
		// Loading Saved Settings
		Settings.initializeSettings();

		// Launching Menu Panel
		SwingUtilities.invokeLater( new Runnable() {
			
			public void run() {				
				JFrame menuFrame = new JFrame();
				MenuPanel menuPanel = new MenuPanel(menuFrame);
								
				menuFrame.add(menuPanel);
				menuFrame.setResizable(false);
				menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				menuFrame.pack();
				menuFrame.setLocationRelativeTo(null);
				menuFrame.setVisible(true);
			}
			
		});
		
	}
	
}
