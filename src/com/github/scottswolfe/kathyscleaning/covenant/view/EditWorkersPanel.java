package com.github.scottswolfe.kathyscleaning.covenant.view;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.scottswolfe.kathyscleaning.covenant.controller.EditWorkersController;
import com.github.scottswolfe.kathyscleaning.general.controller.FlexibleFocusListener;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;

/**
 *  This panel appears when the user selects "Edit" on the Covenant input panel.
 *  This panel allows the user to adjust the available workers.
 */
@SuppressWarnings("serial")
public class EditWorkersPanel extends JPanel {

	
/* FIELDS =================================================================== */
    
    /**
     * The frame containing this panel.
     */
	private JFrame editWorkersFrame;
	
	/**
	 * The controller controlling this panel.
	 */
	private EditWorkersController controller;
	
	/**
	 * The number of rows of workers to choose from.
	 */
	private int rows;
	
	
	
	
/* COMPONENTS =============================================================== */
	
	/**
	 * The combo boxes to select each worker. 
	 */
	private JComboBox<String>[] worker_combo;
	
	/**
	 * The Cancel button.
	 */
	private JButton cancel_button;
	
	/**
	 * The Submit button
	 */
	private JButton submit_button;
	

	
	
/* CONSTRUCTOR ============================================================== */
	@SuppressWarnings("unchecked")
    public EditWorkersPanel(JFrame frame, CovenantPanel cp) {
	
		this.editWorkersFrame = frame;
		
		controller = new EditWorkersController(this, cp);
		
		rows = CovenantPanel.ROWS;
		
		worker_combo = new JComboBox[rows];
		
		for (int i=0; i<rows; i++) {
			
				worker_combo[i] = new JComboBox<String>();
				worker_combo[i].setEditable(true);
				worker_combo[i].setSize(10, UNDEFINED_CONDITION);
				worker_combo[i].setFont( worker_combo[i].getFont().deriveFont( Settings.FONT_SIZE ) );
			
				worker_combo[i].addItem("");   // empty choice
				for(int k=0; k<cp.getController().getCovModel().getDwd().size(); k++){
					worker_combo[i].addItem(cp.getController().getCovModel().getDwd().get(k));
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
		cancel_button.addActionListener(controller.new CancelListener() );

		submit_button = new JButton("Submit");
		submit_button.setFont( submit_button.getFont().deriveFont( Settings.FONT_SIZE ) );
		submit_button.setBackground(Settings.MAIN_COLOR);
		submit_button.setForeground( Settings.FOREGROUND_COLOR );
		submit_button.addActionListener(controller.new SubmitListener() );
		
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
	

	
/* GETTERS/SETTERS ========================================================== */
	
    /**
     * @return the editWorkersFrame
     */
    public JFrame getFrame() {
        return editWorkersFrame;
    }

    /**
     * @param editWorkersFrame the editWorkersFrame to set
     */
    public void setFrame(JFrame editWorkersFrame) {
        this.editWorkersFrame = editWorkersFrame;
    }
	
    /**
     * Returns and string array list of the selected workers.
     */
    public ArrayList<String> getSelectedWorkers() {
        ArrayList<String> workers = new ArrayList<>();
        for (JComboBox<String> box : worker_combo) {
            workers.add(String.valueOf(box.getSelectedItem()));
        }
        return workers;
    }
    
}	

