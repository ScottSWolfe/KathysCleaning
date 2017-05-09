package com.github.scottswolfe.kathyscleaning.scheduled.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import com.github.scottswolfe.kathyscleaning.general.controller.FlexibleFocusListener;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeDocFilter;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeKeyListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.model.BeginExceptionEntry;

import net.miginfocom.swing.MigLayout;


public class NW_ExceptionPanel extends JPanel {
	/*
	 * This panel provides user ability to input exceptions for employee arrival locations and times
	 */
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3133367077235289095L;
	
	
	
	// FIELDS
	JFrame frame;
	NW_DayPanel dp;
	
	JLabel employee_label;
	JComboBox<String>[] employee_combobox;
	
	JLabel location_label;
	JComboBox<String>[] location_combobox;
	
	JLabel time_label;
	JTextField[] time_textfield;
	
	JLabel note_label;
	JTextField[] note_textfield;
	
	JButton cancel_button;
	JButton submit_button;
	
	int previous_key = 0;
	
	public final static int NUM_EXCEPTIONS = 3;
	
	
	
	// CONSTRUCTOR
	
	@SuppressWarnings("unchecked")
    public NW_ExceptionPanel( JFrame frame, NW_DayPanel dp, WorkerList dwd ){
				
		this.frame = frame;
		this.dp = dp;
		
		setLayout( new MigLayout() );
		setBackground( Settings.BACKGROUND_COLOR );
		
		employee_label = new JLabel();
		employee_label.setText("Employee");
		employee_label.setFont( employee_label.getFont().deriveFont(Settings.FONT_SIZE) );
		employee_label.setBackground(Settings.MAIN_COLOR);
		
		employee_combobox = new JComboBox[NUM_EXCEPTIONS];
		for(int i=0; i<NUM_EXCEPTIONS; i++) {
			employee_combobox[i] = new JComboBox<String>();
			employee_combobox[i].setBackground( Settings.BACKGROUND_COLOR );
			employee_combobox[i].setEditable(true);
			employee_combobox[i].setSize(8, UNDEFINED_CONDITION);
			employee_combobox[i].setFont( employee_combobox[i].getFont().deriveFont( Settings.FONT_SIZE ) );
		
			employee_combobox[i].addItem("");   // empty choice
			for(int k=0; k<dwd.size(); k++){
				employee_combobox[i].addItem(dwd.getName(k));
			}
		}
		
		
		location_label = new JLabel();
		location_label.setText("Location");
		location_label.setFont(
		        location_label.getFont().deriveFont(Settings.FONT_SIZE));
		location_label.setBackground(Settings.MAIN_COLOR);
		
		location_combobox = new JComboBox[NUM_EXCEPTIONS];
		for(int i=0; i<NUM_EXCEPTIONS; i++) {
			location_combobox[i] = new JComboBox<String>();
			location_combobox[i].setBackground( Settings.BACKGROUND_COLOR );
			location_combobox[i].setEditable(true);
			location_combobox[i].setFont( location_combobox[i].getFont().deriveFont( Settings.FONT_SIZE ) );
		
			location_combobox[i].addItem("");   // empty choice
		}
		
		
		time_label = new JLabel();
		time_label.setText("Time");
		time_label.setFont( time_label.getFont().deriveFont(Settings.FONT_SIZE) );
		time_label.setBackground(Settings.MAIN_COLOR);
		
		time_textfield = new JTextField[NUM_EXCEPTIONS];
		AbstractDocument[] time_textfield_doc = new AbstractDocument[NUM_EXCEPTIONS];
		for(int i=0; i<NUM_EXCEPTIONS; i++) {
			time_textfield[i] = new JTextField();
			time_textfield[i].setBackground( Settings.BACKGROUND_COLOR );
			time_textfield[i].setColumns(6);
			time_textfield[i].setFont( time_textfield[i].getFont().deriveFont( Settings.FONT_SIZE ) );
				time_textfield_doc[i] = (AbstractDocument) time_textfield[i].getDocument();
				TimeDocFilter tdf = new TimeDocFilter( time_textfield[i] );
				time_textfield_doc[i].setDocumentFilter(tdf);
			time_textfield[i].addKeyListener( new TimeKeyListener(tdf) );
		
		}
		
		
		note_label = new JLabel();
		note_label.setText("Note");
		note_label.setFont( note_label.getFont().deriveFont(Settings.FONT_SIZE) );
		note_label.setBackground(Settings.MAIN_COLOR);
		
		note_textfield = new JTextField[NUM_EXCEPTIONS];
		AbstractDocument[] note_textfield_doc = new AbstractDocument[NUM_EXCEPTIONS];
		for(int i=0; i<NUM_EXCEPTIONS; i++) {
			note_textfield[i] = new JTextField();
			note_textfield[i].setBackground( Settings.BACKGROUND_COLOR );
			note_textfield[i].setColumns(15);
			note_textfield[i].setFont( note_textfield[i].getFont().deriveFont( Settings.FONT_SIZE ) );
			note_textfield_doc[i] = (AbstractDocument) note_textfield[i].getDocument();		
		}
		
		
		cancel_button = new JButton();
		cancel_button.setText("Cancel");
		cancel_button.setBackground(Settings.MAIN_COLOR);
		cancel_button.setForeground( Settings.FOREGROUND_COLOR );
		cancel_button.setFont( cancel_button.getFont().deriveFont( Settings.FONT_SIZE ) );
		cancel_button.addActionListener( new NW_CancelExceptionListener( frame ) );
		
		
		submit_button = new JButton();
		submit_button.setText("Submit");
		submit_button.setBackground(Settings.MAIN_COLOR);
		submit_button.setForeground( Settings.FOREGROUND_COLOR );
		submit_button.setFont( submit_button.getFont().deriveFont( Settings.FONT_SIZE ) );
		submit_button.addActionListener( new NW_SubmitExceptionListener( frame, dp ) );
		
		addFlexibleFocusListeners();
		
		add(employee_label);
		add(location_label);
		add(time_label);
		add(note_label, "wrap");
		
		for (int i=0; i<NUM_EXCEPTIONS; i++) {
			
			add(employee_combobox[i], "growx");
			add(location_combobox[i], "growx");
			add(time_textfield[i], "growx");
			add(note_textfield[i], "wrap, growx");
			
		}
		
		JPanel p = new JPanel();
		p.setLayout( new MigLayout("align right"));
		p.setBackground( Settings.BACKGROUND_COLOR );
		p.add(cancel_button, "");
		p.add(submit_button, "");
		
		add(p, "span 4, grow");
		
	}
	
	
	
