package com.github.scottswolfe.kathyscleaning;

import javax.swing.SwingUtilities;

import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCModel;
import com.github.scottswolfe.kathyscleaning.menu.controller.MenuPanelController;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.menu.model.SettingsModel;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendModel;

/**
 * This class contains the main method and launches the application.
 */
public class Main {

	public static void main(String[] args) {
	    
	    Settings.changeLookAndFeelProgram();
		setupSaveFileStuff();
		
		// Launching Menu Panel
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {				
			    MenuPanelController.initializeMenuPanelFrame();
			}
		});
	}
	
	// TODO put this somewhere else and refactor
	public static void setupSaveFileStuff() {
	    
	    try {
	        SettingsModel.load(Settings.SETTINGS_SAVE_FILE);
	    } catch (Exception e) { // TODO change this to FileNotFoundException
	        e.printStackTrace();
	        SettingsModel.initialize();
	        SettingsModel.save(Settings.SETTINGS_SAVE_FILE);
	    }
	    
        SessionModel.initialize();
	    CompletedModel completed = new CompletedModel();
	    CovenantModel covenant = new CovenantModel();
		LBCModel lbc = new LBCModel();
	    WeekendModel weekend = new WeekendModel();
	    NW_Data nw_data = new NW_Data();

	    SessionModel.save(GeneralController.TEMP_SAVE_FILE);
	    JsonMethods.saveToFileJSON(completed, CompletedModel.class, GeneralController.TEMP_SAVE_FILE, Form.COMPLETED.getNum());
	    JsonMethods.saveToFileJSON(covenant, CovenantModel.class, GeneralController.TEMP_SAVE_FILE, Form.COVENANT.getNum());
        JsonMethods.saveToFileJSON(lbc, LBCModel.class, GeneralController.TEMP_SAVE_FILE, Form.LBC.getNum());
	    JsonMethods.saveToFileJSON(weekend, WeekendModel.class, GeneralController.TEMP_SAVE_FILE, Form.WEEKEND.getNum());
	    JsonMethods.saveToFileJSON(nw_data, NW_Data.class, GeneralController.TEMP_SAVE_FILE, Form.SCHEDULED.getNum());
	}
	
}
