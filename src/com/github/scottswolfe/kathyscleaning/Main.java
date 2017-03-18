package com.github.scottswolfe.kathyscleaning;

import javax.swing.SwingUtilities;

import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.menu.controller.MenuPanelController;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;


/**
 * This class contains the main method and launches the application.
 */
public class Main {

	public static void main(String[] args) {
	    
		// Loading Saved Settings
		Settings.initializeSettings();
		
		// Loading Model
		// TODO implement model initialization
		setupSaveFileStuff();
		
		// Launching Menu Panel
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {				
			    MenuPanelController.initializeMenuPanelFrame();
			}
		});
		
	}
	
	// TODO put this somewhere else and refactor
	private static void setupSaveFileStuff() {
	    
	    // "erasing" temp current save file
	    for (int i = 0; i < 5; i++) {
	        JsonMethods.saveToFileJSON(new Object(), Object.class, GeneralController.TEMP_SAVE_FILE, i);
	    }
	    
	    // setting saveFile
	    Settings.saveFile = GeneralController.TEMP_SAVE_FILE;
	    
	    Settings.saveFileChosen = false;
	}
	
}
