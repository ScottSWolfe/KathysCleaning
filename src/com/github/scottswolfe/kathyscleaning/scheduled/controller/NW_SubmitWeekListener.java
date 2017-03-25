package com.github.scottswolfe.kathyscleaning.scheduled.controller;

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

import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.controller.MenuBarController;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.model.BeginExceptionData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_DayData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_HouseData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.WorkerSchedule;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_ExceptionPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_NotePanel;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;


public class NW_SubmitWeekListener implements ActionListener {

//  FIELDS
	GeneralController<TabbedPane, NW_Data> controller;
	TabbedPane tp;
	JFrame frame;
	FileOutputStream out;
	int mode;
	int wk;
	
	
	
//  CONSTRUCTOR

	public NW_SubmitWeekListener(GeneralController<TabbedPane, NW_Data> controller, 
	        TabbedPane tp, JFrame frame, int mode, int wk) {
	    
	    this.controller = controller;
		this.tp = tp;
		this.frame = frame;
		this.mode = mode;
		this.wk = wk;
	}
	
	
	
//  METHODS
	
	
	
	
//  LISTENER
	
	public void actionPerformed(ActionEvent e){
		
		if (!StaticMethods.confirmSubmitWeek()) {
			return;
		}
		
		controller.readInputAndWriteToFile(Settings.saveFile);
		
		// TODO temporary hack
		MenuBarController<TabbedPane, NW_Data> hack = new MenuBarController<>(controller);
		hack.menuItemGenExcel();
		
		frame.setVisible(false);
		frame.dispose();
		
		try {
			Desktop dt = Desktop.getDesktop();
			dt.open(Settings.excelFile);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(new JFrame(), "The Excel document could not be opened automatically.");
		}
		
		// terminate program
		System.exit(0);
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
			while (counter < 100) {
				
				if (sheet2.getRow(row) != null && sheet2.getRow(row).getCell(house_column) != null) {					
					
					if (sheet2.getRow(row).getCell(house_column).getStringCellValue().equals("Monday") ||
							sheet2.getRow(row).getCell(house_column).getStringCellValue().equals("Tuesday") ||
							sheet2.getRow(row).getCell(house_column).getStringCellValue().equals("Thursday") ||
							sheet2.getRow(row).getCell(house_column).getStringCellValue().equals("Wednesday") ||
							sheet2.getRow(row).getCell(house_column).getStringCellValue().equals("Friday") ||
							sheet2.getRow(row).getCell(house_column).getStringCellValue().equals("Sunday")) {
						
					    try {
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
						catch(Exception e) {
						    e.printStackTrace();
						}
						
					}
					
					// for the one case for Tyler Dubose which is different from everyone else
					else if (sheet2.getRow(row).getCell(house_column).getStringCellValue().equals("Tyler #10")) {
						
					    try {
					    System.out.println("Tyler #10 triggered");
					    
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
					    } catch(Exception e) {
	                            e.printStackTrace();
	                    }
						
					}
					else if (sheet2.getRow(row).getCell(house_column).getStringCellValue().equals("Cris #9")) {
                        
					    try {
	                       System.out.println("Cris #9 triggered");

					    
                        row++;
                        row++;
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
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
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
			
			out = new FileOutputStream(Settings.excelFile);
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
