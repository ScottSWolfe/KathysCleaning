package com.github.scottswolfe.kathyscleaning.scheduled.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.scottswolfe.kathyscleaning.general.controller.FlexibleFocusListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.WorkerPanel;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;

import net.miginfocom.swing.MigLayout;



public class EditCovenantWorkersPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3244919183803930036L;
	
	
	WorkerList dwd;
	WorkerPanel dwp;
	JFrame frame;
	NW_CovenantPanel cov_panel;
	
	int rows = WorkerPanel.COV_ROWS;
	int columns = WorkerPanel.COV_COLUMNS;
	
	JComboBox<String>[][] worker_combo;
	JButton cancel_button;
	JButton submit_button;
	
	
	@SuppressWarnings("unchecked")
    public EditCovenantWorkersPanel( NW_CovenantPanel cov_panel, JFrame frame, WorkerList dwd, WorkerPanel dwp   ) throws Exception {
		
		this.dwd = dwd;
		this.dwp = dwp;
		this.frame = frame;
		this.cov_panel = cov_panel;
		
		worker_combo = new JComboBox[rows][columns];
		
		
		for (int i=0; i<rows; i++) {
			
			for(int j=0; j<columns; j++) {
			
				worker_combo[i][j] = new JComboBox<String>();
				worker_combo[i][j].setEditable(true);
				worker_combo[i][j].setSize(10, UNDEFINED_CONDITION);
				worker_combo[i][j].setFont( worker_combo[i][j].getFont().deriveFont( Settings.FONT_SIZE ) );
			
				worker_combo[i][j].addItem(null);   // empty choice
				for(int k=0; k<dwd.size(); k++){
					worker_combo[i][j].addItem(dwd.getName(k));
				}
			
				worker_combo[i][j].setSelectedItem( dwp.workerCheckBoxes[i][j].getText() );
				
			}
		
		}
		
		addFlexibleFocusListeners();

		setLayout( new MigLayout("insets 5","[]6[]6[]6[]","[]6[]6[]") );
		setBackground(Settings.BACKGROUND_COLOR);
		
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
		cancel_button.setFont( cancel_button.getFont().deriveFont( Settings.FONT_SIZE ) );
		cancel_button.setBackground(Settings.MAIN_COLOR);
		cancel_button.setForeground(Settings.FOREGROUND_COLOR);
		
		submit_button = new JButton("Submit");
		submit_button.setFont( submit_button.getFont().deriveFont( Settings.FONT_SIZE ) );
		submit_button.setBackground(Settings.MAIN_COLOR);
		submit_button.setForeground(Settings.FOREGROUND_COLOR);
		
		cancel_button.addActionListener( new CancelListener( frame ) );
		submit_button.addActionListener( new SubmitListener(frame, cov_panel, this) );
		
		JPanel p = new JPanel();
		p.setLayout( new MigLayout("align right"));
		p.setBackground( Settings.BACKGROUND_COLOR );
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
		
		JFrame frame;
		
		
		
	//  CONSTRUCTORS
		private CancelListener( JFrame frame ) {
			
			this.frame = frame;
			
		}
		
		
	//  LISTENER
		
		public void actionPerformed(ActionEvent e){
			
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			
		}
		
	}
	
	
	private class SubmitListener implements ActionListener {
		
	//  FIELDS
		
		JFrame frame;
		NW_CovenantPanel cov_panel;
		EditCovenantWorkersPanel ecwp;
		
		
	//  CONSTRUCTOR
		
		private SubmitListener(JFrame frame, NW_CovenantPanel cov_panel, EditCovenantWorkersPanel ecwp) {
			
			this.frame = frame;
			this.cov_panel = cov_panel;
			this.ecwp = ecwp;
			
		}
	
		
	//  LISTENER

		public void actionPerformed(ActionEvent e){
			
			// check for repeat selections and reprimand user, end method
			if ( StaticMethods.isRepeatWorker( worker_combo ) ) {
							
				StaticMethods.shareRepeatWorker();
				return;
			}			
			
			// copy workers selected
			WorkerList workerList = new WorkerList();
			for (int i=0; i<rows; i++){
				for (int j=0; j<columns; j++) {
				    workerList.add(String.valueOf(
                            ecwp.worker_combo[i][j].getSelectedItem()));
				}
			}
			
			// change workers on Covenant Panel's worker panel
			try {
				cov_panel.changeWorkerPanel(workerList);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			// close EditDefaultWorkersPanel
			frame.dispatchEvent(
			        new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			
		}
		
	}
	
	
}
