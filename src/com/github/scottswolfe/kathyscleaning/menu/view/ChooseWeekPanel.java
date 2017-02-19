package com.github.scottswolfe.kathyscleaning.menu.view;

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.github.scottswolfe.kathyscleaning.completed.controller.CompletedController;
import com.github.scottswolfe.kathyscleaning.completed.controller.TabChangeListener;
import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.completed.view.ExceptionPanel;
import com.github.scottswolfe.kathyscleaning.completed.view.HousePanel;
import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.DefaultWorkerPanel;
import com.github.scottswolfe.kathyscleaning.general.view.MenuFrame;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.persistance.Savable;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_TabChangeListener;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.ScheduledController;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_ExceptionPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_NotePanel;

import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
public class ChooseWeekPanel extends JPanel {

	
	// FIELDS
	
	private JFrame menu_frame;
	private JFrame choose_week_frame;
	
	private Calendar date;
	
	private int week;
	public final static int PREVIOUS_WEEK =  1;
	public final static int NEXT_WEEK = 2;
	
	int wk;  // weekA or weekB
	
	
	// COMPONENT FIELDS
	
	JLabel choose_date_label;
	JLabel month_label;
	JLabel day_label;
	JLabel year_label;
	JComboBox<String> month_box;
	JComboBox<String> day_box;
	JComboBox<String> year_box;
	
	JLabel choose_week_label;
	ButtonGroup button_group;
	public JRadioButton week_A_rbutton;
	public JRadioButton week_B_rbutton;
	JRadioButton neither_rbutton;
	
	JLabel choose_autofill_label;
	ButtonGroup autofill_button_group;
	JRadioButton autofill_rbutton;
	JRadioButton default_rbutton;
	
	JButton cancel_button;
	JButton submit_button;
	
	
	
	// CONSRUCTOR
	
	public ChooseWeekPanel( JFrame menu_frame, JFrame choose_week_frame, int week, int wk ) {
		
		this.menu_frame = menu_frame;
		this.choose_week_frame = choose_week_frame;
		this.week = week;
		this.wk = wk;
		
		setLayout( new MigLayout("insets 0") );
		setBackground( Settings.BACKGROUND_COLOR );
		//setBorder( new LineBorder(null,1));
		
		add( ChooseDatePanel(), "wrap 20, grow" );
		//add( new JSeparator(SwingConstants.HORIZONTAL), "growx, wrap" );
		add( SelectWeekPanel(), "wrap 20" );
		add( SelectAutoFillPanel(), "grow, wrap 30");
		add( ContinuePanel(), "grow" );
		
	}
	
	public ChooseWeekPanel( JFrame choose_week_frame, int week ) {
		
		
		this.choose_week_frame = choose_week_frame;
		this.week = week;
		
		setLayout( new MigLayout("insets 0") );
		setBackground( Settings.BACKGROUND_COLOR );
		add( ChooseDatePanel(), "wrap 20, grow" );
		//add( new JSeparator(SwingConstants.HORIZONTAL), "growx, wrap" );
		add( SelectWeekPanel(), "wrap 30" );
		add( ContinuePanel(), "grow" );
		
	}
	
	
	// PRIVATE CONSTRUCTION METHODS
	
