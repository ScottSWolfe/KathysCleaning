package com.github.scottswolfe.kathyscleaning.nextweek.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.general.view.DefaultWorkerPanel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.view.SettingsPanel;
import com.github.scottswolfe.kathyscleaning.nextweek.model.BeginExceptionData;
import com.github.scottswolfe.kathyscleaning.nextweek.model.NoteData;
import com.github.scottswolfe.kathyscleaning.nextweek.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.nextweek.view.NW_ExceptionPanel;
import com.github.scottswolfe.kathyscleaning.nextweek.view.NW_HousePanel;
import com.github.scottswolfe.kathyscleaning.nextweek.view.NW_NotePanel;


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
			int rows = DefaultWorkerPanel.NORM_ROWS;
			int columns = DefaultWorkerPanel.NORM_COLUMNS;
				
			for(int m=0; m<rows; m++){
				for(int j=0; j<columns; j++){
					hp.worker_panel.workerCheckBoxes[m][j].setSelected(false);
				}
			}
					
		}
		
		int rows = DefaultWorkerPanel.COV_ROWS;
		int columns = DefaultWorkerPanel.COV_COLUMNS;
		for(int m=0; m<rows; m++){
			for(int j=0; j<columns; j++){
				tp.nw_day_panel[day].cov_panel.dwp.workerCheckBoxes[m][j].setSelected(false);
			}
		}
		
		// set neither button
		dp.header_panel.neither.setSelected(true);  // TODO: is this needed??
		dp.header_panel.weekSelected = SettingsPanel.NEITHER;
		
		// delete exception data
		dp.covenant_note_data = new NoteData(NW_NotePanel.ROWS); 
		
		
		// TODO: add something about adding or deleting panels as needed to get to 3 house panels
		
		
		tp.nw_day_panel[day].header_panel.wk = SettingsPanel.NEITHER;
			
	}
	
	
}
