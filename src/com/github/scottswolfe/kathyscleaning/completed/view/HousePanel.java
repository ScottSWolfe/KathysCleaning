package com.github.scottswolfe.kathyscleaning.completed.view;


import java.util.List;

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

import com.github.scottswolfe.kathyscleaning.completed.controller.AddHouseListener;
import com.github.scottswolfe.kathyscleaning.completed.controller.DeleteHouseListener;
import com.github.scottswolfe.kathyscleaning.completed.controller.ExceptionListener;
import com.github.scottswolfe.kathyscleaning.completed.controller.HouseNameDocListener;
import com.github.scottswolfe.kathyscleaning.completed.controller.HousePayDocFilter;
import com.github.scottswolfe.kathyscleaning.completed.controller.MoveDownListener;
import com.github.scottswolfe.kathyscleaning.completed.controller.MoveUpListener;
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionEntry;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeDocFilter;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeKeyListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.WorkerPanel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
public class HousePanel extends JPanel {

/* INSTANCE VARIABLES ======================================================= */	
	
	DayPanel day_panel;
	WorkerList dwd;
	JFrame frame;
	TabbedPane tp;
	
	AbstractDocument abdoc;
	private ExceptionData exception_data;
	

	
/* COMPONENTS =============================================================== */
	
	String title;
	Border border;
		
	public WorkerPanel worker_panel;
	
	public JButton exceptions;
	
	JLabel house_name_label;
	JLabel pay_label;
	JLabel time_label;
	
	public JTextField house_name_txt;
	public JTextField pay_txt;
	public JTextField time_begin_txt;
	public JTextField time_end_txt;
	
	JButton move_up;
	JButton move_down;
	public JButton add_house;
	public JButton delete_house;
	
	
	
/* CONSTRUCTORS ============================================================= */
	
	public HousePanel(String title, WorkerList dwd, DayPanel day_panel, JFrame frame, TabbedPane tp) {
		
		this.day_panel = day_panel;
		this.frame = frame;
		this.tp = tp;
		
		// TODO temporary hack
		dwd = new WorkerList(WorkerList.HOUSE_WORKERS);
		
		exception_data = new ExceptionData();
		
		setLayout(new MigLayout("insets 0","[grow][grow][grow][grow][grow][grow]","[]"));
		setBackground(Settings.BACKGROUND_COLOR);
		setBorder(BorderFactory.createTitledBorder(new String()));
		
		JPanel house_name_panel = houseNamePanel();
		JPanel pay_panel = payPanel();
		JPanel time_panel = timePanel();
		worker_panel = new WorkerPanel(dwd, Settings.BACKGROUND_COLOR, time_end_txt, exceptions);
		JPanel button_panel = buttonPanel();
				
		exceptions = new JButton("Exceptions");
		exceptions.setFont(exceptions.getFont().deriveFont(Settings.FONT_SIZE));
		exceptions.addActionListener(new ExceptionListener(dwd, frame, this));
		
		add(house_name_panel, "growy");
		add(pay_panel, "growy");
		add(time_panel, "growy");
		add( new JSeparator(SwingConstants.VERTICAL), "growy" );
		
		add(worker_panel, "pushy");
		add(exceptions, "hmin 50, pushy");
		add( new JSeparator(SwingConstants.VERTICAL), "growy" );
		
		add(button_panel, "growy");
	}
	
	public HousePanel() {
		
	}
	
	
	
/* PRIVATE CONSTRUCTION METHODS ============================================= */
	
	//house name panel
	private JPanel houseNamePanel(){
		JPanel panel = new JPanel();
		
		panel.setLayout(new MigLayout("insets 0, ay 50%"));
		panel.setBackground( Settings.BACKGROUND_COLOR );
		
		house_name_label = new JLabel("House Name");
		house_name_label.setFont( house_name_label.getFont().deriveFont( Settings.FONT_SIZE ) );
		
		house_name_txt = new JTextField(10);
		house_name_txt.setFont( house_name_txt.getFont().deriveFont( Settings.FONT_SIZE ) );
		house_name_txt.getDocument().addDocumentListener( new HouseNameDocListener( this ) );
		
		panel.add(house_name_label,"wrap, gap 3");
		panel.add(house_name_txt);
		 
		return panel;
	}
	
	//pay panel
	private JPanel payPanel(){
		JPanel panel = new JPanel();
		
		pay_label = new JLabel("$ Earned");
		pay_label.setFont( pay_label.getFont().deriveFont( Settings.FONT_SIZE ) );
		
		pay_txt = new JTextField(7);
		pay_txt.setFont( pay_txt.getFont().deriveFont( Settings.FONT_SIZE ) );
		
		AbstractDocument pay_txt_doc = (AbstractDocument)pay_txt.getDocument();
		pay_txt_doc.setDocumentFilter( new HousePayDocFilter() );
		
		panel.setLayout( new MigLayout("insets 0, ay 50%") );
		panel.setBackground( Settings.BACKGROUND_COLOR );
		
		panel.add(pay_label, "wrap, gap 3");
		panel.add(pay_txt);
		
		return panel;
	}
	