	private JPanel ChooseDatePanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("fill", "[]15[][][]", "") );
		panel.setBackground( Settings.BACKGROUND_COLOR );
		
		choose_date_label = new JLabel();
		choose_date_label.setText( "Monday's Date: " );
		choose_date_label.setFont( choose_date_label.getFont().deriveFont(Settings.FONT_SIZE) );
		choose_date_label.setBackground( Settings.BACKGROUND_COLOR );
		
		month_label = new JLabel();
		month_label.setText( "Month" );
		month_label.setFont( month_label.getFont().deriveFont(Settings.FONT_SIZE) );
		month_label.setBackground( Settings.BACKGROUND_COLOR );
		
		day_label = new JLabel();
		day_label.setText( "Day" );
		day_label.setFont( day_label.getFont().deriveFont(Settings.FONT_SIZE) );
		day_label.setBackground( Settings.BACKGROUND_COLOR );
		
		year_label = new JLabel();
		year_label.setText( "Year" );
		year_label.setFont( year_label.getFont().deriveFont(Settings.FONT_SIZE) );
		year_label.setBackground( Settings.BACKGROUND_COLOR );
		
		date = getStartDate(week);
		
		month_box = new JComboBox<String>();
		month_box.setFont( month_box.getFont().deriveFont(Settings.FONT_SIZE) );
		month_box.setBackground( Settings.BACKGROUND_COLOR );
		for (int i=1; i<13; i++){
			month_box.addItem( String.valueOf(i) );
		}
		month_box.setSelectedItem( String.valueOf(date.get(Calendar.MONTH)+1) );
		month_box.addActionListener( new ComboBoxListener() );
		
		day_box = new JComboBox<String>();
		day_box.setFont( day_box.getFont().deriveFont(Settings.FONT_SIZE) );
		day_box.setBackground( Settings.BACKGROUND_COLOR );
		Calendar temp_date = Calendar.getInstance();
		// beginning at first of month and adding all mondays
		temp_date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), 1);
		for (int i=1; i<date.getActualMaximum(Calendar.DATE)+1; i++) {
			if ( temp_date.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY ) {
				day_box.addItem( String.valueOf(i) );
			}
			temp_date.add(Calendar.DATE, 1);
		}
		day_box.setSelectedItem( String.valueOf(date.get(Calendar.DATE) ) );
		day_box.addActionListener( new ComboBoxListener() );
		
		year_box = new JComboBox<String>();
		year_box.setFont( year_box.getFont().deriveFont(Settings.FONT_SIZE) );
		year_box.setBackground( Settings.BACKGROUND_COLOR );
		for (int i=-1; i<2; i++){
			year_box.addItem( String.valueOf(Calendar.getInstance().get(Calendar.YEAR)+i) );
		}
		year_box.setSelectedItem( String.valueOf(date.get(Calendar.YEAR) ) );
		year_box.addActionListener( new ComboBoxListener() );
		
		panel.add( month_label, "cell 1 0, center" );
		panel.add( day_label, "cell 2 0, center" );
		panel.add( year_label, "cell 3 0, center" );
		
		panel.add( choose_date_label, "cell 0 1, align left");
		panel.add( month_box, "cell 1 1, center");
		panel.add( day_box, "cell 2 1, center");
		panel.add( year_box, "cell 3 1, center");
				
		return panel;
	}
	
	private JPanel SelectWeekPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("", "[]15[]15[]15[]", "") );
		panel.setBackground( Settings.BACKGROUND_COLOR );

		choose_week_label = new JLabel();
		choose_week_label.setText( "Choose Week:" );
		choose_week_label.setFont( choose_week_label.getFont().deriveFont(Settings.FONT_SIZE) );
		choose_week_label.setBackground( Settings.BACKGROUND_COLOR );
		
		week_A_rbutton = new JRadioButton();
		week_A_rbutton.setText( "Week A" );
		week_A_rbutton.setFont( week_A_rbutton.getFont().deriveFont(Settings.FONT_SIZE) );
		week_A_rbutton.setBackground( Settings.BACKGROUND_COLOR );
		
		week_B_rbutton = new JRadioButton();
		week_B_rbutton.setText( "Week B" );
		week_B_rbutton.setFont( week_B_rbutton.getFont().deriveFont(Settings.FONT_SIZE) );
		week_B_rbutton.setBackground( Settings.BACKGROUND_COLOR );
		
		neither_rbutton = new JRadioButton();
		neither_rbutton.setText( "Neither" );
		neither_rbutton.setFont( neither_rbutton.getFont().deriveFont(Settings.FONT_SIZE) );
		neither_rbutton.setBackground( Settings.BACKGROUND_COLOR );
		
		button_group = new ButtonGroup();
		button_group.add( week_A_rbutton );
		button_group.add( week_B_rbutton );
		button_group.add( neither_rbutton );
		button_group.setSelected(neither_rbutton.getModel(), true);
		
		panel.add(choose_week_label);
		panel.add(week_A_rbutton);
		panel.add(week_B_rbutton);
		panel.add(neither_rbutton);
		
		return panel;
	}
	
	private JPanel ContinuePanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("align right") );
		panel.setBackground( Settings.HEADER_BACKGROUND );
		
		cancel_button = new JButton();
		cancel_button.setText( "Cancel" );
		cancel_button.setFont( cancel_button.getFont().deriveFont(Settings.FONT_SIZE) );
		cancel_button.setBackground(Settings.MAIN_COLOR);
		cancel_button.setForeground( Settings.FOREGROUND_COLOR );
		cancel_button.addActionListener( new CancelListener() );
		
		submit_button = new JButton();
		submit_button.setText( "Submit" );
		submit_button.setFont( submit_button.getFont().deriveFont(Settings.FONT_SIZE) );
		submit_button.setBackground(Settings.MAIN_COLOR);
		submit_button.setForeground( Settings.FOREGROUND_COLOR );
		submit_button.addActionListener( new SubmitListener(week_A_rbutton, week_B_rbutton) );
		
		panel.add(cancel_button);
		panel.add(submit_button);
		
		return panel;
		
	}
	
	private JPanel SelectAutoFillPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("fill") );
		panel.setBackground( Settings.BACKGROUND_COLOR );

		choose_autofill_label = new JLabel();
		choose_autofill_label.setText( "Autofill:" );
		choose_autofill_label.setFont( choose_autofill_label.getFont().deriveFont(Settings.FONT_SIZE) );
		choose_autofill_label.setBackground( Settings.BACKGROUND_COLOR );
		
		autofill_rbutton = new JRadioButton();
		autofill_rbutton.setText( "Saved Schedule" );
		autofill_rbutton.setFont( autofill_rbutton.getFont().deriveFont(Settings.FONT_SIZE) );
		autofill_rbutton.setBackground( Settings.BACKGROUND_COLOR );
		
		default_rbutton = new JRadioButton();
		default_rbutton.setText( "Default" );
		default_rbutton.setFont( default_rbutton.getFont().deriveFont(Settings.FONT_SIZE) );
		default_rbutton.setBackground( Settings.BACKGROUND_COLOR );
		
		autofill_button_group = new ButtonGroup();
		autofill_button_group.add( autofill_rbutton );
		autofill_button_group.add( default_rbutton );
		autofill_button_group.setSelected( autofill_rbutton.getModel(), true );
		
		JPanel p = new JPanel();
		p.setLayout( new MigLayout("") );
		p.setBackground( Settings.BACKGROUND_COLOR );
		p.add(autofill_rbutton, "");
		p.add(default_rbutton, "gap left 15");
		
		panel.add(choose_autofill_label);
		panel.add(p, "align right");
		
		return panel;
		
	}
	
	
	
	// PRIVATE METHODS
	
	private Calendar getStartDate( int week ) {
	// returns previous Monday's Date
		
		Calendar date = Calendar.getInstance();
		
		if ( week == PREVIOUS_WEEK ) {
			date.add(Calendar.DATE, -9); // -9 in case data is entered on Monday or Tuesday of following week
		}
		else if (week == NEXT_WEEK) {
			// do nothing
		}
		else {
			// do nothing
		}
		
		
		while (date.get(Calendar.DAY_OF_WEEK) != 2) {
			date.add(Calendar.DATE, 1);
		}
		
		return date;
	}
	
	private void updateDate() {
		
		date.set( 	Integer.parseInt(String.valueOf(year_box.getSelectedItem())),
					Integer.parseInt(String.valueOf(month_box.getSelectedItem()))-1,
					Integer.parseInt(String.valueOf(day_box.getSelectedItem())));
		
	}
	
	
	// PUBLIC METHODS
	
	
	
	// LISTENERS
	
	private class CancelListener implements ActionListener {
		
		public void actionPerformed( ActionEvent e ) {
			
			if ( week == PREVIOUS_WEEK) {

				choose_week_frame.setVisible( false );
				choose_week_frame.dispose();
				
			}
			
		}
		
	}
	
	private class SubmitListener implements ActionListener {
		
		JRadioButton week_A_rbutton;
		JRadioButton week_B_rbutton;
		
		
		public SubmitListener ( JRadioButton week_A_rbutton, JRadioButton week_B_rbutton) {
			this.week_A_rbutton = week_A_rbutton;
			this.week_B_rbutton = week_B_rbutton;
		}
		
		
		public void actionPerformed( ActionEvent e ) {
				
			//Reading Default Worker Data from save file
			WorkerList dwd_house = new WorkerList();
			try {
				dwd_house = new WorkerList(WorkerList.HOUSE_WORKERS);
			} catch (Exception e1) {
			    System.out.println("failed to read house worker save file");
				e1.printStackTrace();
			}
			
			TabbedPane tp = new TabbedPane();
			tp.setFont(tp.getFont().deriveFont(Settings.TAB_FONT_SIZE));
				        
			// creating array of dates
			Calendar[] day = new Calendar[5];
			Calendar temp_date = (Calendar) date.clone();
			for(int i=0; i<day.length; i++) {
				
				day[i] = Calendar.getInstance();
				day[i].set(temp_date.get(Calendar.YEAR), temp_date.get(Calendar.MONTH), temp_date.get(Calendar.DATE));
				temp_date.add(Calendar.DATE, 1);
				
			}
						
			
            // If DayPanels
			if( week == PREVIOUS_WEEK ) {
				
			    Controller controller = new CompletedController();
                controller.setView(tp);
			    tp.setController(controller);
			    
			    JFrame frame = createFrame((CompletedController)controller);
			    
				if ( week_A_rbutton.isSelected() ) {
					wk = Settings.WEEK_A;
				}
				else if ( week_B_rbutton.isSelected() ) {
					wk = Settings.WEEK_B;
				}
				else {
					wk = Settings.NEITHER;
				}
				
				DayPanel[] day_panel = new DayPanel[5];
				for(int i=0; i<5; i++){
					day_panel[i] = new DayPanel((CompletedController)controller,
					        tp, dwd_house, day[i],
					        frame, Settings.TRUE_MODE, wk);
				}
				tp.day_panel = day_panel;
				
				tp.addTab("Monday", day_panel[0]);
				tp.addTab("Tuesday", day_panel[1]);
				tp.addTab("Wednesday", day_panel[2]);
				tp.addTab("Thursday", day_panel[3]);
				tp.addTab("Friday", day_panel[4]);
				
				tp.changePreviousTab(0);
				tp.addChangeListener( new TabChangeListener( tp, frame ) );

				frame.setBackground(Settings.BACKGROUND_COLOR);
				
				frame.add(tp);
				frame.pack();
				
				Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
				int difference = (int) effectiveScreenSize.getWidth() - frame.getWidth();
				int new_x = (int) difference/2;
				
				frame.setLocation( new Point(new_x , 20) );
				
				// closing menu frames
				if( week == PREVIOUS_WEEK ) {
					menu_frame.setVisible( false );
					menu_frame.dispose();
				}
				choose_week_frame.setVisible( false );
				choose_week_frame.dispose();
				
				
				// fill in data
				
				// if autofill saved schedule is selected
				if ( autofill_rbutton.isSelected() ) {
					if ( week_A_rbutton.isSelected() ) {
						fillSavedSchedule( tp, frame, Settings.WEEK_A );
					}
					else if ( week_B_rbutton.isSelected() ) {
						fillSavedSchedule( tp, frame, Settings.WEEK_B );
					}
					else {
						fillSavedSchedule( tp, frame, Settings.NEITHER );
					}
				}
				// if autofill default data is selected
				else {
					if ( week_A_rbutton.isSelected() ) {
						fillWeek( tp, frame, Settings.WEEK_A );
					}
					else if ( week_B_rbutton.isSelected() ) {
						fillWeek( tp, frame, Settings.WEEK_B );
					}
					else {
						// do nothing
					}
				}
				
				frame.setVisible(true);
				
			}
			
			// else for next week panel
			else {
				
			    Controller controller = new ScheduledController();
			    controller.setView(tp);
                tp.setController(controller);
                                
                JFrame frame = createFrame((ScheduledController)controller);


				if ( week_A_rbutton.isSelected() ) {
					wk = Settings.WEEK_A;
				}
				else if ( week_B_rbutton.isSelected() ) {
					wk = Settings.WEEK_B;
				}
				else {
					wk = Settings.NEITHER;
				}
				
				NW_DayPanel[] day_panel = new NW_DayPanel[5];
				for(int i=0; i<5; i++){
					day_panel[i] = new NW_DayPanel(
					        (ScheduledController) controller, tp, dwd_house,
					        day[i], frame, Settings.TRUE_MODE, wk);
				}
				tp.nw_day_panel = day_panel;
				
				tp.addTab("Monday", day_panel[0]);
				tp.addTab("Tuesday", day_panel[1]);
				tp.addTab("Wednesday", day_panel[2]);
				tp.addTab("Thursday", day_panel[3]);
				tp.addTab("Friday", day_panel[4]);
				//tp.addTab("Covenant", );
				
				tp.changePreviousTab(0);
				tp.addChangeListener( new NW_TabChangeListener(tp, frame) );
				
				
				frame.setBackground(Settings.BACKGROUND_COLOR);
				
				frame.add(tp);
				frame.pack();
				
				Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
				
				int difference = (int) effectiveScreenSize.getWidth() - frame.getWidth();
				int new_x = (int) difference/2;
				
				frame.setLocation( new Point(new_x , 20) );
				
				// closing menu frames
				if( week == PREVIOUS_WEEK ) {
					menu_frame.setVisible( false );
					menu_frame.dispose();
				}
				
				// filling fields
				if ( week_A_rbutton.isSelected() ) {
					NW_DayPanel.fillWeek(tp, frame, Settings.WEEK_A);
				}
				else if ( week_B_rbutton.isSelected() ) {
					NW_DayPanel.fillWeek(tp, frame, Settings.WEEK_B);
				}
				else {
					// do nothing
				}
				
				choose_week_frame.setVisible( false );
				choose_week_frame.dispose();
				
				frame.setVisible(true);
			}
			
			
			
		}
		
	}
	
	public static void fillWeek ( TabbedPane tp, JFrame frame, int wk ) {
						
				File file;
				if (wk == Settings.WEEK_A) {
					file = Settings.SUBMIT_WEEK_A;
				}
				else {
					file = Settings.SUBMIT_WEEK_B;
				}
						
				Scanner input;
				Scanner counter;
				
				try {
					input = new Scanner(file);
					
					String s;
					
					// iterate through each day
					for (int d=0; d<5; d++) {
			
						// find current day
						counter = new Scanner(file);
						input = new Scanner(file);
						s = input.nextLine(); 
						
						while ( !s.equals("Day " + (d)) && input.hasNextLine() ) {
							s = input.nextLine();
							counter.nextLine();
						}
						//System.out.println("Day: " + s);
						
						// find number of houses for day
						String t = counter.nextLine();
						int num_houses = 0;
						while ( !t.equals("Day " + (d+1)) && counter.hasNextLine() ) {
							if ( t.equals("House " + num_houses) ) {
								num_houses++;
							}
							t = counter.nextLine();
						}
						counter.close();
						
						
						// loop through each house
						for (int h=0; h<num_houses; h++) {
							input.nextLine(); // burning "House h"
							
							HousePanel house = tp.day_panel[d].house_panel[h];
									
							// 1. setting name, pay, and times
							house.house_name_txt.setText(input.nextLine());
							/*house.pay_txt.setText(input.nextLine());*/ input.nextLine();
							house.time_begin_txt.setText(input.nextLine());
							house.time_end_txt.setText(input.nextLine());
							
							if (house.pay_txt.getText().equals("0.0")) {
								house.pay_txt.getDocument().remove(0, 3);
							}
									
							// 2. setting workers
							String line = input.nextLine();
							Scanner parser = new Scanner(line);
							parser.useDelimiter(" ");
									
							// a) unselecting any selected workers
							for(int l=0; l<DefaultWorkerPanel.NORM_ROWS; l++){
								for(int m=0; m<DefaultWorkerPanel.NORM_COLUMNS; m++){
									tp.day_panel[d].house_panel[h].worker_panel.workerCheckBoxes[l][m].setSelected(false);		
								}
							}
									
							// b) selecting saved workers
							while (parser.hasNext() ) {
								String worker = parser.next();
										
								for(int l=0; l<DefaultWorkerPanel.NORM_ROWS; l++){
									for(int m=0; m<DefaultWorkerPanel.NORM_COLUMNS; m++){
										//tp.day_panel[0].house_panel[i].worker_panel.worker[l][m].setSelected(false);
										if (worker.equals( tp.day_panel[d].house_panel[h].worker_panel.workerCheckBoxes[l][m].getText() ) ){
											tp.day_panel[d].house_panel[h].worker_panel.workerCheckBoxes[l][m].setSelected(true);
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
							
							// 4. making sure there is a correct number of house panels available to fill

							// if there are more empty house panels and there are more houses to fill in
							if ( h < (num_houses - 1) && (h+1) < tp.day_panel[d].house_panel.length ) {
								// do nothing
							}
							// if there are no more empty house panels and there are more houses to fill in
							else if ( h+1 >= tp.day_panel[d].house_panel.length && h < (num_houses - 1) ){
								ActionEvent event = new ActionEvent(tp.day_panel[d],0,"test");
								ActionListener[] al = tp.day_panel[d].house_panel[h].add_house.getActionListeners();
								al[0].actionPerformed( event );
								
							}
							// if there are more empty house panels and there are no more houses to fill in
							else if ( (h+1) < tp.day_panel[d].house_panel.length && h >= (num_houses - 1 ) ) {
								int numrepeat = tp.day_panel[d].house_panel.length-h-1;
								for (int k=h; k<numrepeat+h; k++) {
									ActionEvent event = new ActionEvent(tp.day_panel[d],0,"test");
									ActionListener[] al = tp.day_panel[d].house_panel[h+1].delete_house.getActionListeners();
									al[0].actionPerformed( event );
									
								}
							}
							// no empty house panels and there are no more houses to fill in
							else if (h+1 >= tp.day_panel[d].house_panel.length && h >= (num_houses - 1 )) {
								// do nothing
							}
							/*
							// 5. preparing for next loop (burning the "House h" line)
							if (input.hasNextLine()) {
								s = input.nextLine();
							}
							else {
								break;
							}
							*/
						}
						
						// done with day!
						
						
					}
					
					input.close();
										
					} catch (Exception e1) {
						e1.printStackTrace();
					}

				for (int j=0; j<5; j++) {
					tp.day_panel[j].header_panel.weekSelected = wk;
				}
				
				TabChangeListener.resize(tp, frame, 4, 0); // resizing from final tab (friday) to first tab (monday)
				
				
				//frame.revalidate();
				//frame.repaint();
		
		
	}
	
	public static void fillSavedSchedule ( TabbedPane tp, JFrame frame, int wk ) {
		
		File file = Settings.SAVED_SCHEDULE;
		
		Scanner input;
		Scanner counter;
		
		try {
			
			input = new Scanner(file);
			
			// iterate through each day
			for (int d=0; d<5; d++) {
				
				DayPanel dp = tp.day_panel[d];
				
				input = new Scanner(file);
				counter = new Scanner(file);
				
				String s = input.nextLine();
				counter.nextLine();
				
				// find current day
				while ( !s.equals("Day " + d) && input.hasNextLine() ) {
					s = input.nextLine();
					counter.nextLine();
				}
										
				// find out how many houses for current day
				String t = counter.nextLine();
				int num_houses = 0;
				while ( !t.equals("Day " + (d+1) ) && counter.hasNextLine() ) {
					if ( t.equals("House " + num_houses) ) {
						num_houses++;
					}
					t = counter.nextLine();
				}
				counter.close();
	
				
				// reading begin data and covenant data
				
				// skip meet_location_box and meet_time_field save items
				input.nextLine();
				input.nextLine();

				//BeginExceptionData[] bed = new BeginExceptionData[NW_ExceptionPanel.NUM_EXCEPTIONS];
				for (int i=0; i<NW_ExceptionPanel.NUM_EXCEPTIONS; i++) {
					
					//bed[i] = new BeginExceptionData();
					
					input.nextLine();  //bed[i].setName( input.nextLine() );
					input.nextLine();  //bed[i].setMeetLocation( input.nextLine() );
					input.nextLine();  //bed[i].setTime( input.nextLine() );
					input.nextLine();  //bed[i].setNote( input.nextLine() );
					
				}
				
				// late edit trying to make 1.1.4 work
				// possibly erroneous
				input.nextLine(); //consuming ??covenant workers?? line
				
				
				//dp.setBeginExceptionData(bed);
				
				// reading covenant workers
				//String line = input.nextLine();
				//Scanner parser1 = new Scanner(line);
				//parser1.useDelimiter(" ");
					
				/*		
				// unselecting any selected workers
				int rows = tp.day_panel[d].cov_panel.dwp.rows;
				int columns = tp.nw_day_panel[d].cov_panel.dwp.columns;
				for(int l=0; l<rows; l++){
					for(int m=0; m<columns; m++){
						tp.nw_day_panel[d].cov_panel.dwp.worker[l][m].setSelected(false);
					}
				}
				
				// selecting saved workers
				while (parser1.hasNext() ) {
					String worker = parser1.next();
					
					for(int l=0; l<rows; l++){
						for(int m=0; m<columns; m++){
							if (worker.equals( tp.nw_day_panel[d].cov_panel.dwp.worker[l][m].getText() ) ){
								tp.nw_day_panel[d].cov_panel.dwp.worker[l][m].setSelected(true);
								break;
							}
						}
					}	
				}
				parser1.close();
				*/
				
				// notes
				
				//tp.nw_day_panel[d].covenant_note_data = new NoteData( NW_ExceptionPanel.NUM_EXCEPTIONS );
				//tp.nw_day_panel[d].covenant_note_data.name_box_data = new String[NW_NotePanel.ROWS];
				//tp.nw_day_panel[d].covenant_note_data.note_field_data = new String[NW_NotePanel.ROWS];

				for (int i=0; i<NW_NotePanel.ROWS; i++) {
					
					input.nextLine(); //tp.nw_day_panel[d].covenant_note_data.name_box_data[i] = input.nextLine();
					input.nextLine(); //tp.nw_day_panel[d].covenant_note_data.note_field_data[i] = input.nextLine();
					
				}
				
							
				
				// iterate through houses
				for ( int h=0; h<num_houses; h++ ) {
					
					input.nextLine(); // burn "House i"
					
					tp.day_panel[d].house_panel[h].house_name_txt.setText( input.nextLine() );
										
					String line = input.nextLine();
					Scanner parser = new Scanner(line);
					parser.useDelimiter(" ");
								
						// unselecting any selected workers
						for(int l=0; l<DefaultWorkerPanel.NORM_ROWS; l++){
							for(int m=0; m<DefaultWorkerPanel.NORM_COLUMNS; m++){
								tp.day_panel[d].house_panel[h].worker_panel.workerCheckBoxes[l][m].setSelected(false);
							}
						}
						
						// selecting saved workers
						while (parser.hasNext() ) {
							String worker = parser.next();
							
							for(int l=0; l<DefaultWorkerPanel.NORM_ROWS; l++){
								for(int m=0; m<DefaultWorkerPanel.NORM_COLUMNS; m++){
									//tp.day_panel[0].house_panel[i].worker_panel.worker[l][m].setSelected(false);
									if (worker.equals( tp.day_panel[d].house_panel[h].worker_panel.workerCheckBoxes[l][m].getText() ) ){
										tp.day_panel[d].house_panel[h].worker_panel.workerCheckBoxes[l][m].setSelected(true);
										break;
									}
								}
							}
							
						}
						parser.close();
						
						// 4. making sure there is a correct number of house panels available to fill
						
						// if there are more empty house panels and there are more houses to fill in
						if ( h < (num_houses - 1) && (h+1) < dp.house_panel.length ) {
							// do nothing
						}
						// if there are no more empty house panels and there are more houses to fill in
						else if ( h+1 >= tp.day_panel[d].house_panel.length && h < (num_houses - 1) ){
							ActionEvent event = new ActionEvent(tp.day_panel[d],0,"test");
							ActionListener[] al = tp.day_panel[d].house_panel[h].add_house.getActionListeners();
							al[0].actionPerformed( event );
						}
						// if there are more empty house panels and there are no more houses to fill in
						else if ( (h+1) < tp.day_panel[d].house_panel.length && h >= (num_houses - 1 ) ) {
							int numrepeat = tp.day_panel[d].house_panel.length-h-1;
							for (int k=h; k<numrepeat+h; k++) {
								ActionEvent event = new ActionEvent(tp.day_panel[d],0,"test");
								ActionListener[] al = tp.day_panel[d].house_panel[h+1].delete_house.getActionListeners();
								al[0].actionPerformed( event );
							}
						}
						// no empty house panels and there are no more houses to fill in
						else if (h+1 >= tp.day_panel[d].house_panel.length && h >= (num_houses - 1 )) {
							// do nothing
						}
						
							
								
				}

			}
			input.close();
			
					
			
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(new JFrame(), "Error: Could not read saved schedule file correctly", null, JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	
		
		TabChangeListener.resize(tp, frame, 4, 0); // resizing from final tab (friday) to first tab (monday)
		
		for (int j=0; j<5; j++) {
			tp.day_panel[j].header_panel.weekSelected = wk;
		}
		
		if ( wk == Settings.WEEK_A ) {
			for (int j=0; j<5; j++) {
				tp.day_panel[j].header_panel.week_A.setSelected(true);
			}
		}
		else if ( wk == Settings.WEEK_B ){
			for (int j=0; j<5; j++) {
				tp.day_panel[j].header_panel.week_B.setSelected(true);
			}
		}
		else {
			for (int j=0; j<5; j++) {
				tp.day_panel[j].header_panel.neither.setSelected(true);
			}
		}
		
	}
	
	
	private class ComboBoxListener implements ActionListener {
		
		public void actionPerformed( ActionEvent e ) {
			
			Calendar selected_date = Calendar.getInstance();
			
			if (month_box.getSelectedItem() != null) {
				selected_date.set(Calendar.MONTH, Integer.parseInt( String.valueOf( month_box.getSelectedItem() ) ) - 1 );
			}
			else {
				selected_date.set(Calendar.MONTH, date.get(Calendar.MONTH));
			}
			
			if (day_box.getSelectedItem() != null) {
				selected_date.set(Calendar.DATE, Integer.parseInt( String.valueOf( day_box.getSelectedItem() ) ) );
			}
			else {
				selected_date.set(Calendar.DATE, date.get(Calendar.DATE));
			}
			
			if (year_box.getSelectedItem() != null) {
				selected_date.set(Calendar.YEAR, Integer.parseInt( String.valueOf( year_box.getSelectedItem() ) ) );
			}
			else {
				selected_date.set(Calendar.YEAR, date.get(Calendar.YEAR));
			}
			
			day_box.removeAllItems();
						
			Calendar temp_date = Calendar.getInstance();
			temp_date.set(selected_date.get(Calendar.YEAR), selected_date.get(Calendar.MONTH), 1);

			for (int i=1; i<selected_date.getActualMaximum(Calendar.DATE) + 1; i++) {
				if ( temp_date.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY ) {
					day_box.addItem( String.valueOf(i) );
				}
				temp_date.add(Calendar.DATE, 1);
			}
			
			boolean b = true;
			for (int i=0; i<day_box.getItemCount(); i++) {
				if ( String.valueOf( selected_date.get(Calendar.DATE) ).equals( day_box.getItemAt(i) ) ){
					day_box.setSelectedItem( day_box.getItemAt(i));
					b = false;
					break;
				}
			}
			if ( b ) {
				day_box.setSelectedItem( day_box.getItemAt(0));
			}
			
			updateDate();
			
		}
		
	}
	
	
	private JFrame createFrame(Savable savable) {
	    JFrame frame = new MenuFrame(savable);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        //frame.addWindowListener(new MainWindowListener());
        return frame;
	}
	
}
