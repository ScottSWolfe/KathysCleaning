package com.github.scottswolfe.kathyscleaning.completed.view;

import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.github.scottswolfe.kathyscleaning.completed.controller.CopyWorkersListener;
import com.github.scottswolfe.kathyscleaning.completed.controller.SubmitWeekListener;
import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.component.KcButton;
import com.github.scottswolfe.kathyscleaning.general.controller.EditDefaultWorkersListener;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.controller.NextDayListener;
import com.github.scottswolfe.kathyscleaning.general.controller.PreviousDayListener;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.WorkerPanel;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
public class HeaderPanel extends JPanel {
	
// FIELDS
	
	TabbedPane tp;
	MainFrame<TabbedPane, CompletedModel> frame;
	WorkerList dwd;
	DayPanel day_panel;
	DayData day_data;
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
	
	JButton submit_week;
	
	public int weekSelected = -1;
	public static final int WEEK_A = 0;
	public static final int WEEK_B = 1;
	public static final int NEITHER = 2;
	
	int mode;
	int wk;

	
// CONSTRUCTOR
	
	public HeaderPanel(GeneralController<TabbedPane, CompletedModel> controller, TabbedPane tp,
	        WorkerList dwd, DayPanel day_panel, Calendar date,
	        MainFrame<TabbedPane, CompletedModel> frame, int mode, int wk) {
		
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
		
		JPanel date_panel = datePanel(SessionModel.getCompletedStartDay());
		JPanel worker_panel = workerPanel(dwd);
		JPanel change_day_panel = changeDayPanel();
		JPanel submit_week_panel = submitWeekPanel(controller);
		
		int day_panel_width_min = 133 + (int) Settings.FONT_SIZE; // temp fix
		
		add(date_panel, new String("growy, growx, wmin " + day_panel_width_min +", ay center"));
		add( new JSeparator(SwingConstants.VERTICAL), "growy" );
		//add(choose_week_panel, "growy");
		//add( new JSeparator(SwingConstants.VERTICAL), "growy" );
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
		
		day_label = new JLabel();
        day_label.setFont(day_label.getFont().deriveFont(Settings.HEADER_FONT_SIZE));
        
        date_label = new JLabel();
        date_label.setFont( date_label.getFont().deriveFont( Settings.FONT_SIZE ) );

        setDate(date);
        
		panel.add(this.day_label, "wrap, ax center");
		if (mode == Settings.TRUE_MODE) {
			panel.add(this.date_label, "ax center, top push");
		}
		
		return panel;
	}	
	
	private JPanel workerPanel( WorkerList dwd ){
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("insets 2") );
		panel.setBackground( Settings.HEADER_BACKGROUND );
		
		dwp = new WorkerPanel(dwd, Settings.HEADER_BACKGROUND, null, null );
		
		edit_default_workers = new KcButton("Edit", new EditDefaultWorkersListener(dwd, day_panel, frame));
		copy_workers = new KcButton("Copy", new CopyWorkersListener(tp, day_data, day_panel));
		
		panel.add(dwp, "span 1 2, pushy");
		panel.add(copy_workers, "growx, pushy");
		panel.add(edit_default_workers, "growx, pushy");
		
		return panel;
	}
	
	
	private JPanel changeDayPanel(){
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("insets 2","[][]","[grow]") );
		panel.setBackground( Settings.HEADER_BACKGROUND );

		previous_day = new KcButton("Previous", new PreviousDayListener(tp,frame));
	    previous_day.setBackground(Settings.CHANGE_DAY_COLOR);
        previous_day.setForeground( Settings.FOREGROUND_COLOR );
        previous_day.setPreferredSize(new Dimension(100,40));

		next_day = new KcButton("  Next  ", new NextDayListener(tp,frame));
		next_day.setBackground(Settings.CHANGE_DAY_COLOR);
	    next_day.setForeground( Settings.FOREGROUND_COLOR );
		next_day.setPreferredSize(new Dimension(100,40));
		
		panel.add(previous_day, "");
		panel.add(next_day, "");
		return panel;
	}
	
	private JPanel submitWeekPanel(GeneralController<TabbedPane, CompletedModel> controller) {
		
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("insets 2","[]","[grow]"));
		panel.setBackground(Settings.HEADER_BACKGROUND);
		
		submit_week = new KcButton("Next", new SubmitWeekListener(controller, tp, frame, date, mode, wk));
		submit_week.setPreferredSize(new Dimension(100,40));
		submit_week.setBackground(Settings.MAIN_COLOR);
		submit_week.setForeground(Settings.FOREGROUND_COLOR);
		
		panel.add(submit_week);
		return panel;
	}
	
	public WorkerList getWorkers() {
	    return dwp.getWorkers();
	}

	public void setWorkers(WorkerList workers) {
	    dwp.setWorkers(workers);
	}
	
	public void setDate(Calendar c) {
	    SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
	    String weekDay = dayFormat.format(c.getTime());
	    day_label.setText(weekDay);
	    date_label.setText((Integer.parseInt(String.valueOf(c.get(Calendar.MONTH)))+1 ) + "/" + c.get(Calendar.DATE) + "/" + c.get(Calendar.YEAR));
	}
	
	public Calendar getDate() {
	    return (Calendar) date.clone();
	}
	
}
