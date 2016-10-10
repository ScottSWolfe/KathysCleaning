package com.github.scottswolfe.kathyscleaning;

import javax.swing.SwingUtilities;

import com.github.scottswolfe.kathyscleaning.menu.controller.MenuPanelController;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;


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
			    MenuPanelController.initializeMenuPanelFrame();
			}
			
		});
		
	}
	
}
