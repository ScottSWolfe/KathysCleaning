package com.github.scottswolfe.kathyscleaning.scheduled.view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.scottswolfe.kathyscleaning.general.controller.FlexibleFocusListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_NoteListener;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NoteData;

import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
public class NW_NotePanel extends JPanel {	
	
	// FIELDS
	JFrame frame;
	NW_DayPanel day_panel;
	WorkerList dwd;
	NW_NoteListener listener;
	
	public static final int ROWS = 8;

	   
	
	// COMPONENTS
	JLabel name_label;
	JLabel note_label;
	
	JComboBox<String>[] name_box;
	JTextField[] note_field;
	
	JButton cancel_button;
	JButton submit_button;	
	
	
	
	//  CONSTRUCTOR	
	@SuppressWarnings("unchecked")
    public NW_NotePanel( JFrame frame, NW_DayPanel day_panel, WorkerList dwd,
						 String[] selected_workers, NoteData note_data, NW_NoteListener listener ) {
		
		this.frame = frame;
		this.day_panel = day_panel;
		this.dwd = dwd;
		this.listener = listener;
		
		note_data = day_panel.noteData;
		
		// generating string for migLayout format based on number of rows
		String column_format = "[grow]";
		for(int i=1; i<ROWS; i++) {
			column_format = new String(column_format + "[grow]");
		}
		//column_format = new String(column_format + "[grow]");
		
		setLayout( new MigLayout("", "[grow]15[grow]", "") );
		setBackground( Settings.BACKGROUND_COLOR );
		
		name_label = new JLabel();
		name_label.setText("Name");
		name_label.setFont( name_label.getFont().deriveFont( Settings.FONT_SIZE ) );
		name_label.setBackground( Settings.BACKGROUND_COLOR );
		
		note_label = new JLabel();
		note_label.setText("Note");
		note_label.setFont( note_label.getFont().deriveFont( Settings.FONT_SIZE ) );
		note_label.setBackground( Settings.BACKGROUND_COLOR );
		
		name_box = new JComboBox[ROWS];
		note_field = new JTextField[ROWS];
		
		
		for(int i=0; i<ROWS; i++){
			
			name_box[i] = new JComboBox<String>();
			name_box[i].setEditable(true);
			name_box[i].setSize(7, UNDEFINED_CONDITION);
			name_box[i].setFont( name_box[i].getFont().deriveFont( Settings.FONT_SIZE ) );
			name_box[i].setBackground( Settings.BACKGROUND_COLOR );
			name_box[i].addItem("");	// empty choice
			for(int k=0; k<dwd.size(); k++){
				name_box[i].addItem(dwd.getName(k));
			}
			
			note_field[i] = new JTextField(15);
			note_field[i].setFont( note_field[i].getFont().deriveFont( Settings.FONT_SIZE ) );
			
			// Setting Values if NoteData exists
			if(note_data != null){
				name_box[i].setSelectedItem(note_data.getNameBoxData()[i]);				
				note_field[i].setText(note_data.getNoteFieldData()[i]);
			}
			
		}
		
		cancel_button = new JButton("Cancel");
		cancel_button.setFont( cancel_button.getFont().deriveFont( Settings.FONT_SIZE ) );
		cancel_button.addActionListener( new CancelListener(frame) );
		cancel_button.setBackground(Settings.MAIN_COLOR);
		cancel_button.setForeground( Settings.FOREGROUND_COLOR );
		
		submit_button = new JButton("Submit");
		submit_button.setFont( submit_button.getFont().deriveFont( Settings.FONT_SIZE ) );
		submit_button.addActionListener( new SubmitListener() );
		submit_button.setBackground(Settings.MAIN_COLOR);
		submit_button.setForeground( Settings.FOREGROUND_COLOR );
		
		addFlexibleFocusListeners();
		
		add(name_label, "");
		add(note_label, "wrap");
		
		for(int i=0; i<ROWS; i++){
			add(name_box[i], "grow");
			add(note_field[i], "grow, wrap");
		}
		
		JPanel p = new JPanel();
		p.setLayout( new MigLayout("align right"));
		p.setBackground( Settings.BACKGROUND_COLOR );
		p.add(cancel_button, "");
		p.add(submit_button, "");
		
		add(p, "span 2, grow");
	}

	
	
	// PRIVATE METHODS
	private void addFlexibleFocusListeners() {
		
		Component box_up = null;
		Component box_down = null;
		Component field_up = null;
		Component field_down = null;
		
		for (int i=0; i<ROWS; i++) {
			
			if (i>0) {
				box_up = name_box[i-1];
				field_up = note_field[i-1];
			}
			
			if (i<ROWS-1) {
				box_down = name_box[i+1];
				field_down = note_field[i+1];
			}
			
			FlexibleFocusListener ffl1 = new FlexibleFocusListener( name_box[i], 
					FlexibleFocusListener.COMBOBOX,
					null, note_field[i],
					box_up, box_down,
					null);
			
			name_box[i].getEditor().getEditorComponent().addFocusListener( ffl1 );
			
			FlexibleFocusListener ffl2 = new FlexibleFocusListener( note_field[i], 
					FlexibleFocusListener.TEXTFIELD,
					name_box[i], null,
					field_up, field_down,
					box_down);
			
			note_field[i].addFocusListener( ffl2 );
		}
		
	}
	
	
	
	
	//  PRIVATE LISTENER
	
	// if cancel button is selected
	private class CancelListener implements ActionListener {
		
	//  FIELDS
		JFrame frame;

		
	//  CONSTRUCTORS
		private CancelListener( JFrame frame ) {
			
			this.frame = frame;
		}
		
		
	//  LISTENER
		public void actionPerformed(ActionEvent e){
			
			frame.setVisible(false);
			frame.dispose();
			
		}
		
	}
	
	
	// if submit button is selected
	private class SubmitListener implements ActionListener {
			
		//  LISTENER
		public void actionPerformed(ActionEvent e){
			
			// create NoteData
			NoteData nd;
			String[] worker = new String[ROWS];
			String[] note = new String[ROWS];

			for(int i=0; i<ROWS; i++){
			    worker[i] = String.valueOf(name_box[i].getSelectedItem());
			    note[i] = note_field[i].getText();
			}
			
			nd = new NoteData(worker, note, dwd);
			
			// set nw day panel's note data and listener data
			day_panel.setNoteData(nd);
			listener.note_data = nd;
			
			// close note panel
			frame.setVisible( false );
			frame.dispose();
		}
	}
	
}

 