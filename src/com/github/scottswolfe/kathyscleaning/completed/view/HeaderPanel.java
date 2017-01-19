package com.github.scottswolfe.kathyscleaning.completed.view;

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

import com.github.scottswolfe.kathyscleaning.completed.controller.CopyWorkersListener;
import com.github.scottswolfe.kathyscleaning.completed.controller.NeitherRadioListener;
import com.github.scottswolfe.kathyscleaning.completed.controller.SubmitWeekListener;
import com.github.scottswolfe.kathyscleaning.completed.controller.WeekRadioListener;
import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.completed.model.HeaderData;
import com.github.scottswolfe.kathyscleaning.general.controller.EditDefaultWorkersListener;
import com.github.scottswolfe.kathyscleaning.general.controller.NextDayListener;
import com.github.scottswolfe.kathyscleaning.general.controller.PreviousDayListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.DefaultWorkerPanel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
public class HeaderPanel extends JPanel {
	
// FIELDS
	
	TabbedPane tp;
	JFrame frame;
	WorkerList dwd;
	DayPanel day_panel;
	DayData day_data;
	public Calendar date;
	
	JLabel day_label;
	JLabel date_label;
	
	public JRadioButton week_A;
	public JRadioButton week_B;
	public JRadioButton neither;
	ButtonGroup button_group;
	
	public DefaultWorkerPanel dwp;
	JButton copy_workers;
	JButton edit_default_workers;
	
	JButton previous_day;
	JButton next_day;
	
	JButton submit_week;
	
	public int weekSelected = -1;
	public static final int WEEK_A = 0;
	public static final int WEEK_B = 1;
	public static final int NEITHER = 2;
	
	int mode;
	int wk;

	
// CONSTRUCTOR
	
	public HeaderPanel( TabbedPane tp, WorkerList dwd, DayPanel day_panel, Calendar date, JFrame frame, int mode, int wk ) {
		
		this.tp = tp;
		this.frame = frame;
		this.dwd = dwd;
		this.day_panel = day_panel;
		this.date = date;
		this.mode = mode;
		this.wk = wk;
		
		setLayout( new MigLayout("gap 0 px, insets 1","[grow][grow][grow][grow][grow]","[grow]") );
		setBackground(Settings.HEADER_BACKGROUND);
		setBorder(BorderFactory.createLineBorder(null, 2));
		
		JPanel date_panel = datePanel(date);
		JPanel choose_week_panel = chooseWeekPanel();
		JPanel worker_panel = workerPanel(dwd);
		JPanel change_day_panel = changeDayPanel();
		JPanel submit_week_panel = submitWeekPanel();
		
		int day_panel_width_min = 133 + (int) Settings.FONT_SIZE; // temp fix
		
		add(date_panel, new String("growy, growx, wmin " + day_panel_width_min +", ay center"));
		add( new JSeparator(SwingConstants.VERTICAL), "growy" );
		add(choose_week_panel, "growy");
		add( new JSeparator(SwingConstants.VERTICAL), "growy" );
		add(worker_panel, "growy");
		add( new JSeparator(SwingConstants.VERTICAL), "growy" );
		add(change_day_panel, "growy");
		add( new JSeparator(SwingConstants.VERTICAL), "growy" );
		add(submit_week_panel, "growy");
		
		
	}
	
	
	
// METHODS
	
	private JPanel datePanel(Calendar date){
				
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("insets 2","[grow]","[grow][grow]") );
		panel.setBackground( Settings.HEADER_BACKGROUND );
		
		String weekDay;
		SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
		weekDay = dayFormat.format(date.getTime());
		
		day_label = new JLabel();
		day_label.setText(weekDay);
		day_label.setFont(day_label.getFont().deriveFont(Settings.HEADER_FONT_SIZE));
		
		if (mode == Settings.TRUE_MODE) {
			date_label = new JLabel((Integer.parseInt(String.valueOf(date.get(Calendar.MONTH)))+1 ) + "/" + date.get(Calendar.DATE) + "/" + date.get(Calendar.YEAR));
			date_label.setFont( date_label.getFont().deriveFont( Settings.FONT_SIZE ) );
		}
		
