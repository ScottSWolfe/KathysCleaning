package com.github.scottswolfe.kathyscleaning.nextweek.view;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;

import com.github.scottswolfe.kathyscleaning.general.model.DefaultWorkerData;
import com.github.scottswolfe.kathyscleaning.general.view.DefaultWorkerPanel;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.nextweek.controller.NW_AddHouseListener;
import com.github.scottswolfe.kathyscleaning.nextweek.controller.NW_DeleteHouseListener;
import com.github.scottswolfe.kathyscleaning.nextweek.controller.NW_MoveDownListener;
import com.github.scottswolfe.kathyscleaning.nextweek.controller.NW_MoveUpListener;
import com.github.scottswolfe.kathyscleaning.submit.model.ExceptionData;

import net.miginfocom.swing.MigLayout;


public class NW_HousePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8949680089019757964L;
	
	
	
// FIELDS
	
	NW_DayPanel day_panel;
	DefaultWorkerData dwd;
	JFrame frame;
	
	AbstractDocument abdoc;
	public int lastKeyPress = 0;
	
	ExceptionData exception_data;
	
	String title;
	Border border;
	
	public DefaultWorkerPanel worker_panel;
	
	JLabel house_name_label;
	public JTextField house_name_txt;
	
	JButton move_up;
	JButton move_down;
	public JButton add_house;
	public JButton delete_house;
	
	
	
// CONSTRUCTOR
	
	public NW_HousePanel( DefaultWorkerData dwd, NW_DayPanel day_panel, JFrame frame ) {
		
		this.day_panel = day_panel;
		this.dwd = dwd;
		this.frame = frame;
				
		setBorder(BorderFactory.createTitledBorder( new String() ));
		setBackground(Settings.BACKGROUND_COLOR);
		setLayout(new MigLayout("insets 0","[grow][grow][grow]","[]"));

		JPanel house_name_panel = houseNamePanel();
		worker_panel = new DefaultWorkerPanel( dwd, Settings.BACKGROUND_COLOR, house_name_txt, null );
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
		
		house_name_txt = new JTextField(10);
		house_name_txt.setFont( house_name_txt.getFont().deriveFont( Settings.FONT_SIZE ) );
		 
		panel.setLayout(new MigLayout("insets 0 1 0 1, ay 50%"));
		panel.setBackground(Settings.BACKGROUND_COLOR);
		
		panel.add(house_name_label,"wrap");
		panel.add(house_name_txt);
		 
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
		
		move_up.addActionListener( new NW_MoveUpListener(day_panel,this,dwd,frame) );
		add_house.addActionListener( new NW_AddHouseListener(day_panel,this,dwd,frame) );
		move_down.addActionListener( new NW_MoveDownListener(day_panel,this,dwd,frame) );
		delete_house.addActionListener( new NW_DeleteHouseListener(day_panel,this,dwd,frame) );
		
		return panel;
	}

	
	
//  PUBLIC METHODS

	public NW_HousePanel copyPanel( ) {
		
		NW_HousePanel new_panel = new NW_HousePanel( this.dwd, this.day_panel, this.frame );
		
		//create public methods to do this in best practice:
		new_panel.house_name_txt.setText(this.house_name_txt.getText());
		
		int rows = DefaultWorkerPanel.NORM_ROWS;
		int columns = DefaultWorkerPanel.NORM_COLUMNS;
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				if(this.worker_panel.worker[i][j].isSelected()){
					new_panel.worker_panel.worker[i][j].setSelected(true);
				}
			}
		}
		
		return new_panel;
		
	}
	
	public String[] getSelectedWorkers() {
		return worker_panel.getSelected();
	}
	
	
	
	public NW_HousePanel changeHouseWorkers( DefaultWorkerData dwd ) {
		
		NW_HousePanel new_panel = new NW_HousePanel( dwd, this.day_panel, this.frame);
		
		//create public methods to do this in best practice:
		new_panel.title = this.title;
		new_panel.house_name_txt.setText(this.house_name_txt.getText());
		
		/*
		int rows = DefaultWorkerPanel.ROWS;
		int columns = DefaultWorkerPanel.COLUMNS;
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				if(this.worker_panel.worker[i][j].isSelected()){
					new_panel.worker_panel.worker[i][j].setSelected(true);
				}
			}
		}
		*/
		
		return new_panel;
		
	}
	
	
	public void setTitle( String title ){
		this.title = title;
		this.setBorder(BorderFactory.createTitledBorder(border,title));
	}
	
	
	
	
}
