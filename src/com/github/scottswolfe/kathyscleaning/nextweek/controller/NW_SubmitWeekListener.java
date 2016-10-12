package com.github.scottswolfe.kathyscleaning.nextweek.controller;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.scottswolfe.kathyscleaning.general.controller.StaticMethods;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.menu.view.MenuPanel;
import com.github.scottswolfe.kathyscleaning.menu.view.SettingsPanel;
import com.github.scottswolfe.kathyscleaning.nextweek.model.BeginExceptionData;
import com.github.scottswolfe.kathyscleaning.nextweek.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.nextweek.model.NW_DayData;
import com.github.scottswolfe.kathyscleaning.nextweek.model.NW_HouseData;
import com.github.scottswolfe.kathyscleaning.nextweek.model.WorkerSchedule;
import com.github.scottswolfe.kathyscleaning.nextweek.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.nextweek.view.NW_ExceptionPanel;
import com.github.scottswolfe.kathyscleaning.nextweek.view.NW_HousePanel;
import com.github.scottswolfe.kathyscleaning.nextweek.view.NW_NotePanel;
import com.github.scottswolfe.kathyscleaning.submit.controller.SubmitWeekListener;


public class NW_SubmitWeekListener implements ActionListener {

//  FIELDS
	
	TabbedPane tp;
	JFrame frame;
	FileOutputStream out;
	int mode;
	int wk;
	
	
	
//  CONSTRUCTOR

	public NW_SubmitWeekListener( TabbedPane tp, JFrame frame, int mode, int wk ){
		this.tp = tp;
		this.frame = frame;
		this.mode = mode;
		this.wk = wk;
	}
	
	
	
//  METHODS
	
	
	
	
//  LISTENER
	
