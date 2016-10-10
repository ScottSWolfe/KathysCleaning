package com.github.scottswolfe.kathyscleaning.covenant.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.scottswolfe.kathyscleaning.general.controller.FlexibleFocusListener;
import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.controller.StaticMethods;
import com.github.scottswolfe.kathyscleaning.general.model.DefaultWorkerData;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

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
		
		rows = CovenantPanel.ROWS;
		
		worker_combo = new JComboBox[rows];
		
		for (int i=0; i<rows; i++) {
			
				worker_combo[i] = new JComboBox<String>();
				worker_combo[i].setEditable(true);
				worker_combo[i].setSize(10, UNDEFINED_CONDITION);
				worker_combo[i].setFont( worker_combo[i].getFont().deriveFont( Settings.FONT_SIZE ) );
			
				worker_combo[i].addItem("");   // empty choice
				for(int k=0; k<cp.getController().getCovModel().getDwd().getDefault_workers().length; k++){
					worker_combo[i].addItem(cp.getController().getCovModel().getDwd().getDefault_workers()[k]);
				}
			
				worker_combo[i].setSelectedItem(cp.getNameLabels()[i].getText());
				
			
		
		}
		
		addFlexibleFocusListeners();

		setLayout( new MigLayout() );
		setBackground( Settings.BACKGROUND_COLOR );
		
		for (int i=0; i<rows; i++) {

				
				if (i<rows ) {
					add(worker_combo[i], "wrap 10");
				}
				else{
					add(worker_combo[i]);
				}
				
			
			
		}
		
		cancel_button = new JButton("Cancel");
		cancel_button.setFont( cancel_button.getFont().deriveFont( Settings.FONT_SIZE ) );
		cancel_button.setBackground(Settings.MAIN_COLOR);
		cancel_button.setForeground( Settings.FOREGROUND_COLOR );
		cancel_button.addActionListener( new CancelListener() );

		submit_button = new JButton("Submit");
		submit_button.setFont( submit_button.getFont().deriveFont( Settings.FONT_SIZE ) );
		submit_button.setBackground(Settings.MAIN_COLOR);
		submit_button.setForeground( Settings.FOREGROUND_COLOR );
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
			cp.getController().getCovModel().getDwd().setDefault_workers(dwd.getDefault_workers());
			
			for (int i=0; i<s.length; i++) {
				cp.getNameLabels()[i].setText( s[i] );
			}
			
			cp.getParent().revalidate();
			cp.getParent().repaint();
			
			// close EditWorkersPanel
			frame.setVisible( false );
			frame.dispose();
			
		}
		
	}
	
	
/* PUBLIC METHODS =========================================================== */
	
	/**
	 * Initialize and load edit workers panel and frame and freeze Covenant
	 * Panel.
	 */
	public static void initializePanelFrame(CovenantPanel covPanel) {
	    
	    JFrame editWorkerFrame = new JFrame();
	    editWorkerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    editWorkerFrame.setResizable(false);
	    editWorkerFrame.addWindowListener(
	            new FrameCloseListener(covPanel.getCovFrame()));
        
        EditWorkersPanel editWorkersPanel =
                new EditWorkersPanel(editWorkerFrame, covPanel);
        
        editWorkerFrame.add(editWorkersPanel);
        editWorkerFrame.pack();
        StaticMethods.findSetLocation(editWorkerFrame);
        editWorkerFrame.setVisible(true);
	}
	
}	

