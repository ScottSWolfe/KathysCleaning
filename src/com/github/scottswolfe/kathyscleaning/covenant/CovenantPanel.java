package com.github.scottswolfe.kathyscleaning.covenant;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.scottswolfe.kathyscleaning.general.controller.FlexibleFocusListener;
import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener;
import com.github.scottswolfe.kathyscleaning.general.controller.StaticMethods;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeDocFilter;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeKeyListener;
import com.github.scottswolfe.kathyscleaning.general.model.DefaultWorkerData;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.menu.view.SettingsPanel;
import com.github.scottswolfe.kathyscleaning.submit.controller.HousePayDocFilter;
import com.github.scottswolfe.kathyscleaning.submit.controller.SubmitWeekListener;
import com.github.scottswolfe.kathyscleaning.weekend.WeekendPanel;

import net.miginfocom.swing.MigLayout;


public class CovenantPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1581761399539373721L;
	
	
	
	// FIELDS
	JFrame frame;
	DefaultWorkerData dwd;
	Calendar date;
	
	public static final int HOUSE_TIME = 0;
	public static final int COVENANT_TIME = 1;
	
	int mode;
	int wk;
	
	public final static int ROWS = 12;
	int rows = ROWS;
	
	private final static File COVENANT_EARNED_SAVE_FILE = new File( System.getProperty("user.dir") + "\\save\\CovenantEarnedSaveFile" );
	
	String[] day = {"Monday","Tuesday","Wednesday","Thursday","Friday"};
	
	
	// COMPONENT FIELDS
	JLabel[] name_label;
	JLabel[] day_label;
	
	JTextField[][] begin_time_field;
	AbstractDocument[][] begin_doc;
	TimeDocFilter[][] begin_tdf;
	
	JTextField[][] end_time_field;
	AbstractDocument[][] end_doc;
	TimeDocFilter[][] end_tdf;
	
	JLabel other_label;
	JTextField[] other_field;
	
	JButton edit_button;
	JButton cancel_button;
	JButton submit_button;
	
	JLabel amount_earned_label;
	JTextField[] amount_earned_field;
	
	
	
	// CONSTRUCTOR

	public CovenantPanel(JFrame frame, DefaultWorkerData dwd2, Calendar date, int mode, int wk) {
		
		this.frame = frame;
		this.dwd = dwd2;
		this.date = date;
		this.mode = mode;
		this.wk = wk;
		
		
		this.dwd = new DefaultWorkerData( SettingsPanel.COV_WORKER_SAVE.getPath() );//System.getProperty("user.dir") + "\\save\\CovenantWorkerSaveFile");
		
		setLayout( new MigLayout() );
		setBackground( Settings.BACKGROUND_COLOR );
		
		// getting number of rows based on number of workers
		
		day_label = new JLabel[day.length];
		name_label = new JLabel[rows];
		begin_time_field = new JTextField[rows][day.length];
		begin_tdf = new TimeDocFilter[rows][day.length];
		begin_doc = new AbstractDocument[rows][day.length];
		end_time_field = new JTextField[rows][day.length];
		end_tdf = new TimeDocFilter[rows][day.length];
		end_doc = new AbstractDocument[rows][day.length];
		amount_earned_field = new JTextField[day.length];
		
		String layout_format;
		
		
		
		
		//worker labels
		for(int i=0; i<rows; i++){
			
			name_label[i] = new JLabel();
			if (dwd.default_workers != null && i<dwd.default_workers.length && dwd.default_workers[i] != null) {
				name_label[i].setText( dwd.default_workers[i] );
			}
			else {
				name_label[i].setText("");
			}
			name_label[i].setFont( name_label[i].getFont().deriveFont( Settings.HEADER_FONT_SIZE ));
			name_label[i].setBackground( Settings.BACKGROUND_COLOR );

			
			if ( i > 0 ) {
				layout_format = new String("cell "+ (0) + " " + (i+1) + ", align right, gapy 10, wrap" );
			}
			else {
				layout_format = new String("cell "+ (0) + " " + (i+1) + ", align right, gapy 1" );
			}
			
			add( name_label[i], layout_format);
			
		}
		
		amount_earned_label = new JLabel();
		amount_earned_label.setText("Earned: ");
		amount_earned_label.setFont( amount_earned_label.getFont().deriveFont(Settings.HEADER_FONT_SIZE) );
		
		add( amount_earned_label, new String("cell "+ (0) + " " + (rows+1) + ", align right, gapy 10, wrap") );
		
		
		//time text fields
		int num_v_sep = 0;
		
		
		/*
		 * TODO: WARNING: I screwed up the i and j here. i is columns and j is rows
		 */
		
		for(int i=0; i<day.length; i++){
			
			day_label[i] = new JLabel();
			day_label[i].setText( day[i] );
			day_label[i].setFont( day_label[i].getFont().deriveFont( Settings.HEADER_FONT_SIZE ));
			day_label[i].setBackground( Settings.BACKGROUND_COLOR );
			
			
			layout_format = new String("cell "+ (i+1+num_v_sep) + " " + (0) + ", center, wrap");
			add( day_label[i], layout_format );
			
			for(int j=0; j<rows; j++) {
				
				begin_time_field[j][i] = new JTextField();
				begin_time_field[j][i].setColumns( 5 );
				begin_time_field[j][i].setFont( begin_time_field[j][i].getFont().deriveFont(Settings.FONT_SIZE) );
				begin_time_field[j][i].setBackground( Settings.BACKGROUND_COLOR );
					begin_doc[j][i] = (AbstractDocument) begin_time_field[j][i].getDocument();
					begin_tdf[j][i] = new TimeDocFilter( begin_time_field[j][i] );
					begin_doc[j][i].setDocumentFilter( begin_tdf[j][i] );
				begin_time_field[j][i].addKeyListener( new TimeKeyListener( begin_tdf[j][i] ) );
				
				
				end_time_field[j][i] = new JTextField();
				end_time_field[j][i].setColumns( 5 );
				end_time_field[j][i].setFont( end_time_field[j][i].getFont().deriveFont(Settings.FONT_SIZE) );
				end_time_field[j][i].setBackground( Settings.BACKGROUND_COLOR );
					end_doc[j][i] = (AbstractDocument) end_time_field[j][i].getDocument();
					end_tdf[j][i] = new TimeDocFilter( end_time_field[j][i] );
					end_doc[j][i].setDocumentFilter( end_tdf[j][i] );
				end_time_field[j][i].addKeyListener( new TimeKeyListener( end_tdf[j][i] ) );
				
				layout_format = new String("cell " + (i+1+num_v_sep) + " " + (j+1) + ", gapx 10, split 2" );
				add(begin_time_field[j][i], layout_format);
				add(end_time_field[j][i], layout_format);
								
			}
			
			amount_earned_field[i] = new JTextField();
			amount_earned_field[i].setFont( amount_earned_field[i].getFont().deriveFont(Settings.FONT_SIZE) );
			amount_earned_field[i].setColumns(7);
			AbstractDocument amount_earned_doc = (AbstractDocument) amount_earned_field[i].getDocument();
			amount_earned_doc.setDocumentFilter( new HousePayDocFilter() );
			
			layout_format = new String("cell " + (i+1+num_v_sep) + " " + (rows+1) + ", center" );
			add(amount_earned_field[i], layout_format);			
			
			try {
				FileInputStream inp = new FileInputStream(COVENANT_EARNED_SAVE_FILE);
				Scanner scanner = new Scanner(inp);
				
				String amount = "";
				
				for (int m=0; m<i+1; m++) {
					amount = scanner.nextLine();
				}
				
				amount_earned_field[i].setText( amount );
				
				scanner.close();
				inp.close();
			}
			catch (Exception e1) {
				
			}
			
			
			if ( i < day.length - 1) {	
				add( new JSeparator(SwingConstants.VERTICAL), "cell " + (i+2+num_v_sep) + " " + 1 + ", span 1 " + (rows+1) + ", growy, gapx 10" );
				num_v_sep++;
			}
			
			
		}
		
		
		/*
		other_label = new JLabel();
		other_label.setText( "Other $$" );
		other_label.setFont( other_label.getFont().deriveFont( Settings.HEADER_FONT_SIZE ));
		other_label.setBackground( Settings.BACKGROUND_COLOR );
		
		layout_format = new String("cell "+ (5+1+num_v_sep) + " " + (0) + ", center, wrap");
		add( other_label, layout_format );
		
		other_field = new JTextField[ name_label.length ];
		AbstractDocument[] other_doc = new AbstractDocument[ name_label.length ];
		HousePayDocFilter[] filter = new HousePayDocFilter[name_label.length ];
		for (int j=0; j<name_label.length; j++) {
			
			other_field[j] = new JTextField();
			other_field[j].setColumns( 5 );
			other_field[j].setFont( other_field[j].getFont().deriveFont(Settings.FONT_SIZE) );
			other_field[j].setBackground( Settings.BACKGROUND_COLOR );
				other_doc[j] = (AbstractDocument) other_field[j].getDocument();
				filter[j] = new HousePayDocFilter();
				other_doc[j].setDocumentFilter( filter[j] );
			
			layout_format = new String("cell " + (5+1+num_v_sep) + " " + (j+1) + ", gapx 10" );
			add(other_field[j], layout_format);
		}
		*/
		
		addFlexibleFocusListeners();
		
		
		JPanel panel = buttonPanel();
		layout_format = new String("cell 3 " + (rows+2) + ", span " + (4 + num_v_sep) + ", growx" );
		add(panel, layout_format);
		
	}
	
	
	
	// CONSTRUCTION METHODS

	private JPanel buttonPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("align right", "", "") );
		panel.setBackground( Settings.BACKGROUND_COLOR );
		
		edit_button = new JButton();
		edit_button.setText("Edit Workers");
		edit_button.setFont( edit_button.getFont().deriveFont(Settings.FONT_SIZE));
		edit_button.setBackground(Settings.MAIN_COLOR);
		edit_button.setForeground( Settings.FOREGROUND_COLOR );
		edit_button.addActionListener( new EditListener( this ) );
		
		/* TODO add cancel_button */
		
		submit_button = new JButton();
		submit_button.setText("Submit Week");
		submit_button.setFont( submit_button.getFont().deriveFont(Settings.FONT_SIZE));
		submit_button.setBackground(Settings.MAIN_COLOR);
		submit_button.setForeground( Settings.FOREGROUND_COLOR );
		submit_button.addActionListener( new SubmitListener() );
		
		panel.add(edit_button, "");
		panel.add(submit_button, "");
		
		return panel;
	}

	
	private void addFlexibleFocusListeners () {
		
		
		// adding time field focus listeners
		for (int i=0; i<rows; i++) {
			for (int j=0; j<day.length; j++) {
				
				Component up_begin = null;
				Component up_end = null;
				
				Component down_begin = null;
				Component down_end = null;
				
				Component right_begin = null;
				Component right_end = null;
				
				Component left_begin = null;
				Component left_end = null;
				
				Component enter_begin = null;
				Component enter_end = null;
				
				// up components
				if ( i>0 ) {
					up_begin = begin_time_field[i-1][j];
					up_end = end_time_field[i-1][j];
				}
				else {
					
				}
				
				// down components
				if ( i<rows-1 ) {
					down_begin = begin_time_field[i+1][j];
					down_end = end_time_field[i+1][j];
				}
				else {
					down_begin = amount_earned_field[j];
					down_end = amount_earned_field[j];
				}
				
				// left components
				left_end = begin_time_field[i][j];
				if ( j>0 ) {
					left_begin = end_time_field[i][j-1];
				}
				else {
					
				}
				
				// right components
				right_begin = end_time_field[i][j];
				if ( j<day.length-1 ) {
					right_end = begin_time_field[i][j+1];
				}
				else {
					
				}
				
				// enter components
				enter_begin = end_time_field[i][j];
				if ( i<rows-1 ) {
					enter_end = begin_time_field[i+1][j];
				}
				else {
					enter_end = amount_earned_field[j];
				}
				

				begin_time_field[i][j].addFocusListener( new FlexibleFocusListener(
						begin_time_field[i][j],
						FlexibleFocusListener.TEXTFIELD,
						left_begin, right_begin,
						up_begin, down_begin,
						enter_begin ) );
				
				end_time_field[i][j].addFocusListener( new FlexibleFocusListener(
						end_time_field[i][j],
						FlexibleFocusListener.TEXTFIELD,
						left_end, right_end,
						up_end, down_end,
						enter_end ) );
						
			}
		}
		
		
		// adding amount earned focus listeners
		for (int i=0; i<day.length; i++) {
			
			Component up = begin_time_field[rows-1][i];
			Component down = null;
			Component left = null;
			Component right = null;
			Component enter = null;
			
			if ( i>0 ) {
				left = amount_earned_field[i-1];
			}
			else {
				left = null;
			}
			if ( i<day.length-1 ) {
				right = amount_earned_field[i+1];
			}
			else{
				right = null;
			}
			if ( i<day.length-1 ) {
				enter = begin_time_field[0][i+1];
			}
			else {
				enter = null;
			}
			
			amount_earned_field[i].addFocusListener( new FlexibleFocusListener(
					amount_earned_field[i],
					FlexibleFocusListener.TEXTFIELD,
					left, right,
					up, down,
					enter ) );
			
		}
		
	}
	
	
	
	// LISTENERS
	
	private class EditListener implements ActionListener {
		
		// FIELD
		CovenantPanel cp;
		
		// CONSTRUCTOR
		public EditListener (CovenantPanel cp) {
			this.cp = cp;
		}
		
		public void actionPerformed ( ActionEvent e ) {
			
			JFrame f = new JFrame();
			f.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
			f.setResizable( false );
			
			f.addWindowListener( new FrameCloseListener(frame) );
			frame.setEnabled(false);
			
			EditWorkersPanel ewp = new EditWorkersPanel( f, cp );
			
			f.add( ewp );
			f.pack();
			
			StaticMethods.findSetLocation(f);
			
			
			f.setVisible( true );
			
		}
		
	}
	
	
	private class SubmitListener implements ActionListener {
		
		public void actionPerformed (ActionEvent e) {
			
			int a = StaticMethods.confirmSubmitWeek();
			if (a==0) {
				return;
			}
			
			// initializing variables
			
			// in case any names are blank
			String[] names = new String[ROWS];
			int count = 0;
			for (int i=0; i<ROWS; i++) {
				if (name_label[i] != null && !name_label[i].getText().equals("")) {
					names[count] = name_label[i].getText();
					count++;
				}
			}
			
			String[] worker = new String[count];
			for( int i=0; i<count; i++ ) {
				worker[i] = names[i];
			}
			String[] day_of_week = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Other"};
			
			
			// writing data to excel document
			try{
			File f = SubmitWeekListener.new_save;
			InputStream inp = new FileInputStream(f);
			XSSFWorkbook wb = new XSSFWorkbook(inp);
			Sheet sheet = wb.getSheet("COVENANT"); // = wb.getSheetAt(1);
			/*
			for (int l=0; l<wb.getNumberOfSheets(); l++) {
				if (wb.getSheetName(l).equals("COVENANT")) {
					sheet = wb.getSheet("COVENANT");
					System.out.println("got sheet");
				}
			}
			*/
			Row row;
			Cell cell;
			boolean day_complete;
			//boolean friday_complete;
			boolean missing_employee = false;
			String s1;
			String s2;
			
			// iterate through each worker on the Covenant panel
			for (int i=0; i<worker.length; i++) {
				
				//System.out.println("Worker " + i);
				
				row = sheet.getRow(0);
					
					// iterate through each day of the week
					for (int j=0; j<5; j++) {
						
						//System.out.println(day_of_week[j]);
						
						boolean day_match = false;
						while (day_match == false) {
							
							// making sure the row and columns are not null
							if ( row != null && row.getCell(0) != null ) {
																
								cell = row.getCell(0);
								
								//System.out.println("Compare to Day: " + row.getCell(0).getStringCellValue());
								
								// checking for the cell that matches the current day of week
								if ( cell.getStringCellValue().equals(day_of_week[j]) ) {
									
									day_match = true;
									
									//System.out.println("Day Match: True");
									
									// iterate through list of workers on excel sheet
									// to find current worker from Covenant panel
									day_complete = false;
									while ( day_complete == false ) {
										
										//System.out.println("Worker: " + worker[i]);
										
										// checking that the row and cell are not null
										if ( row != null && row.getCell(0) != null ) {
											
											//System.out.println("Match? " + row.getCell(0).getStringCellValue());
											
											// checking for the cell that matches the current worker
											if ( row.getCell(0).getStringCellValue().equals(worker[i]) ) {
											
												//System.out.println("Match!");
												
												s1 = convertFormat(begin_time_field[i][j], COVENANT_TIME);
												s2 = convertFormat(end_time_field[i][j], COVENANT_TIME);
													
												row.getCell(1).setCellValue( DateUtil.convertTime(s1) );
												row.getCell(2).setCellValue( DateUtil.convertTime(s2) );
													
												//System.out.println(row.getCell(1));
													
												day_complete = true;
												break;
											}
											// or if the next day comes first; the worker is missing from the excel sheet
											else if (j<4 && row.getCell(0).getStringCellValue().equals(day_of_week[j+1]) ) {
												
												String message = "Error: The selected employee " + worker[i] +
														" cannot be found on the Excel Document.\nYou will need " +
														"to enter the Employee's payroll data manually on the Excel Sheet.\n" +
														"Please correct the employee's name so that it matches the Excel Sheet.";
														
												JOptionPane.showMessageDialog( new JFrame(), message,
														null, JOptionPane.ERROR_MESSAGE);
												
												row = sheet.getRow( row.getRowNum() - 1 );
												missing_employee = true;
											}
											
										if (missing_employee == true) {
											break;
										}
									}
									row = sheet.getRow( row.getRowNum() + 1 );
										
										
								}
								row = sheet.getRow( row.getRowNum() + 1 );
										
							}
							row = sheet.getRow( row.getRowNum() + 1 );	
						}
						
						
					}
					if (missing_employee == true) {
						missing_employee = false;
						break;
					}
					//System.out.println("**Moving on to Next Day**");
						
				}
				//System.out.println("** Moving on to next Employee **");
				
				
			}
			
			// adding in amount earned
			sheet = wb.getSheet("PAYROLL");
			int counter = 0;
			int row_num = 0;
			
			// for each day
			for (int i=0; i<day.length; i++) {				
				
				while ( counter < 150 ) {
					
					row = sheet.getRow( row_num );
										
					if ( row != null &&
							 row.getCell(9) != null &&
							 row.getCell(9).getCellType() == Cell.CELL_TYPE_STRING &&
							 row.getCell(9).getStringCellValue().equalsIgnoreCase(day[i]) ) {
						
						row_num++;
						break;
						
					}
					
					row_num++;
					counter++;
				}
				
				
				counter = 0;
				while ( counter < 11 ) {
					
					row = sheet.getRow( row_num );
					
					if ( row != null &&
						 row.getCell(2) != null &&
						 row.getCell(2).getCellType() == Cell.CELL_TYPE_STRING &&
						 row.getCell(2).getStringCellValue().equals("COV SCHL") ) {
						
						if ( amount_earned_field[i].getText().length() > 0 ) {
							row.getCell(4).setCellValue( Double.parseDouble(amount_earned_field[i].getText()) );
						}
						else {
							row.getCell(4).setCellValue( 0 );
						}
						
						row_num++;
						counter = 0;
						break;
						
					}
					else {
						counter++;
						row_num++;
					}
					
				}
			
			}
			
			// putting in total covenant earned on Covenant tab
			sheet = wb.getSheet("COVENANT");
			counter = 0;
			row_num = 0;
			while ( counter < 150 ) {
				
				row = sheet.getRow( row_num );
				double money = 0;
				
				if ( row != null &&
					 row.getCell(1) != null &&
					 row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING &&
					 row.getCell(1).getStringCellValue().equals("KATHY INCOME") ) {
					
					for (int i=0; i<day.length; i++) {
						
						if ( amount_earned_field[i].getText().length() > 0 ) {
							money = money + Double.parseDouble( amount_earned_field[i].getText() );
						}
						else {
							
						}
						
					}
					
					row.getCell(4).setCellValue( money );
					
					row_num++;
					break;
					
				}
				else {
					counter++;
					row_num++;
				}
				
			}
			
			
			
			XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
			
			
			OutputStream out = new FileOutputStream(f);
			wb.write( out );
			
			wb.close();
			inp.close();
			out.close();
			}
			catch (Exception e1) {
				String message = "There was an error in copying the Covenant Data onto the Excel Document.\n" +
									"You will need to enter the data manually into the Excel Sheet.";
				JOptionPane.showMessageDialog( new JFrame(), message, null, JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
			
			
			
			// saving chosen workers into CovenantWorkerSaveFile
			try {

				FileWriter fw = new FileWriter( SettingsPanel.COV_WORKER_SAVE.getPath() ); //new File(System.getProperty("user.dir") + "\\save\\CovenantWorkerSaveFile") );
				BufferedWriter bw = new BufferedWriter( fw );
				
				for (int i=0; i<rows; i++) {
					
					bw.write( name_label[i].getText() );
					bw.newLine();
					
				}
				
				boolean match;
				if (dwd.default_workers != null) {
				for (int i=0; i<dwd.default_workers.length; i++) {
					
					match = false;
					
					for (int j=0; j<name_label.length; j++) {
					
						if ( dwd.default_workers[i].equals( name_label[j].getText() ) ) {
							match = true;
							break;
						}
						
					}
					
					if (match == false) {
						bw.write(dwd.default_workers[i]);
						bw.newLine();
					}
					
				}}
				
				bw.close();
				
			}
			catch(Exception e2) {
				e2.printStackTrace();
			}
			
			
			// Saving amount earned into save file
			try {

				FileWriter fw = new FileWriter( COVENANT_EARNED_SAVE_FILE );
				BufferedWriter bw = new BufferedWriter( fw );
				
				for (int i=0; i<day.length; i++) {
					
					if ( amount_earned_field[i].getText().length() > 0 ) {
						bw.write( amount_earned_field[i].getText() );
						bw.newLine();
					}
					else {
						bw.write( 0 );
						bw.newLine();
					}
				
				}
				
				bw.close();
				
			}
			catch(Exception e2) {
				e2.printStackTrace();
			}
			
			
			// creating new frame for next week panel and disposing of Covenant panel
			JFrame nwframe = new JFrame();
			nwframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			nwframe.setResizable(false);
			nwframe.addWindowListener( new MainWindowListener() );
			
			WeekendPanel wp = new WeekendPanel(nwframe,date,mode,wk);
			
			nwframe.add(wp);
			nwframe.pack();
			nwframe.setLocationRelativeTo( null );
			
			
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
			
			
			frame.setVisible( false );
			frame.dispose();
			
			nwframe.setVisible(true);
			
		}
		
	}
	
	
	private class TimeFocusListener implements FocusListener {

		// FIELDS
		JTextField tf;
		TempKeyListener tkl;
		
		public static final int BEGIN = 0;
		public static final int END = 1;
		
		/*
		// CONSTRUCTOR
		public TimeFocusListener ( JTextField tf, CovenantPanel cp, int row, int column, int type ) {
			this.tf = tf;
			tkl = new TempKeyListener(cp, row, column, type);
		}
		*/
		
		// LISTENERS
		@Override
		public void focusGained(FocusEvent e) {
			
			tf.addKeyListener( tkl );
			
		}

		@Override
		public void focusLost(FocusEvent e) {
			
			tf.removeKeyListener( tkl );
			
		}
		
		
	}

	
	private class TempKeyListener implements KeyListener {

		// FIELDS
		
		//CovenantPanel cp;
		JTextField[][] begin_time_field;
		JTextField[][] end_time_field;
		int row;
		int column;
		int type;
		
		/*
		// CONSTRUCTOR
		public TempKeyListener( CovenantPanel cp, int row, int column, int type ) {
			//this.cp = cp;
			this.begin_time_field = cp.begin_time_field;
			this.end_time_field = cp.end_time_field;
			this.row = row;
			this.column = column;
			this.type = type;
		}
		*/
		
		
		// LISTENERS
		@Override
		public void keyPressed(KeyEvent arg0) {

			if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				
				// if end time field and there is a row below
				if (type == TimeFocusListener.END && row + 1 < begin_time_field.length) {
					begin_time_field[row+1][column].requestFocus();
				}
				// if end time field and there is not a row below and  there is a column to the right
				else if (type == TimeFocusListener.END && row + 1 >= begin_time_field.length &&
						 column + 1 < begin_time_field[0].length) {
					begin_time_field[row][column+1].requestFocus();
				}
				// if begin time field
				else if (type == TimeFocusListener.BEGIN) {
					end_time_field[row][column].requestFocus();
				}
				else {
					// do nothing
				}
				
			}
			
			else if (arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
				
				// if begin time field
				if (type == TimeFocusListener.BEGIN) {
					end_time_field[row][column].requestFocus();
				}
				// if end time field and there is a column to right
				else if (type == TimeFocusListener.END  &&  column + 1 < begin_time_field[0].length) {
					begin_time_field[row][column+1].requestFocus();
				}
				
			}
			
			else if (arg0.getKeyCode() == KeyEvent.VK_LEFT) {
				
				// if begin time field and column to left
				if (type == TimeFocusListener.BEGIN && column > 0) {
					end_time_field[row][column-1].requestFocus();
				}
				// if end time field
				else if (type == TimeFocusListener.END ) {
					begin_time_field[row][column].requestFocus();
				}
				
			}
			
			else if (arg0.getKeyCode() == KeyEvent.VK_UP) {
				
				// if begin time field and row above
				if (type == TimeFocusListener.BEGIN && row > 0) {
					begin_time_field[row-1][column].requestFocus();
				}
				// if end time field and there is a row above
				else if (type == TimeFocusListener.END  &&  row > 0) {
					end_time_field[row-1][column].requestFocus();
				}
				
			}
			
			else if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
				
				// if begin time field and row above
				if (type == TimeFocusListener.BEGIN  &&  row + 1 < begin_time_field.length) {
					begin_time_field[row+1][column].requestFocus();
				}
				// if end time field and there is a row above
				else if (type == TimeFocusListener.END  &&  row + 1 < begin_time_field.length) {
					end_time_field[row+1][column].requestFocus();
				}
				
			}
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	// TODO: create a time class with this stuff (this was copied from housedata class)
	
	public static double getHours( String time_begin, String time_end ){
		
		double hours;

		int minutes = convertToMinutes( time_end ) - convertToMinutes( time_begin );
		hours = convertToHours( minutes );
		
		return hours;
	}
	
	
	public static int convertToMinutes( String time ){
		
		char[] temp_ch = time.toCharArray();
		char[] ch = new char[ temp_ch.length - 1 ];
		int minutes;
		
		// remove the ':'
		int shift = 0;
		for(int i=0; i<temp_ch.length; i++){
			Character k = temp_ch[i];
			if(!Character.isDigit(k)){
				shift++;
			}
			else{
				ch[i-shift]=temp_ch[i];
			}
		}
		
		// TODO: **ERROR occurs for work times beginning before 10am and ending after 10am**
		// converting from hhmm to minutes
		if ( ch.length == 4) {
			minutes = (Character.getNumericValue(ch[0]) * 600 + Character.getNumericValue(ch[1]) * 60 + Character.getNumericValue(ch[2]) * 10 + Character.getNumericValue(ch[3]) );
		}
		// TODO: I would like to make this time calculation work more generally (eg if she started covenant before 9am or 
		//        finished after 9pm)
		else if ( ch.length == 3) {
			if (Character.getNumericValue(ch[0]) <= 9) { 
				minutes =  (Character.getNumericValue(ch[0]) + 12) * 60 + Character.getNumericValue(ch[1]) * 10 + Character.getNumericValue(ch[2]);
			}
			else {
				minutes =  ((Character.getNumericValue(ch[0]) - 7)%12 + 7) * 60 + Character.getNumericValue(ch[1]) * 10 + Character.getNumericValue(ch[2]);
			}
		}
		else {
			minutes = 0;  // TODO throw some type of error message here??
		}
		
		return minutes;
	}
	
	
	public static double convertToHours( int minutes ) {
		
		return ((double) minutes) / 60;
		
	}
	
	
	
	public static String convertFormat (JTextField field, int type) {
		
		if ( field.getText() == null ) {
			return "00:00";
		}
		else if ( field.getText().isEmpty() ) {
			return "00:00";
		}
		else {
			
			
			int time_break = 7;
			if (type == HOUSE_TIME) {
				time_break = 7;
			}
			else if (type == COVENANT_TIME) {
				time_break = 9;
			}
			
			char[] temp_ch = field.getText().toCharArray();
			char[] ch = new char[ temp_ch.length - 1 ];
			//int minutes;
			
			// remove the ':'
			int shift = 0;
			for(int i=0; i<temp_ch.length; i++){
				Character k = temp_ch[i];
				if(!Character.isDigit(k)){
					shift++;
				}
				else{
					ch[i-shift]=temp_ch[i];
				}
			}
			
			// converting from 12:00 to 24:00
			if ( ch.length == 4) {
				return field.getText();
			}
			
			
			else if ( ch.length == 3) {
				
				// if time comes before the time_break: add 12 hours
				if (Character.getNumericValue(ch[0]) < time_break) { 
					
					int time = Integer.parseInt( String.valueOf(ch) );
					time = time + 1200;
					
					String s1 = String.valueOf( time );
					
					char[] c = new char[5];
					c[0] = s1.charAt(0);
					c[1] = s1.charAt(1);
					c[2] = ':';
					c[3] = s1.charAt(2);
					c[4] = s1.charAt(3);
					
					return String.valueOf(c);
					
				}
				// if time comes after the time break: do nothing
				else {
					return field.getText();
				}
			}
			else {
				JOptionPane.showMessageDialog(new JFrame(), "Error: Time conversion error.", null, JOptionPane.ERROR_MESSAGE);
				return "00:00";
			}			
			
			
		}
		
	}
	
	public static String convertFormat (String s, int type) {
		
		if ( s == null ) {
			return "00:00";
		}
		else if ( s.isEmpty() ) {
			return "00:00";
		}
		else {
			
			int time_break = 7;
			if (type == HOUSE_TIME) {
				time_break = 7;
			}
			else if (type == COVENANT_TIME) {
				time_break = 9;
			}
			
			char[] temp_ch = s.toCharArray();
			char[] ch = new char[ temp_ch.length - 1 ];
			//int minutes;
			
			// remove the ':'
			int shift = 0;
			for(int i=0; i<temp_ch.length; i++){
				Character k = temp_ch[i];
				if(!Character.isDigit(k)){
					shift++;
				}
				else{
					ch[i-shift]=temp_ch[i];
				}
			}
			
			// converting from 12:00 to 24:00
			if ( ch.length == 4) {
				return s;
			}
			
			
			else if ( ch.length == 3) {
				
				// if time comes before the time_break: add 12 hours
				if (Character.getNumericValue(ch[0]) < time_break) { 
					
					int time = Integer.parseInt( String.valueOf(ch) );
					time = time + 1200;
					
					String s1 = String.valueOf( time );
					
					char[] c = new char[5];
					c[0] = s1.charAt(0);
					c[1] = s1.charAt(1);
					c[2] = ':';
					c[3] = s1.charAt(2);
					c[4] = s1.charAt(3);
					
					return String.valueOf(c);
					
				}
				// if time comes after the time break: do nothing
				else {
					return s;
				}
			}
			else {
				JOptionPane.showMessageDialog(new JFrame(), "Error: Time conversion error.", null, JOptionPane.ERROR_MESSAGE);
				return "00:00";
			}
		}
	}

}
