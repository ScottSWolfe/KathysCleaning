package com.github.scottswolfe.kathyscleaning.menu.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.utility.CalendarMethods;

import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
public class ChooseWeekPanel extends JPanel {

/* FIELDS =================================================================== */
    
    // TODO remove these
    JFrame frame;
    Calendar date;
    Controller controller;
    
    
/* COMPONENTS =============================================================== */
    
	JLabel choose_date_label;
	JLabel month_label;
	JLabel day_label;
	JLabel year_label;
	JComboBox<String> month_box;
	JComboBox<String> day_box;
	JComboBox<String> year_box;
			
	JButton cancel_button;
	JButton submit_button;
	
	
	
/* CONSRUCTOR =============================================================== */
	
	public ChooseWeekPanel(JFrame frame, Controller controller) {
	    this.frame = frame;
	    this.controller = controller;
		setLayout(new MigLayout("insets 0"));
		setBackground(Settings.BACKGROUND_COLOR);		
		add(ChooseDatePanel(), "wrap 20, grow");
		add(ContinuePanel(), "grow");
	}



/* PRIVATE METHODS ========================================================== */
	
	private JPanel ChooseDatePanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("fill", "[]15[][][]", ""));
		panel.setBackground(Settings.BACKGROUND_COLOR);
		
		choose_date_label = new JLabel();
		choose_date_label.setText( "Monday's Date: " );
		choose_date_label.setFont( choose_date_label.getFont().deriveFont(Settings.FONT_SIZE) );
		choose_date_label.setBackground( Settings.BACKGROUND_COLOR );
		
		month_label = new JLabel();
		month_label.setText( "Month" );
		month_label.setFont( month_label.getFont().deriveFont(Settings.FONT_SIZE) );
		month_label.setBackground( Settings.BACKGROUND_COLOR );
		
		day_label = new JLabel();
		day_label.setText( "Day" );
		day_label.setFont( day_label.getFont().deriveFont(Settings.FONT_SIZE) );
		day_label.setBackground( Settings.BACKGROUND_COLOR );
		
		year_label = new JLabel();
		year_label.setText( "Year" );
		year_label.setFont( year_label.getFont().deriveFont(Settings.FONT_SIZE) );
		year_label.setBackground( Settings.BACKGROUND_COLOR );
		
		date = CalendarMethods.getFirstDayOfWeek();
		
		month_box = new JComboBox<String>();
		month_box.setFont( month_box.getFont().deriveFont(Settings.FONT_SIZE) );
		month_box.setBackground( Settings.BACKGROUND_COLOR );
		for (int i=1; i<13; i++){
			month_box.addItem( String.valueOf(i) );
		}
		month_box.setSelectedItem( String.valueOf(date.get(Calendar.MONTH)+1) );
		month_box.addActionListener( new ComboBoxListener() );
		
		day_box = new JComboBox<String>();
		day_box.setFont( day_box.getFont().deriveFont(Settings.FONT_SIZE) );
		day_box.setBackground( Settings.BACKGROUND_COLOR );
		Calendar temp_date = Calendar.getInstance();
		// beginning at first of month and adding all mondays
		temp_date.set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), 1);
		for (int i=1; i<date.getActualMaximum(Calendar.DATE)+1; i++) {
			if ( temp_date.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY ) {
				day_box.addItem( String.valueOf(i) );
			}
			temp_date.add(Calendar.DATE, 1);
		}
		day_box.setSelectedItem( String.valueOf(date.get(Calendar.DATE) ) );
		day_box.addActionListener( new ComboBoxListener() );
		
		year_box = new JComboBox<String>();
		year_box.setFont( year_box.getFont().deriveFont(Settings.FONT_SIZE) );
		year_box.setBackground( Settings.BACKGROUND_COLOR );
		for (int i=-1; i<2; i++){
			year_box.addItem( String.valueOf(Calendar.getInstance().get(Calendar.YEAR)+i) );
		}
		year_box.setSelectedItem( String.valueOf(date.get(Calendar.YEAR) ) );
		year_box.addActionListener( new ComboBoxListener() );
		
		panel.add( month_label, "cell 1 0, center" );
		panel.add( day_label, "cell 2 0, center" );
		panel.add( year_label, "cell 3 0, center" );
		
		panel.add( choose_date_label, "cell 0 1, align left");
		panel.add( month_box, "cell 1 1, center");
		panel.add( day_box, "cell 2 1, center");
		panel.add( year_box, "cell 3 1, center");
				
		return panel;
	}
		
	private JPanel ContinuePanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("align right"));
		panel.setBackground(Settings.HEADER_BACKGROUND);
		
		cancel_button = new JButton();
		cancel_button.setText("Cancel");
		cancel_button.setFont(cancel_button.getFont().deriveFont(Settings.FONT_SIZE));
		cancel_button.setBackground(Settings.MAIN_COLOR);
		cancel_button.setForeground(Settings.FOREGROUND_COLOR);
		cancel_button.addActionListener(new CancelListener());
		
		submit_button = new JButton();
		submit_button.setText("Submit");
		submit_button.setFont(submit_button.getFont().deriveFont(Settings.FONT_SIZE));
		submit_button.setBackground(Settings.MAIN_COLOR);
		submit_button.setForeground(Settings.FOREGROUND_COLOR);
		submit_button.addActionListener(new SubmitListener());
		
		panel.add(cancel_button);
		panel.add(submit_button);
		
		return panel;
		
	}
	
	
	
