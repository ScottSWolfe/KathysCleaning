package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.scottswolfe.kathyscleaning.general.view.WorkerPanel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.model.BeginExceptionData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NoteData;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_ExceptionPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_NotePanel;


public class NW_WeekListener implements ActionListener {

//  FIELDS
	
	TabbedPane tp;
	//final String filename = "NW_WeekASaveFile";
	int wk;
	
//  CONSTRUCTOR
	
	public NW_WeekListener( TabbedPane tp, int wk ){
		this.tp = tp;
		this.wk = wk;
	}
	
//  LISTENER
	
	public void actionPerformed(ActionEvent e){

		// if Selected Week button is already selected, do nothing
		if (tp.nw_day_panel[ tp.getSelectedIndex() ].header_panel.weekSelected == wk) {
			return;
		}
		// if selected button was not already selected, change/insert data for day
		else {
				
		File file;
		if (wk == Settings.WEEK_A) {
			file = Settings.NEXT_WEEK_A;
		}
		else {
			file = Settings.NEXT_WEEK_B;
		}
		
		
		Scanner input;
		Scanner counter1;
		Scanner counter2;
		
		try {
			input = new Scanner(file);
			counter1 = new Scanner(file);
			counter2 = new Scanner(file);
					
			int h=0;  	// house index
			int d=0;	// day index

			// find the day number
			d = tp.getSelectedIndex();
			NW_DayPanel dp = tp.nw_day_panel[d];
			String s = input.nextLine();  		// initialize s
			counter1.nextLine();
			counter2.nextLine();
						
			// find current day
			while ( !s.equals("Day " + d) ) {
				s = input.nextLine();
				counter1.nextLine();
				counter2.nextLine();
			}
									
			// find out how many houses
			String t = counter2.nextLine();
			int num_houses = 0;
			while ( !t.equals("Day " + (d+1) )) {
				if ( t.equals("House " + num_houses) ) {
					num_houses++;
				}
				if (counter2.hasNextLine()) {
					t = counter2.nextLine();
				}
				else {
					break;
				}
			}
			counter1.close();

			
			// reading begin data and covenant data
			
			dp.meet_location_box.setSelectedItem( input.nextLine() );
			dp.meet_time_field.setText( input.nextLine() );
			

			List<BeginExceptionData> bedList = new ArrayList<>();
			for (int i=0; i<NW_ExceptionPanel.NUM_EXCEPTIONS; i++) {
				
			    BeginExceptionData bed = new BeginExceptionData();
			    
				bed = new BeginExceptionData();
				
				bed.setName(input.nextLine());
				bed.setMeetLocation(input.nextLine());
				bed.setTime(input.nextLine());
				bed.setNote(input.nextLine());
				
				if (bed.getName() != null && bed.getName().length() > 0) {
					dp.setException_exist(true);
				}
				bedList.add(bed);
			}
			dp.setBeginExceptionData(bedList);
			
			// reading covenant workers
			String line = input.nextLine();
			Scanner parser1 = new Scanner(line);
			parser1.useDelimiter(" ");
					
					
			// unselecting any selected workers
			int rows = tp.nw_day_panel[d].cov_panel.dwp.rows;
			int columns = tp.nw_day_panel[d].cov_panel.dwp.columns;
			for(int l=0; l<rows; l++){
				for(int m=0; m<columns; m++){
					tp.nw_day_panel[d].cov_panel.dwp.workerCheckBoxes[l][m].setSelected(false);
				}
			}
			
			// selecting saved workers
			while (parser1.hasNext() ) {
				String worker = parser1.next();
				
				for(int l=0; l<rows; l++){
					for(int m=0; m<columns; m++){
						if (worker.equals( tp.nw_day_panel[d].cov_panel.dwp.workerCheckBoxes[l][m].getText() ) ){
							tp.nw_day_panel[d].cov_panel.dwp.workerCheckBoxes[l][m].setSelected(true);
							break;
						}
					}
				}
			}
			parser1.close();
			
			// notes
			
			tp.nw_day_panel[d].covenant_note_data = new NoteData();
			tp.nw_day_panel[d].covenant_note_data.name_box_data = new String[NW_NotePanel.ROWS];
			tp.nw_day_panel[d].covenant_note_data.note_field_data = new String[NW_NotePanel.ROWS];
			
			for (int i=0; i<NW_NotePanel.ROWS; i++) {
				
				tp.nw_day_panel[d].covenant_note_data.name_box_data[i] = input.nextLine();
				tp.nw_day_panel[d].covenant_note_data.note_field_data[i] = input.nextLine();
				
			}
			
						
			
			// iterate through houses
			for ( int i=0; i<num_houses; i++ ) {
				input.nextLine(); // burn "House i"
				

				tp.nw_day_panel[d].house_panel[i].house_name_txt.setText( input.nextLine() );
									
				line = input.nextLine();
				Scanner parser = new Scanner(line);
				parser.useDelimiter(" ");
						
						
					// unselecting any selected workers
					for(int l=0; l<WorkerPanel.NORM_ROWS; l++){
						for(int m=0; m<WorkerPanel.NORM_COLUMNS; m++){
							tp.nw_day_panel[d].house_panel[i].worker_panel.workerCheckBoxes[l][m].setSelected(false);
						}
					}
					
					// selecting saved workers
					while (parser.hasNext() ) {
						String worker = parser.next();
						
						for(int l=0; l<WorkerPanel.NORM_ROWS; l++){
							for(int m=0; m<WorkerPanel.NORM_COLUMNS; m++){
								//tp.day_panel[0].house_panel[i].worker_panel.worker[l][m].setSelected(false);
								if (worker.equals( tp.nw_day_panel[d].house_panel[i].worker_panel.workerCheckBoxes[l][m].getText() ) ){
									tp.nw_day_panel[d].house_panel[i].worker_panel.workerCheckBoxes[l][m].setSelected(true);
									break;
								}
							}
						}
						
					}
					parser.close();
					
					// 4. making sure there is a correct number of house panels available to fill
					
					// if there are more empty house panels and there are more houses to fill in
					if ( h < (num_houses - 1) && (h+1) < tp.nw_day_panel[d].house_panel.length ) {
						// do nothing
					}
					// if there are no more empty house panels and there are more houses to fill in
					else if ( h+1 >= tp.nw_day_panel[d].house_panel.length && h < (num_houses - 1) ){
						ActionEvent event = new ActionEvent(this,0,"test");
						ActionListener[] al = tp.nw_day_panel[d].house_panel[h].add_house.getActionListeners();
						al[0].actionPerformed( event );
					}
					// if there are more empty house panels and there are no more houses to fill in
					else if ( (h+1) < tp.nw_day_panel[d].house_panel.length && h >= (num_houses - 1 ) ) {
						int numrepeat = tp.nw_day_panel[d].house_panel.length-h-1;
						for (int k=h; k<numrepeat+h; k++) {
							ActionEvent event = new ActionEvent(this,0,"test");
							ActionListener[] al = tp.nw_day_panel[d].house_panel[h+1].delete_house.getActionListeners();
							al[0].actionPerformed( event );
						}
					}
					// no empty house panels and there are no more houses to fill in
					else if (h+1 >= tp.nw_day_panel[d].house_panel.length && h >= (num_houses - 1 )) {
						// do nothing
					}
					
						
							
			}

			
			input.close();
			
			
			// change the header panel wk field
			tp.nw_day_panel[d].header_panel.wk = wk;
			
			
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(new JFrame(), "Error: Could not read save file correctly", null, JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	
		
		} // end if/else statement checking if radio button is selected 
		
		
		tp.nw_day_panel[tp.getSelectedIndex()].header_panel.weekSelected = wk;
		
	}
	
	
//  PRIVATE METHODS
	
	
}
