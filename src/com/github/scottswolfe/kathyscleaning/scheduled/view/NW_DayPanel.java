package com.github.scottswolfe.kathyscleaning.scheduled.view;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.MatteBorder;
import javax.swing.text.AbstractDocument;

import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.general.controller.FlexibleFocusListener;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeDocFilter;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeKeyListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_ExceptionListener;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_NoteListener;
import com.github.scottswolfe.kathyscleaning.scheduled.model.BeginExceptionEntry;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NoteData;

import net.miginfocom.swing.MigLayout;



@SuppressWarnings("serial")
public class NW_DayPanel extends JPanel{	
	
	//  FIELDS
    GeneralController<TabbedPane, NW_Data> controller;
	
	public NoteData covenant_note_data;
	NoteData day_note_data;
	
	WorkerList workers;
	public List<BeginExceptionEntry> bed;
	
	boolean exception_exist = false;
	
	int mode;
	int wk;
		
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

	public NW_DayPanel(GeneralController<TabbedPane, NW_Data> controller, TabbedPane tp,
	        WorkerList dwd, Calendar date, JFrame frame, int mode, int wk ) {
		
	    this.controller = controller;
		this.workers = dwd;
		this.date = date;
		this.frame = frame;
		this.mode = mode;
		this.wk = wk;
		this.tp = tp;
		
		setLayout(new MigLayout());
		setBackground(Settings.BACKGROUND_COLOR);
		
		header_panel = new NW_HeaderPanel(controller, tp, dwd, this, date, frame, mode, wk);
		begin_panel = createBeginPanel();
		house_panel = new NW_HousePanel[DayPanel.NUM_HOUSE_PANELS];
		for(int i=0; i<DayPanel.NUM_HOUSE_PANELS; i++) {
			house_panel[i] = new NW_HousePanel(dwd,this,frame);
		}
		cov_panel = new NW_CovenantPanel( this, new WorkerList(), frame );
		
		
		// creating scroll pane and adding house panels
		jsp_panel = new JPanel();
		jsp_panel.setLayout( new MigLayout("fillx") );
		jsp_panel.setBackground( Settings.BACKGROUND_COLOR );
		
		for(int i=0; i<house_panel.length - 1; i++) {
			jsp_panel.add(house_panel[i], new String("wrap " + DayPanel.PANEL_PADDING + ", grow") );
		}
		jsp_panel.add(house_panel[house_panel.length-1], new String("wrap " + DayPanel.PANEL_PADDING + ", grow") );
		
		jsp = new JScrollPane(jsp_panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				   ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		jsp.setBackground( Settings.BACKGROUND_COLOR );
		
		MatteBorder mborder2 = BorderFactory.createMatteBorder(0, 1, 0, 1, Color.BLACK);
				
		jsp.setBorder( mborder2 );
		
		addFlexibleFocusListeners();
		
		// Adding Elements onto Panel
		add(header_panel, new String("dock north, grow") );
		add(begin_panel, new String("dock north, grow"));
		add(jsp, new String("grow") );
		add(cov_panel, "dock south, grow");		
	}
	
	
	public NW_DayPanel() {
		
	}
	
	
	
	// PRIVATE CONSTRUCTION METHOD
	
	protected JPanel createBeginPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout( "insets 10, fill" ) );
		panel.setBackground( Settings.BACKGROUND_COLOR );

		MatteBorder mborder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);
		
		panel.setBorder( mborder );
				
		meet_location_label = new JLabel("Meet Location:");
		meet_location_label.setFont( meet_location_label.getFont().deriveFont(Settings.FONT_SIZE));
		meet_location_label.setForeground( Settings.MAIN_COLOR);
		
		meet_location_box = new JComboBox<String>();
		meet_location_box.setEditable(true);
		meet_location_box.setFont( meet_location_box.getFont().deriveFont( Settings.FONT_SIZE ));
		
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
		exception_button.setFont( exception_button.getFont().deriveFont(Settings.FONT_SIZE));
		exception_button.addActionListener( new NW_ExceptionListener( this, workers, frame ) );
		
		note_button = new JButton();
		note_button.setText( "Note");
		note_button.setFont( note_button.getFont().deriveFont(Settings.FONT_SIZE));
		note_button.addActionListener( new NW_NoteListener( this, workers, day_note_data, NW_NotePanel.DAY_NOTE, frame ) );
				
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
	
	public void changeWorkerPanels( WorkerList new_dwd ){
		
        int header_width = header_panel.getWidth();
        int house_panel_width = house_panel[0].getWidth();

        
	    header_panel.dwp.setWorkers(new_dwd);
	    for (int i = 0; i < house_panel.length; i++) {
	        house_panel[i].changeHouseWorkers(new_dwd);
	    }		

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
	
	
	public void changeCovenantWorkerPanel( WorkerList dwd ) {
		
		remove( cov_panel );
		
		NW_CovenantPanel new_panel = new NW_CovenantPanel( this, dwd, frame );
		
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
	
	
	public void setBeginExceptionData(List<BeginExceptionEntry> bed) {
		this.bed = bed;
	}
	
	
	public List<String> getExceptionNames() {
		List<String> s = new ArrayList<>();
		if (!this.doesBedExist()) {
			return s;
		} else {
			for (BeginExceptionEntry exception : bed) {
				s.add(exception.getName());
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
		
	}
	
	
	public int getNumBED() {
		
		if (bed == null) {
			return 0;
		}
		else {
			return bed.size();
		}
		
	}
	
	
	// this method returns the number of unique employees for a given day
	public int getNumberUniqueWorkers() {
		
        List<String> workers = new ArrayList<>();
        
        // add workers from houses
        for (int h = 0; h < house_panel.length; h++) {
            List<String> workerList = house_panel[h].getSelectedWorkers();
            for (String worker : workerList) {
                if (!workers.contains(worker)) {
                    workers.add(worker);
                }
            }
        }
        
        // add workers from covenant panel
        List<String> covWorkers = cov_panel.getSelectedWorkers();
        for (String worker : covWorkers) {
            if (!workers.contains(worker)) {
                workers.add(worker);
            }
        }
        
        return workers.size(); 
	}
	
	
	// this method returns a list of the unique employees for a given day
	public List<String> getUniqueWorkersForDay() {
			
	    List<String> workers = new ArrayList<>();
		
		// add workers from houses
		for (int h = 0; h < house_panel.length; h++) {
			List<String> workerList = house_panel[h].getSelectedWorkers();
			for (String worker : workerList) {
			    if (!workers.contains(worker)) {
			        workers.add(worker);
			    }
			}
		}
		
		// add workers from covenant panel
		List<String> covWorkers = cov_panel.getSelectedWorkers();
		for (String worker : covWorkers) {
		    if (!workers.contains(worker)) {
		        workers.add(worker);
		    }
		}
		
		return workers; 
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
					null, hp.worker_panel.workerCheckBoxes[0][0],
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

}


