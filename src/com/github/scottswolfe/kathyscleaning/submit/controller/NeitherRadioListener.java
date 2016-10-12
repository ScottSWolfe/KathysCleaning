package com.github.scottswolfe.kathyscleaning.submit.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.scottswolfe.kathyscleaning.general.view.DefaultWorkerPanel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.submit.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.submit.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.submit.view.HousePanel;


public class NeitherRadioListener implements ActionListener {

	// FIELDS
	
	TabbedPane tp;
	JFrame frame;
	
	
	
	// CONSTRUCTOR
	
	public NeitherRadioListener( TabbedPane tp, JFrame frame ){
		this.tp = tp;
		this.frame = frame;
	}
		
		
	
	// LISTENER
	
	public void actionPerformed(ActionEvent e) {
		
		
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
			int rows = DefaultWorkerPanel.NORM_ROWS;
			int columns = DefaultWorkerPanel.NORM_COLUMNS;
			
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