		panel.add(this.day_label, "wrap, ax center");
		if (mode == Settings.TRUE_MODE) {
			panel.add(this.date_label, "ax center, top push");
		}
		
		
		return panel;
		
	}
	
	
	
	private JPanel chooseWeekPanel(){
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("insets 2") );
		panel.setBackground( Settings.HEADER_BACKGROUND );
		
		week_A = new JRadioButton("Week A");
		week_B = new JRadioButton("Week B");
		neither = new JRadioButton("Neither");
		
		week_A.setFont(week_A.getFont().deriveFont(Settings.FONT_SIZE));
		week_B.setFont(week_B.getFont().deriveFont(Settings.FONT_SIZE));
		neither.setFont(neither.getFont().deriveFont(Settings.FONT_SIZE));
		
		button_group = new ButtonGroup();
		button_group.add(week_A);
		button_group.add(week_B);
		button_group.add(neither);
		if ( wk == Settings.WEEK_A ) {
			button_group.setSelected(week_A.getModel(), true);
		}
		else if ( wk == Settings.WEEK_B ) {
			button_group.setSelected(week_B.getModel(), true);	
		}
		else {
			button_group.setSelected(neither.getModel(), true);
		}
		
		week_A.addActionListener(
		        new WeekRadioListener(tp, frame, Settings.WEEK_A));
		week_B.addActionListener(
		        new WeekRadioListener(tp, frame, Settings.WEEK_B));
		neither.addActionListener(new NeitherRadioListener(tp, frame));
		
		panel.add(week_A, "wrap");
		panel.add(week_B, "wrap");
		panel.add(neither, "wrap");
		
		
		return panel;
		
	}
	
	
	private JPanel workerPanel( WorkerList dwd ){
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("insets 2") );
		panel.setBackground( Settings.HEADER_BACKGROUND );
		
			
		dwp = new DefaultWorkerPanel(dwd, Settings.HEADER_BACKGROUND, null, null );
		
		edit_default_workers = new JButton("Edit");
		edit_default_workers.addActionListener( new EditDefaultWorkersListener(dwd, day_panel, frame) );
		edit_default_workers.setFont( edit_default_workers.getFont().deriveFont( Settings.FONT_SIZE ) );
		
		copy_workers = new JButton("Copy");
		copy_workers.addActionListener( new CopyWorkersListener(tp, day_data, day_panel) );
		copy_workers.setFont( copy_workers.getFont().deriveFont( Settings.FONT_SIZE ) );
		
		panel.add(dwp, "span 1 2, pushy");
		panel.add(copy_workers, "growx, pushy");
		panel.add(edit_default_workers, "growx, pushy");
		
		return panel;
	}
	
	
	private JPanel changeDayPanel(){
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("insets 2","[][]","[grow]") );
		panel.setBackground( Settings.HEADER_BACKGROUND );

		previous_day = new JButton("Previous");
		next_day = new JButton("  Next  ");
		
		previous_day.setFont( previous_day.getFont().deriveFont( Settings.FONT_SIZE ) );
		next_day.setFont( next_day.getFont().deriveFont( Settings.FONT_SIZE ) );
		
		previous_day.setBackground(Settings.CHANGE_DAY_COLOR);
		next_day.setBackground(Settings.CHANGE_DAY_COLOR);
		
		previous_day.setForeground( Settings.FOREGROUND_COLOR );
		next_day.setForeground( Settings.FOREGROUND_COLOR );
		
		previous_day.addActionListener( new PreviousDayListener(tp,frame) );
		next_day.addActionListener( new NextDayListener(tp,frame) );
		
		previous_day.setPreferredSize(new Dimension(100,40));
		next_day.setPreferredSize(new Dimension(100,40));
		
		panel.add(previous_day, "");
		panel.add(next_day, "");

		
		return panel;
	}
	
	private JPanel submitWeekPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("insets 2","[]","[grow]") );
		panel.setBackground( Settings.HEADER_BACKGROUND );
		
		submit_week = new JButton("Submit Week");
		submit_week.setFont( submit_week.getFont().deriveFont( Settings.FONT_SIZE ) );
		submit_week.setPreferredSize(new Dimension(100,40));
		submit_week.addActionListener( new SubmitWeekListener(tp, frame, date, mode, wk) );
		submit_week.setBackground(Settings.MAIN_COLOR);
		submit_week.setForeground( Settings.FOREGROUND_COLOR );
		
		panel.add(submit_week);
		
		return panel;
		
	}
	
	
	
	public HeaderData getHeaderData() {
		
		HeaderData header_data = new HeaderData();
		
		header_data.setDate(this.date);
		header_data.setDWD(this.dwd);     // TODO make sure this is up to date
		
		if (week_A.isSelected()) {
		    header_data.setWeekSelected(WEEK_A);
		} else if (week_B.isSelected()) {
            header_data.setWeekSelected(WEEK_B);
        } else {
            header_data.setWeekSelected(NEITHER);
        }
				
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
	
	public int getWeekSelected() {
        
        if( week_A.isSelected() ){
            return WEEK_A;
        }
        else if( week_B.isSelected() ) {
            return WEEK_B;
        }
        else {
            return NEITHER;
        }
        
    }
	
	
	public WorkerList getWorkers() {
	    return dwd;
	}
	
	public void setWorkers(WorkerList workers) {
	    this.dwd = workers;
	}
	
}