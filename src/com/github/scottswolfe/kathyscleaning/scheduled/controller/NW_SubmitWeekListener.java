package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.helper.ExcelMethods;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;


public class NW_SubmitWeekListener implements ActionListener {

	GeneralController<TabbedPane, NW_Data> controller;
	TabbedPane tp;
	JFrame frame;
	FileOutputStream out;
	int mode;
	int wk;
	
	public NW_SubmitWeekListener(GeneralController<TabbedPane, NW_Data> controller, 
	        TabbedPane tp, JFrame frame, int mode, int wk) {
	    
	    this.controller = controller;
		this.tp = tp;
		this.frame = frame;
		this.mode = mode;
		this.wk = wk;
	}
	
	public void actionPerformed(ActionEvent e){
		ExcelMethods.chooseFileAndGenerateExcelDoc(controller);		
	}
		
}