	public void actionPerformed(ActionEvent e){
		
		int a = StaticMethods.confirmSubmitWeek();
		if (a==0) {
			return;
		}
		
		if (mode == Settings.TRUE_MODE) {
		/* Steps:
		 * 
		 * 		1. Read User Input
		 * 		2. Create new Excel Sheet
		 * 		3. Write Data onto Excel Sheet and Save
		 * 		4. Close Frame
		 * 
		 * 		?? 5. give option to input covenant schedule? open excel doc? options to print excel doc?
		 * 
		 */
		
		
		// *** 1. Read User Input ***
		
		NW_Data data = new NW_Data();
		NW_DayData[] dayData = new NW_DayData[5]; 	// 5 days in week
		
		// for each day of the week
		for (int d=0; d<5; d++) {
			
			NW_DayPanel dp = tp.nw_day_panel[d];
			
			String[] dworkers = dp.getWorkers();
			String[] exceptnames = dp.getExceptionNames();
			WorkerSchedule[] ws;
			
			if (dworkers != null) {
			
			// gathering exception data
			
			// setting the worker names for the day
			ws = new WorkerSchedule[dworkers.length];		// one schedule for each worker that day
			for (int i=0; i<dworkers.length; i++) {
				ws[i] = new WorkerSchedule();
				ws[i].setName( dworkers[i] );
			}
			
			// for each worker for that day
			for (int w=0; w<ws.length; w++) {
				
				// a) Add houses they will work at
				
				// for each house of the day
				for(int j=0; j<dp.house_panel.length; j++) {
					
					NW_HousePanel house = dp.house_panel[j];
					String[] hworkers = house.getSelectedWorkers();
					
					// for each worker at the house
					for (int h=0; h<hworkers.length; h++) {
												
						// if current worker is selected at the house, add the house to the worker's schedule
						if (ws[w].getName() != null && hworkers[h] != null) {
							if (ws[w].getName().equals(hworkers[h])) {
								ws[w].addHouse( house.house_name_txt.getText() );
								break;
							}
						}
					}
					
				}
				
				
				// b) If they have an exception, add exception location, time, note
				// 	  If no exception, add general location, time, note
				
				// for each exception, check if worker matches exception
				Boolean isException = false;
				int index = 0;
				if(exceptnames != null){
					for (int i=0; i<exceptnames.length; i++) {
						if ( ws[w].getName().equals(exceptnames[i]) ) {
							isException = true;
							index = i;
							break;
						}
					}
				}
				
				
				// adding general notes and covenant notes
				
				// if current worker has a note, add the note to the worker's schedule
				if (ws[w].getName() != null && dp.getCovenant_note_data() != null &&
						dp.getCovenant_note_data().name_box_data != null &&
						dp.getCovenant_note_data().note_field_data != null) {
					
					// iterating through names in note data
					for (int j=0; j<dp.getCovenant_note_data().name_box_data.length; j++) {
						
						
						if ( ws[w].getName().equals(dp.getCovenant_note_data().name_box_data[j]) ) {
							
							// adding covenant note
							ws[w].addNote( dp.getCovenant_note_data().note_field_data[j]);
							
						}
					}
				}
				/*
				if (ws[w].getName() != null && dp.getDay_note_data() != null &&
						dp.getDay_note_data().name_box_data != null &&
						dp.getDay_note_data().note_field_data != null) {
					
					
					// iterating through each name in note data
					for (int j=0; j<dp.getDay_note_data().name_box_data.length; j++) {
						
						if ( ws[w].getName().equals(dp.getDay_note_data().name_box_data[j]) ) {
							
							// adding day note
							ws[w].addNote( dp.getDay_note_data().note_field_data[j]);
							
						}
					}
				}
				*/
				
				
				
				// if worker matches an exception
				if ( isException ) {
					ws[w].setTime( dp.bed[index].getTime() );
					ws[w].setMeetLocation( dp.bed[index].getMeetLocation() );
					ws[w].ex_note = dp.bed[index].getNote();
				}
				// if worker does not have an exception
				else {
					// if worker is at first house
					if (ws[w].getHouse() != null && ws[w].getHouse()[0].equals(dp.house_panel[0].house_name_txt.getText())) {
						ws[w].setTime( dp.getMeetTime() );
						ws[w].setMeetLocation( dp.getMeetLocation() );
					}
					// if worker joins midday
					else {
						
					}
				}
				
				
				// adding Covenant
				// if current worker is selected on Covenant panel
				for (int k=0; k<dp.cov_panel.dwp.rows; k++) {
					for (int l=0; l<dp.cov_panel.dwp.columns; l++) {
						if ( ws[w].getName().equals(dp.cov_panel.dwp.workerCheckBoxes[k][l].getText()) &&
							 dp.cov_panel.dwp.workerCheckBoxes[k][l].isSelected() ) {
							
							ws[w].working_covenant = true;
						}
					}
				}
				
				String s = new String();
				
				// add the time
				if( ws[w].time != null && ws[w].time.length() > 2 ) {
					s = new String(s + ws[w].time );
				}
				
				// add the meeting location 
				// if has a meeting location
				if( ws[w].getMeetLocation() != null &&
					ws[w].getMeetLocation().length() > 0 &&
					ws[w].getHouse() != null &&
					!ws[w].getMeetLocation().equals( ws[w].getHouse()[0] )) {
					
					// if has an exception note
					if (ws[w].ex_note != null && ws[w].ex_note.length() > 0) {
						s = new String( s + " " + ws[w].getMeetLocation() + " (" + ws[w].ex_note + ")..." );
						ws[w].ex_note_written = true;
					}
					// if no exception note
					else {
						s = new String( s + " " + ws[w].getMeetLocation() + "..." );
					}
					
				}
				// if no meeting location
				else {
					// if time has already been written
					if (ws[w].time != null && ws[w].time.length() > 2) {
						s = new String( s + "...");
					}
					// if time has not been written
					else {
						// do nothing
					}
					
				}
				
				// add the houses
				if (ws[w].getHouse() != null) {
					for (int i=0; i<ws[w].house.length; i++) {
						// if last house, don't add a comma
						if (i >= ws[w].house.length - 1) {
							
							s = new String( s + ws[w].getHouse()[i] );
							
							// ex_note exists that has not yet been written to the string
							if ( ws[w].ex_note_written == false && ws[w].ex_note != null &&
								 ws[w].ex_note.length() > 0 ){
								
								s = new String( s + " (" + ws[w].ex_note + ")");
								ws[w].ex_note_written = true;
								
							}
							
						}
						// if first house and ex_note exists and it has not already been written 
						else if ( i==0 && ws[w].ex_note_written == false &&
								ws[w].ex_note != null && ws[w].ex_note.length() > 0 ) {
							
							s = new String( s + ws[w].getHouse()[i] + " (" + ws[w].ex_note + "), ");
							ws[w].ex_note_written = true;
							
						}
						// if every other house, add a comma
						else {
							s = new String(s + ws[w].getHouse()[i] + ", ");
						}
					}
					
				}
				
				// add Covenant Info
				
				if (ws[w].working_covenant == true) {
					// if employee worked at houses as well, add a comma
					if(ws[w].getHouse() != null && ws[w].getHouse()[0].length() > 0) {
						s = new String( s + ", Covenant" );
					}
					// if employee only working at Covenant, no comma
					else {
						s = new String( s + "Covenant" );
					}
				}
				
				
				// add any notes
				if (ws[w].getNote() != null) {
					for (int i=0; i<ws[w].note.length; i++) {
						// if only note and either houses or covenant exist, add a preceding semicolon
						if (ws[w].note.length <= 1 && ( ws[w].getHouse() != null || ws[w].working_covenant == true ) ) {
							s = new String( s + "; " + ws[w].getNote()[i] );
						}
						// if only note and houses and covenant do not exist, no preceding semicolon
						else if (ws[w].note.length <= 1 && ( ws[w].getHouse() == null && ws[w].working_covenant == false ) ) {
							s = new String( s + ws[w].getNote()[i] );
						}
						// if last note, don't add a semicolon
						else if (i >= ws[w].note.length - 1) {
							s = new String( s + ws[w].getNote()[i] );
						}
						// if first note and either houses or covenant exist, add a preceding semicolon and succeeding semicolon
						else if (i<=0 && ( ws[w].getHouse() != null || ws[w].working_covenant == true ) ) {
							s = new String( s + "; " + ws[w].getNote()[i] + "; ");
						}
						// if first note and houses and covenant do not exist, no preceding semicolon and succeeding semicolon
						else if (i<=0 && ( ws[w].getHouse() == null  && ws[w].working_covenant == false ) ) {
							s = new String( s + ws[w].getNote()[i] + "; ");
						}
						// for every other note, add a semicolon
						else {
							// was this: s = new String(s + ws[w].getHouse()[i] + "; ");
							s = new String(s + ws[w].getNote()[i] + "; ");
						}
					}
				}
				
				if (s.equals("...")){
					s = "";
				}
				
				ws[w].setSchedule( s );
				
				
				
			}
			} // end if dworkers != null
			// else if dworkers == null
			else {
				ws = new WorkerSchedule[0];
			}
			
			dayData[d] = new NW_DayData();
			dayData[d].setWorkerSchedule(ws);
							
		}  // end day iteration
		
		data.setDayData( dayData );
		
		
		
		// *** 3a. Write User Data to Text File for next week***
		try {
			writeEditWeekData(readEditWeekData(), Settings.SAVED_SCHEDULE, tp);
		}
		catch(Exception e2){
			JOptionPane.showMessageDialog( new JFrame(), "Error: The data did not save to the Text File for next week correctly.", null, JOptionPane.ERROR_MESSAGE);
		}
		
		
		
		
		// *** 3b. Write User Data to Excel Sheet and Save***
		
		try {
			writeDayData( dayData, SubmitWeekListener.new_save);
		}
		catch(Exception e2){
			JOptionPane.showMessageDialog( new JFrame(), "Error: The data did not copy to the Excel document correctly.", null, JOptionPane.ERROR_MESSAGE);
		}
		
		
		
				
		// *** 4. Close Frame  ***
		
		// closing frame
		frame.setVisible(false);
		frame.dispose();
		
		
		
		// *** 5. Open the New Excel Document ***
		
		//Data.setFile( new File( System.getProperty("user.dir") + "/New.xlsx" ) );
		
		try {
			Desktop dt = Desktop.getDesktop();
			dt.open( SubmitWeekListener.new_save );
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(new JFrame(), "The Excel document could not be opened automatically.");
		}
		
		
		// terminate program
		System.exit(0);
		
		
		} // end if true mode
		
		
		
		
		
		
		// else if settings mode
		else {
			
			
			// *** 1. Read User Input ***
				
			NW_Data data = new NW_Data();
			NW_DayData[] dayData = new NW_DayData[5]; // 5 days in week
			
			// for each day
			for (int d=0; d<dayData.length; d++) {
				
				NW_DayPanel dp = tp.nw_day_panel[d];
				
				dayData[d] = new NW_DayData(); 
				
				// Get Begin Information Data
				dayData[d].meet_location = dp.getMeetLocation();
				dayData[d].meet_time = dp.getMeetTime();
				
				if ( dp.bed != null ) {
					//dayData[d].bed = new BeginExceptionData[NW_ExceptionPanel.NUM_EXCEPTIONS];
					dayData[d].bed = new BeginExceptionData[dp.bed.length];
					for (int i=0; i<dp.bed.length; i++) {  
						dayData[d].bed[i] = new BeginExceptionData();
						dayData[d].bed[i].setMeetLocation( dp.bed[i].getMeetLocation() );
						dayData[d].bed[i].setName( dp.bed[i].getName() );
						dayData[d].bed[i].setTime( dp.bed[i].getTime() );
						dayData[d].bed[i].setNote( dp.bed[i].getNote() );
					}
					/*
					for (int i=dp.bed.length; i<NW_ExceptionPanel.NUM_EXCEPTIONS; i++) {
						
					}
					*/
				}
				
				
				/*
				dayData[d].bed = new BeginExceptionData[ dp.bed.length ]; 
				for (int i=0; i<dp.bed.length; i++) {
					dayData[d].bed[i] = new BeginExceptionData();
					dayData[d].bed[i] = dp.bed[i];
				}
				*/
				
				// Get Notes
				dayData[d].dayNoteData = dp.getDay_note_data();
				

				// gather data from each house
				
				NW_HouseData[] houseData = new NW_HouseData[ tp.nw_day_panel[d].house_panel.length ];		// houses in day 
				int shift = 0;
										
				for (int h=0; h<houseData.length; h++) {
						
					houseData[h-shift] = new NW_HouseData();
						
					houseData[h-shift].setHouseName(tp.nw_day_panel[d].house_panel[h].house_name_txt.getText()); 						//read house name
					houseData[h-shift].setSelectedWorkers( tp.nw_day_panel[d].house_panel[h].worker_panel.getSelected() );														//get selected workers
				
				} // end house panels
				dayData[d].setHouseData(houseData);
				
				
				// Get Covenant Data
				dayData[d].cov_worker = dp.cov_panel.dwp.getSelected();
				dayData[d].covNoteData = dp.getCovenant_note_data();

					
			}  // end day panels
				
			data.setDayData( dayData );
				
				

		// *** 2. Open Text File ***
				
		File f;
		if (wk == Settings.WEEK_A) {
			f = Settings.NEXT_WEEK_B;
		}
		else {
			f = Settings.NEXT_WEEK_A;
		}			
		
				
				
				
		// *** 3. Write User Data to Text File and Save***	
		writeEditWeekData( data, f, tp);	
				
				
				
		// *** 4. Creating new frame for weekend panel and disposing of current panel
		JFrame newframe = new JFrame();
		newframe.setResizable( false );
		
		// create new menu frame with menu panel
		JFrame menu_frame = new JFrame();
		menu_frame.setResizable(false);
		menu_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MenuPanel mp = new MenuPanel(menu_frame);
		menu_frame.add(mp);
		menu_frame.pack();
		menu_frame.setLocationRelativeTo(null);
		menu_frame.setVisible(true);
		
		SettingsPanel sp = new SettingsPanel( menu_frame );
		newframe.add(sp);
		newframe.pack();
		newframe.setLocationRelativeTo( null );
			
		frame.setVisible(false);
		frame.dispose();
			
		newframe.setVisible(true);
			
		}
	}
		

	
	// PRIVATE METHODS
	
