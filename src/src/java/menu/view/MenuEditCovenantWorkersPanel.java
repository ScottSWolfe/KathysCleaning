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
import src.java.submit.view.DayPanel;


public class MenuEditCovenantWorkersPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JFrame frame;
	
	public final static int ROWS = 12;
	
	JComboBox<String>[] worker_combo;
	JButton cancel_button;
	JButton submit_button;
	
	
	@SuppressWarnings("unchecked")
    public MenuEditCovenantWorkersPanel( JFrame frame ) {
	
		int rows = ROWS;
		this.frame = frame;
		
		worker_combo = new JComboBox[rows];
		DefaultWorkerData dwd = new DefaultWorkerData( DefaultWorkerData.COVENANT_WORKERS );
		
		for (int i=0; i<rows; i++) {
			
				worker_combo[i] = new JComboBox<String>();
				worker_combo[i].setEditable(true);
				worker_combo[i].setSize(10, UNDEFINED_CONDITION);
				worker_combo[i].setFont( worker_combo[i].getFont().deriveFont( DayPanel.FONT_SIZE ) );
			
				worker_combo[i].addItem("");   // empty choice
				for(int k=0; k<dwd.default_workers.length; k++){
					worker_combo[i].addItem( dwd.default_workers[k] );
				}
			
				worker_combo[i].setSelectedItem( dwd.default_workers[i] );
		
		}
		
		addFlexibleFocusListeners();

		setLayout( new MigLayout() );
		setBackground( DayPanel.BACKGROUND_COLOR );
		
		for (int i=0; i<rows; i++) {

				
				if (i<rows ) {
					add(worker_combo[i], "wrap 10");
				}
				else{
					add(worker_combo[i]);
				}
				
			
			
		}
		
		cancel_button = new JButton("Cancel");
		cancel_button.setFont( cancel_button.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		cancel_button.setBackground( DayPanel.MAIN_COLOR );
		cancel_button.setForeground( DayPanel.FOREGROUND_COLOR );
		cancel_button.addActionListener( new CancelListener( ) );

		submit_button = new JButton("Submit");
		submit_button.setFont( submit_button.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		submit_button.setBackground( DayPanel.MAIN_COLOR );
		submit_button.setForeground( DayPanel.FOREGROUND_COLOR );
		submit_button.addActionListener( new SubmitListener( ) );
		
		add(cancel_button, "split 2");
		add(submit_button, "");
			
	}
	


	// PRIVATE METHODS
	
	private void addFlexibleFocusListeners () {
		
		int rows = ROWS;
		
		for (int i=0; i<rows; i++) {
			
			JComboBox<String> up_cb = null;
			JComboBox<String> down_cb = null;
			
			if ( i > 0 ) {
				up_cb = worker_combo[i-1];
			}
			else if ( i <= 0 ) {
				up_cb = null;
			}
			if ( i < rows - 1 ) {
				down_cb = worker_combo[i+1];
			}
			else if ( i >= rows - 1 ) {
				down_cb = null;
			}
			
			worker_combo[i].getEditor().getEditorComponent().addFocusListener( new FlexibleFocusListener( worker_combo[i],
					FlexibleFocusListener.COMBOBOX,
					null, null,
					up_cb, down_cb,
					null) );
			
		}
		
	}
	
	
	
//  PRIVATE LISTENER
	
	private class CancelListener implements ActionListener {
		
		// FIELDS
		//JFrame frame;
			
				
		// CONSTRUCTOR
		private CancelListener(  ) {
			//frame = frame;
		}
				
		
		// LISTENER
		public void actionPerformed(ActionEvent e){
			
			// TODO possible JOptionPane message asking if sure they want to cancel
		
			frame.setVisible(false);
			frame.dispose();
			
		}
		
	}
	
	
	private class SubmitListener implements ActionListener {
		
		// FIELDS
		//JFrame frame;
		
		
		// CONSTRUCTOR
		private SubmitListener(  ) {
			//frame = frame;
		}
		
		
		// LISTENER
		public void actionPerformed(ActionEvent e){
			
			// TODO check for repeat selections and reprimand user, end method
			
			try {

				FileWriter fw = new FileWriter( DefaultWorkerData.COVENANT_WORKERS );
				BufferedWriter bw = new BufferedWriter( fw );
				
				int rows = ROWS;
				
				for (int i=0; i<rows; i++) {
					
					bw.write( String.valueOf( worker_combo[i].getSelectedItem() ));
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
			
			// close EditWorkersPanel
			frame.setVisible( false );
			frame.dispose();
			
		}
		
	}
	
	
}	

