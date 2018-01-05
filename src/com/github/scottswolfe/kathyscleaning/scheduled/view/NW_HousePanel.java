package com.github.scottswolfe.kathyscleaning.scheduled.view;


import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.WorkerPanel;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_AddHouseListener;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_DeleteHouseListener;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_MoveDownListener;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_MoveUpListener;

import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
public class NW_HousePanel extends JPanel {	

    
// TODO: TO BE REMOVED 

    WorkerList workers;
    
    
// COMPONENTS
    
    JFrame parent_frame;
	NW_DayPanel parent_day_panel;
		
	String title;
	public WorkerPanel worker_panel;
	JLabel house_name_label;
	public JTextField house_name_text_field;
	JButton move_up;
	JButton move_down;
	public JButton add_house;
	public JButton delete_house;
	
	
// CONSTRUCTOR
	
	public NW_HousePanel(WorkerList not_used, NW_DayPanel day_panel, JFrame frame) {
		
	    this.workers = new WorkerList(WorkerList.HOUSE_WORKERS);
		this.parent_day_panel = day_panel;
		this.parent_frame = frame;
				
		setBorder(BorderFactory.createTitledBorder( new String() ));
		setBackground(Settings.BACKGROUND_COLOR);
		setLayout(new MigLayout("insets 0","[grow][grow][grow]","[]"));

		JPanel house_name_panel = houseNamePanel();
		worker_panel = new WorkerPanel( workers, Settings.BACKGROUND_COLOR, house_name_text_field, null );
		JPanel button_panel = buttonPanel();
		
		add(house_name_panel, "growy");
		add( new JSeparator(SwingConstants.VERTICAL), "growy" );
		
		add(worker_panel, "push y");
		add( new JSeparator(SwingConstants.VERTICAL), "growy" );
		
		add(button_panel, "growy");
		
	}
	
	
	public NW_HousePanel() {
		
	}
	
	
// PRIVATE METHODS
	
	//house name panel
	private JPanel houseNamePanel(){
		JPanel panel = new JPanel();
		
		house_name_label = new JLabel(" House Name");
		house_name_label.setFont( house_name_label.getFont().deriveFont( Settings.FONT_SIZE ) );
		house_name_label.setForeground(Settings.MAIN_COLOR);
		
		house_name_text_field = new JTextField(10);
		house_name_text_field.setFont( house_name_text_field.getFont().deriveFont( Settings.FONT_SIZE ) );
		 
		panel.setLayout(new MigLayout("insets 0 1 0 1, ay 50%"));
		panel.setBackground(Settings.BACKGROUND_COLOR);
		
		panel.add(house_name_label,"wrap");
		panel.add(house_name_text_field);
		 
		return panel;
	}
	
	
	// button panel
	private JPanel buttonPanel(){
		JPanel panel = new JPanel();
		
		move_up = new JButton("Up");
		move_up.setFont( move_up.getFont().deriveFont( Settings.FONT_SIZE ) );
		
		
		move_down = new JButton("Down");
		move_down.setFont( move_down.getFont().deriveFont( Settings.FONT_SIZE ) );
		
		add_house = new JButton("Add");
		add_house.setFont( add_house.getFont().deriveFont( Settings.FONT_SIZE ) );
		//add_house.setBackground( DayPanel.ADD_HOUSE_COLOR );
		
		delete_house = new JButton("Delete");
		delete_house.setFont( delete_house.getFont().deriveFont( Settings.FONT_SIZE ) );
		//delete_house.setBackground( DayPanel.DELETE_HOUSE_COLOR );
		
		panel.setLayout( new MigLayout("insets 0") );
		panel.setBackground(Settings.BACKGROUND_COLOR);
		
		panel.add(move_up, "growx");
		panel.add(add_house,"wrap, growx");
		panel.add(move_down);
		panel.add(delete_house);
		
		move_up.addActionListener(new NW_MoveUpListener(parent_day_panel, this, workers, parent_frame));
		add_house.addActionListener(new NW_AddHouseListener(parent_day_panel,this, workers, parent_frame));
		move_down.addActionListener(new NW_MoveDownListener(parent_day_panel,this, workers, parent_frame));
		delete_house.addActionListener(new NW_DeleteHouseListener(parent_day_panel,this, workers, parent_frame));
		
		return panel;
	}

	
	
//  PUBLIC METHODS

	public NW_HousePanel copyPanel( ) {
		
		NW_HousePanel new_panel = new NW_HousePanel( this.workers, this.parent_day_panel, this.parent_frame );
		
		//create public methods to do this in best practice:
		new_panel.house_name_text_field.setText(this.house_name_text_field.getText());
		
		int rows = WorkerPanel.NORM_ROWS;
		int columns = WorkerPanel.NORM_COLUMNS;
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				if(this.worker_panel.workerCheckBoxes[i][j].isSelected()){
					new_panel.worker_panel.workerCheckBoxes[i][j].setSelected(true);
				}
			}
		}
		
		return new_panel;
		
	}
	
	public List<String> getSelectedWorkers() {
		return worker_panel.getSelected();
	}
	
	
	
	public void changeHouseWorkers( WorkerList workers ) {
		
	    worker_panel.setWorkers(workers);
		
	}
	
	
	public void setTitle( String title ){
		this.title = title;
		this.setBorder(BorderFactory.createTitledBorder(null, title));
	}
	
	public String getHouseName() {
	    return house_name_text_field.getText();
	}
	
	public void setHouseName(String house_name) {
        house_name_text_field.setText(house_name);
    }
	
}
