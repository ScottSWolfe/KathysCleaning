package src.java.submit.view;

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

import net.miginfocom.swing.MigLayout;
import src.java.general.controller.TimeDocFilter;
import src.java.general.controller.TimeKeyListener;
import src.java.general.model.DefaultWorkerData;
import src.java.general.view.DefaultWorkerPanel;
import src.java.general.view.TabbedPane;
import src.java.submit.controller.AddHouseListener;
import src.java.submit.controller.DeleteHouseListener;
import src.java.submit.controller.ExceptionListener;
import src.java.submit.controller.HouseNameDocListener;
import src.java.submit.controller.HousePayDocFilter;
import src.java.submit.controller.MoveDownListener;
import src.java.submit.controller.MoveUpListener;
import src.java.submit.model.ExceptionData;


public class HousePanel extends JPanel {

// FIELDS
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4893953882769089967L;
	
	
	DayPanel day_panel;
	DefaultWorkerData dwd;
	JFrame frame;
	TabbedPane tp;
	
	AbstractDocument abdoc;
	
	public ExceptionData exception_data;
	
	String title;
	Border border;
	
	public DefaultWorkerPanel worker_panel;
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
	
	
	
// CONSTRUCTOR
	
	public HousePanel(String title, DefaultWorkerData dwd, DayPanel day_panel, JFrame frame, TabbedPane tp) {
		
		this.day_panel = day_panel;
		this.dwd = dwd;
		this.frame = frame;
		this.tp = tp;
		
		this.exception_data = new ExceptionData();
		
		setLayout(new MigLayout("insets 0","[grow][grow][grow][grow][grow][grow]","[]"));
		setBackground( DayPanel.BACKGROUND_COLOR );
		setBorder(BorderFactory.createTitledBorder( new String() ));
		
		JPanel house_name_panel = houseNamePanel();
		JPanel pay_panel = payPanel();
		JPanel time_panel = timePanel();
		worker_panel = new DefaultWorkerPanel( dwd, DayPanel.BACKGROUND_COLOR, time_end_txt, exceptions );
		//JPanel exceptions_panel = exceptionsPanel();
		JPanel button_panel = buttonPanel();
		
		exceptions = new JButton("Exceptions");
		exceptions.setFont( exceptions.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		exceptions.addActionListener( new ExceptionListener( this, dwd, frame ) );

		
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
	
	
	
// PRIVATE CONSTRUCTION METHODS
	
	//house name panel
	private JPanel houseNamePanel(){
		JPanel panel = new JPanel();
		
		panel.setLayout(new MigLayout("insets 0, ay 50%"));
		panel.setBackground( DayPanel.BACKGROUND_COLOR );
		
		house_name_label = new JLabel("House Name");
		house_name_label.setFont( house_name_label.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		
		house_name_txt = new JTextField(10);
		house_name_txt.setFont( house_name_txt.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		house_name_txt.getDocument().addDocumentListener( new HouseNameDocListener( this ) );
		
		/*
		 * this works but to be even better I changed to above
			AbstractDocument house_name_doc = (AbstractDocument) house_name_txt.getDocument();
		HouseNameDocFilter house_name_doc_filter = new HouseNameDocFilter( this );
		house_name_doc.setDocumentFilter( house_name_doc_filter );
		*/
		
		 
		panel.add(house_name_label,"wrap, gap 3");
		panel.add(house_name_txt);
		 
		return panel;
	}
	
	
	//pay panel
	private JPanel payPanel(){
		JPanel panel = new JPanel();
		
		pay_label = new JLabel("$ Earned");
		pay_label.setFont( pay_label.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		
		pay_txt = new JTextField(7);
		pay_txt.setFont( pay_txt.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		
		AbstractDocument pay_txt_doc = (AbstractDocument)pay_txt.getDocument();
		pay_txt_doc.setDocumentFilter( new HousePayDocFilter() );
		
		panel.setLayout( new MigLayout("insets 0, ay 50%") );
		panel.setBackground( DayPanel.BACKGROUND_COLOR );
		
		panel.add(pay_label, "wrap, gap 3");
		panel.add(pay_txt);
		
		return panel;
			
	}
	
		
	//time panel
	private JPanel timePanel(){
		JPanel panel = new JPanel();
		
		time_label = new JLabel("Time");
		time_label.setFont( time_label.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		
		time_begin_txt = new JTextField( 5 );
		time_begin_txt.setFont( time_begin_txt.getFont().deriveFont( DayPanel.FONT_SIZE ) );
			AbstractDocument time_begin_doc = (AbstractDocument)time_begin_txt.getDocument();
			TimeDocFilter tdf_begin = new TimeDocFilter(time_begin_txt );
		time_begin_doc.setDocumentFilter( tdf_begin );
		time_begin_txt.addKeyListener( new TimeKeyListener( tdf_begin ) );
		//time_begin_txt.addFocusListener(  );
		
		
		time_end_txt = new JTextField(5);
		time_end_txt.setFont( time_end_txt.getFont().deriveFont( DayPanel.FONT_SIZE ) );
			AbstractDocument time_end_doc = (AbstractDocument)time_end_txt.getDocument();
			TimeDocFilter tdf_end = new TimeDocFilter(time_end_txt );
		time_end_doc.setDocumentFilter( tdf_end );
		time_end_txt.addKeyListener( new TimeKeyListener( tdf_end ) );
		//time_end_txt.addFocusListener(  );
		
		
		
		panel.setLayout( new MigLayout("insets 0, ay 50%") );
		panel.setBackground( DayPanel.BACKGROUND_COLOR );
		
		/*
		panel.add(time_label, "cell 0 0 2 1, wrap, ax 50%");
		panel.add(time_begin_txt);
		panel.add(time_end_txt, "gap 0");
		*/
		panel.add(time_label, "cell 0 0 1 1, wrap, ax 50%");
		panel.add(time_begin_txt, "cell 0 1");
		panel.add(time_end_txt, "cell 0 1, gap 0");
		
		return panel;
		
	}
	
	
	
	
	/*
	//exception panel
	private JPanel exceptionsPanel(){
		JPanel panel = new JPanel();
		
		exceptions = new JButton("Exceptions");
		
		panel.add(exceptions);
			 
		//panel.setBorder(new EmptyBorder(10,10,10,10));
			 
		return panel;
	}
	*/
	
	
	// button panel
	private JPanel buttonPanel(){
		JPanel panel = new JPanel();
		
		move_up = new JButton("Up");
		move_up.setFont( move_up.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		//move_up.setBackground( DayPanel.MOVE_COLOR );
		//move_up.setForeground( DayPanel.FOREGROUND_COLOR );
		
		move_down = new JButton("Down");
		move_down.setFont( move_down.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		//move_down.setBackground( DayPanel.MOVE_COLOR );
		//move_down.setForeground( DayPanel.FOREGROUND_COLOR );
		
		add_house = new JButton("Add");
		add_house.setFont( add_house.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		//add_house.setBackground( DayPanel.ADD_HOUSE_COLOR );
		//add_house.setForeground( DayPanel.FOREGROUND_COLOR );
		
		delete_house = new JButton("Delete");
		delete_house.setFont( delete_house.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		//delete_house.setBackground( DayPanel.DELETE_HOUSE_COLOR );
		//delete_house.setForeground( DayPanel.FOREGROUND_COLOR );
		
		panel.setLayout( new MigLayout("insets 0") );
		panel.setBackground( DayPanel.BACKGROUND_COLOR );
		
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

	
	
	// PRIVATE METHODS
	
	/*
	private void addFlexibleFocusListeners(){
		
		// adding focus listeners for textfields and buttons
		
		int length = day_panel.house_panel.length;
		int index = 0;
		for (int i=0; i<length; i++) {
			if ( day_panel.house_panel[i] == this ) {
				index = i;
			}
		}
			
		HousePanel hp_up;
		HousePanel hp_down;
					
		if( index > 0 ){
			hp_up = day_panel.house_panel[index-1];
		}
		else {
			hp_up = new HousePanel();  // all null fields
		}
		if ( index < length - 1 ) {
			hp_down = day_panel.house_panel[index+1];
		}
		else {
			hp_down = new HousePanel(); // all null fields
		}
			
			house_name_txt.addFocusListener( new FlexibleFocusListener( house_name_txt,
					FlexibleFocusListener.TEXTFIELD,
					null, pay_txt,
					hp_up.house_name_txt, hp_down.house_name_txt, 
					null) );
			
			pay_txt.addFocusListener( new FlexibleFocusListener(pay_txt, 
					FlexibleFocusListener.TEXTFIELD,
					house_name_txt, time_begin_txt,
					hp_up.pay_txt, hp_down.pay_txt,
					null));
				
			time_begin_txt.addFocusListener(new FlexibleFocusListener(time_begin_txt, 
					FlexibleFocusListener.TEXTFIELD,
					pay_txt, time_end_txt,
					hp_up.time_begin_txt, hp_down.time_begin_txt,
					null));
			
			time_end_txt.addFocusListener(new FlexibleFocusListener(time_end_txt, 
					FlexibleFocusListener.TEXTFIELD,
					time_begin_txt, null,  // TODO: add in worker checkboxes
					hp_up.time_end_txt, hp_down.time_end_txt,
					hp_down.time_begin_txt));
			
			exceptions.addFocusListener( new FlexibleFocusListener(exceptions, 
					FlexibleFocusListener.BUTTON, null, null, null, null, null));
		

		
	}
	*/
	
	
//  PUBLIC METHODS

	public HousePanel copyPanel( ) {
		
		HousePanel new_panel = new HousePanel(this.title, this.dwd, this.day_panel, this.frame, this.tp);
		
		//create public methods to do this in best practice:
		//new_panel.title = this.title;
		new_panel.house_name_txt.setText(this.house_name_txt.getText());
		new_panel.pay_txt.setText(this.pay_txt.getText());
		new_panel.time_begin_txt.setText(this.time_begin_txt.getText());
		new_panel.time_end_txt.setText(this.time_end_txt.getText());
		
		
		for (int i=0; i<ExceptionPanel.EXCEPTION_ROWS; i++) {  
			new_panel.exception_data.worker_name[i] = this.exception_data.worker_name[i];
			new_panel.exception_data.time_begin[i] = this.exception_data.time_begin[i];
			new_panel.exception_data.time_end[i] = this.exception_data.time_end[i];
		}
		new_panel.exception_data.edited = true;
		
		
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
	
	
	public HousePanel changeHouseWorkers( DefaultWorkerData dwd ) {
		
		HousePanel new_panel = new HousePanel(this.title, dwd, this.day_panel, this.frame, this.tp);
		
		//create public methods to do this in best practice:
		new_panel.title = this.title;
		new_panel.house_name_txt.setText(this.house_name_txt.getText());
		new_panel.pay_txt.setText(this.pay_txt.getText());
		new_panel.time_begin_txt.setText(this.time_begin_txt.getText());
		new_panel.time_end_txt.setText(this.time_end_txt.getText());
		
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
