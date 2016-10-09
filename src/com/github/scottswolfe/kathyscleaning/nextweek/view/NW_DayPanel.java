package com.github.scottswolfe.kathyscleaning.nextweek.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.MatteBorder;
import javax.swing.text.AbstractDocument;

import com.github.scottswolfe.kathyscleaning.general.controller.FlexibleFocusListener;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeDocFilter;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeKeyListener;
import com.github.scottswolfe.kathyscleaning.general.model.DefaultWorkerData;
import com.github.scottswolfe.kathyscleaning.general.view.DefaultWorkerPanel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.menu.view.SettingsPanel;
import com.github.scottswolfe.kathyscleaning.nextweek.controller.NW_ExceptionListener;
import com.github.scottswolfe.kathyscleaning.nextweek.controller.NW_NoteListener;
import com.github.scottswolfe.kathyscleaning.nextweek.model.BeginExceptionData;
import com.github.scottswolfe.kathyscleaning.nextweek.model.NoteData;
import com.github.scottswolfe.kathyscleaning.submit.controller.TabChangeListener;
import com.github.scottswolfe.kathyscleaning.submit.model.DayData;
import com.github.scottswolfe.kathyscleaning.submit.view.DayPanel;

import net.miginfocom.swing.MigLayout;



public class NW_DayPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2486547027126980271L;
	
	
	//  FIELDS
	
	public NoteData covenant_note_data;
	NoteData day_note_data;
	
	DefaultWorkerData dwd;
	public BeginExceptionData[] bed;
	
	boolean exception_exist = false;
	
	int mode;
	int wk;
	
	
