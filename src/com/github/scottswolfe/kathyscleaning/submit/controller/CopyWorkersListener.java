package com.github.scottswolfe.kathyscleaning.submit.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.github.scottswolfe.kathyscleaning.general.view.DefaultWorkerPanel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.submit.model.DayData;
import com.github.scottswolfe.kathyscleaning.submit.view.DayPanel;





public class CopyWorkersListener implements ActionListener {
	
//  FIELDS
	
	TabbedPane tabbed_pane;
	DayData day_data;
	DayPanel day_panel;
	
	
	
//  CONSTRUCTOR
	
	public CopyWorkersListener( TabbedPane tabbed_pane, DayData day_data, DayPanel day_panel ) {
		
		this.tabbed_pane = tabbed_pane;
		this.day_data = day_data;
		this.day_panel = day_panel;
		
	}
	
	
	
//  LISTENER
		
	public void actionPerformed(ActionEvent e) {
		
		// manipulating data
		
		int rows = DefaultWorkerPanel.NORM_ROWS;
		int columns = DefaultWorkerPanel.NORM_COLUMNS;
		
		boolean[][] bool = new boolean[rows][columns];
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				if ( day_panel.header_panel.dwp.workerCheckBoxes[i][j].isSelected() ){
					bool[i][j] = true;
				}
				else{
					bool[i][j] = false;
				}
			}
		}
		
		
		for(int k=0; k<day_panel.house_panel.length; k++){
			for(int i=0; i<rows; i++){
				for(int j=0; j<columns; j++){
					if (bool[i][j]) {
						day_panel.house_panel[k].worker_panel.workerCheckBoxes[i][j].setSelected(true);
					}
					else {
						day_panel.house_panel[k].worker_panel.workerCheckBoxes[i][j].setSelected(false);
					}
				}
			}
		}
		
	}
		
	
}
