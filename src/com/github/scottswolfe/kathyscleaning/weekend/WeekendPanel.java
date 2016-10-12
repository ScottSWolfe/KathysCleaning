package com.github.scottswolfe.kathyscleaning.weekend;

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener;
import com.github.scottswolfe.kathyscleaning.general.controller.StaticMethods;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.menu.view.ChooseWeekPanel;
import com.github.scottswolfe.kathyscleaning.nextweek.controller.NW_TabChangeListener;
import com.github.scottswolfe.kathyscleaning.nextweek.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.submit.controller.SubmitWeekListener;

import net.miginfocom.swing.MigLayout;

/**
 * Panel in which user can enter other cleaning jobs.
 * 
 */
@SuppressWarnings("serial")
public class WeekendPanel extends JPanel {
	

	// FIELDS
	
	JFrame frame;
	
	static final int NUM_JOB_PANELS = 3;
	JobPanel[] jp;
	
	int mode;
	int wk;
	
	public static final int NUM_JOBS_CAP = 4;
	
	
	// COMPONENTS
	JLabel heading_label;
	
	JLabel date_label;
	Calendar date;
	
	public JRadioButton weekA_button;
	public JRadioButton weekB_button;
	public JRadioButton neither_button;
	ButtonGroup bg;
	
	JButton submit_button;
	
	
	
	
	
	// CONSTRUCTORS
	public WeekendPanel (JFrame frame, Calendar date, int mode, int wk) {
		
		this.frame = frame;
		this.date = date;
		this.mode = mode;
		this.wk = wk;
		
		while(date.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
			date.add(Calendar.DATE, -1);
		}
		
		
		
		setLayout( new MigLayout() );
		setBackground(Settings.BACKGROUND_COLOR);
		
		add( createHeaderPanel(), "grow, wrap 0" );
		add( createJobsWorkedPanel(), "grow" );
		
	}
	
	
	
	// CONSTRUCTION METHODS
	
