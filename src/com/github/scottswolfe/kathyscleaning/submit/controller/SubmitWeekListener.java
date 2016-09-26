package com.github.scottswolfe.kathyscleaning.submit.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.scottswolfe.kathyscleaning.covenant.CovenantPanel;
import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener;
import com.github.scottswolfe.kathyscleaning.general.controller.StaticMethods;
import com.github.scottswolfe.kathyscleaning.general.model.DefaultWorkerData;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.view.SettingsPanel;
import com.github.scottswolfe.kathyscleaning.submit.model.Data;
import com.github.scottswolfe.kathyscleaning.submit.model.DayData;
import com.github.scottswolfe.kathyscleaning.submit.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.submit.model.HouseData;
import com.github.scottswolfe.kathyscleaning.submit.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.weekend.WeekendPanel;


public class SubmitWeekListener implements ActionListener {

//  FIELDS
	
	TabbedPane tp;
	JFrame frame;
	Calendar date;
	int mode;
	int wk;
	
	// for calendar date stuff
	int firstday;
	int lastday;
	
	// for testing
	public static File new_save;
	// end testing
	
//  CONSTRUCTOR

	public SubmitWeekListener( TabbedPane tp, JFrame frame, Calendar date, int mode, int wk ){
		this.tp = tp;
		this.frame = frame;
		this.date = date;
		this.mode = mode;
		this.wk = wk;
	}
	
	
	
//  METHODS
	
	
	
	
//  LISTENER
	// TODO: condense this method by making parts of the method their own private methods
	public void actionPerformed(ActionEvent e) {
		
		int a = StaticMethods.confirmSubmitWeek();
		if (a==0) {
			return;
		}
		
		
		if (mode == SettingsPanel.TRUE_MODE) {
		/* Steps:
		 * 
		 * 		1. Read User Input
		 * 		2. Create new Excel Sheet
		 * 		3. Write Data onto Excel Sheet and Save
		 * 		4. Close Frame and open New Frame for submitting Covenant data
		 * 
		 */
		
		// *** 1. Read User Input ***
		
		Data data = new Data();
		DayData[] dayData = new DayData[5]; // 5 days in week
		
		for (int d=0; d<dayData.length; d++) {
				
			HouseData[] houseData = new HouseData[ tp.day_panel[d].house_panel.length ];		// houses in day 
			int shift = 0;
			
			// TODO: make it so only house panels with data get read into houseData objects
			// this will cause fewer problems below when writing to excel workbook
			
			for (int h=0; h<houseData.length; h++) {
				
				
				houseData[h-shift] = new HouseData();
				
				houseData[h-shift].setHouseName(tp.day_panel[d].house_panel[h].house_name_txt.getText()); 						//read house name
				houseData[h-shift].setHousePay( tp.day_panel[d].house_panel[h].pay_txt.getText() ); 		//read house pay
				houseData[h-shift].setTimeBegin( tp.day_panel[d].house_panel[h].time_begin_txt.getText() );	//read begin time
				houseData[h-shift].setTimeEnd( tp.day_panel[d].house_panel[h].time_end_txt.getText() );	//read end time
				houseData[h-shift].setSelectedWorkers( tp.day_panel[d].house_panel[h].worker_panel.getSelected() );														//get selected workers
				houseData[h-shift].setExceptionData( tp.day_panel[d].house_panel[h].exception_data.getExceptionData() );													//get exception info
		
				
				
			} // end house panels
			
			dayData[d] = new DayData();
			dayData[d].setHouseData(houseData);
			
		}  // end day panels
		
		data.setDayData( dayData );
		data.setDate( tp.day_panel[0].header_panel.date );
		
		

		// *** 2. Open Excel Sheet ***
		
		
		File template = SettingsPanel.getDefaultExcelFile();
		
		
		
		// *** 3. Write User Data to Excel Sheet and Save***
		
		/*
		// checking that file is xlsx
		String ext = FilenameUtils.getExtension(template.getAbsolutePath());
		if (ext.equals( "xlsx" )) {
			// do nothing
		}
		else {
			// TODO Joption pane that says the default must be xlsx!!
			// send program back to menu or settings panel
		}
		*/
		
		writeDayData( data, template);
		
		
		
				
		// *** 4. Close Frame and Create New Frame for Inserting Next Week Schedule ***
		
		// closing frame
		
		frame.setVisible(false);
		frame.dispose();
		//frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		/*
		// for testing
		Desktop dt = Desktop.getDesktop();
		try {
			dt.open( new_save );
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(new JFrame(), "The Excel document could not be opened automatically.");
		}
		// end testing
		*/
		
		JFrame cov_frame = new JFrame();
		cov_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		cov_frame.setResizable(false);
		cov_frame.addWindowListener( new MainWindowListener() );
		
		CovenantPanel cov_panel = new CovenantPanel(cov_frame, new DefaultWorkerData("CovenantWorkerSaveFile"), date, mode, wk );
		
		cov_frame.add(cov_panel);
		cov_frame.pack();
		cov_frame.setLocationRelativeTo( null );
		cov_frame.setVisible(true);
		
		}
		// end if mode
		// else now do submit for settings mode
		else {
		
			// TODO: add in reading data and writing data to save file
			/* Steps:
			 * 
			 * 		1. Read User Input
			 * 		2. Create new Excel Sheet
			 * 		3. Write Data onto Excel Sheet and Save
			 * 		4. Close Frame and open New Frame for submitting Covenant data
			 * 
			 */
			
			// *** 1. Read User Input ***
			
			Data data = new Data();
			DayData[] dayData = new DayData[5]; // 5 days in week
			
			for (int d=0; d<dayData.length; d++) {
					
				HouseData[] houseData = new HouseData[ tp.day_panel[d].house_panel.length ];		// houses in day 
				int shift = 0;
				
				// TODO: make it so only house panels with data get read into houseData objects
				
				
				for (int h=0; h<houseData.length; h++) {
					
					
					houseData[h-shift] = new HouseData();
					
					houseData[h-shift].setHouseName(tp.day_panel[d].house_panel[h].house_name_txt.getText()); 						//read house name
					houseData[h-shift].setHousePay( tp.day_panel[d].house_panel[h].pay_txt.getText() ); 		//read house pay
					houseData[h-shift].setTimeBegin( tp.day_panel[d].house_panel[h].time_begin_txt.getText() );	//read begin time
					houseData[h-shift].setTimeEnd( tp.day_panel[d].house_panel[h].time_end_txt.getText() );	//read end time
					houseData[h-shift].setSelectedWorkers( tp.day_panel[d].house_panel[h].worker_panel.getSelected() );														//get selected workers
					houseData[h-shift].setExceptionData( tp.day_panel[d].house_panel[h].exception_data.getExceptionData() );													//get exception info
			
					
					
				} // end house panels
				
				dayData[d] = new DayData();
				dayData[d].setHouseData(houseData);
				
			}  // end day panels
			
			data.setDayData( dayData );
			data.setDate( tp.day_panel[0].header_panel.date );
			
			

			// *** 2. Open Text File ***
			
			File f;
			if (wk == SettingsPanel.WEEK_A) {
				f = SettingsPanel.SUBMIT_WEEK_A;
			}
			else {
				f = SettingsPanel.SUBMIT_WEEK_B;
			}			
			
			
			
			// *** 3. Write User Data to Text File and Save***
					
			writeEditWeekData( data, f );	
			
			
			
			// *** 4. Creating new frame for weekend panel and disposing of current panel
			JFrame nwframe = new JFrame();
			nwframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			nwframe.setResizable(false);
			nwframe.addWindowListener( new MainWindowListener() );
			
			WeekendPanel wp = new WeekendPanel(nwframe,date,mode,wk);
						
			nwframe.add(wp);
			nwframe.pack();
			nwframe.setLocationRelativeTo( null );
						
			frame.setVisible( false );
			frame.dispose();
			
			// populate data from save file
			if ( wk == SettingsPanel.WEEK_A ) {
				wp.weekA_button.setSelected(true);
				ActionEvent event = new ActionEvent(this, 0, "");
				ActionListener[] al = wp.weekA_button.getActionListeners();
				al[0].actionPerformed(event);
			}
			else if ( wk == SettingsPanel.WEEK_B ) {
				wp.weekB_button.setSelected(true);
				ActionEvent event = new ActionEvent(this, 0, "");
				ActionListener[] al = wp.weekB_button.getActionListeners();
				al[0].actionPerformed(event);
			}
			else {
				// do nothing
			}
			
			wp.weekA_button.setEnabled(false);
			wp.weekB_button.setEnabled(false);
			wp.neither_button.setEnabled(false);

						
			nwframe.setVisible(true);
			
		}
		
		writeHousePay();
		
	}
	
	
	// private functions
	