	private NW_Data readEditWeekData() {
		
					NW_Data data = new NW_Data();
					NW_DayData[] dayData = new NW_DayData[5]; // 5 days in week
					
					// for each day
					for (int d=0; d<dayData.length; d++) {
						
						NW_DayPanel dp = tp.nw_day_panel[d];
						
						dayData[d] = new NW_DayData(); 
						
						// Get Begin Information Data
						dayData[d].meet_location = dp.getMeetLocation();
						dayData[d].meet_time = dp.getMeetTime();
						
						if ( dp.bed != null ) {
							//dayData[d].bed = new BeginExceptionData[NW_ExceptionPanel.NUM_EXCEPTIONS];
							dayData[d].bed = new BeginExceptionData[dp.bed.length];
							for (int i=0; i<dp.bed.length; i++) {  
								dayData[d].bed[i] = new BeginExceptionData();
								dayData[d].bed[i].setMeetLocation( dp.bed[i].getMeetLocation() );
								dayData[d].bed[i].setName( dp.bed[i].getName() );
								dayData[d].bed[i].setTime( dp.bed[i].getTime() );
								dayData[d].bed[i].setNote( dp.bed[i].getNote() );
							}
							/*
							for (int i=dp.bed.length; i<NW_ExceptionPanel.NUM_EXCEPTIONS; i++) {
								
							}
							*/
						}
						
						
						/*
						dayData[d].bed = new BeginExceptionData[ dp.bed.length ]; 
						for (int i=0; i<dp.bed.length; i++) {
							dayData[d].bed[i] = new BeginExceptionData();
							dayData[d].bed[i] = dp.bed[i];
						}
						*/
						
						// Get Notes
						dayData[d].dayNoteData = dp.getDay_note_data();
						

						// gather data from each house
						
						NW_HouseData[] houseData = new NW_HouseData[ tp.nw_day_panel[d].house_panel.length ];		// houses in day 
						int shift = 0;
														
						for (int h=0; h<houseData.length; h++) {
								
							houseData[h-shift] = new NW_HouseData();
								
							houseData[h-shift].setHouseName(tp.nw_day_panel[d].house_panel[h].house_name_txt.getText()); 						//read house name
							houseData[h-shift].setSelectedWorkers( tp.nw_day_panel[d].house_panel[h].worker_panel.getSelected() );														//get selected workers
						
						} // end house panels
						dayData[d].setHouseData(houseData);
						
						
						// Get Covenant Data
						dayData[d].cov_worker = dp.cov_panel.dwp.getSelected();
						dayData[d].covNoteData = dp.getCovenant_note_data();

							
					}  // end day panels
						
					data.setDayData( dayData );
					return data;
		
	}
	
	
	private void writeDayData( NW_DayData[] dd, File f ){
		
		// 1) create workbook,  2) input schedules for each employee on each day,  3) write to file 
		
		
		try{

			// 1) create workbook
			InputStream inp = new FileInputStream(f);
			XSSFWorkbook wb = new XSSFWorkbook(inp);
			Sheet sheet2 = wb.getSheetAt(2);			// PRINTOUTS SHEET
			
						
			
			// 2) input schedules for each employee on each day
			
			int n_column = 3;		// column with worker names is column 3
			int d_column = 1;		// column with days is column 1
			
			Row nrow;				// current row in looking for worker names
			Row drow;				// current row in looking for days
			
			int nnum;
			int dnum;
			
			String s1;
			String s2;
			
			WorkerSchedule ws;

			String[] weekday = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
			
			//System.out.println("*** Begin Submit Test ***");
			
			// for each day
			for(int d=0; d<5; d++){
				
				//System.out.println("Day: " + d);
				//System.out.println("Num of Schedules: " + dd[d].getNumWorkSchedules());
				
				// for each employee
				for (int w=0; w<dd[d].getNumWorkSchedules(); w++) {
					
					ws = dd[d].getWorkerSchedule()[w];
					//System.out.println("Worker: " + w);
					//System.out.println("Name: " + ws.getName() );
					
					nnum = 0;
					nrow = sheet2.getRow(nnum);
					//System.out.println("Row Number: " + nnum);
					boolean d1 = false;
					boolean d2 = false;
					
					//System.out.println("!d1 " + !d1);
					
					// while worker name has not been found
					while ( !d1 ) {
						
						//System.out.println("TEST");
						if (nrow != null) {
							//Cell cell = nrow.getCell(n_column);
							//System.out.println("check");
							//System.out.println("cell == null " + (nrow.getCell(n_column) == null) );
							
							if (nrow.getCell(n_column) != null && nrow.getCell(n_column).getCellType() == Cell.CELL_TYPE_STRING) {
								s1 = nrow.getCell(n_column).getStringCellValue();
								//System.out.println("cell value: " + nrow.getCell(n_column).getStringCellValue());
							}
							else {
								s1 = null;
							}
						}
						else {
							s1 = null;
						}
						
						//System.out.println("isEquivalent() " + ws.isEquivalent(s1));
						// compare strings to worker name; if name is found
						if ( ws.isEquivalent(s1) ) {
							
							dnum = nnum + 1;					//begin row after name was found
							drow = sheet2.getRow( dnum );
							//System.out.println("Row Number: " + dnum);
							//System.out.println("!d2 " + (!d2));
							// while correct day of week has not been found
							while ( !d2 ) {
								
								if (drow != null) {
									if (drow.getCell(d_column) != null && drow.getCell(d_column).getCellType() == Cell.CELL_TYPE_STRING) {
										//System.out.println("cell value: " + drow.getCell(d_column).getStringCellValue());
										s2 = drow.getCell(d_column).getStringCellValue();
									}
									else {
										s2 = "";
									}
								}
								else {
									s2 = "";
								}
									
								//System.out.println("equals() " + s2.equals( weekday[d]));
					
								// compare strings in succeeding rows and column 1? to String[] s = [Monday, Tuesday, Wednesday, Thursday, Friday]
								// if day is found
								if ( s2.equals( weekday[d] ) ) {
								
									// move over one cell and input schedule
									drow.getCell(d_column + 1).setCellValue( ws.getSchedule() );
									d1 = true;
									d2 = true;
									
								}
								dnum++;
								drow = sheet2.getRow( dnum );
								//System.out.println("Row Number: " + dnum);
								if (nnum > 5000) {
									d2 = true;
								}
							}
							
						}
						nnum++;
						nrow = sheet2.getRow(nnum);
						if (nnum > 5000) {
							JOptionPane.showMessageDialog(new JFrame(), "Employee " + ws.getName() + " does not match any name in the Excel file");
							d1 = true;
						}
					}
					
				}
				
				
			}
			
			
			// go through printout sheet and delete houses that people did not work at ( 0 hours )
			try {
			int hours_column = 4;
			int house_column = 3;
			int row = 0;
			
			int counter = 0;
			while (counter < 50) {
				
				
				
				if (sheet2.getRow(row) != null && sheet2.getRow(row).getCell(house_column) != null) {					
					
					if (sheet2.getRow(row).getCell(house_column).getStringCellValue().equals("Monday") ||
							sheet2.getRow(row).getCell(house_column).getStringCellValue().equals("Tuesday") ||
							sheet2.getRow(row).getCell(house_column).getStringCellValue().equals("Thursday") ||
							sheet2.getRow(row).getCell(house_column).getStringCellValue().equals("Wednesday") ||
							sheet2.getRow(row).getCell(house_column).getStringCellValue().equals("Friday") ) {
						
						//System.out.println("Match!");
						
						for (int i=0; i<6; i++) {
							
							row++;
							
							if (sheet2.getRow(row) != null && sheet2.getRow(row).getCell(hours_column) != null &&
									sheet2.getRow(row).getCell(hours_column).getNumericCellValue() == 0 ) {
								
								sheet2.getRow(row).getCell(house_column).setCellValue("");
								sheet2.getRow(row).getCell(hours_column).setCellValue("");
								sheet2.getRow(row).getCell(hours_column+1).setCellValue("");
								
								counter = 0;
								
							}
							
							
						}
						
					}
					
					// for the one case for Tyler Dubose which is different from everyone else
					else if (sheet2.getRow(row).getCell(house_column).getStringCellValue().equals("Tyler DuBose  #10")) {
						
						row++;
						row++;
						row++;
						
						for (int j=0; j<5; j++) {
							if (sheet2.getRow(row) != null && sheet2.getRow(row).getCell(hours_column) != null &&
									sheet2.getRow(row).getCell(hours_column).getNumericCellValue() == 0 ) {
								
								sheet2.getRow(row).getCell(house_column).setCellValue("");
								sheet2.getRow(row).getCell(hours_column).setCellValue("");
								sheet2.getRow(row).getCell(hours_column+1).setCellValue("");
								
							}
							row++;
							row++;
						}
						
						counter = 0;
						
					}
					
				}
				
				
				row++;
				counter++;
			}
			}
			catch( Exception exc) {
				exc.printStackTrace();
			}
			
			
			
			//XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
			
			
			// 3) write to new file
			
			// TEMPORARY
			//Data.setFile( new File( System.getProperty("user.dir") + "/New.xlsx" ) );
			// END TEMPORARY
			
			out = new FileOutputStream( SubmitWeekListener.new_save );
			wb.write(out);
			
			out.close();
			wb.close();
			
			
		}catch(Exception exception){
			exception.printStackTrace();
			String message = "Error: Next week's schedule was not copied successfully to the Excel document.\n" +
								"You will have to enter the data manually into the Excel document.";
			JOptionPane.showMessageDialog( new JFrame(), message, null, JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		
		
	}
	
	
	private void writeEditWeekData( NW_Data data, File f, TabbedPane tp ) {
		
		
		BufferedWriter bw = null;
		try {
			
			FileWriter fw = new FileWriter( f );
			bw = new BufferedWriter( fw );			
			
			
			
			
			
			NW_DayData day = new NW_DayData();
			NW_HouseData house = new NW_HouseData();
			
			for (int d=0; d<data.dayData.length; d++) {
				
				bw.write("Day " + d);
				bw.newLine();
				
				day = data.dayData[d];
				
				// writing Begin Information
				
				if (day.meet_location != null) {
					bw.write(day.meet_location);
					bw.newLine();
				}
				else {
					bw.newLine();
				}
				
				if (day.meet_time != null) {
					bw.write(day.meet_time);
					bw.newLine();
				}
				else {
					bw.newLine();
				}
				
				if (day.bed != null) {
					for(int i=0; i<day.bed.length; i++) {
						bw.write( day.bed[i].getName());
						bw.newLine();
						
						if ( day.bed[i].getMeetLocation() != null && day.bed[i].getMeetLocation().length() > 0) {
							bw.write( day.bed[i].getMeetLocation());
							bw.newLine();
						}
						else {
							bw.write("");
							bw.newLine();
						}
					
						bw.write( day.bed[i].getTime());
						bw.newLine();
					
						bw.write( day.bed[i].getNote());
						bw.newLine();
					}
					for(int i=day.bed.length; i<NW_ExceptionPanel.NUM_EXCEPTIONS; i++){
					
						bw.write("");
						bw.newLine();
					
						bw.write("");
						bw.newLine();
					
						bw.write("");
						bw.newLine();
					
						bw.write("");
						bw.newLine();
					}
				}
				else {
					for (int i=0; i<NW_ExceptionPanel.NUM_EXCEPTIONS; i++) {
						bw.write("");
						bw.newLine();
					
						bw.write("");
						bw.newLine();
					
						bw.write("");
						bw.newLine();
					
						bw.write("");
						bw.newLine();
					}
				}
				
				/*
				if (day.dayNoteData != null) {
				for (int i=0; i<day.dayNoteData.name_box_data.length; i++) {
					
					bw.write(day.dayNoteData.name_box_data[i]);
					bw.newLine();
					
					bw.write(day.dayNoteData.note_field_data[i]);
					bw.newLine();
				}
				}
				*/
				// 3) Covenant Data
				
				
				// a) workers
				String t = "";
				if (day.cov_worker != null) {
					for (int i=0; i<day.cov_worker.length; i++) {
						t = new String(t + day.cov_worker[i] + " ");
					}
				}
				else {
					// do nothing
				}
				bw.write(t);
				bw.newLine();
				
				// b) notes (not actually for covenant, but that's what I called them...)
				if (day.covNoteData != null) {
					for (int i=0; i<NW_NotePanel.ROWS; i++) {
					
						bw.write(day.covNoteData.name_box_data[i]);
						bw.newLine();
					
						bw.write(day.covNoteData.note_field_data[i]);
						bw.newLine();
					}
				}
				else {
					for (int i=0; i<NW_NotePanel.ROWS; i++) {
						
						bw.write("");
						bw.newLine();
					
						bw.write("");
						bw.newLine();
					}
				}
				
				
				
				// 4) input house data
				
				for (int h=0; h<day.houseData.length; h++) {
					
					house = day.houseData[h];
					
					bw.write("House " + h);
					bw.newLine();
					
					// write all data...
					bw.write(house.getHouseName());
					bw.newLine();
					
					String s = "";
					String[] temp = house.getSelectedWorkers();
					for (int i=0; i<temp.length; i++) {
						s = new String(s + temp[i] + " ");
					}
					bw.write(s);
					bw.newLine();
						
				} // end house panel
				
				
				
				
			}  // end day panels
			
			bw.close();
			
		}catch(Exception exception){
			exception.printStackTrace();
			JOptionPane.showMessageDialog(new JFrame(), "Error: Saved data was not created properly.");
		}
		
		
	}


}
