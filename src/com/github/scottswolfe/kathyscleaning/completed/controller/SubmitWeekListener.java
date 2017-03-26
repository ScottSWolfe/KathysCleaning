package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.covenant.view.CovenantPanel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;

// TODO can clean this up more by passing any needed frame, date, mode, wk stuff
// either globally or through the controller
public class SubmitWeekListener implements ActionListener {

/* FIELDS =================================================================== */
    
    GeneralController<TabbedPane, CompletedModel> controller;
	TabbedPane tp;
	MainFrame<?,?> frame;
	Calendar date;
	int mode;
	int wk;

	
	
/* CONSTRUCTOR ============================================================== */

	public SubmitWeekListener(GeneralController<TabbedPane, CompletedModel> controller, TabbedPane tp, MainFrame<TabbedPane, CompletedModel> frame, Calendar date, int mode, int wk ){
		this.controller = controller;
	    this.tp = tp;
		this.frame = frame;
		this.date = date;
		this.mode = mode;
		this.wk = wk;
	}	

	
	
/* PUBLIC METHOD ============================================================ */
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!StaticMethods.confirmSubmitWeek()) {
			return;
		}
        controller.readInputAndWriteToFile(Settings.saveFile);
        CompletedControllerHelper.saveHousePay(tp);
        frame.eliminate();
        GeneralController<CovenantPanel, CompletedModel> covController = new GeneralController<>(Form.COVENANT);
        covController.initializeForm(covController);
	}

}
