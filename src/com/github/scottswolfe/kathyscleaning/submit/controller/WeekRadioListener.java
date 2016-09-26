package com.github.scottswolfe.kathyscleaning.submit.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.general.view.DefaultWorkerPanel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.view.SettingsPanel;
import com.github.scottswolfe.kathyscleaning.submit.view.ExceptionPanel;
import com.github.scottswolfe.kathyscleaning.submit.view.HousePanel;


public class WeekRadioListener implements ActionListener {

	/*
	 * for when Week A and Week B buttons are selected 
	 */
	
//  FIELDS
	
	TabbedPane tp;
	JFrame frame;
	int wk;
	
	
//  CONSTRUCTOR
	
	public WeekRadioListener( TabbedPane tp, JFrame frame, int wk ){
		this.tp = tp;
		this.frame = frame;
		this.wk = wk;
	}
	
	
	
//  LISTENER
	
	public void actionPerformed(ActionEvent e){
		
		// if Selected Week button is already selected, do nothing
		if (tp.day_panel[ tp.getSelectedIndex() ].header_panel.weekSelected == wk) {
			return;
		}
		// if selected button was not already selected, change/insert data for day
		else {
				
		File file;
		if (wk == SettingsPanel.WEEK_A) {
			file = SettingsPanel.SUBMIT_WEEK_A;
		}
		else {
			file = SettingsPanel.SUBMIT_WEEK_B;
		}
				
		Scanner input;
		Scanner counter;
		
		try {
			input = new Scanner(file);
			counter = new Scanner(file);
			
			// find the day number
			int day = tp.getSelectedIndex();
			String s = input.nextLine();  		// initialize s
			counter.nextLine();
			
			// find current day
			while ( !s.equals("Day " + day) ) {
				s = input.nextLine();
				counter.nextLine();
			}
						
			// find out how many houses
			String t = counter.nextLine();
			int num_houses = 0;
			while ( !t.equals("Day " + (day+1) )) {
				if ( t.equals("House " + num_houses) ) {
					num_houses++;
				}
				if (counter.hasNextLine()) {
					t = counter.nextLine();
				}
				else {
					break;
				}
			}
			counter.close();
								
			// loop through each house
			s = input.nextLine();
			
			for (int h=0; h<num_houses; h++) {
				
				HousePanel house = tp.day_panel[day].house_panel[h];
						
				// 1. setting name, pay, and times
				house.house_name_txt.setText(input.nextLine());
				house.pay_txt.setText(input.nextLine());
				house.time_begin_txt.setText(input.nextLine());
				house.time_end_txt.setText(input.nextLine());
						
				// 2. setting workers
				String line = input.nextLine();
				Scanner parser = new Scanner(line);
				parser.useDelimiter(" ");
						
				// a) unselecting any selected workers
				for(int l=0; l<DefaultWorkerPanel.NORM_ROWS; l++){
					for(int m=0; m<DefaultWorkerPanel.NORM_COLUMNS; m++){
						tp.day_panel[day].house_panel[h].worker_panel.worker[l][m].setSelected(false);		
					}
				}
						
				// b) selecting saved workers
				while (parser.hasNext() ) {
					String worker = parser.next();
							
					for(int l=0; l<DefaultWorkerPanel.NORM_ROWS; l++){
						for(int m=0; m<DefaultWorkerPanel.NORM_COLUMNS; m++){
							//tp.day_panel[0].house_panel[i].worker_panel.worker[l][m].setSelected(false);
							if (worker.equals( tp.day_panel[day].house_panel[h].worker_panel.worker[l][m].getText() ) ){
								tp.day_panel[day].house_panel[h].worker_panel.worker[l][m].setSelected(true);
								break;
							}
						}
					}
				}
				parser.close();
						
				// 3. setting exception data
				for (int i=0; i<ExceptionPanel.EXCEPTION_ROWS; i++) {  
					house.exception_data.worker_name[i] = input.nextLine();
					house.exception_data.time_begin[i] = input.nextLine();
					house.exception_data.time_end[i] = input.nextLine();
				}
				house.exception_data.edited = true;
				// TODO make sure this isn't overwritten
				
				
				// 4. making sure there is a correct number of house panels available to fill
				
				// if there are more empty house panels and there are more houses to fill in
				if ( h < (num_houses - 1) && (h+1) < tp.day_panel[day].house_panel.length ) {
					// do nothing
				}
				// if there are no more empty house panels and there are more houses to fill in
				else if ( h+1 >= tp.day_panel[day].house_panel.length && h < (num_houses - 1) ){
					ActionEvent event = new ActionEvent(this,0,"test");
					ActionListener[] al = tp.day_panel[day].house_panel[h].add_house.getActionListeners();
					al[0].actionPerformed( event );
				}
				// if there are more empty house panels and there are no more houses to fill in
				else if ( (h+1) < tp.day_panel[day].house_panel.length && h >= (num_houses - 1 ) ) {
					int numrepeat = tp.day_panel[day].house_panel.length-h-1;
					for (int k=h; k<numrepeat+h; k++) {
						ActionEvent event = new ActionEvent(this,0,"test");
						ActionListener[] al = tp.day_panel[day].house_panel[h+1].delete_house.getActionListeners();
						al[0].actionPerformed( event );
					}
				}
				// no empty house panels and there are no more houses to fill in
				else if (h+1 >= tp.day_panel[day].house_panel.length && h >= (num_houses - 1 )) {
					// do nothing
				}
				
				// 5. preparing for next loop (burning the "House h" line)
				if (input.hasNextLine()) {
					s = input.nextLine();
				}
				else {
					break;
				}
			}
			
			// done with day!
		
			
		input.close();
			
						
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	
		
		} // end if/else statement checking if radio button is selected 
		
		
		tp.day_panel[tp.getSelectedIndex()].header_panel.weekSelected = wk;
		
		// resizing
		//TabChangeListener.Resize(tp, frame);
		
	}

	

	
//  PRIVATE METHODS
	
	
}
