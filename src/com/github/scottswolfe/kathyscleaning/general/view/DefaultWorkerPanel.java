package com.github.scottswolfe.kathyscleaning.general.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.github.scottswolfe.kathyscleaning.general.controller.FlexibleFocusListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;


/**
 * Allows the user to select the workers who worked.
 */
@SuppressWarnings("serial")
public class DefaultWorkerPanel extends JPanel {
		
	
/* CONSTANTS ================================================================ */
	
	public static final int NORM_ROWS = 2;
	public static final int NORM_COLUMNS = 5;
	
	public final static int COV_ROWS = 2;
	public final static int COV_COLUMNS = 6;
		
	
	
	
/* FIELDS =================================================================== */
	
	/**
	 * List of avilable workers.
	 */
	WorkerList workers;

	/**
	 * Number of rows of workers on this panel.
	 */
	public int rows;
	
	/**
     * Number of columns of workers on this panel.
     */
	public int columns;
	
	
	
	
/* COMPONENTS =============================================================== */
	
	/**
	 * Checkboxes for each worker.
	 */
	public JCheckBox[][] workerCheckBoxes;

	/*
	 * Components for focus listener.
	 */
	Component left_component;
	Component right_component;
	
	
/* CONSTRUCTOR ============================================================== */
	
	public DefaultWorkerPanel(WorkerList workers, Color color,
	        Component left_component, Component right_component) {
		
		this.workers = workers;
		this.rows = NORM_ROWS;
		this.columns = NORM_COLUMNS;
		this.left_component = left_component;
		this.right_component = right_component;
		
		workerCheckBoxes = new JCheckBox[rows][columns];
		
		setLayout( new MigLayout("insets 0") );
		setBackground(color);
		
		for(int i=0; i < rows; i++) {
			for(int j=0; j < columns; j++) {
				
				if (columns*i + j < workers.size()) {
					workerCheckBoxes[i][j] =
					        new JCheckBox(workers.get(columns*i + j)); 
				}
				else {
					workerCheckBoxes[i][j] = new JCheckBox("");
				}
				
				workerCheckBoxes[i][j].setFont(workerCheckBoxes[i][j].getFont()
				        .deriveFont(Settings.FONT_SIZE));
				workerCheckBoxes[i][j].setBackground(color);
				
				if(i<columns-1 && j>columns-2) {
					add(workerCheckBoxes[i][j], "grow, wrap");
				}
				else {
					add(workerCheckBoxes[i][j], "grow");
				}
				
			}
		}
		
		addFlexibleFocusListeners();
		
	}
	
	
	public DefaultWorkerPanel(WorkerList workers, Color color,
	        int rows, int columns,
	        Component left_component, Component right_component) {
		
		this.workers = workers;
		this.rows = rows;
		this.columns = columns;
		this.left_component = left_component;
		this.right_component = right_component;
		
		workerCheckBoxes = new JCheckBox[rows][columns];
		
		
		setLayout( new MigLayout("insets 0") );
		setBackground(color);
		
		for(int i=0; i < rows; i++) {
			for(int j=0; j < columns; j++) {
				
				if (columns*i + j < workers.size()) {
					workerCheckBoxes[i][j] =
					        new JCheckBox(workers.get(columns*i + j)); 
				}
				else {
					workerCheckBoxes[i][j] = new JCheckBox("");
				}
				
				//worker[i][j].(Settings.MAIN_COLOR);
				workerCheckBoxes[i][j].setFont(workerCheckBoxes[i][j].getFont()
				        .deriveFont(Settings.FONT_SIZE));
				workerCheckBoxes[i][j].setBackground(color);
				
				if(i<rows-1 && j>columns-2) {
					add(workerCheckBoxes[i][j], "grow, wrap");
				}
				else {
					add(workerCheckBoxes[i][j], "grow");
				}
				
			}
		}
		
		addFlexibleFocusListeners();
		
	}
	
	
	
	// PRIVATE METHODS
	
	private void addFlexibleFocusListeners() {
		
		for (int i=0; i<rows; i++) {
			for (int j=0; j<columns; j++) {
				
				Component left_box = null;
				Component right_box = null;
				Component up_box = null;
				Component down_box = null;
				Component enter_box = null;
				
				if ( j > 0 ) {
					left_box = workerCheckBoxes[i][j-1];
				}
				else if ( j <= 0 ) {
					left_box = left_component;
				}
				if ( j < columns - 1 ) {
					right_box = workerCheckBoxes[i][j+1];
				}
				else if ( j >= columns - 1 ) {
					right_box = right_component;
				}
				
				if ( i > 0 ) {
					up_box = workerCheckBoxes[i-1][j];
				}
				else if ( i <= 0 ) {
					up_box = null;
				}
				if ( i < rows - 1 ) {
					down_box = workerCheckBoxes[i+1][j];
				}
				else if ( i >= rows - 1 ) {
					down_box = null;
				}
				
				if ( i < rows - 1  && j >= columns - 1 ) {
					enter_box = workerCheckBoxes[i+1][0];
				}

				
				FlexibleFocusListener ffl = new FlexibleFocusListener( workerCheckBoxes[i][j], 
						FlexibleFocusListener.CHECKBOX,
						left_box, right_box,
						up_box, down_box,
						enter_box);
				
				workerCheckBoxes[i][j].addFocusListener( ffl );
				
			}
		}
		
	}
	
	
	
	// PUBLIC METHODS
	
	public void setSelected(String[] sel_workers) {
		
		for(int i=0; i < workerCheckBoxes.length; i++){
			for(int j=0; j < workerCheckBoxes[i].length; j++) {
				for(int k=0; k < sel_workers.length; k++) {
				
					if (sel_workers[k].equals( workerCheckBoxes[i][j].getText() )) {
						workerCheckBoxes[i][j].setSelected(true);
						break;
					}
					
				}
			}
		}
		
	}
	
	
	public String[] getSelected() {
		
		String[] selected_workers = new String[rows*columns];
		int k=0;
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				if(workerCheckBoxes[i][j].isSelected()){
					selected_workers[k] = workerCheckBoxes[i][j].getText();
					k++;
				}
			}
		}
		
		// changing selected_workers to correct length
		String[] temp = new String[k];
		for(int i=0; i<k; i++){
			temp[i] = selected_workers[i];
		}
		selected_workers = temp;
		
		return selected_workers;
	}
	
	
	
	
}