	//time panel
	private JPanel timePanel(){
		JPanel panel = new JPanel();
		
		time_label = new JLabel("Time");
		time_label.setFont( time_label.getFont().deriveFont( Settings.FONT_SIZE ) );
		
		time_begin_txt = new JTextField( 5 );
		time_begin_txt.setFont( time_begin_txt.getFont().deriveFont( Settings.FONT_SIZE ) );
			AbstractDocument time_begin_doc = (AbstractDocument)time_begin_txt.getDocument();
			TimeDocFilter tdf_begin = new TimeDocFilter(time_begin_txt );
		time_begin_doc.setDocumentFilter( tdf_begin );
		time_begin_txt.addKeyListener( new TimeKeyListener( tdf_begin ) );
		
		time_end_txt = new JTextField(5);
		time_end_txt.setFont( time_end_txt.getFont().deriveFont( Settings.FONT_SIZE ) );
			AbstractDocument time_end_doc = (AbstractDocument)time_end_txt.getDocument();
			TimeDocFilter tdf_end = new TimeDocFilter(time_end_txt );
		time_end_doc.setDocumentFilter( tdf_end );
		time_end_txt.addKeyListener( new TimeKeyListener( tdf_end ) );
		
		panel.setLayout( new MigLayout("insets 0, ay 50%") );
		panel.setBackground( Settings.BACKGROUND_COLOR );
		
		panel.add(time_label, "cell 0 0 1 1, wrap, ax 50%");
		panel.add(time_begin_txt, "cell 0 1");
		panel.add(time_end_txt, "cell 0 1, gap 0");
		
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
		
		delete_house = new JButton("Delete");
		delete_house.setFont( delete_house.getFont().deriveFont( Settings.FONT_SIZE ) );
		
		panel.setLayout( new MigLayout("insets 0") );
		panel.setBackground( Settings.BACKGROUND_COLOR );
		
		panel.add(move_up, "growx");
		panel.add(add_house,"wrap, growx");
		panel.add(move_down);
		panel.add(delete_house);
		
		move_up.addActionListener( new MoveUpListener(day_panel,this,dwd,frame,tp) );
		add_house.addActionListener( new AddHouseListener(day_panel,this,dwd,frame,tp) );
		move_down.addActionListener( new MoveDownListener(day_panel,this,dwd,frame,tp) );
		delete_house.addActionListener( new DeleteHouseListener(day_panel,this,dwd,frame,tp) );
		
		return panel;
	}	

	
	
/* PUBLIC METHODS =========================================================== */

	public HousePanel copyPanel( ) {
		
		HousePanel new_panel = new HousePanel(this.title, this.dwd, this.day_panel, this.frame, this.tp);
		
		new_panel.house_name_txt.setText(this.house_name_txt.getText());
		new_panel.pay_txt.setText(this.pay_txt.getText());
		new_panel.time_begin_txt.setText(this.time_begin_txt.getText());
		new_panel.time_end_txt.setText(this.time_end_txt.getText());
		new_panel.exception_data = this.exception_data;
				
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
	
	public HousePanel changeHouseWorkers(WorkerList dwd) {
		HousePanel new_panel = new HousePanel(this.title, dwd, this.day_panel, this.frame, this.tp);		
		new_panel.title = this.title;
		new_panel.house_name_txt.setText(this.house_name_txt.getText());
		new_panel.pay_txt.setText(this.pay_txt.getText());
		new_panel.time_begin_txt.setText(this.time_begin_txt.getText());
		new_panel.time_end_txt.setText(this.time_end_txt.getText());
		return new_panel;
	}
	
	public void setTitle( String title ){
		this.title = title;
		this.setBorder(BorderFactory.createTitledBorder(border,title));
	}
	
	/**
	 * Sets the exception button to a new color if an exception exists
	 *  
	 * @param isException true if there is an exception, otherwise false
	 */
	public void setExceptionButtonColor(boolean isException) {
	    exceptions.setForeground(Settings.MAIN_COLOR);
	}
	
	public ExceptionData getExceptionData() {
	    return exception_data;
	}
	
	public void setExceptionData(ExceptionData exceptionData) {
	    exception_data = exceptionData;
	    setExceptionButtonColor();
	}
	
	public void setExceptionDataEntries(List<ExceptionEntry> entries) {
	    exception_data.setEntries(entries);
	    setExceptionButtonColor();
	}
	
	public boolean hasExceptionData() {
	    return exception_data.isException();
	}
	
	private void setExceptionButtonColor() {
        if (exception_data.isException()) {
            exceptions.setBackground(Settings.MAIN_COLOR);
        }
        // TODO add code to set button to default if no exceptions
	}
	
}