	// PRIVATE METHODS
	
	private void addFlexibleFocusListeners() {
		
		Component employee_box_up = null;
		Component employee_box_down = null;
		
		Component location_box_up = null;
		Component location_box_down = null;
		
		Component time_field_up = null;
		Component time_field_down = null;
		
		Component note_field_up = null;
		Component note_field_down = null;
		
		for (int i=0; i<NUM_EXCEPTIONS; i++) {
			
			if (i>0) {
				employee_box_up = employee_combobox[i-1];
				location_box_up = location_combobox[i-1];
				time_field_up = time_textfield[i-1];
				note_field_up = note_textfield[i-1];
			}
			
			if (i<NUM_EXCEPTIONS-1) {
				employee_box_down = employee_combobox[i+1];
				location_box_down = location_combobox[i+1];
				time_field_down = time_textfield[i+1];
				note_field_down = note_textfield[i+1];
			}
			
			FlexibleFocusListener ffl1 = new FlexibleFocusListener( employee_combobox[i], 
					FlexibleFocusListener.COMBOBOX,
					null, location_combobox[i],
					employee_box_up, employee_box_down,
					null);
			
			employee_combobox[i].getEditor().getEditorComponent().addFocusListener( ffl1 );
			
			
			FlexibleFocusListener ffl2 = new FlexibleFocusListener( location_combobox[i], 
					FlexibleFocusListener.COMBOBOX,
					employee_combobox[i], time_textfield[i],
					location_box_up, location_box_down,
					null);
			
			location_combobox[i].getEditor().getEditorComponent().addFocusListener( ffl2 );
			
			
			FlexibleFocusListener ffl3 = new FlexibleFocusListener( time_textfield[i], 
					FlexibleFocusListener.TEXTFIELD,
					location_combobox[i], note_textfield[i],
					time_field_up, time_field_down,
					null);
			
			time_textfield[i].addFocusListener( ffl3 );
			
			
			FlexibleFocusListener ffl4 = new FlexibleFocusListener( note_textfield[i], 
					FlexibleFocusListener.TEXTFIELD,
					time_textfield[i], null,
					note_field_up, note_field_down,
					employee_box_down);
			
			note_textfield[i].addFocusListener( ffl4 );
			
		}
		
	}
	
	
	
