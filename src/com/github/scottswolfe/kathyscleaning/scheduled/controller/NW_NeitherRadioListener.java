package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.general.view.WorkerPanel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.model.BeginExceptionData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NoteData;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_ExceptionPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_HousePanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_NotePanel;


public class NW_NeitherRadioListener implements ActionListener {

	// FIELDS
	
		TabbedPane tp;
		JFrame frame;
		
		
		
	// CONSTRUCTOR
		
	public NW_NeitherRadioListener( TabbedPane tp, JFrame frame ){
		this.tp = tp;
		this.frame = frame;
	}
			
			
		
	// LISTENER
	
	public void actionPerformed(ActionEvent e) {
		
		int day = tp.getSelectedIndex();
		NW_DayPanel dp = tp.nw_day_panel[day];
		
		dp.meet_location_box.setSelectedItem("");
		dp.meet_time_field.setText("");
		for (int i=0; i<NW_ExceptionPanel.NUM_EXCEPTIONS; i++) {
			dp.bed[i] = new BeginExceptionData();
		}
		
		
		for (int i=0; i<dp.house_panel.length; i++) {
			
			NW_HousePanel hp = dp.house_panel[i];
				
			hp.house_name_txt.setText("");
				
			// unselecting workers
			int rows = WorkerPanel.NORM_ROWS;
			int columns = WorkerPanel.NORM_COLUMNS;
				
			for(int m=0; m<rows; m++){
				for(int j=0; j<columns; j++){
					hp.worker_panel.workerCheckBoxes[m][j].setSelected(false);
				}
			}
					
		}
		
		int rows = WorkerPanel.COV_ROWS;
		int columns = WorkerPanel.COV_COLUMNS;
		for(int m=0; m<rows; m++){
			for(int j=0; j<columns; j++){
				tp.nw_day_panel[day].cov_panel.dwp.workerCheckBoxes[m][j].setSelected(false);
			}
		}
		
		// set neither button
		dp.header_panel.neither.setSelected(true);
		dp.header_panel.weekSelected = Settings.NEITHER;
		
		// delete exception data
		dp.covenant_note_data = new NoteData(NW_NotePanel.ROWS); 		
		
		tp.nw_day_panel[day].header_panel.wk = Settings.NEITHER;
			
	}
	
	
}
