package com.github.scottswolfe.kathyscleaning.scheduled.view;

import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.controller.NextDayListener;
import com.github.scottswolfe.kathyscleaning.general.controller.PreviousDayListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.WorkerPanel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_CopyWorkersListener;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_EditDefaultWorkersListener;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_SubmitWeekListener;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_DayData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_HeaderData;

import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
public class NW_HeaderPanel extends JPanel {
	
// FIELDS
	GeneralController<TabbedPane, NW_Data> controller;
	TabbedPane tp;
	JFrame frame;
	WorkerList dwd;
	NW_DayPanel day_panel;
	NW_DayData day_data;
	public Calendar date;
	
	public JLabel day_label;
	public JLabel date_label;
	
	public JRadioButton week_A;
	public JRadioButton week_B;
	public JRadioButton neither;
	ButtonGroup button_group;
	
	public WorkerPanel dwp;
	JButton copy_workers;
	JButton edit_default_workers;
	
	JButton previous_day;
	JButton next_day;
	
	JButton submit_schedule;
	
	public int weekSelected = -1;
	public static final int WEEK_A = 0;
	public static final int WEEK_B = 1;
	public static final int NEITHER = 2;

	int mode;
	public int wk;
	
	
	
	
// CONSTRUCTOR
	
	public NW_HeaderPanel(GeneralController<TabbedPane, NW_Data> controller,
	        TabbedPane tp, WorkerList dwd, NW_DayPanel day_panel, 
	        Calendar date, JFrame frame, int mode, int wk ) {
		
	    this.controller = controller;
		this.tp = tp;
		this.frame = frame;
		this.dwd = dwd;
		this.day_panel = day_panel;
		this.date = date;
		this.mode = mode;
		this.wk = wk;
		
		setLayout( new MigLayout("gap 0 px, insets 1","[grow][grow][grow]","[grow]") );
		setBorder(BorderFactory.createLineBorder(null, 2));
		setBackground(Settings.BACKGROUND_COLOR);
		setBackground(Settings.HEADER_BACKGROUND);
		
		JPanel date_panel = datePanel(date);
		JPanel worker_panel = workerPanel(dwd);
		JPanel change_day_panel = changeDayPanel();
		JPanel submit_schedule_panel = submitSchedulePanel();
		
		int day_panel_width_min = 133 + (int) Settings.FONT_SIZE; // temp fix
		
		add(date_panel, new String("growy, growx, wmin " + day_panel_width_min +", ay center"));
		add( new JSeparator(SwingConstants.VERTICAL), "growy" );
		add(worker_panel, "growy, push y");
		add( new JSeparator(SwingConstants.VERTICAL), "growy" );
		add(change_day_panel, "growy");
		add( new JSeparator(SwingConstants.VERTICAL), "growy" );
		add(submit_schedule_panel, "growy");
		
			
	}
	
	public NW_HeaderPanel () {
		
	}
	

	
// METHODS
	
	protected JPanel datePanel(Calendar date){
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("insets 2","[grow]","[grow][grow]") );
		panel.setBackground( Settings.HEADER_BACKGROUND );
		
		String weekDay;
		SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
		weekDay = dayFormat.format(date.getTime());
		
		
		day_label = new JLabel();
		day_label.setText( weekDay );
		day_label.setFont( day_label.getFont().deriveFont( Settings.HEADER_FONT_SIZE ) );
		
		date_label = new JLabel( ( Integer.parseInt(String.valueOf(date.get(Calendar.MONTH)))+1 ) + "/" + date.get(Calendar.DATE) + "/" + date.get(Calendar.YEAR) );
		date_label.setFont( date_label.getFont().deriveFont( Settings.FONT_SIZE ) );
		
		panel.add(this.day_label, "wrap, ax center");
		panel.add(this.date_label, "ax center, top push");
		
		return panel;
		
	}
	
	
	
	
	protected JPanel workerPanel( WorkerList dwd ){
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("insets 2") );
		panel.setBackground(Settings.BACKGROUND_COLOR);
		panel.setBackground(Settings.HEADER_BACKGROUND);
			
		dwp = new WorkerPanel(dwd, Settings.HEADER_BACKGROUND, null, null );
		
		edit_default_workers = new JButton("Edit");
		edit_default_workers.addActionListener( new NW_EditDefaultWorkersListener(dwd, day_panel, frame) );
		edit_default_workers.setFont( edit_default_workers.getFont().deriveFont( Settings.FONT_SIZE ) );
		
		copy_workers = new JButton("Copy");
		copy_workers.addActionListener( new NW_CopyWorkersListener(tp, day_data, day_panel) );
		copy_workers.setFont( copy_workers.getFont().deriveFont( Settings.FONT_SIZE ) );
		
		panel.add(dwp, "span 1 2, push y");
		panel.add(copy_workers, "push y, growx");
		panel.add(edit_default_workers, "push y, growx");
		
		return panel;
	}
	
	protected JPanel changeDayPanel(){
		
		tp.toString();
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("insets 2","[][]","[grow]") );
		panel.setBackground( Settings.HEADER_BACKGROUND );

		previous_day = new JButton("Previous");
		previous_day.setFont( previous_day.getFont().deriveFont( Settings.FONT_SIZE ) );
		previous_day.addActionListener( new PreviousDayListener(tp,frame) );
		previous_day.setPreferredSize(new Dimension(100,40));
		previous_day.setBackground(Settings.CHANGE_DAY_COLOR);
		previous_day.setForeground(Settings.FOREGROUND_COLOR);

		next_day = new JButton("  Next  ");
		next_day.setFont( next_day.getFont().deriveFont( Settings.FONT_SIZE ) );
		next_day.addActionListener( new NextDayListener(tp,frame) );
		next_day.setPreferredSize(new Dimension(100,40));
		next_day.setBackground(Settings.CHANGE_DAY_COLOR); 
		next_day.setForeground(Settings.FOREGROUND_COLOR);
		
		
		panel.add(previous_day, "");
		panel.add(next_day, "");

		
		return panel;
	}
	
	protected JPanel submitSchedulePanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("insets 2","[]","[grow]") );
		panel.setBackground( Settings.HEADER_BACKGROUND );
		
		submit_schedule = new JButton("Create Excel Doc");
		submit_schedule.setFont( submit_schedule.getFont().deriveFont( Settings.FONT_SIZE ) );
		submit_schedule.setBackground(Settings.MAIN_COLOR);
		submit_schedule.setForeground( Settings.FOREGROUND_COLOR );
		submit_schedule.setPreferredSize(new Dimension(100,40));
		submit_schedule.addActionListener( new NW_SubmitWeekListener(controller, tp, frame, mode, wk) );
		
		panel.add(submit_schedule);
		
		return panel;
		
	}
	
	
//  
	public NW_HeaderData getHeaderData() {
		
		NW_HeaderData header_data = new NW_HeaderData( this.getWeek(), this.dwd );
		
		return header_data;
	}
	
	
	public String getWeek() {
		
		if( week_A.isSelected() ){
			return "Week A";
		}
		else if( week_B.isSelected() ) {
			return "Week B";
		}
		else if( neither.isSelected() ) {
			return "Neither";
		}
		else {
			return null;
		}
		
	}
	
	
	
	
}
