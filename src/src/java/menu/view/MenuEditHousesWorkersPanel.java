package src.java.menu.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import src.java.general.controller.FlexibleFocusListener;
import src.java.general.model.DefaultWorkerData;
import src.java.general.view.DefaultWorkerPanel;
import src.java.submit.view.DayPanel;


@SuppressWarnings("serial")
public class MenuEditHousesWorkersPanel extends JPanel {

	JFrame frame;
	DefaultWorkerData dwd;
	
	int rows = DefaultWorkerPanel.NORM_ROWS;
	int columns = DefaultWorkerPanel.NORM_COLUMNS;
	
	JComboBox<String>[][] worker_combo;
	JButton cancel_button;
	JButton submit_button;
	
	
	@SuppressWarnings("unchecked")
    public MenuEditHousesWorkersPanel( JFrame frame ) {
	
		this.frame = frame;
		
		worker_combo = new JComboBox[rows][columns];
		DefaultWorkerData dwd = new DefaultWorkerData( DefaultWorkerData.HOUSE_WORKERS );
		
		for (int i=0; i<rows; i++) {
			
			for(int j=0; j<columns; j++) {
			
				worker_combo[i][j] = new JComboBox<String>();
				worker_combo[i][j].setEditable(true);
				worker_combo[i][j].setSize(10, UNDEFINED_CONDITION);
				worker_combo[i][j].setFont( worker_combo[i][j].getFont().deriveFont( DayPanel.FONT_SIZE ) );
			
				worker_combo[i][j].addItem("");   // empty choice
				for(int k=0; k<dwd.default_workers.length; k++){
					worker_combo[i][j].addItem( dwd.default_workers[k] );
				}
			
				worker_combo[i][j].setSelectedItem( dwd.default_workers[i*columns + j] );
				
			}
		
		}
		

		setLayout( new MigLayout("insets 5","[]6[]6[]6[]","[]6[]6[]") );
		setBackground( DayPanel.BACKGROUND_COLOR );
		
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
		
		addFlexibleFocusListeners();
		
		cancel_button = new JButton("Cancel");
		cancel_button.setFont( cancel_button.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		cancel_button.setBackground( DayPanel.MAIN_COLOR );
		cancel_button.setForeground( DayPanel.FOREGROUND_COLOR );
		
		submit_button = new JButton("Submit");
		submit_button.setFont( submit_button.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		submit_button.setBackground( DayPanel.MAIN_COLOR );
		submit_button.setForeground( DayPanel.FOREGROUND_COLOR );
		
		cancel_button.addActionListener( new CancelListener(  ) );
		submit_button.addActionListener( new SubmitListener(  ) );
		
		JPanel p = new JPanel();
		p.setLayout( new MigLayout("align right"));
		p.setBackground( DayPanel.BACKGROUND_COLOR );
		p.add(cancel_button, "");
		p.add(submit_button, "");
		
		add(p, "span " + columns + ", grow");
			
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
		
		//JFrame frame;
		
		
		
	//  CONSTRUCTORS
		private CancelListener( ) {
			
			//this.frame = frame;
			
		}
		
		
	//  LISTENER
		
		public void actionPerformed(ActionEvent e){
			
			frame.setVisible(false);
			frame.dispose();
			
		}
		
	}
	
	
	private class SubmitListener implements ActionListener {
		
	//  FIELDS
		
		//EditDefaultWorkersPanel edwp;
		
		
	//  CONSTRUCTOR
		
		private SubmitListener() {
						
		}
	
		
	//  LISTENER

		public void actionPerformed(ActionEvent e){
			
			// TODO: check for repeat selections and reprimand user, end method
			
			
			
			// copy workers selected
			String[] tempString = new String[rows*columns];
			int k = 0;
			
			for (int i=0; i<rows; i++){
				for (int j=0; j<columns; j++) {
					
					tempString[k] = (String) worker_combo[i][j].getSelectedItem();
					k++;
				}
			}
			
			//dwd = new DefaultWorkerData(tempString);
			
			// save to file
			try {

				FileWriter fw = new FileWriter( DefaultWorkerData.HOUSE_WORKERS );
				BufferedWriter bw = new BufferedWriter( fw );
				
				for (int i=0; i<tempString.length; i++) {
					
					bw.write( tempString[i] );
					bw.newLine();
					
				}
				
				/*
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
				*/
				
				bw.close();
				
			}
			catch(Exception e2) {
				e2.printStackTrace();
			} 
			
			
			
			// close EditDefaultWorkersPanel
			frame.setVisible(false);
			frame.dispose();
			
		}
		
	}
	
	
}	