/*
	public final static int PANEL_PADDING = 12;
	public final static int TOP_INSET = 5;
	
	public final static float FONT_SIZE = 18;
	public final static float HEADER_FONT_SIZE = 22;
	public final static float TAB_FONT_SIZE = 18;
	
	public final static int NUM_HOUSE_PANELS = 3;
*/	
	
	TabbedPane tp;
	DayData day_data;
	Calendar date;
	JFrame frame;
	
	
	
	// COMPONENTS
	
	public NW_HeaderPanel header_panel;
	public NW_HousePanel[] house_panel;
	public NW_CovenantPanel cov_panel;
	public JPanel jsp_panel;
	public JScrollPane jsp;
	
	JPanel begin_panel;
	JLabel meet_location_label;
	public JComboBox<String> meet_location_box;
	JLabel meet_time_label;
	public JTextField meet_time_field;
	JButton exception_button;
	JButton note_button;
	
	
	
	// CONSTRUCTORS

	public NW_DayPanel( TabbedPane tp, DefaultWorkerData dwd, Calendar date, JFrame frame, int mode, int wk ) {
		
		this.dwd = dwd;
		this.date = date;
		this.frame = frame;
		this.mode = mode;
		this.wk = wk;
		this.tp = tp;
		
		//covenant_note_data = new NoteData( NW_NotePanel.ROWS );
		//day_note_data = new NoteData( NW_NotePanel.ROWS );
		
		setLayout( new MigLayout() ); //( new String("insets " + DayPanel.TOP_INSET + " 5 0 5"),"","") );
		setBackground(Settings.BACKGROUND_COLOR);
		

		header_panel = new NW_HeaderPanel(tp, dwd, this, date, frame, mode, wk);
		begin_panel = createBeginPanel();
		house_panel = new NW_HousePanel[DayPanel.NUM_HOUSE_PANELS];
		for(int i=0; i<DayPanel.NUM_HOUSE_PANELS; i++) {
			house_panel[i] = new NW_HousePanel(dwd,this,frame);
		}
		cov_panel = new NW_CovenantPanel( this, new DefaultWorkerData( DefaultWorkerData.COVENANT_WORKERS ), frame );
		
		
		// creating scroll pane and adding house panels
		jsp_panel = new JPanel();
		jsp_panel.setLayout( new MigLayout("fillx") );
		jsp_panel.setBackground( Settings.BACKGROUND_COLOR );
		
		for(int i=0; i<house_panel.length - 1; i++) {
			jsp_panel.add(house_panel[i], new String("wrap " + DayPanel.PANEL_PADDING + ", grow") );
		}
		jsp_panel.add(house_panel[house_panel.length-1], new String("wrap " + DayPanel.PANEL_PADDING + ", grow") );
		//jsp_panel.add(cov_panel, "dock south, growx");
		
		jsp = new JScrollPane(jsp_panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				   ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		jsp.setBackground( Settings.BACKGROUND_COLOR );
		
		//Border border = BorderFactory.createLineBorder(null,2);
				MatteBorder mborder2 = BorderFactory.createMatteBorder(0, 1, 0, 1, Color.BLACK);
				//CompoundBorder border2 = BorderFactory.createCompoundBorder(mborder, BorderFactory.createLoweredBevelBorder());
				
		jsp.setBorder( mborder2 );
		
		addFlexibleFocusListeners();
		
		// Adding Elements onto Panel
		add(header_panel, new String("dock north, grow") );
		add(begin_panel, new String("dock north, grow"));
		add(jsp, new String("grow") );
		add(cov_panel, "dock south, grow");
		
		
		//add(header_panel, new String("wrap " + DayPanel.PANEL_PADDING + ", growx, span 2") );
		//add(begin_panel, new String("cell 0 1 1 50, growy"));

		
		//add(footer_panel, new String( "wrap " + DayPanel.PANEL_PADDING + ", growx" ) );
	}
	
	
	public NW_DayPanel() {
		
	}
	
	
	
	// PRIVATE CONSTRUCTION METHOD
	
	protected JPanel createBeginPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout( "insets 10, fill" ) );
		panel.setBackground( Settings.BACKGROUND_COLOR );

		//Border border = BorderFactory.createLineBorder(null,2);
		MatteBorder mborder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);
		//CompoundBorder border2 = BorderFactory.createCompoundBorder(mborder, BorderFactory.createLoweredBevelBorder());
		
		panel.setBorder( mborder );
		
		JLabel header_label = new JLabel();
		header_label.setText( "Begin Information");
		header_label.setFont( header_label.getFont().deriveFont( Settings.HEADER_FONT_SIZE ));
		
		meet_location_label = new JLabel("Meet Location:");
		meet_location_label.setFont( meet_location_label.getFont().deriveFont(Settings.FONT_SIZE));
		meet_location_label.setForeground( Settings.MAIN_COLOR);
		
		meet_location_box = new JComboBox<String>();
		meet_location_box.setEditable(true);
		meet_location_box.setFont( meet_location_box.getFont().deriveFont( Settings.FONT_SIZE ));
		// TODO stuff about gathering in options
		
		meet_time_label = new JLabel("Meet Time:");
		meet_time_label.setFont( meet_time_label.getFont().deriveFont(Settings.FONT_SIZE));
		meet_time_label.setForeground(Settings.MAIN_COLOR);
		
		meet_time_field = new JTextField();
		meet_time_field.setFont( meet_time_field.getFont().deriveFont(Settings.FONT_SIZE));
		meet_time_field.setColumns( 7 );
			TimeDocFilter tdf = new TimeDocFilter( meet_time_field );
			AbstractDocument ad = (AbstractDocument) meet_time_field.getDocument();
			ad.setDocumentFilter( tdf );
		meet_time_field.addKeyListener( new TimeKeyListener( tdf ) );
		
		exception_button = new JButton();
		exception_button.setText("Exceptions");
		//exception_button.setBackground( Settings.MAIN_COLOR);
		exception_button.setFont( exception_button.getFont().deriveFont(Settings.FONT_SIZE));
		//exception_button.setForeground( Settings.FOREGROUND_COLOR );
		exception_button.addActionListener( new NW_ExceptionListener( this, dwd, frame ) );
		
		note_button = new JButton();
		note_button.setText( "Note");
		//note_button.setBackground( Settings.MAIN_COLOR);
		note_button.setFont( note_button.getFont().deriveFont(Settings.FONT_SIZE));
		//note_button.setForeground( Settings.FOREGROUND_COLOR );
		note_button.addActionListener( new NW_NoteListener( this, dwd, day_note_data, NW_NotePanel.DAY_NOTE, frame ) );
		
		// Adding Elements onto Panel
		
		panel.add(header_label, "");
		panel.add(meet_location_label,"gapx 0, align right");
		panel.add(meet_location_box,"gapx 0, align left");
		panel.add(meet_time_label,"gapx 0, align right");
		panel.add(meet_time_field,"gapx 0, align left");
		panel.add(exception_button,"gapx 0, align right");
		//panel.add( new JSeparator(SwingConstants.HORIZONTAL), "gapx 6, growy, wrap 20" );
		//panel.add(note_button, "gapx 6, growx");
		
		return panel;
	}
	
	
	
	
	//  PUBLIC METHODS	
	
	public void changeWorkerPanels( DefaultWorkerData new_dwd ){
		
		//getting old size
		int header_width = header_panel.getWidth();
		int house_panel_width = house_panel[0].getWidth();
		
		//copy and edit old panels
		tp.toString();
		NW_HeaderPanel new_header_panel = new NW_HeaderPanel(tp, new_dwd, this, date, frame, mode, wk);
		NW_HousePanel[] new_house_panel = new NW_HousePanel[ house_panel.length ];
		for(int i=0; i<house_panel.length; i++){
			//TODO String temp = new String("House " + (i+1) );
			new_house_panel[i] = house_panel[i].changeHouseWorkers(new_dwd);
		}
		
		//removing old panels
		remove( header_panel );
		remove(begin_panel);
		for(int i=0; i<house_panel.length; i++){
			jsp_panel.remove(house_panel[i]);
		}
		
		//changing field data
		this.header_panel = new_header_panel;
		this.house_panel = new_house_panel;
		
		// add new panels
		add(new_header_panel, "dock north" );
		add ( createBeginPanel(), "dock north" );
		for(int i=0; i<house_panel.length - 1; i++) {
			jsp_panel.add(new_house_panel[i], new String("wrap " + DayPanel.PANEL_PADDING + ", growx") );
		}
		jsp_panel.add(new_house_panel[house_panel.length-1], new String("wrap " + DayPanel.PANEL_PADDING + ", growx") );
		
		// revalidate and repaint
		frame.revalidate();
		frame.repaint();
		
		int new_header_width = header_panel.getWidth();
		int new_house_panel_width = house_panel[0].getWidth();
		
		int header_change = new_header_width - header_width;
		int house_panel_change = new_house_panel_width - house_panel_width;
		int change = 0;
		if(header_change > house_panel_change) {
			change = header_change;
		}
		else {
			change = house_panel_change;
		}
		 
		
		frame.setSize( frame.getWidth() + change , frame.getHeight() );
		
		frame.revalidate();
		frame.repaint();
		
	}
	
	
	public void changeCovenantWorkerPanel( DefaultWorkerData dwd ) {
		
		//DefaultWorkerData dwd_copy = cov_panel.dwd;
		//TODO: NoteData ndata_copy = cov_panel.ndata;
		remove( cov_panel );
		
		NW_CovenantPanel new_panel = new NW_CovenantPanel( this, dwd, frame );
		//TODO: new_panel.ndata = ndata_copy;
		
		add( new_panel, "dock south, growx" );
		cov_panel = new_panel;
		
		frame.revalidate();
		frame.repaint();
		
	}

	
	public boolean isException_exist() {
		return exception_exist;
	}

	
	public void setException_exist(boolean exception_exist) {
		this.exception_exist = exception_exist;
	}
	
	
	public String getMeetLocation() {
		if ( String.valueOf(meet_location_box.getSelectedItem()) != null &&
			 String.valueOf(meet_location_box.getSelectedItem()).length() > 0 ) {
			
			return String.valueOf(meet_location_box.getSelectedItem());
		}
		else {
			return "";
		}
	}
	
	
	public String getMeetTime() {
		if (meet_time_field.getText() != null) {
			return meet_time_field.getText();
		}
		else {
			return "";
		}
	}
	
	
	public void setBeginExceptionData( BeginExceptionData[] bed ) {
		this.bed = new BeginExceptionData[ bed.length ];
		for(int i=0; i<bed.length; i++){
			this.bed[i] = new BeginExceptionData();
			this.bed[i].setName( bed[i].getName() );
			this.bed[i].setTime( bed[i].getTime() );
			this.bed[i].setMeetLocation( bed[i].getMeetLocation() );
			this.bed[i].setNote( bed[i].getNote() );
		}
	}
	
	
	public String[] getExceptionNames() {
		
		String[] s;
		
		if ( !this.doesBedExist() ) {
			s = null;
			return s;
		}
		else {
			s = new String[bed.length];
			
			for (int i=0; i<bed.length; i++) {
				s[i] = bed[i].getName();
			}
			
			return s;
		}
	}
	
	
	public boolean doesBedExist() {
		
		if (bed == null) {
			return false;
		}
		else {
			return true;
		}
		
		/*
		boolean exist = false;
		for(int i=0; i<NW_ExceptionPanel.NUM_EXCEPTIONS; i++){
			if (bed[i].getName() == null){
				// do nothing
			}
			else {
				exist = true;
			}
		}
		return exist;
		*/
	}
	
	
	public int getNumBED() {
		
		if (bed == null) {
			return 0;
		}
		else {
			return bed.length;
		}
		
	}
	
	
	// this method returns the number of unique employees for a given day
	public int getNumberWorkers() {
		
		int num = 0;
		String[] workers = new String[50]; 		// 50 is arbitrary large number
		boolean match;
		int index;
		
		// for each house panel in the day
		for (int h=0; h<house_panel.length; h++) {
			
			String[] s = house_panel[h].getSelectedWorkers();
			
			// for each selected worker
			for (int w=0; w<s.length; w++) {
				
				match = false;
				index = 0;
				// compare to list of accumulating worker names
				for (int i=0; i<workers.length; i++) {
					
					// check that next worker exists and is not null
					if (workers[i] == null) {
						index = i;
						break;
					}
					
					// if it equals one of the names, break out of the loop, and go on to the next employee
					if ( s[w].equals(workers[i])) {
						match = true;
						break;
					}
					
				}
				
				// if no matches were found, then current worker is a new addition
				if ( match == false) {
					num++;
					workers[index] = s[w];
				}
				
			}
			
		}
		
		// for the covenant panel
		for(int i=0; i<cov_panel.dwp.rows; i++) {
			
			for (int j=0; j<cov_panel.dwp.columns; j++) {
				
				if (cov_panel.dwp.worker[i][j].isSelected()) {
					
					for (int k=0; k<workers.length; k++) {
						
						// check that next worker exists and is not null
						if (workers[k] == null) {
							workers[k] = cov_panel.dwp.worker[i][j].getText();
							num++;
							break;
						}
						
						// if it equals one of the names, break out of the loop, and go on to the next employee
						else if ( cov_panel.dwp.worker[i][j].getText().equals(workers[k]) ) {
							break;
						}
						
					}
					
				}
				
			}
			
		}
		
		return num;
	}
	
	
	// this method returns a list of the unique employees for a given day
	public String[] getWorkers() {
			
		String[] workers = new String[50]; 		// 50 is arbitrary large number
		boolean match;
		int index = 0;
		
		// for each house panel in the day
		for (int h=0; h<house_panel.length; h++) {
			
			String[] s = house_panel[h].getSelectedWorkers();
			
			// for each selected worker
			for (int w=0; w<s.length; w++) {
				
				match = false;
				
				// compare to list of accumulating worker names
				for (int i=0; i<workers.length; i++) {
					
					// check that next worker exists and is not null
					if (workers[i] == null) {
						index = i;
						break;
					}
					
					// if it equals one of the names, break out of the loop, and go on to the next employee
					if ( s[w].equals(workers[i])) {
						match = true;
						break;
					}
					
				}
				
				// if no matches were found, then current worker is a new addition
				if ( match == false) {
					workers[index] = s[w];
				}
				
			}
			
		}
		
		
		// for the covenant panel
		for(int i=0; i<cov_panel.dwp.rows; i++) {
			
			for (int j=0; j<cov_panel.dwp.columns; j++) {
				
				if (cov_panel.dwp.worker[i][j].isSelected()) {
					
					
					for (int k=0; k<workers.length; k++) {
						
						// check that next worker exists and is not null
						if (workers[k] == null) {
							workers[k] = cov_panel.dwp.worker[i][j].getText();
							index = k;
							break;
						}
						
						// if it equals one of the names, break out of the loop, and go on to the next employee
						else if ( cov_panel.dwp.worker[i][j].getText().equals(workers[k]) ) {
							break;
						}
						
					}
					
				}
				
			}
			
		}
		
		// adjust string to correct length
		if (workers[0] == null) {
			return null;
		}
		else {
			String[] s_adjusted = new String[index+1];
			for (int i=0; i<index+1; i++) {
				s_adjusted[i] = workers[i];
			}
			
			return s_adjusted;
		}
		
	}
	
	
	/*
	public Component getPanelComponents(int x, int y) {
		
		return this.getComponentAt(new Point(x,y) );
		
	}
	*/
	
	
	public NoteData getCovenant_note_data() {
		return covenant_note_data;
	}

	
	public void setCovenant_note_data(NoteData covenant_note_data) {
		this.covenant_note_data = covenant_note_data;
	}

	
	public NoteData getDay_note_data() {
		return day_note_data;
	}

	
	public void setDay_note_data(NoteData day_note_data) {
		/*
		System.out.println(this.day_note_data.toString());
		this.day_note_data.name_box_data = new String[day_note_data.name_box_data.length];
		this.day_note_data.note_field_data = new String[day_note_data.note_field_data.length];
		
		for(int i=0; i<day_note_data.name_box_data.length; i++){
			this.day_note_data.name_box_data[i] = new String();
			this.day_note_data.name_box_data[i] = day_note_data.name_box_data[i];
		}
		for(int i=0; i<day_note_data.note_field_data.length; i++){
			this.day_note_data.note_field_data[i] = new String();
			this.day_note_data.note_field_data[i] = day_note_data.note_field_data[i];
		}
		*/
		//this.day_note_data.name_box_data = day_note_data.name_box_data;
		//this.day_note_data.note_field_data = day_note_data.note_field_data;
		this.day_note_data = day_note_data;
	}

	
	public void addFlexibleFocusListeners(){
		

		// adding focus listeners for textfields and buttons
		for (int i=0; i<house_panel.length; i++) {
					
			NW_HousePanel hp = house_panel[i];
			
			NW_HousePanel hp_up;
			NW_HousePanel hp_down;
					
			if ( i > 0 ) {
				hp_up = house_panel[i-1];
			}
			else {
				hp_up = new NW_HousePanel();  // all null fields
			}
			if ( i < house_panel.length - 1 ) {
				hp_down = house_panel[i+1];
			}
			else {
				hp_down = new NW_HousePanel(); // all null fields
			}
			
			hp.house_name_txt.addFocusListener( new FlexibleFocusListener(hp.house_name_txt,
					FlexibleFocusListener.TEXTFIELD,
					null, hp.worker_panel.worker[0][0],
					hp_up.house_name_txt, hp_down.house_name_txt, 
					null) );
				
		}
		
		meet_location_box.getEditor().getEditorComponent().addFocusListener(new FlexibleFocusListener(meet_location_box, 
				FlexibleFocusListener.COMBOBOX,
				null, meet_time_field,
				null, null,
				null));
		
		meet_time_field.addFocusListener(new FlexibleFocusListener(meet_time_field, 
				FlexibleFocusListener.TEXTFIELD,
				meet_location_box, exception_button,
				null, null,
				null));
		
		exception_button.addFocusListener(new FlexibleFocusListener(exception_button, 
				FlexibleFocusListener.BUTTON,
				meet_time_field, null,
				null, null,
				null));

		
	}
	
	
	public static void fillWeek( TabbedPane tp, JFrame frame, int wk ) {
		
		File file;
		if (wk == SettingsPanel.WEEK_A) {
			file = SettingsPanel.NEXT_WEEK_A;
		}
		else {
			file = SettingsPanel.NEXT_WEEK_B;
		}
		
		
		Scanner input;
		Scanner counter;
		
		try {
			
			input = new Scanner(file);
			
			// iterate through each day
			for (int d=0; d<5; d++) {
				
				NW_DayPanel dp = tp.nw_day_panel[d];
				
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
				
				dp.meet_location_box.setSelectedItem( input.nextLine() );
				if (String.valueOf(dp.meet_location_box.getSelectedItem()).equals("null")) {
					dp.meet_location_box.setSelectedItem("");
				}
				
				dp.meet_time_field.setText( input.nextLine() );
				
	
				BeginExceptionData[] bed = new BeginExceptionData[NW_ExceptionPanel.NUM_EXCEPTIONS];
				for (int i=0; i<NW_ExceptionPanel.NUM_EXCEPTIONS; i++) {
					
					bed[i] = new BeginExceptionData();
					
					bed[i].setName( input.nextLine() );
					bed[i].setMeetLocation( input.nextLine() );
					bed[i].setTime( input.nextLine() );
					bed[i].setNote( input.nextLine() );
					
					if (bed[i].getName() != null && bed[i].getName().length() > 0) {
						dp.setException_exist(true);
					}
					
				}
				dp.setBeginExceptionData(bed);
				
				// reading covenant workers
				String line = input.nextLine();
				Scanner parser1 = new Scanner(line);
				parser1.useDelimiter(" ");
						
						
				// unselecting any selected workers
				int rows = tp.nw_day_panel[d].cov_panel.dwp.rows;
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
				
				// notes
				
				tp.nw_day_panel[d].covenant_note_data = new NoteData( NW_ExceptionPanel.NUM_EXCEPTIONS );
				tp.nw_day_panel[d].covenant_note_data.name_box_data = new String[NW_NotePanel.ROWS];
				tp.nw_day_panel[d].covenant_note_data.note_field_data = new String[NW_NotePanel.ROWS];
				
				for (int i=0; i<NW_NotePanel.ROWS; i++) {
					
					tp.nw_day_panel[d].covenant_note_data.name_box_data[i] = input.nextLine();
					tp.nw_day_panel[d].covenant_note_data.note_field_data[i] = input.nextLine();
					
				}
				
							
				
				// iterate through houses
				for ( int h=0; h<num_houses; h++ ) {
					input.nextLine(); // burn "House i"
					
	
					tp.nw_day_panel[d].house_panel[h].house_name_txt.setText( input.nextLine() );
										
					line = input.nextLine();
					Scanner parser = new Scanner(line);
					parser.useDelimiter(" ");
							
							
						// unselecting any selected workers
						for(int l=0; l<DefaultWorkerPanel.NORM_ROWS; l++){
							for(int m=0; m<DefaultWorkerPanel.NORM_COLUMNS; m++){
								tp.nw_day_panel[d].house_panel[h].worker_panel.worker[l][m].setSelected(false);
							}
						}
						
						// selecting saved workers
						while (parser.hasNext() ) {
							String worker = parser.next();
							
							for(int l=0; l<DefaultWorkerPanel.NORM_ROWS; l++){
								for(int m=0; m<DefaultWorkerPanel.NORM_COLUMNS; m++){
									//tp.day_panel[0].house_panel[i].worker_panel.worker[l][m].setSelected(false);
									if (worker.equals( tp.nw_day_panel[d].house_panel[h].worker_panel.worker[l][m].getText() ) ){
										tp.nw_day_panel[d].house_panel[h].worker_panel.worker[l][m].setSelected(true);
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
						else if ( h+1 >= tp.nw_day_panel[d].house_panel.length && h < (num_houses - 1) ){
							ActionEvent event = new ActionEvent(tp.nw_day_panel[d],0,"test");
							ActionListener[] al = tp.nw_day_panel[d].house_panel[h].add_house.getActionListeners();
							al[0].actionPerformed( event );
						}
						// if there are more empty house panels and there are no more houses to fill in
						else if ( (h+1) < tp.nw_day_panel[d].house_panel.length && h >= (num_houses - 1 ) ) {
							int numrepeat = tp.nw_day_panel[d].house_panel.length-h-1;
							for (int k=h; k<numrepeat+h; k++) {
								ActionEvent event = new ActionEvent(tp.nw_day_panel[d],0,"test");
								ActionListener[] al = tp.nw_day_panel[d].house_panel[h+1].delete_house.getActionListeners();
								al[0].actionPerformed( event );
							}
						}
						// no empty house panels and there are no more houses to fill in
						else if (h+1 >= tp.nw_day_panel[d].house_panel.length && h >= (num_houses - 1 )) {
							// do nothing
						}
						
							
								
				}

			}
			
			
			
			input.close();
			
					
			
		} catch (FileNotFoundException e1) {
			JOptionPane.showMessageDialog(new JFrame(), "Error: Could not read save file correctly", null, JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		}
	
		System.out.println("frame height before: " + frame.getHeight());
		TabChangeListener.nw_resize(tp, frame, 4, 0); // resizing from final tab (friday) to first tab (monday)
		System.out.println("frame height after: " + frame.getHeight());
		
		for (int j=0; j<5; j++) {
			tp.nw_day_panel[j].header_panel.weekSelected = wk;
		}
		
		if ( wk == SettingsPanel.WEEK_A ) {
			for (int j=0; j<5; j++) {
				tp.nw_day_panel[j].header_panel.week_A.setSelected(true);
			}
		}
		else {
			for (int j=0; j<5; j++) {
				tp.nw_day_panel[j].header_panel.week_B.setSelected(true);
			}
		}
		
		tp.nw_day_panel[tp.getSelectedIndex()].header_panel.weekSelected = wk;
		
	}
	

}


