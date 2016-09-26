package com.github.scottswolfe.kathyscleaning.nextweek.view;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.scottswolfe.kathyscleaning.general.controller.NextDayListener;
import com.github.scottswolfe.kathyscleaning.general.controller.PreviousDayListener;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.submit.view.DayPanel;

import net.miginfocom.swing.MigLayout;


public class NW_FooterPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2932203179550580016L;
	
	
	
	TabbedPane tp;
	JFrame frame;
	NW_DayPanel day_panel;
	
	JButton previous_day;
	JButton next_day;
	
	JButton submit_schedule;
	
	public NW_FooterPanel (TabbedPane tp, NW_DayPanel day_panel, JFrame frame) {
		
		this.tp = tp;
		this.frame = frame;
		this.day_panel = day_panel;
		
		setLayout( new MigLayout("gap 0 px, insets 1","[grow][grow][grow]","[grow]") );
		setBackground(DayPanel.BACKGROUND_COLOR);
		
		JPanel change_day_panel = changeDayPanel();
		JPanel submit_schedule_panel = submitSchedulePanel();
		
		
		add(change_day_panel, "cell 2 1, growy, align right");
		//add( new JSeparator(SwingConstants.VERTICAL), "growy" );
		add(submit_schedule_panel, "cell 3 1, growy, align right");
	}
	
	
	
	// Private Methods
	
	private JPanel changeDayPanel(){
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("insets 2","[][]","[grow]") );

		previous_day = new JButton("Previous Day");
		previous_day.setFont( previous_day.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		previous_day.addActionListener( new PreviousDayListener(tp,frame) );
		previous_day.setPreferredSize(new Dimension(100,40));
		previous_day.setBackground(DayPanel.MAIN_COLOR);
		previous_day.setForeground(DayPanel.FOREGROUND_COLOR);

		next_day = new JButton("Next Day");
		next_day.setFont( next_day.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		next_day.addActionListener( new NextDayListener(tp,frame) );
		next_day.setPreferredSize(new Dimension(100,40));
		next_day.setBackground(DayPanel.MAIN_COLOR); 
		next_day.setForeground(DayPanel.FOREGROUND_COLOR);
		//next_day.setBorder();
		
		
		panel.add(previous_day, "");
		panel.add(next_day, "");

		
		return panel;
	}
	
	private JPanel submitSchedulePanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("insets 2","[]","[grow]") );
		
		submit_schedule = new JButton("Submit Schedule");
		submit_schedule.setFont( submit_schedule.getFont().deriveFont( DayPanel.FONT_SIZE ) );
		submit_schedule.setPreferredSize(new Dimension(100,40));
		// TODO complete submit schedule listener
		//submit_schedule.addActionListener( new SubmitScheduleListener(tp, frame) );
		
		panel.add(submit_schedule);
		
		return panel;
		
	}
	
	
}
