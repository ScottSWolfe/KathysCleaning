package com.github.scottswolfe.kathyscleaning.nextweek.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.general.view.DefaultWorkerPanel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.nextweek.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.nextweek.view.NW_HousePanel;



/*
 *   I don't think this class is being used. NW_WeekListener is being used instead.
 * 
 * 		I think.... I could be incorrect.
 * 
 * 
 * 
 * 
 * 
 * 
 */




public class NW_WeekRadioListener implements ActionListener {

	/*
	 * for when Week A and Week B buttons are selected 
	 */
	
//  FIELDS
	
	TabbedPane tp;
	JFrame frame;
	int wk;
	
	
//  CONSTRUCTOR
	
	public NW_WeekRadioListener( TabbedPane tp, JFrame frame, int wk ){
		this.tp = tp;
		this.frame = frame;
		this.wk = wk;
	}
	
	
	
//  LISTENER
	
	public void actionPerformed(ActionEvent e){
		System.out.println(tp.nw_day_panel[ tp.getSelectedIndex() ].header_panel.weekSelected);
		
		// if Selected Week button is already selected, do nothing
		if (tp.nw_day_panel[ tp.getSelectedIndex() ].header_panel.weekSelected == wk) {
			
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
		Scanner counter;
		
		try {
			input = new Scanner(file);
			counter = new Scanner(file);
			
			// find the day number
			int day = tp.getSelectedIndex();
			String s = input.nextLine(); 
			NW_DayPanel dp = tp.nw_day_panel[tp.getSelectedIndex()];
			counter.nextLine();
			
			// find current day
			while ( !s.equals("Day " + day) ) {
				s = input.nextLine();
				counter.nextLine();
			}
						
			// find out how many houses
			String t = counter.nextLine();
			int num_houses = 0;
			while ( !t.equals("Day " + (day+1)) && counter.hasNextLine() ) {
				if ( t.equals("House " + num_houses) ) {
					num_houses++;
				}
			}
			counter.close();
								
			dp.meet_location_box.setSelectedItem(input.nextLine());
			dp.meet_time_field.setText(input.nextLine());
			
			
			
			
			// loop through each house
			for (int h=0; h<num_houses; h++) {
				
				NW_HousePanel house = tp.nw_day_panel[day].house_panel[h];
						
				// 1. setting house name
				house.house_name_txt.setText(input.nextLine());

						
				// 2. setting workers
				String line = input.nextLine();
				Scanner parser = new Scanner(line);
				parser.useDelimiter(" ");
						
				// a) unselecting any selected workers
				for(int l=0; l<DefaultWorkerPanel.NORM_ROWS; l++){
					for(int m=0; m<DefaultWorkerPanel.NORM_COLUMNS; m++){
						tp.nw_day_panel[day].house_panel[h].worker_panel.workerCheckBoxes[l][m].setSelected(false);		
					}
				}
						
				// b) selecting saved workers
				while (parser.hasNext() ) {
					String worker = parser.next();
							
					for(int l=0; l<DefaultWorkerPanel.NORM_ROWS; l++){
						for(int m=0; m<DefaultWorkerPanel.NORM_COLUMNS; m++){
							//tp.day_panel[0].house_panel[i].worker_panel.worker[l][m].setSelected(false);
							if (worker.equals( tp.nw_day_panel[day].house_panel[h].worker_panel.workerCheckBoxes[l][m].getText() ) ){
								tp.nw_day_panel[day].house_panel[h].worker_panel.workerCheckBoxes[l][m].setSelected(true);
								break;
							}
						}
					}
				}
				parser.close();
				
				
				// 4. making sure there is a correct number of house panels available to fill
				
				// if there are more empty house panels and there are more houses to fill in
				if ( h < (num_houses - 1) && (h+1) < tp.nw_day_panel[day].house_panel.length ) {
					// do nothing
				}
				// if there are no more empty house panels and there are more houses to fill in
				else if ( h+1 >= tp.nw_day_panel[day].house_panel.length && h < (num_houses - 1) ){
					ActionEvent event = new ActionEvent(this,0,"test");
					ActionListener[] al = tp.nw_day_panel[day].house_panel[h].add_house.getActionListeners();
					al[0].actionPerformed( event );
				}
				// if there are more empty house panels and there are no more houses to fill in
				else if ( (h+1) < tp.nw_day_panel[day].house_panel.length && h >= (num_houses - 1 ) ) {
					int numrepeat = tp.nw_day_panel[day].house_panel.length-h-1;
					for (int k=h; k<numrepeat+h; k++) {
						ActionEvent event = new ActionEvent(this,0,"test");
						ActionListener[] al = tp.nw_day_panel[day].house_panel[h+1].delete_house.getActionListeners();
						al[0].actionPerformed( event );
					}
				}
				// no empty house panels and there are no more houses to fill in
				else if (h+1 >= tp.nw_day_panel[day].house_panel.length && h >= (num_houses - 1 )) {
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
		
		
		tp.nw_day_panel[tp.getSelectedIndex()].header_panel.weekSelected = wk;
		
		
		// resizing
		//TabChangeListener.Resize(tp, frame);
		
	}
	
	
//  PRIVATE METHODS
	
	
}
