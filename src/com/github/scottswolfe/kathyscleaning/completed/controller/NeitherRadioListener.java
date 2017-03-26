package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.completed.view.HousePanel;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.view.WorkerPanel;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;


public class NeitherRadioListener implements ActionListener {

	// FIELDS
	
    GeneralController<TabbedPane, CompletedModel> controller;
	TabbedPane tp;
	MainFrame<TabbedPane, CompletedModel> frame;
	
	
	
	// CONSTRUCTOR
	
	public NeitherRadioListener(GeneralController<TabbedPane, CompletedModel> controller,
	                            TabbedPane tp, MainFrame<TabbedPane, CompletedModel> frame) {
	    this.controller = controller;
		this.tp = tp;
		this.frame = frame;
	}
		
		
	
	// LISTENER
	
	public void actionPerformed(ActionEvent e) {
		
		if (controller.isOpeningFile()) {
		    return;
		}
		
		int day = tp.getSelectedIndex();
		DayPanel dp = tp.day_panel[day];
		
		for (int i=0; i<dp.house_panel.length; i++) {
			
			HousePanel hp = dp.house_panel[i];
			
			hp.house_name_txt.setText("");
			try {
				hp.pay_txt.getDocument().remove(
				        0, hp.pay_txt.getText().length());
			}
			catch(Exception e1){
				JOptionPane.showMessageDialog(new JFrame(),
				        null, "Error: Failed to delete $ Earned text box.",
				        JOptionPane.ERROR_MESSAGE);;
			}
			hp.time_begin_txt.setText("");
			hp.time_end_txt.setText("");
			
			// unselecting workers
			int rows = WorkerPanel.NORM_ROWS;
			int columns = WorkerPanel.NORM_COLUMNS;
			
			for(int m=0; m<rows; m++){
				for(int j=0; j<columns; j++){
					hp.worker_panel.workerCheckBoxes[m][j].setSelected(false);
				}
			}
			
			// delete exception data
			hp.exception_data = new ExceptionData();
			hp.exception_data.edited = false;
			
			// set neither button
			dp.header_panel.neither.setSelected(true);
			dp.header_panel.weekSelected = Settings.NEITHER;
			
		}		
		
	}
	
}
