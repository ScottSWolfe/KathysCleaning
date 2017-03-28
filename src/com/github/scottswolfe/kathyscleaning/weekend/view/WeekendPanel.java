package com.github.scottswolfe.kathyscleaning.weekend.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.model.Worker;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendModel;

import net.miginfocom.swing.MigLayout;

/**
 * Panel in which user can enter other cleaning jobs.
 * 
 */
@SuppressWarnings("serial")
public class WeekendPanel extends JPanel {
	
	// FIELDS
    GeneralController<WeekendPanel, WeekendModel> controller;
	JFrame frame;
	
	public static final int NUM_JOB_PANELS = 3;
	public JobPanel[] jp;
	
	int mode;
	int wk;
	
	public static final int NUM_JOBS_CAP = 4;
	
	
	// COMPONENTS
	JLabel heading_label;
	
	public JLabel date_label;
	public Calendar date;
	
	public JRadioButton weekA_button;
	public JRadioButton weekB_button;
	public JRadioButton neither_button;
	ButtonGroup bg;
	
	JButton submit_button;
	
	
	
	// CONSTRUCTORS
	public WeekendPanel (GeneralController<WeekendPanel, WeekendModel> controller, int mode, int wk) {
		this.controller = controller;
		this.mode = mode;
		this.wk = wk;
		
		setLayout( new MigLayout() );
		setBackground(Settings.BACKGROUND_COLOR);
		
		add( createHeaderPanel(), "grow, wrap 0" );
		add( createJobsWorkedPanel(), "grow" );
	}
	
	
	
	// CONSTRUCTION METHODS
	
