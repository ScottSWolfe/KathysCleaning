package src.java.nextweek.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import src.java.general.controller.FlexibleFocusListener;
import src.java.general.controller.StaticMethods;
import src.java.general.model.DefaultWorkerData;
import src.java.general.view.DefaultWorkerPanel;
import src.java.submit.view.DayPanel;


public class NW_EditDefaultWorkersPanel extends JPanel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7882195672958519636L;
	
	
	DefaultWorkerPanel dwp;
	JFrame frame;
	NW_DayPanel day_panel;
	
	int rows = DefaultWorkerPanel.NORM_ROWS;
	int columns = DefaultWorkerPanel.NORM_COLUMNS;
	
	JComboBox<String>[][] worker_combo;
	JButton cancel_button;
	JButton submit_button;
	
	
	@SuppressWarnings("unchecked")
    public NW_EditDefaultWorkersPanel( DefaultWorkerPanel dwp, JFrame frame, NW_DayPanel day_panel ) throws Exception {
	
		this.dwp = dwp;
		this.frame = frame;
		this.day_panel = day_panel;
		
		worker_combo = new JComboBox[rows][columns];
		DefaultWorkerData dwd = new DefaultWorkerData( DefaultWorkerData.HOUSE_WORKERS );
		
		for (int i=0; i<rows; i++) {
			
			for(int j=0; j<columns; j++) {
			
				worker_combo[i][j] = new JComboBox<String>();
				worker_combo[i][j].setEditable(true);
				worker_combo[i][j].setSize(10, UNDEFINED_CONDITION);
				worker_combo[i][j].setFont( worker_combo[i][j].getFont().deriveFont( DayPanel.FONT_SIZE ) );
				worker_combo[i][j].setBackground(DayPanel.HEADER_BACKGROUND);
			
				worker_combo[i][j].addItem(null);   // empty choice
				for(int k=0; k<dwd.default_workers.length; k++){
					worker_combo[i][j].addItem( dwd.default_workers[k] );
				}
			
				worker_combo[i][j].setSelectedItem( dwp.worker[i][j].getText() );
				
			}
		
		}
		
		addFlexibleFocusListeners();

		setLayout( new MigLayout("insets 5","[]6[]6[]6[]","[]6[]6[]") );
		setBackground(DayPanel.BACKGROUND_COLOR);
		
		for (int i=0; i<rows; i++) {
			for (int j=0; j<columns; j++) {
				
				if (i<rows && j == columns - 1 ) {
					add(worker_combo[i][j], "wrap");
				}
				else{
					add(worker_combo[i][j]);
				}
				
			}
			
		}
		
		cancel_button = new JButton("Cancel");
		cancel_button.setFont( cancel_button.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		cancel_button.setBackground( DayPanel.MAIN_COLOR );
		cancel_button.setForeground( DayPanel.FOREGROUND_COLOR );
		
		submit_button = new JButton("Submit");
		submit_button.setFont( submit_button.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		submit_button.setBackground( DayPanel.MAIN_COLOR );
		submit_button.setForeground( DayPanel.FOREGROUND_COLOR );
		
		
		cancel_button.addActionListener( new CancelListener( frame ) );
		submit_button.addActionListener( new SubmitListener(frame, day_panel, this) );
		
		JPanel p = new JPanel();
		p.setLayout( new MigLayout("align right"));
		p.setBackground( DayPanel.BACKGROUND_COLOR );
		p.add(cancel_button, "");
		p.add(submit_button, "");
		
		add(p, "span " + DefaultWorkerPanel.NORM_COLUMNS + ", grow");
			
	}
	


	// PRIVATE METHODS
	
	private void addFlexibleFocusListeners() {
		
		for (int i=0; i<rows; i++) {
			for (int j=0; j<columns; j++) {
				
				JComboBox<String> left_box = null;
				JComboBox<String> right_box = null;
				JComboBox<String> up_box = null;
				JComboBox<String> down_box = null;
				

				if ( j > 0 ) {
					left_box = worker_combo[i][j-1];
				}
				else if ( j <= 0 ) {
					left_box = null;
				}
				if ( j < columns - 1 ) {
					right_box = worker_combo[i][j+1];
				}
				else if ( j >= columns - 1 ) {
					right_box = null;
				}
				
				if ( i > 0 ) {
					up_box = worker_combo[i-1][j];
				}
				else if ( i <= 0 ) {
					up_box = null;
				}
				if ( i < rows - 1 ) {
					down_box = worker_combo[i+1][j];
				}
				else if ( i >= rows - 1 ) {
					down_box = null;
				}
				
				FlexibleFocusListener ffl = new FlexibleFocusListener( worker_combo[i][j], 
						FlexibleFocusListener.COMBOBOX,
						left_box, right_box,
						up_box, down_box,
						null);
				
				worker_combo[i][j].getEditor().getEditorComponent().addFocusListener( ffl );
				
			}
		}
		
	}
	
	
	
//  PRIVATE LISTENER
	
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
			
			/*
			frame.setVisible(false);
			frame.dispose();
			*/
			
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			
		}
		
	}
	
	
	private class SubmitListener implements ActionListener {
		
	//  FIELDS
		
		JFrame frame;
		NW_DayPanel day_panel;
		NW_EditDefaultWorkersPanel edwp;
		
		
	//  CONSTRUCTOR
		
		private SubmitListener(JFrame frame, NW_DayPanel day_panel, NW_EditDefaultWorkersPanel edwp) {
			
			this.frame = frame;
			this.day_panel = day_panel;
			this.edwp = edwp;
			
		}
	
		
	//  LISTENER

		public void actionPerformed(ActionEvent e){
			
			// check for repeat selections and reprimand user, end method
			if ( StaticMethods.isRepeatWorker( worker_combo ) ) {
							
				StaticMethods.shareRepeatWorker();
				return;
			}			
			
			// copy workers selected
			String[] tempString = new String[rows*columns];
			int k = 0;
			
			for (int i=0; i<rows; i++){
				for (int j=0; j<columns; j++) {
					
					tempString[k] = (String) edwp.worker_combo[i][j].getSelectedItem();
					k++;
				}
			}
			
			DefaultWorkerData dwd = new DefaultWorkerData( tempString );
			
			
			// paste workers selected on header dwp and house panel dwps
			day_panel.changeWorkerPanels( dwd );
			
			
			// resize frame width
			
			
			// close EditDefaultWorkersPanel
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			
		}
		
	}
	
	
}	

