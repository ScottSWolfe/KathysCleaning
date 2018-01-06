package com.github.scottswolfe.kathyscleaning.general.view;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import com.github.scottswolfe.kathyscleaning.general.controller.FlexibleFocusListener;
import com.github.scottswolfe.kathyscleaning.general.model.Worker;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;


/**
 * Allows the user to select the workers who worked.
 */
@SuppressWarnings("serial")
public class WorkerPanel extends JPanel {
		
	
/* CONSTANTS ================================================================ */
	
	public static final int NORM_ROWS = 2;
	public static final int NORM_COLUMNS = 5;
	
	public final static int COV_ROWS = 2;
	public final static int COV_COLUMNS = 6;
		
	
	
	
/* FIELDS =================================================================== */
	
	/**
	 * Number of rows of workers on this panel.
	 */
	private int rows;
	
	/**
     * Number of columns of workers on this panel.
     */
	private int columns;
	
	
	
	
/* COMPONENTS =============================================================== */
	
	/**
	 * Checkboxes for each worker.
	 */
	private JCheckBox[][] workerCheckBoxes;

	/*
	 * Components for focus listener.
	 */
	private Component left_component;
	private Component right_component;
	
	
	
	
/* CONSTRUCTOR ============================================================== */
	
	public WorkerPanel(WorkerList workers, Color color,
	        Component left_component, Component right_component) {

	    this(workers, color, NORM_ROWS, NORM_COLUMNS, left_component, right_component);
	}
	
	public WorkerPanel(WorkerList workers, Color color,
	        int rows, int columns,
	        Component left_component, Component right_component) {
		
		this.rows = rows;
		this.columns = columns;
		this.left_component = left_component;
		this.right_component = right_component;
		
		setLayout(new MigLayout("insets 0"));
		setBackground(color);
	    initializeWorkerCheckBoxes(workers, color);
		addFlexibleFocusListeners();
	}
	
	
	
	
/* PUBLIC METHODS =========================================================== */
	
	public void setWorkers(WorkerList workers) {
	    for(int i = 0; i < rows; i++) {
	        for(int j = 0; j < columns; j++) {
	            if (columns * i + j < workers.size()) {
	                workerCheckBoxes[i][j].setText(workers.get(columns * i + j).getName());
	                workerCheckBoxes[i][j].setSelected(workers.get(columns * i + j).isSelected());
	            } else {
	                workerCheckBoxes[i][j].setText("");
	                workerCheckBoxes[i][j].setSelected(false);
	            }
	        }
	    }
	}
	
	public WorkerList getWorkers() {
	    WorkerList workers = new WorkerList();
	    for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                String name = workerCheckBoxes[i][j].getText();
                if (name != null && name != "") {
                    workers.add(new Worker(name, workerCheckBoxes[i][j].isSelected()));
                }
            }
        }
	    return workers;
	}
	
	public void setSelected(String[] sel_workers) {
        List<String> list = new ArrayList<String>();
        for (String worker : sel_workers) {
            list.add(worker);
        }
        setSelected(list);
    }
	
	public void setSelected(List<String> workers) {
	    uncheckAllWorkers();
	    for (int i = 0; i < workerCheckBoxes.length; i++) {
            for (int j = 0; j < workerCheckBoxes[i].length; j++) {
                for (String worker : workers) {
                    if (worker.equals(workerCheckBoxes[i][j].getText())) {
                        workerCheckBoxes[i][j].setSelected(true);
                        break;
                    }
                }
            }
        }
	}
    
    public List<String> getSelected() {
        List<String> selected_workers = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j =0 ; j < columns; j++) {
                if (workerCheckBoxes[i][j].isSelected()) {
                    selected_workers.add(workerCheckBoxes[i][j].getText());
                }
            }
        }       
        return selected_workers;
    }
    
    public boolean isSelected(String name) {
        if (name == null) {
            return false;
        }
        for (int k = 0; k < rows; k++) {
            for (int l = 0; l < columns; l++) {
                if (name.equals(workerCheckBoxes[k][l].getText()) &&
                     workerCheckBoxes[k][l].isSelected()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public String getNameAt(int row, int col) {
        return workerCheckBoxes[row][col].getText();
    }
    
    public int getNumRows() {
        return rows;
    }
	
    public int getNumColumns() {
        return columns;
    }
    
    public JCheckBox getFirstCheckBox() {
        return workerCheckBoxes[0][0];
    }
	
	
    
    
/* PRIVATE METHODS ========================================================== */
	
	private void initializeWorkerCheckBoxes(WorkerList workers, Color color) { // TODO make better solution for getting color
	    workerCheckBoxes = new JCheckBox[rows][columns];
	    for(int i=0; i < rows; i++) {
            for(int j=0; j < columns; j++) {
                
                workerCheckBoxes[i][j] = new JCheckBox(); 
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
	    setWorkers(workers);
	}
	
	private void uncheckAllWorkers() {
	    for (int i = 0; i < rows; i++) {
	        for (int j = 0; j < columns; j++) {
	            workerCheckBoxes[i][j].setSelected(false);
	        }
	    }
	}
	
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
	
	
}