	private JPanel createHeaderPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("fill"));
		panel.setBackground(Settings.BACKGROUND_COLOR);
		
		heading_label = new JLabel();
		heading_label.setText( "Weekend Work" );
		heading_label.setFont( heading_label.getFont().deriveFont(Settings.HEADER_FONT_SIZE) );
		heading_label.setBackground( Settings.BACKGROUND_COLOR );
		
		Calendar date = Settings.completedStartDay;
		date_label = new JLabel();
		String s = new String( "Week of " +
				(Integer.parseInt(String.valueOf(date.get(Calendar.MONTH))) + 1) +
				"/" + (date.get(Calendar.DATE)-1) + "/" + date.get(Calendar.YEAR) );
		date_label.setText(s);
		date_label.setFont(date_label.getFont().deriveFont( Settings.FONT_SIZE ));
		date_label.setBackground(Settings.BACKGROUND_COLOR);
				
		submit_button = new JButton();
		submit_button.setText( "Submit" );
		submit_button.setFont( submit_button.getFont().deriveFont( Settings.FONT_SIZE ) );
		submit_button.setBackground(Settings.MAIN_COLOR);
		submit_button.setForeground( Settings.FOREGROUND_COLOR );
		submit_button.addActionListener( new SubmitListener() );
		
		
		panel.add(heading_label, "span, center, wrap");
		
		JPanel p = new JPanel();
		p.setLayout(new MigLayout("fill"));
		p.setBackground(Settings.HEADER_BACKGROUND);
		p.setBorder(new LineBorder(null,1));
		
		
		p.add(date_label, "center");
		p.add(new JSeparator(SwingConstants.VERTICAL), "growy" );
				
		p.add(submit_button, "");
		
		panel.add(p, "grow");
		return panel;
	}
	
	private JPanel createJobsWorkedPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout() );
		panel.setBackground( Settings.BACKGROUND_COLOR );
		
		jp = new JobPanel[NUM_JOB_PANELS];
		for (int i=0; i<NUM_JOB_PANELS; i++) {
			jp[i] = new JobPanel();
			panel.add(jp[i], "grow, wrap");
		}
		
		addFlexibleFocusListeners(jp);
		return panel;
	}
	
	public class JobPanel extends JPanel {		
		
		// FIELDS		
		public JCheckBox worked_checkbox;
		public JComboBox<String> customer_combobox;
		public JTextField jobpaid_field;
		public JComboBox<String> employee_combobox;
		public JTextField workerpaid_field;
		
		
		// CONSTRUCTOR
		private JobPanel() {
				
			setLayout( new MigLayout("fill", "[]20[]20[]20[]20[]") );
			setBackground( Settings.BACKGROUND_COLOR );
			//setBorder( new LineBorder(null,1));
			setBorder(BorderFactory.createTitledBorder( new String() ));

			
			worked_checkbox = new JCheckBox();
			worked_checkbox.setBackground( Settings.BACKGROUND_COLOR );
			worked_checkbox.setText("");
			
			customer_combobox = new JComboBox<String>();
			customer_combobox.setFont( customer_combobox.getFont().deriveFont(Settings.FONT_SIZE) );
			customer_combobox.setEditable(true);
			customer_combobox.addItem("");
			
			WorkerList weekendWorkers = new WorkerList(WorkerList.WEEKEND_WORKERS);
			for (Worker worker : weekendWorkers) {
				customer_combobox.addItem(worker.getName());
			}
			
			jobpaid_field = new JTextField();
			jobpaid_field.setColumns( 5 );
			jobpaid_field.setFont( jobpaid_field.getFont().deriveFont(Settings.FONT_SIZE) );
			
			employee_combobox = new JComboBox<String>();
			employee_combobox.setFont( employee_combobox.getFont().deriveFont(Settings.FONT_SIZE) );
			employee_combobox.setEditable(true);
			employee_combobox.addItem("");
			WorkerList houseWorkers = new WorkerList(WorkerList.HOUSE_WORKERS);
			for (Worker worker : houseWorkers) {
				employee_combobox.addItem(worker.getName());
			}
			
			workerpaid_field = new JTextField();
			workerpaid_field.setColumns( 5 );
			workerpaid_field.setFont( workerpaid_field.getFont().deriveFont(Settings.FONT_SIZE) );			

			JLabel worked_label = new JLabel();
			worked_label.setText("Worked?");
			worked_label.setFont(worked_label.getFont().deriveFont(Settings.FONT_SIZE));
			worked_label.setBackground(Settings.BACKGROUND_COLOR);
			
			JLabel customer_label = new JLabel();
			customer_label.setText("Customer");
			customer_label.setFont(customer_label.getFont().deriveFont(Settings.FONT_SIZE));
			customer_label.setBackground(Settings.BACKGROUND_COLOR);
			
			JLabel job_paid_label = new JLabel();
			job_paid_label.setText("$ Job");
			job_paid_label.setFont(job_paid_label.getFont().deriveFont(Settings.FONT_SIZE));
			job_paid_label.setBackground(Settings.BACKGROUND_COLOR);
			
			JLabel worker_label = new JLabel();
			worker_label.setText("Employee");
			worker_label.setFont(worker_label.getFont().deriveFont(Settings.FONT_SIZE));
			worker_label.setBackground(Settings.BACKGROUND_COLOR);
			
			JLabel worker_paid_label = new JLabel();
			worker_paid_label.setText("$ Paid");
			worker_paid_label.setFont(worker_paid_label.getFont().deriveFont(Settings.FONT_SIZE));
			worker_paid_label.setBackground(Settings.BACKGROUND_COLOR);
			
			add(worked_label, "");
			add(customer_label, "");
			add(job_paid_label, "");
			add(worker_label, "");
			add(worker_paid_label, "wrap");
			
			
			add(worked_checkbox, "center");
			add(customer_combobox, "");
			add(jobpaid_field, "");
			add(employee_combobox, "");
			add(workerpaid_field, "");
			
		}
		
	}
	
	
	// PRIVATE METHODS
	
	private void addFlexibleFocusListeners ( JobPanel[] jp ) {
		/*
		for ( int i=0; i<jp.length; i++ ) {
			
			Component worked_checkbox_up;
			Component worked_checkbox_down;
			
			Component customer_combobox_up;
			Component customer_combobox_down;
			
			Component jobpaid_field_up;
			Component jobpaid_field_down;
			
			Component employee_combobox_up;
			Component employee_combobox_down;
			
			Component workerpaid_field_up;
			Component workerpaid_field_down;
			
			if ( i>0 ) {
				worked_checkbox_up = jp[i-1].worked_checkbox;
				customer_combobox_up = jp[i-1].customer_combobox;
			}
			
			jp[i].worked_checkbox.addFocusListener( new FocusListener(jp[i].worked_checkbox,
					FlexibleFocusListener.CHECKBOX,
					null, jp[i].customer_combobox,
					up, down,
					enter) );
			
		}
		*/
	}
	
	private class SubmitListener implements ActionListener {
	
		public void actionPerformed(ActionEvent e) {
		    
            if (!StaticMethods.confirmSubmitWeek()) {
                return;
            }
            
		    controller.readInputAndWriteToFile(null);
		    
		    frame.setVisible(false);
	        frame.dispose();
	        
	        GeneralController<TabbedPane, NW_Data> scheduledController = new GeneralController<>(Form.SCHEDULED);
	        scheduledController.initializeForm(scheduledController);
		}
		
	}	
	
    // GETTERS/SETTERS
    public void setFrame(JFrame frame) {
        this.frame = frame;
    }
    
}