	private JPanel createHeaderPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("fill") );
		panel.setBackground( Settings.BACKGROUND_COLOR );
		//panel.setBorder( new LineBorder(null,1) );
		
		heading_label = new JLabel();
		heading_label.setText( "Weekend Work" );
		heading_label.setFont( heading_label.getFont().deriveFont(Settings.HEADER_FONT_SIZE) );
		heading_label.setBackground( Settings.BACKGROUND_COLOR );
		
		date_label = new JLabel();
		String s = new String( "Week of " +
				( Integer.parseInt(String.valueOf(date.get(Calendar.MONTH)))+1 ) +
				"/" + date.get(Calendar.DATE) + "/" + date.get(Calendar.YEAR) );
		date_label.setText(s);
		date_label.setFont(date_label.getFont().deriveFont( Settings.FONT_SIZE ));
		date_label.setBackground( Settings.BACKGROUND_COLOR );
		
		weekA_button = new JRadioButton("Week A");
		weekA_button.setFont( weekA_button.getFont().deriveFont( Settings.FONT_SIZE ) );
		weekA_button.setBackground( Settings.BACKGROUND_COLOR );
		weekA_button.addActionListener( new WeekListener( ) );

		weekB_button = new JRadioButton("Week B");
		weekB_button.setFont( weekB_button.getFont().deriveFont( Settings.FONT_SIZE ) );
		weekB_button.setBackground( Settings.BACKGROUND_COLOR );
		weekB_button.addActionListener( new WeekListener( ) );

		neither_button = new JRadioButton("Neither");
		neither_button.setFont( neither_button.getFont().deriveFont( Settings.FONT_SIZE ) );
		neither_button.setBackground( Settings.BACKGROUND_COLOR );
		neither_button.addActionListener( new NeitherListener() );
		
		bg = new ButtonGroup();
		bg.add(weekA_button);
		bg.add(weekB_button);
		bg.add(neither_button);
		
		submit_button = new JButton();
		submit_button.setText( "Submit" );
		submit_button.setFont( submit_button.getFont().deriveFont( Settings.FONT_SIZE ) );
		submit_button.setBackground(Settings.MAIN_COLOR);
		submit_button.setForeground( Settings.FOREGROUND_COLOR );
		submit_button.addActionListener( new SubmitListener() );
		
		
		panel.add( heading_label, "span, center, wrap");
		
		JPanel p = new JPanel();
		p.setLayout( new MigLayout("fill") );
		p.setBackground( Settings.HEADER_BACKGROUND );
		p.setBorder(new LineBorder(null,1));
		
		
		p.add(date_label);
		p.add( new JSeparator(SwingConstants.VERTICAL), "growy" );
		
		p.add(weekA_button, "");
		p.add(weekB_button, "");
		p.add(neither_button, "");
		
		p.add( new JSeparator(SwingConstants.VERTICAL), "growy" );
		
		p.add(submit_button, "");
		
		panel.add(p, "grow");
		return panel;
		
	}
	
	private JPanel createJobsWorkedPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout() );
		panel.setBackground( Settings.BACKGROUND_COLOR );
		
		jp = new JobPanel[NUM_JOB_PANELS];
		for (int i=0; i<NUM_JOB_PANELS; i++) {
			jp[i] = new JobPanel();
			panel.add(jp[i], "grow, wrap" );
		}
		
		addFlexibleFocusListeners( jp );
				
		return panel;
	}
	
	private class JobPanel extends JPanel {		
		
		// FIELDS		
		JCheckBox worked_checkbox;
		JComboBox<String> customer_combobox;
		JTextField jobpaid_field;
		JComboBox<String> employee_combobox;
		JTextField workerpaid_field;
		
		
		// CONSTRUCTOR
		private JobPanel() {
				
			setLayout( new MigLayout("fill", "[]20[]20[]20[]20[]") );
			setBackground( Settings.BACKGROUND_COLOR );
			//setBorder( new LineBorder(null,1));
			setBorder(BorderFactory.createTitledBorder( new String() ));

			
			worked_checkbox = new JCheckBox();
			worked_checkbox.setBackground( Settings.BACKGROUND_COLOR );
			worked_checkbox.setText("");
			
			customer_combobox = new JComboBox<String>();
			customer_combobox.setFont( customer_combobox.getFont().deriveFont(Settings.FONT_SIZE) );
			customer_combobox.setEditable(true);
			customer_combobox.addItem("");
			
			WorkerList weekendWorkers = new WorkerList(WorkerList.WEEKEND_WORKERS);
			for (String worker : weekendWorkers) {
				customer_combobox.addItem(worker);
			}
			
			jobpaid_field = new JTextField();
			jobpaid_field.setColumns( 5 );
			jobpaid_field.setFont( jobpaid_field.getFont().deriveFont(Settings.FONT_SIZE) );
			
			employee_combobox = new JComboBox<String>();
			employee_combobox.setFont( employee_combobox.getFont().deriveFont(Settings.FONT_SIZE) );
			employee_combobox.setEditable(true);
			employee_combobox.addItem("");
			WorkerList houseWorkers = new WorkerList(WorkerList.HOUSE_WORKERS);
			for (String worker : houseWorkers) {
				employee_combobox.addItem(worker);
			}
			
			workerpaid_field = new JTextField();
			workerpaid_field.setColumns( 5 );
			workerpaid_field.setFont( workerpaid_field.getFont().deriveFont(Settings.FONT_SIZE) );			

			JLabel worked_label = new JLabel();
			worked_label.setText("Worked?");
			worked_label.setFont(worked_label.getFont().deriveFont(Settings.FONT_SIZE));
			worked_label.setBackground(Settings.BACKGROUND_COLOR);
			
			JLabel customer_label = new JLabel();
			customer_label.setText("Customer");
			customer_label.setFont(customer_label.getFont().deriveFont(Settings.FONT_SIZE));
			customer_label.setBackground(Settings.BACKGROUND_COLOR);
			
			JLabel job_paid_label = new JLabel();
			job_paid_label.setText("$ Job");
			job_paid_label.setFont(job_paid_label.getFont().deriveFont(Settings.FONT_SIZE));
			job_paid_label.setBackground(Settings.BACKGROUND_COLOR);
			
			JLabel worker_label = new JLabel();
			worker_label.setText("Employee");
			worker_label.setFont(worker_label.getFont().deriveFont(Settings.FONT_SIZE));
			worker_label.setBackground(Settings.BACKGROUND_COLOR);
			
			JLabel worker_paid_label = new JLabel();
			worker_paid_label.setText("$ Paid");
			worker_paid_label.setFont(worker_paid_label.getFont().deriveFont(Settings.FONT_SIZE));
			worker_paid_label.setBackground(Settings.BACKGROUND_COLOR);
			
			add(worked_label, "");
			add(customer_label, "");
			add(job_paid_label, "");
			add(worker_label, "");
			add(worker_paid_label, "wrap");
			
			
			add(worked_checkbox, "center");
			add(customer_combobox, "");
			add(jobpaid_field, "");
			add(employee_combobox, "");
			add(workerpaid_field, "");
			
		}
		
	}
	
	
	// PRIVATE METHODS
	
	private void addFlexibleFocusListeners ( JobPanel[] jp ) {
		/*
		for ( int i=0; i<jp.length; i++ ) {
			
			Component worked_checkbox_up;
			Component worked_checkbox_down;
			
			Component customer_combobox_up;
			Component customer_combobox_down;
			
			Component jobpaid_field_up;
			Component jobpaid_field_down;
			
			Component employee_combobox_up;
			Component employee_combobox_down;
			
			Component workerpaid_field_up;
			Component workerpaid_field_down;
			
			if ( i>0 ) {
				worked_checkbox_up = jp[i-1].worked_checkbox;
				customer_combobox_up = jp[i-1].customer_combobox;
			}
			
			jp[i].worked_checkbox.addFocusListener( new FocusListener(jp[i].worked_checkbox,
					FlexibleFocusListener.CHECKBOX,
					null, jp[i].customer_combobox,
					up, down,
					enter) );
			
		}
		*/
	}
	
	
	
	// LISTENERS
 	private class WeekListener implements ActionListener {
		
		
		public void actionPerformed(ActionEvent e) {
			
			try {
			Scanner input;
			if (weekA_button.isSelected()) {
				input = new Scanner(Settings.WEEKEND_WEEK_A);
			}
			else  {
				input = new Scanner(Settings.WEEKEND_WEEK_B);
			}
			//else {
			//	
			//}
			
			String str = input.nextLine();
			for (int i=0; i<NUM_JOB_PANELS; i++) {
				
				if ( str.equals("true")  ) {		
					
					jp[i].worked_checkbox.setSelected(true);
					jp[i].customer_combobox.setSelectedItem( input.nextLine() );
					jp[i].jobpaid_field.setText( input.nextLine() );
					jp[i].employee_combobox.setSelectedItem( input.nextLine() );
					jp[i].workerpaid_field.setText( input.nextLine() );
					
					if (input.hasNextLine()) {
						str = input.nextLine();
					}
					
				}
				else if (str.equals("false")) {
					jp[i].worked_checkbox.setSelected(false);			input.nextLine();
					jp[i].customer_combobox.setSelectedItem( "" );		input.nextLine();
					jp[i].jobpaid_field.setText( "" );					input.nextLine();
					jp[i].employee_combobox.setSelectedItem( "" );		input.nextLine();
					jp[i].workerpaid_field.setText( "" );				
					
					if (input.hasNextLine()) {
						str = input.nextLine();
					}
					
				}
				else {
					
					while( input.hasNextLine() && !str.equals("true") ) {
						str = input.nextLine();
					}
				}

			}
			input.close();
			
			}
			catch(Exception e2) {
				e2.printStackTrace();
			}
			
		}
		
	}
	
	
	private class NeitherListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
		
			for (int i=0; i<jp.length; i++) {
				
				jp[i].worked_checkbox.setSelected(false);
				jp[i].customer_combobox.setSelectedItem("");
				jp[i].jobpaid_field.setText("");
				jp[i].employee_combobox.setSelectedItem("");
				jp[i].workerpaid_field.setText("");
				
			}
			
		}
		
	}

	
	private class SubmitListener implements ActionListener {
	
		public void actionPerformed(ActionEvent e) {
			
			int integer = StaticMethods.confirmSubmitWeek();
			if (integer == 0) {
				return;
			}
			
			if (mode == Settings.TRUE_MODE) {
			// reading data and writing to Excel Document
			try {
			File f = SubmitWeekListener.new_save;
			InputStream inp = new FileInputStream(f);
			XSSFWorkbook wb = new XSSFWorkbook(inp);
			Sheet sheet = wb.getSheet("PAYROLL");
			
			Row row;
			boolean found_row;
			boolean found_worker;
			int num_jobs_cap = NUM_JOBS_CAP;
			int unchecked = 0;
			int job_num;
			int repeat = 0;
			
			
			// for each job panel
			for (int i=0; i<NUM_JOB_PANELS; i++) {
				
				// if checkbox is checked
				if (jp[i].worked_checkbox.isSelected()) {
					
					// counting the unique job number, so the program knows 
					// whether to write to a new line on the excel doc or not
					job_num = i + 1 - unchecked - repeat;
					if (i>0) {
						
						for (int j=0; j<i; j++) {
							if ( String.valueOf(jp[i].customer_combobox.getSelectedItem()).equals(String.valueOf(jp[j].customer_combobox.getSelectedItem())) ) {
								job_num = j + 1;
								repeat++;
								break;
							}
							
						}
						
					}
					if ( job_num > num_jobs_cap ) {
						
						JOptionPane.showMessageDialog( new JFrame(), "Error: the number of weekend jobs you chose will not fit in the Excel Sheet.\nYou need to modify the Excel sheet if you want to include that many unique jobs.", null, JOptionPane.ERROR_MESSAGE);
						break;
					}
					
					
					row = sheet.getRow(0);
					
					// find correct row
					found_row = false;
					while(found_row == false) {
						
						if (row != null && row.getCell(9) != null && String.valueOf(row.getCell(9)).equals("WEEKEND WORK")) {
							found_row = true;
							row = sheet.getRow(row.getRowNum() + 1 + job_num);
							break;
						}
						row = sheet.getRow(row.getRowNum() + 1);
					}
					
					if ( !String.valueOf( jp[i].customer_combobox.getSelectedItem()).isEmpty() ) {
						row.getCell(3).setCellValue( String.valueOf( jp[i].customer_combobox.getSelectedItem() ));
					}
					if ( !jp[i].jobpaid_field.getText().isEmpty() ) {
						row.getCell(4).setCellValue( Integer.parseInt( jp[i].jobpaid_field.getText() ));
					}
					
					// if worker selected
					if ( String.valueOf(jp[i].employee_combobox.getSelectedItem()) != null &&
							!String.valueOf(jp[i].employee_combobox.getSelectedItem()).isEmpty() ) {
						
						// find worker
						row = sheet.getRow(row.getRowNum() - job_num);
						found_worker = false;
						int index = 5; // this is where names begin on the excel sheet
						while (found_worker == false) {
							
							// if cell matches worker's name
							if (row.getCell(index) != null &&
								String.valueOf(row.getCell(index)).equals(String.valueOf(jp[i].employee_combobox.getSelectedItem()))) {
								found_worker = true;
								break;
							}
							else if (row.getCell(index) != null &&
									String.valueOf(row.getCell(index)).equals("Kathy")) {
								
								String message = "Error: the selected employee " + String.valueOf(jp[i].employee_combobox.getSelectedItem()) +
										" is not on the Excel Sheet. Please modify the Excel sheet as needed.";
								JOptionPane.showMessageDialog(new JFrame(), message, null, JOptionPane.ERROR_MESSAGE);
								break;
							}
						
							index++;
						}
						
						// do stuff once worker is found
						if (found_worker == true) {
							row = sheet.getRow(row.getRowNum() + job_num);
							row.getCell(index).setCellValue( Integer.parseInt( jp[i].workerpaid_field.getText() ));
						}
						
					}
					
				}
				else {
					unchecked++;
				}
				
			}
			
			
			XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
			
			OutputStream out = new FileOutputStream(f);
			wb.write(out);
			wb.close();
			inp.close();
			
			}
			catch(Exception e1){
				JOptionPane.showMessageDialog(new JFrame(), "Error", null, JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
			
			
			
			// creating new frame for next week panel and disposing of Weekend panel
			JFrame nwframe = new JFrame();
			nwframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			nwframe.setResizable(false);
			nwframe.addWindowListener( new MainWindowListener() );
				
			ChooseWeekPanel cwp = new ChooseWeekPanel(nwframe, ChooseWeekPanel.NEXT_WEEK);
					
			nwframe.add(cwp);
			nwframe.pack();
			
			Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
			
			int difference = (int) effectiveScreenSize.getWidth() - nwframe.getWidth();
			int new_x = (int) difference/2;
			
			nwframe.setLocation( new Point(new_x , 20) );
				
			frame.setVisible( false );
			frame.dispose();
						
			// set 
			if ( wk == Settings.WEEK_A ) {
				cwp.week_B_rbutton.setSelected(true);
			}
			else if ( wk == Settings.WEEK_B ) {
				cwp.week_A_rbutton.setSelected(true);
			}
			else {
				// do nothing
			}
			
			
			
			
			nwframe.setVisible(true);
			
			} // end if true mode
			
			
			
			// else if settings mode
			else {
				
				
				
				File f;
				if (wk == Settings.WEEK_A) {
					f = Settings.WEEKEND_WEEK_A;
				}
				else {
					f = Settings.WEEKEND_WEEK_B;
				}
				
				BufferedWriter bw = null;
				try {
					
					FileWriter fw = new FileWriter( f );
					bw = new BufferedWriter( fw );
					
					for (int i=0; i<NUM_JOB_PANELS; i++) {
						
						if (jp[i].worked_checkbox.isSelected()) {
							bw.write("true");
						}
						else {
							bw.write("false");
						}
						bw.newLine();
						
						if (jp[i].customer_combobox != null) {
							bw.write(String.valueOf(jp[i].customer_combobox.getSelectedItem()));
						}
						else {
							bw.write("");
						}
						bw.newLine();
						
						bw.write(jp[i].jobpaid_field.getText());
						bw.newLine();
						
						if (jp[i].employee_combobox != null) {
							bw.write(String.valueOf(jp[i].employee_combobox.getSelectedItem()));
						}
						else {
							bw.write("");
						}
						bw.newLine();
						
						bw.write(jp[i].workerpaid_field.getText());
						bw.newLine();
						
					}
					
					bw.close();
					
				}
				catch(Exception e1) {
					e1.printStackTrace();
				}
				
				
				JFrame nwframe = new JFrame();
				nwframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				nwframe.setResizable(false);
				nwframe.addWindowListener( new MainWindowListener() );
				
				
				//Reading Default Worker Data from save file
				WorkerList dwd_house = null;
				try {
					dwd_house = new WorkerList( WorkerList.HOUSE_WORKERS);
				} catch (Exception e1) {
					e1.printStackTrace();
				}				
				
				TabbedPane tp = new TabbedPane();
				tp.setFont( tp.getFont().deriveFont(Settings.TAB_FONT_SIZE) );
				tp.setBackground( Settings.BACKGROUND_COLOR );
				
				
				Calendar date = Calendar.getInstance();
				while (date.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
					date.add(Calendar.DAY_OF_MONTH, 1);
				}
				Calendar[] day = new Calendar[5];
				for(int i=0; i<day.length; i++) {	
					day[i] = (Calendar) date.clone();
					date.add(Calendar.DATE, 1);
				}
				
				
				NW_DayPanel[] day_panel = new NW_DayPanel[5];
				for(int i=0; i<5; i++){
					day_panel[i] = new NW_DayPanel(tp, dwd_house, day[i],
					        nwframe, Settings.EDIT_MODE, wk);
				}
				tp.nw_day_panel = day_panel;
				
				tp.addTab("Monday", day_panel[0]);
				tp.addTab("Tuesday", day_panel[1]);
				tp.addTab("Wednesday", day_panel[2]);
				tp.addTab("Thursday", day_panel[3]);
				tp.addTab("Friday", day_panel[4]);
				
				tp.changePreviousTab(0);
				tp.addChangeListener( new NW_TabChangeListener(tp, nwframe) );
				
				nwframe.setBackground(Settings.BACKGROUND_COLOR);
				
				nwframe.add(tp);
				nwframe.pack();
				
				Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
				
				int difference = (int) effectiveScreenSize.getWidth() - nwframe.getWidth();
				int new_x = (int) difference/2;
				
				nwframe.setLocation( new Point(new_x , 20) );
				
				frame.setVisible( false );
				frame.dispose();
				
				try {
					if ( wk == Settings.WEEK_A ) {
						NW_DayPanel.fillWeek(tp, nwframe, Settings.WEEK_B);
						for (int j=0; j<5; j++) {
							tp.nw_day_panel[j].header_panel.week_B.setSelected(true);
						}
					}
					else if ( wk == Settings.WEEK_B ) {
						NW_DayPanel.fillWeek(tp, nwframe, Settings.WEEK_A);
						for (int j=0; j<5; j++) {
							tp.nw_day_panel[j].header_panel.week_A.setSelected(true);
						}
					}
					else {
						for (int j=0; j<5; j++) {
							tp.nw_day_panel[j].header_panel.neither.setSelected(true);
						}
					}
				}
				catch (Exception e2) {
					// do nothing
				}
				
				
				for (int i=0; i<5; i++) {
					day_panel[i].header_panel.week_A.setEnabled(false);
					day_panel[i].header_panel.week_B.setEnabled(false);
					day_panel[i].header_panel.neither.setEnabled(false);
				}
				
				nwframe.setVisible(true);				
											
			}
			
			
		}
		
	}
	
	
}
