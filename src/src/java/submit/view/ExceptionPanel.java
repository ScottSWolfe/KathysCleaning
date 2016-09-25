package src.java.submit.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import net.miginfocom.swing.MigLayout;
import src.java.general.controller.FlexibleFocusListener;
import src.java.general.controller.TimeDocFilter;
import src.java.general.controller.TimeKeyListener;
import src.java.general.model.DefaultWorkerData;
import src.java.submit.model.ExceptionData;



@SuppressWarnings("serial")
public class ExceptionPanel extends JPanel {

	JFrame frame;
	DefaultWorkerData dwd;
	HousePanel hp;
	
	JLabel name_label;
	JLabel time_label;
	
	JComboBox<String>[] name_box;
	
	JTextField[] time_begin;
	JTextField[] time_end;
	
	JButton cancel_button;
	JButton submit_button;
	
	public final static int EXCEPTION_ROWS = 3; 
	
	
//  CONSTRUCTOR
	
	@SuppressWarnings("unchecked")
    public ExceptionPanel( JFrame frame, DefaultWorkerData dwd, HousePanel hp ) {
		
		this.frame = frame;
		this.dwd = dwd;
		this.hp = hp;
		ExceptionData exception_data = hp.exception_data;
		
		// generating string for migLayout based on EXCEPTION_ROWS
		String temp = "[grow]";
		for(int i=1; i<EXCEPTION_ROWS; i++) {
			temp = new String(temp + "[grow]");
		}
		
		setLayout( new MigLayout("insets 5", temp, "[grow]3[grow]6[grow]6[grow]") );
		setBackground(DayPanel.BACKGROUND_COLOR);
		
		name_label = new JLabel("Name");
		name_label.setFont( name_label.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		
		time_label = new JLabel("Time");
		time_label.setFont( time_label.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		
		name_box = new JComboBox[EXCEPTION_ROWS];
		
		time_begin = new JTextField[EXCEPTION_ROWS];
		AbstractDocument[] time_begin_doc = new AbstractDocument[EXCEPTION_ROWS];
		
		time_end = new JTextField[EXCEPTION_ROWS];
		AbstractDocument[] time_end_doc = new AbstractDocument[EXCEPTION_ROWS];
		
		
		for(int i=0; i<EXCEPTION_ROWS; i++){
			name_box[i] = new JComboBox<String>();
			name_box[i].setEditable(true);
			name_box[i].setSize(10, UNDEFINED_CONDITION);
			name_box[i].setFont( name_box[i].getFont().deriveFont( DayPanel.FONT_SIZE ) );
			
			
			name_box[i].addItem("");   // empty choice
			for(int k=0; k<dwd.default_workers.length; k++){
				name_box[i].addItem( dwd.default_workers[k] );
			}
			
			
			time_begin[i] = new JTextField(5);
			time_begin[i].setFont( time_begin[i].getFont().deriveFont( DayPanel.FONT_SIZE ) );
			time_begin_doc[i] = (AbstractDocument)time_begin[i].getDocument();
				TimeDocFilter tdf_begin = new TimeDocFilter(time_begin[i] );
			time_begin_doc[i].setDocumentFilter( tdf_begin );
			time_begin[i].addKeyListener( new TimeKeyListener( tdf_begin ) );
			//time_begin[i].addFocusListener(  );
			
			time_end[i] = new JTextField(5);
			time_end[i].setFont( time_end[i].getFont().deriveFont( DayPanel.FONT_SIZE ) );
			time_end_doc[i] = (AbstractDocument)time_end[i].getDocument();
				TimeDocFilter tdf_end = new TimeDocFilter(time_end[i] );
			time_end_doc[i].setDocumentFilter( tdf_end );
			time_end[i].addKeyListener( new TimeKeyListener( tdf_end) );
			//time_begin[i].addFocusListener(  );
			
			
			// Setting Values if exception_data exists
			if(exception_data.edited){
				
				name_box[i].setSelectedItem(exception_data.worker_name[i]);				
				time_begin[i].setText(exception_data.time_begin[i]);
				time_end[i].setText(exception_data.time_end[i]);
				
				
			}
			
			
		}
		
		cancel_button = new JButton("Cancel");
		cancel_button.setFont( cancel_button.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		cancel_button.addActionListener( new CancelListener(frame) );
		cancel_button.setBackground( DayPanel.MAIN_COLOR );
		cancel_button.setForeground( DayPanel.FOREGROUND_COLOR );
		
		submit_button = new JButton("Submit");
		submit_button.setFont( submit_button.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		submit_button.addActionListener( new SubmitListener(frame, this, hp) );
		submit_button.setBackground( DayPanel.MAIN_COLOR );
		submit_button.setForeground( DayPanel.FOREGROUND_COLOR );
		
		addFlexibleFocusListeners();
		
		add(name_label, "ax 10%");
		add(time_label, "span 2 1, wrap, ax center, gapx 15");
		
		for(int i=0; i<EXCEPTION_ROWS; i++){
			add(name_box[i], "ay center, growy");
			add(time_begin[i], "ay center, growy, gapx 15");
			add(time_end[i], "wrap, ay center, growy");
		}
		
		JPanel p = new JPanel();
		p.setLayout( new MigLayout("align right"));
		p.setBackground( DayPanel.BACKGROUND_COLOR );
		p.add(cancel_button, "");
		p.add(submit_button, "");
		
		add (p, new String ("span "+EXCEPTION_ROWS+", growx"));
		//add(cancel_button, new String("span "+EXCEPTION_ROWS+", split 2, growx") );
		//add(submit_button, "growx");
	}

	
	
	
//  PRIVATE METHODS
	
	private void addFlexibleFocusListeners() {
		
		for (int i=0; i<EXCEPTION_ROWS; i++) {
			
			JComboBox<String> up_cb = null;
			JComboBox<String> down_cb = null;
			JTextField up_begin_field = null;
			JTextField down_begin_field = null;
			JTextField up_end_field = null;
			JTextField down_end_field = null;
			
			if ( i > 0 ) {
				up_cb = name_box[i-1];
				up_begin_field = time_begin[i-1];
				up_end_field = time_end[i-1];
			}
			else if ( i <= 0 ) {
				up_cb = null;
				up_begin_field = null;
				up_end_field = null;
			}
			if ( i < EXCEPTION_ROWS - 1 ) {
				down_cb = name_box[i+1];
				down_begin_field = time_begin[i+1];
				down_end_field = time_end[i+1];
			}
			else if ( i >= EXCEPTION_ROWS - 1 ) {
				down_cb = null;
				down_begin_field = null;
				down_end_field = null;
			}
			
			name_box[i].getEditor().getEditorComponent().addFocusListener( new FlexibleFocusListener( name_box[i],
					FlexibleFocusListener.COMBOBOX,
					null, time_begin[i],
					up_cb, down_cb,
					null) );
			
			time_begin[i].addFocusListener( new FlexibleFocusListener( time_begin[i],
					FlexibleFocusListener.TEXTFIELD,
					name_box[i], time_end[i],
					up_begin_field, down_begin_field,
					null) );
			
			time_end[i].addFocusListener( new FlexibleFocusListener( time_end[i],
					FlexibleFocusListener.TEXTFIELD,
					time_begin[i], null,
					up_end_field, down_end_field,
					down_cb) );
			
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
			
			// possible JOptionPane message asking if sure they want to cancel
			
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			
		}
		
	}
	
	
	// if submit button is selected
	private class SubmitListener implements ActionListener {
		
		//  FIELDS
			JFrame frame;
			ExceptionPanel ep;
			HousePanel hp;
			
			
		//  CONSTRUCTORS
			private SubmitListener( JFrame frame, ExceptionPanel ep, HousePanel hp ) {
				
				this.frame = frame;
				this.ep = ep;
				this.hp = hp;
			}
			
			
		//  LISTENER
			public void actionPerformed(ActionEvent e){
				
				// TODO: possible JOptionPane message asking if sure they want to submit
				
				
				// create ExceptionData
				
				String[] workers = new String[EXCEPTION_ROWS];
				String[] time_begin = new String[EXCEPTION_ROWS];
				String[] time_end = new String[EXCEPTION_ROWS];
				
				
				for(int i=0; i<EXCEPTION_ROWS; i++){
					
					workers[i] = String.valueOf(ep.name_box[i].getSelectedItem());
												
					time_begin[i] = ep.time_begin[i].getText();
					time_end[i] = ep.time_end[i].getText();
					
				}
				
				hp.exception_data.setWorkers( workers );
				hp.exception_data.setTimeBegin(time_begin);
				hp.exception_data.setTimeEnd(time_end);
				hp.exception_data.edited = true;
				
				// close exceptionpanel
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				
			}
			
	}
	
}