/* LISTENERS ================================================================ */
	
	private class CancelListener implements ActionListener {
		public void actionPerformed( ActionEvent e ) {
			frame.setVisible(false);
			frame.dispose();
		}
		
	}
	
	private class SubmitListener implements ActionListener {		
		public void actionPerformed( ActionEvent e ) {
		    Calendar c = Calendar.getInstance();
		    c.set(year_box.getSelectedIndex(), month_box.getSelectedIndex(), day_box.getSelectedIndex());
		    Settings.completedStartDay = c; 
		    controller.updateDate();
		    frame.setVisible(false);
		    frame.dispose();
		}
	}
	
	
	
	private class ComboBoxListener implements ActionListener {
		public void actionPerformed( ActionEvent e ) {
			Calendar selected_date = Calendar.getInstance();
			
			if (month_box.getSelectedItem() != null) {
				selected_date.set(Calendar.MONTH, Integer.parseInt( String.valueOf( month_box.getSelectedItem() ) ) - 1 );
			}
			else {
				selected_date.set(Calendar.MONTH, date.get(Calendar.MONTH));
			}
			
			if (day_box.getSelectedItem() != null) {
				selected_date.set(Calendar.DATE, Integer.parseInt( String.valueOf( day_box.getSelectedItem() ) ) );
			}
			else {
				selected_date.set(Calendar.DATE, date.get(Calendar.DATE));
			}
			
			if (year_box.getSelectedItem() != null) {
				selected_date.set(Calendar.YEAR, Integer.parseInt( String.valueOf( year_box.getSelectedItem() ) ) );
			}
			else {
				selected_date.set(Calendar.YEAR, date.get(Calendar.YEAR));
			}
			
			day_box.removeAllItems();
						
			Calendar temp_date = Calendar.getInstance();
			temp_date.set(selected_date.get(Calendar.YEAR), selected_date.get(Calendar.MONTH), 1);

			for (int i=1; i<selected_date.getActualMaximum(Calendar.DATE) + 1; i++) {
				if ( temp_date.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY ) {
					day_box.addItem( String.valueOf(i) );
				}
				temp_date.add(Calendar.DATE, 1);
			}
			
			boolean b = true;
			for (int i=0; i<day_box.getItemCount(); i++) {
				if ( String.valueOf( selected_date.get(Calendar.DATE) ).equals( day_box.getItemAt(i) ) ){
					day_box.setSelectedItem( day_box.getItemAt(i));
					b = false;
					break;
				}
			}
			if ( b ) {
				day_box.setSelectedItem( day_box.getItemAt(0));
			}
		}
	}
}