	private void writeDayData( Data data, File tmp ){
		
		/*
		 * 
		 * 	1) Create workbook
		 *  2) Set date
		 *  3) Input house data
		 *  4) Write to file
		 *  	
		 */
		
		try{
			

			// 1) Create workbook
			InputStream inp = new FileInputStream(tmp);
			XSSFWorkbook wb = new XSSFWorkbook(inp);
			Sheet sheet0 = wb.getSheetAt(0);

			
			
			// 2) set date
			
			String day;
			String month;
			String year;
			
			String[] months = {	"January", "February", "March",
					"April", "May", "June", "July",
					"August", "September", "October",
					"November", "December" };
			
			Calendar c = data.date;
			
			Calendar copy = (Calendar) c.clone();
			firstday = copy.get( Calendar.DAY_OF_MONTH );
			copy.add( Calendar.DAY_OF_MONTH, 4);
			lastday = copy.get( Calendar.DAY_OF_MONTH );
			
			if ( lastday < firstday ) {
				day = new String( 	firstday +		// beginning of week
						" - " +
						months[copy.get( Calendar.MONTH )] + " " +
						lastday ); // end of week
			}
			else {
				day = new String( 	firstday +		// beginning of week
						" - " +
						lastday ); // end of week
			}
			
			
			
			month = new String( months[c.get(Calendar.MONTH)] );
			
			year = new String( String.valueOf(c.get( Calendar.YEAR )) );
			Row row = sheet0.getRow(0);
			
			row.getCell(3).setCellValue( month );
			row.getCell(5).setCellValue( day );
			row.getCell(8).setCellValue( year );			
					
			
			
			// 3) input house data
			
			// for each day
			for(int d=0; d<5; d++){
				
				Row name_row = sheet0.getRow(d*9 + 1);
				
				// for each house
				for (int h=0; h<data.dayData[d].houseData.length; h++){
					
					// TODO: issues here.... about when and what it should do based on how much data given or not given
					
					// if the house data is NOT empty
					if ( !data.dayData[d].houseData[h].getHouseName().isEmpty() &&
							data.dayData[d].houseData[h].getHours() != 0 ) {
						
						// setting excel row number to write to
						int row_num = d*9 + 2 + h;  // this formula gives the correct line in the excel sheet template
						row = sheet0.getRow( row_num );
						
						String s1 = CovenantPanel.convertFormat( data.dayData[d].houseData[h].getTimeBegin(), CovenantPanel.HOUSE_TIME );
						String s2 = CovenantPanel.convertFormat( data.dayData[d].houseData[h].getTimeEnd(), CovenantPanel.HOUSE_TIME );
						
						row.getCell(0).setCellValue( DateUtil.convertTime(s1) );
						row.getCell(1).setCellValue( DateUtil.convertTime(s2) );
						
						// setting # of hours worked
						row.getCell(2).setCellValue( data.dayData[d].houseData[h].getHours() );
						
						// setting house name
						row.getCell(3).setCellValue( data.dayData[d].houseData[h].getHouseName() );
						
						// setting money paid
						row.getCell(4).setCellValue( data.dayData[d].houseData[h].getHousePay() );
											
						
						// writing in zero for all employees who did not work
						//System.out.println("House " + (h+1));
						boolean names_remaining = true;
						boolean name_match;
						int index = 5;
						String[] worker = data.dayData[d].getHouseData()[h].getSelectedWorkers();
						/*
						for (int i=0; i<worker.length; i++) {
							System.out.println(worker[i]);
						}
						*/
						// while there are still names remaining in the excel sheet
						while ( names_remaining == true && index < 25 ) {
							// for the number of selected workers at the house	
							name_match = false;
							for ( int j=0; j<worker.length; j++) {
								
								// if selected worker matches name on excel sheet
								//System.out.println(name_row.getCell(index).getStringCellValue());
								//System.out.println(worker[j]);
								//System.out.println();
								if ( name_row.getCell(index).getStringCellValue().equals(worker[j]) ) {
									name_match = true;
									//System.out.println("Name Match: " + "True");
									break;
								}
								// if name on excel sheet is kathy
								else if ( name_row.getCell(index).getStringCellValue().equals("Kathy")) {
									names_remaining = false;
									name_match = true;
									//System.out.println("Name Match: Kathy");
									break;
								}
								
							}
							// if none of the selected workers match the name on the excel sheet
							if ( name_match == false ) {
								//System.out.println("Name Match: False");
								
								if (row.getCell(index) == null) {
									// do nothing
								}
								else if (row.getCell(index) != null && row.getCell(index).getCellType() == Cell.CELL_TYPE_FORMULA) {
									String s = row.getCell(index).getCellFormula();
									s = changeFormula(s,(double)0);
									row.getCell(index).setCellFormula( s );
								}
								else {
									row.getCell(index).setCellValue( 0 );
								}
								// TODO: should redo excel sheet so no blank cells in this area (Cris, Tyler)
								
							}
							index++;
						}
					
					}
					else if (data.dayData[d].houseData[h].getHouseName().isEmpty() &&
							data.dayData[d].houseData[h].getHours() != 0
							 ||
							 !data.dayData[d].houseData[h].getHouseName().isEmpty() &&
							 data.dayData[d].houseData[h].getHours() == 0 ) {
						
						// TODO: throw an error message??
					}
					else {
						// do nothing
					}
					
					// if there is exception data, add data to excel sheet
					if (!data.dayData[d].houseData[h].getHouseName().isEmpty()) { // only if the house has been named
					
					ExceptionData exd = data.dayData[d].houseData[h].getExceptionData();
					if ( exd.edited == true ) {
						
						// iterate through the names in the exception data
						for (int m=0; m<exd.worker_name.length; m++) {
							
							// checking that worker name and times are not null or empty
							if (exd.worker_name[m] != null && !exd.worker_name[m].isEmpty() &&
								exd.time_begin[m] != null && !exd.time_begin[m].isEmpty() &&
								exd.time_end[m] != null && !exd.time_end[m].isEmpty()) {
								
								
								// find name row; trace down name row looking for match with worker_name[m]
								// 		if no match (find kathy) send error report and move on
								boolean name_found = false;
								//Cell name_cell = row.getCell(5);
								int cell_number = 5;
								while (name_found == false) {
									
									// if cell name matches current worker name
									if ( name_row.getCell(cell_number).getStringCellValue().equals(exd.worker_name[m]) ) {
										
										// calculate hours worked
										double hours = CovenantPanel.getHours(exd.time_begin[m], exd.time_end[m]);
										
										// insert data into excel doc
										Row house_row = sheet0.getRow( d*9 + 2 + h );
										//if () {
											String s = house_row.getCell(cell_number).getCellFormula(); // issue with numeric cells??
											s = changeFormula(s,hours);
											house_row.getCell(cell_number).setCellFormula(s);
										//}
										//else {
											// TODO: error message
										//}
										break;
										
									}
									// if cell name is Kathy
									else if ( name_row.getCell(cell_number).getStringCellValue().equals("Kathy") ) {
										String message = "Error: Employee " + exd.worker_name[m] + 
														 " from the exception at " + data.dayData[d].houseData[h].getHouseName() +
														 "could not be found on the excel document.\n" +
														 "You will need to enter the data manually.";
										JOptionPane.showMessageDialog(new JFrame(), message, null, JOptionPane.ERROR_MESSAGE);
										break;
									}
									// else move on to the next cell
									else {
										// move on to next cell
										cell_number++;
									}
								}
								
								
								 /* 
								 * find house row
								 * 
								 * calculate length of time
								 * find worker's pay
								 * calculate amount earned for worker
								 * 
								 * insert amount earned into correct cell
								 * 
								 * done!
								 * 
								 */
								
							}
							
						}
						
					}} // ending if house named
					
				}
				
			}
			XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
			
			
			
			
			
			// 4) write to new file
			/*
			String path = new String("C:\\Users\\Scott\\Documents\\MATLAB\\KathysCleaning\\2015");
			Runtime.getRuntime().exec("explorer.exe /select," + path);
			*/
			/*
			Data.setFile( new File( System.getProperty("user.dir") + "/New.xlsx" ) );
			//FileOutputStream out = new FileOutputStream(new String (System.getProperty("user.dir") + "/New.xlsx") );
			FileOutputStream out = new FileOutputStream( Data.new_file );
			wb.write(out);
			*/
			
			File save_location = SettingsPanel.getDefaultSaveLocation();
			
			// generate save name
			String save_name = new String();
			Calendar copy2 = (Calendar) c.clone();
			firstday = copy2.get( Calendar.DAY_OF_MONTH );
			copy2.add( Calendar.DAY_OF_MONTH, 4);
			lastday = copy2.get( Calendar.DAY_OF_MONTH );
			
			if ( lastday < firstday ) {
				save_name = new String( 	
						months[c.get(Calendar.MONTH)] +
						firstday +
						"-" +
						months[copy2.get( Calendar.MONTH )] +
						lastday + "," +
						c.get(Calendar.YEAR) +
						".xlsx");
			}
			else {
				save_name = new String( 	
						months[c.get(Calendar.MONTH)] +
						firstday +
						"-" +
						lastday + "," +
						c.get(Calendar.YEAR) +
						".xlsx");
			}
			// end generate save name
			
			String pathname = new String( save_location.getAbsolutePath() + "\\" + save_name );
			
			// check if this exact path already exists.
			try {
				boolean newfile = false;
				int count = 0;
				while( newfile == false ){
					newfile = true;
				File folder = new File( save_location.getAbsolutePath() );
				File[] listOfFiles = folder.listFiles();

				 	for (int i=0; i<listOfFiles.length; i++) {
				    	if (listOfFiles[i].isFile()) {
				    		//System.out.println("File " + listOfFiles[i].getName());
				    		
				    		if ( pathname.equals( listOfFiles[i].getAbsolutePath() ) ) {

				    			newfile = false;
				    			count++;
				    			if ( lastday < firstday ) {
				    				save_name = new String( 	
				    						months[c.get(Calendar.MONTH)] +
				    						firstday +
				    						"-" +
				    						months[copy2.get( Calendar.MONTH )] +
				    						lastday + "," +
				    						c.get(Calendar.YEAR) +
				    						"("+count+")" +					// TODO: temporary quick fix
				    						".xlsx");
				    			}
				    			else {
				    				save_name = new String( 	
				    						months[c.get(Calendar.MONTH)] +
				    						firstday +
				    						"-" +
				    						lastday + "," +
				    						c.get(Calendar.YEAR) +
				    						"("+count+")" +					// TODO: temporary quick fix
				    						".xlsx");
				    			}
				    			
				    			pathname = new String( save_location.getAbsolutePath() + "\\" + save_name );

				    		}
				    		
				    	} 
				    	
				 	}
				} 	
			}
			catch (Exception e1) {
				
			}
			
			new_save = new File( pathname );
			
			FileOutputStream out = new FileOutputStream( new_save );
			wb.write( out );
			
			out.close();
			wb.close();
			
			// TODO: make sure this submit week and the NW submit week are writing to the same file.
			
			
		}catch(Exception exception){
			exception.printStackTrace();
			JOptionPane.showMessageDialog(new JFrame(), "Error: Excel document was not created properly.");
		}
		
		
		
	}
	
	
	private void writeEditWeekData( Data data, File f ) {
		
		
		
		BufferedWriter bw = null;
		try {
			
			FileWriter fw = new FileWriter( f );
			bw = new BufferedWriter( fw );
			
			//bw.write( excel_template_file.getAbsolutePath() );				bw.newLine();
			
			
		
			// 2) set date					
			
			
			// 3) input house data
			
			DayData day = new DayData();
			HouseData house = new HouseData();
			
			for (int d=0; d<data.dayData.length; d++) {
				
				bw.write("Day " + d);
				bw.newLine();
				
				day = data.dayData[d];
				
				// TODO: make it so only house panels with data get written into save file
				
				for (int h=0; h<day.houseData.length; h++) {
					
					house = day.houseData[h];
					
					bw.write("House " + h);
					bw.newLine();
					
					// write all data...
					bw.write(house.getHouseName());
					bw.newLine();
					
					bw.write( String.valueOf(house.getHousePay()) );
					bw.newLine();
					
					bw.write(house.getTimeBegin());
					bw.newLine();
					
					bw.write(house.getTimeEnd());
					bw.newLine();
					
					String s = "";
					for (int i=0; i<house.getSelectedWorkers().length; i++) {
						s = new String(s + house.getSelectedWorkers()[i] + " ");
					}
					bw.write(s);
					bw.newLine();
					
					ExceptionData ex = house.getExceptionData();
					for (int i=0; i<ex.worker_name.length; i++ ) {
						bw.write(ex.worker_name[i]);
						bw.newLine();
						
						bw.write(ex.time_begin[i]);
						bw.newLine();
						
						bw.write(ex.time_end[i]);
						bw.newLine();
					}
			
					
					
				} // end house panel
				
				
				
			}  // end day panels
			
			bw.close();
			
		}catch(Exception exception){
			exception.printStackTrace();
			JOptionPane.showMessageDialog(new JFrame(), "Error: Saved data was not created properly.");
		}
		
		
	}
	
	
	private void writeHousePay() {
		
		
		BufferedWriter bw = null;
		try {
			
			
			
			Scanner input = new Scanner( HouseNameDocListener.HOUSE_PAY_FILE );
			Scanner input2 = new Scanner( HouseNameDocListener.HOUSE_PAY_FILE );
						
			int i=0;
			while (input2.hasNextLine()) {
				input2.nextLine();
				i++;
			}
			
			
			String[] s = new String[i]; // 500 is arbitrarily large
			
			for( int j=0; j<i; j++) {
				s[j] = input.nextLine();
			}
			
			
			input.close();
			input2.close();
			
			// for each day
			for (int d=0; d<5; d++) {
				
				DayPanel dp = tp.day_panel[d];
				
				// for each house
				for (int h=0; h<dp.house_panel.length; h++) {
					
					boolean match = false;
					
					// for length of array
					for (int k=0; k<s.length; k++) {
						
						if (s[k].equalsIgnoreCase( dp.house_panel[h].house_name_txt.getText() )) {
							
							s[k+1] = dp.house_panel[h].pay_txt.getText();
							match = true;
							break;
						}
						
					}
					if (match == false) {
						
						String[] r = new String[s.length+2];
						
						for (int l=0; l<s.length; l++) {
							r[l] = s[l];
						}
						
						r[r.length-2] = dp.house_panel[h].house_name_txt.getText();
						r[r.length-1] = dp.house_panel[h].pay_txt.getText();
						
						s = r;
					}
				}
			}
			
			FileWriter fw = new FileWriter( HouseNameDocListener.HOUSE_PAY_FILE );
			bw = new BufferedWriter( fw );
			
			for (int m=0; m<s.length; m++) {
				
				bw.write(s[m]);
				bw.newLine();
			}
			
			bw.close();
			fw.close();
			
		}
		catch(Exception e1){
			
		}
	}
	
	
	public static String changeFormula( String s, Double hours ) {
		
		// change double to 3 digits after decimal
		DecimalFormat numberFormat = new DecimalFormat("#.000");
		String insert = numberFormat.format(hours);
		int skip = 0;		
		
		// parse the string and change (capital letter,number) (eg A5) to double
		char[] c = s.toCharArray();
		
		//int count = 0;
		for (int i=1; i<c.length; i++) {
			if (c[i] == '*') {
				break;
			}
			else {
				skip++;
			}
		}
		
		char[] k = new char[ c.length - skip + insert.length() ];
		
		// formuals come in the form: +C6*PAY!$C$7
		// I want to send them out as: +1.362*PAY!$C$7
		
		int shift = 0;
		k[0] = c[0];
		for (int i=1; i<insert.length()+1; i++) {
			k[i] = insert.toCharArray()[i-1];
			shift++;
		}
		for (int i=1+shift; i<k.length; i++) {
			k[i] = c[i-shift + skip];
		}
		return String.valueOf(k);
	}
	
	
	
		
	
	
}
