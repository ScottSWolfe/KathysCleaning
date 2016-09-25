package src.java.general.view;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import src.java.general.controller.FlexibleFocusListener;
import src.java.general.model.DefaultWorkerData;
import src.java.submit.view.DayPanel;



public class DefaultWorkerPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6634101189352792441L;
	
	
	// FIELDS
	
	public static final int NORM_ROWS = 2;
	public static final int NORM_COLUMNS = 5;
	
	public final static int COV_ROWS = 2;
	public final static int COV_COLUMNS = 6;
	// TODO repair this so ROWS and COLUMNS can be changed to any number and program still works
	
	public JCheckBox[][] worker;
	DefaultWorkerData dwd;
	
	public int rows;
	public int columns;
	
	Component left_component; // for focus listener
	Component right_component;
	
	
	//CONSTRUCTOR
	public DefaultWorkerPanel( DefaultWorkerData dwd, Color color, Component left_component, Component right_component ) {
		
		this.dwd = dwd;
		this.rows = NORM_ROWS;
		this.columns = NORM_COLUMNS;
		this.left_component = left_component;
		this.right_component = right_component;
		
		worker = new JCheckBox[rows][columns];
		
		setLayout( new MigLayout("insets 0") );
		setBackground(color);
		
		for(int i=0; i < rows; i++) {
			for(int j=0; j < columns; j++) {
				
				if (columns*i + j < dwd.default_workers.length) {
					worker[i][j] = new JCheckBox( dwd.default_workers[ columns*i + j ] ); 
				}
				else {
					worker[i][j] = new JCheckBox( "" );
				}
				
				
				//worker[i][j].( DayPanel.MAIN_COLOR );
				worker[i][j].setFont( worker[i][j].getFont().deriveFont( DayPanel.FONT_SIZE ) );
				worker[i][j].setBackground(color);
				
				if(i<columns-1 && j>columns-2) {
					add(worker[i][j], "grow, wrap");
				}
				else {
					add(worker[i][j], "grow");
				}
				
			}
		}
		
		addFlexibleFocusListeners();
		
	}
	
	
	public DefaultWorkerPanel( DefaultWorkerData dwd, Color color, int rows, int columns, Component left_component, Component right_component ) {
		
		this.dwd = dwd;
		this.rows = rows;
		this.columns = columns;
		this.left_component = left_component;
		this.right_component = right_component;
		
		worker = new JCheckBox[rows][columns];
		
		
		setLayout( new MigLayout("insets 0") );
		setBackground(color);
		
		for(int i=0; i < rows; i++) {
			for(int j=0; j < columns; j++) {
				
				if (columns*i + j < dwd.default_workers.length) {
					worker[i][j] = new JCheckBox( dwd.default_workers[ columns*i + j ] ); 
				}
				else {
					worker[i][j] = new JCheckBox( "" );
				}
				
				//worker[i][j].( DayPanel.MAIN_COLOR );
				worker[i][j].setFont( worker[i][j].getFont().deriveFont( DayPanel.FONT_SIZE ) );
				worker[i][j].setBackground(color);
				
				if(i<rows-1 && j>columns-2) {
					add(worker[i][j], "grow, wrap");
				}
				else {
					add(worker[i][j], "grow");
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
					left_box = worker[i][j-1];
				}
				else if ( j <= 0 ) {
					left_box = left_component;
				}
				if ( j < columns - 1 ) {
					right_box = worker[i][j+1];
				}
				else if ( j >= columns - 1 ) {
					right_box = right_component;
				}
				
				if ( i > 0 ) {
					up_box = worker[i-1][j];
				}
				else if ( i <= 0 ) {
					up_box = null;
				}
				if ( i < rows - 1 ) {
					down_box = worker[i+1][j];
				}
				else if ( i >= rows - 1 ) {
					down_box = null;
				}
				
				if ( i < rows - 1  && j >= columns - 1 ) {
					enter_box = worker[i+1][0];
				}

				
				FlexibleFocusListener ffl = new FlexibleFocusListener( worker[i][j], 
						FlexibleFocusListener.CHECKBOX,
						left_box, right_box,
						up_box, down_box,
						enter_box);
				
				worker[i][j].addFocusListener( ffl );
				
			}
		}
		
	}
	
	
	
	// PUBLIC METHODS
	
	public void setSelected(String[] sel_workers) {
		
		for(int i=0; i < worker.length; i++){
			for(int j=0; j < worker[i].length; j++) {
				for(int k=0; k < sel_workers.length; k++) {
				
					if (sel_workers[k].equals( worker[i][j].getText() )) {
						worker[i][j].setSelected(true);
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
				if(worker[i][j].isSelected()){
					selected_workers[k] = worker[i][j].getText();
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
