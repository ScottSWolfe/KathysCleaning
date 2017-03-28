package com.github.scottswolfe.kathyscleaning;

import javax.swing.SwingUtilities;

import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.menu.controller.MenuPanelController;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.utility.CalendarMethods;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendModel;


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
	    
	    Settings.completedStartDay = CalendarMethods.getFirstDayOfWeek(); 
	            
	    // "erasing" temp current save file
	    CompletedModel completed = new CompletedModel();
	    CovenantModel covenant = new CovenantModel();
	    WeekendModel weekend = new WeekendModel();
	    NW_Data nw_data = new NW_Data();
	    
	    JsonMethods.saveToFileJSON(completed, CompletedModel.class, GeneralController.TEMP_SAVE_FILE, 0);
	    JsonMethods.saveToFileJSON(covenant, CovenantModel.class, GeneralController.TEMP_SAVE_FILE, 1);
	    JsonMethods.saveToFileJSON(weekend, WeekendModel.class, GeneralController.TEMP_SAVE_FILE, 2);
	    JsonMethods.saveToFileJSON(nw_data, NW_Data.class, GeneralController.TEMP_SAVE_FILE, 3);
	    // TODO need a settings save
	    
	    // setting saveFile
	    Settings.saveFile = GeneralController.TEMP_SAVE_FILE;	    
	    Settings.saveFileChosen = false;
	}
	
}