	// PUBLIC METHODS
	
	
	
	
	
	// PRIVATE LISTENERS
	
	private class NW_CancelExceptionListener implements ActionListener {
		
		// FIELDS
		JFrame frame;
		
		// CONSTRUCXTOR
		public NW_CancelExceptionListener( JFrame frame ){
			this.frame = frame;
		}
		
		// ACTION METHOD
		public void actionPerformed( ActionEvent e ){
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}
		
	}
	
	
	private class NW_SubmitExceptionListener implements ActionListener {
		
		// FIELDS
		JFrame frame;
		
		// CONSTRUCTOR
		public NW_SubmitExceptionListener( JFrame frame, NW_DayPanel dp ){
			this.frame = frame;
		}
		
		// ACTION METHOD
		public void actionPerformed( ActionEvent e ) {
		    
	        List<BeginExceptionEntry> bedList = new ArrayList<>();
			for (int i = 0; i < NUM_EXCEPTIONS; i++) {
				
			    BeginExceptionEntry bed = new BeginExceptionEntry();
			    
			    if (employee_combobox[i].getSelectedItem() != null) {
			        bed.setName((String) employee_combobox[i].getSelectedItem());
			    } else {
			        bed.setName("");
			    }
			    
			    if (location_combobox[i].getSelectedItem() != null) {
			        bed.setMeetLocation((String) location_combobox[i].getSelectedItem());
			    } else {
			        bed.setMeetLocation("");
			    }
			    
	            if (time_textfield[i].getText() != null) {
	                bed.setTime(String.valueOf(time_textfield[i].getText()));
	            } else {
	                bed.setTime("");
	            }
	            
	            if (note_textfield[i].getText() != null) {
	                bed.setNote(note_textfield[i].getText());
	            } else {
	                bed.setNote("");
	            }
	            bedList.add(bed);								
			}
			dp.setBeginExceptionData(bedList);
			
	        dp.setException_exist(true);
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}
		
	}

	
	// Getters and Setters
	
	public JComboBox<String>[] getEmployee_combobox() {
		return employee_combobox;
	}


	public void setEmployee_combobox_text( String s, int i ) {
		getEmployee_combobox()[i].setSelectedItem( s );
	}


	public JComboBox<String>[] getLocation_combobox() {
		return location_combobox;
	}


	public void setLocation_combobox_text( String s, int i ) {
		getLocation_combobox()[i].setSelectedItem( s );
	}


	public JTextField[] getTime_textfield() {
		return time_textfield;
	}


	public void setTime_textfield_text( String s, int i ) {
		getTime_textfield()[i].setText( s );
	}


	public JTextField[] getNote_textfield() {
		return note_textfield;
	}


	public void setNote_textfield_text( String s, int i ) {
		getNote_textfield()[i].setText( s );
	}
	
	
	
}
