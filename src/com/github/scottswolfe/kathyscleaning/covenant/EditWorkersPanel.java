package com.github.scottswolfe.kathyscleaning.covenant;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.scottswolfe.kathyscleaning.general.controller.FlexibleFocusListener;
import com.github.scottswolfe.kathyscleaning.general.controller.StaticMethods;
import com.github.scottswolfe.kathyscleaning.general.model.DefaultWorkerData;
import com.github.scottswolfe.kathyscleaning.submit.view.DayPanel;

import net.miginfocom.swing.MigLayout;


public class EditWorkersPanel extends JPanel {

	/**
	 * 	This panel appears when the user selects "Edit" on the Covenant input panel.
	 * 	This panel allows the user to adjust the available workers.
	 */
	private static final long serialVersionUID = -2541074258497213344L;
	
	JFrame frame;
	CovenantPanel cp;
	
	int rows;
	
	JComboBox<String>[] worker_combo;
	JButton cancel_button;
	JButton submit_button;
	
	
	@SuppressWarnings("unchecked")
    public EditWorkersPanel( JFrame frame, CovenantPanel cp ) {
	
		this.frame = frame;
		this.cp = cp;
		
		rows = cp.rows; // TODO make safe
		
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
			
				worker_combo[i].setSelectedItem( cp.name_label[i].getText() );
				
			
		
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
		cancel_button.addActionListener( new CancelListener() );

		submit_button = new JButton("Submit");
		submit_button.setFont( submit_button.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		submit_button.setBackground( DayPanel.MAIN_COLOR );
		submit_button.setForeground( DayPanel.FOREGROUND_COLOR );
		submit_button.addActionListener( new SubmitListener() );
		
		add(cancel_button, "split 2");
		add(submit_button, "");
			
	}
	


	// PRIVATE METHODS
	
	private void addFlexibleFocusListeners () {
		
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
		
		public void actionPerformed(ActionEvent e){
			
			// TODO possible JOptionPane message asking if sure they want to cancel
		
			frame.setVisible(false);
			frame.dispose();
			
		}
		
	}
	
	
	private class SubmitListener implements ActionListener {

		public void actionPerformed(ActionEvent e){
			
			// check for repeat selections and reprimand user, end method
			if ( StaticMethods.isRepeatWorker( worker_combo ) ) {
							
				StaticMethods.shareRepeatWorker();
				return;
			}
			
			// copy workers selected
			String[] s = new String[rows];
			int k = 0;
			
			for (int i=0; i<rows; i++){					
					s[k] = (String) worker_combo[i].getSelectedItem();
					k++;
				
			}
			
			DefaultWorkerData dwd = new DefaultWorkerData( s );
			
			
			// change workers on CovenantPanel
			cp.dwd = dwd;
			
			for (int i=0; i<s.length; i++) {
				cp.name_label[i].setText( s[i] );
			}
			
			cp.getParent().revalidate();
			cp.getParent().repaint();
			
			// close EditWorkersPanel
			frame.setVisible( false );
			frame.dispose();
			
		}
		
	}
	
	
}	

