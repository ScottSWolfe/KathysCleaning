package com.github.scottswolfe.kathyscleaning.menu.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener;
import com.github.scottswolfe.kathyscleaning.submit.view.DayPanel;

import net.miginfocom.swing.MigLayout;


public class MenuPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4379504716346245298L;
	
	
	// COMPONENT FIELDS
	JLabel compname_label;
	JLabel subname_label;
	JLabel menu_label;
	JButton start_button;
	JButton settings_button;
	JButton close_button;
	
	
	// OTHER FIELDS
	JFrame menu_frame;
	Image img = new ImageIcon( System.getProperty("user.dir") + "\\lib\\Galapagos Island.jpg").getImage();
	
	
	// CONSTRUCTOR
	public MenuPanel( JFrame menu_frame ) {
		
		this.menu_frame = menu_frame;
		
		
		setLayout( new MigLayout("", "[250]", "[][][][][]100") );
		setBackground( new Color(1,187,244) );
		//setPreferredSize( new Dimension(250,400) );

		
		
		compname_label = new JLabel();
		compname_label.setText("Kathy's Cleaning");
		compname_label.setForeground(DayPanel.FOREGROUND_COLOR);
		compname_label.setFont( compname_label.getFont().deriveFont( (float) 40 ) );
		
		subname_label = new JLabel();
		subname_label.setText("Payroll and Schedule Data Entry");
		subname_label.setFont( subname_label.getFont().deriveFont(DayPanel.HEADER_FONT_SIZE) );
		subname_label.setForeground(DayPanel.FOREGROUND_COLOR);
		
		menu_label = new JLabel();
		menu_label.setText("Menu");
		menu_label.setFont( menu_label.getFont().deriveFont(DayPanel.HEADER_FONT_SIZE) );
		menu_label.setForeground(DayPanel.FOREGROUND_COLOR);
		
		Dimension preferred_size = new Dimension(200,50);
		
		start_button = new JButton();
		start_button.setText( "Start" );
		//start_button.setBackground( DayPanel.MAIN_COLOR );
		start_button.setFont( start_button.getFont().deriveFont(DayPanel.FONT_SIZE) );
		start_button.setPreferredSize( preferred_size );
		//start_button.setForeground( DayPanel.FOREGROUND_COLOR );
		start_button.addActionListener( new StartListener() );
		
		settings_button = new JButton();
		settings_button.setText( "Settings" );
		//settings_button.setBackground( DayPanel.MAIN_COLOR );
		settings_button.setFont( settings_button.getFont().deriveFont(DayPanel.FONT_SIZE) );
		settings_button.setPreferredSize( preferred_size );
		//settings_button.setForeground( DayPanel.FOREGROUND_COLOR );
		settings_button.addActionListener( new SettingsListener() );
		
		close_button = new JButton();
		close_button.setText( "Close" );
		//close_button.setBackground( DayPanel.MAIN_COLOR );
		close_button.setFont( close_button.getFont().deriveFont(DayPanel.FONT_SIZE) );
		close_button.setPreferredSize( preferred_size );
		//close_button.setForeground( DayPanel.FOREGROUND_COLOR );
		close_button.addActionListener( new CloseListener( ) );
		
		add(compname_label, "center, wrap 0 ");
		add(subname_label, "wrap 50, center");
		add(menu_label, "wrap 10, center");
		add(start_button, "wrap 20, gapleft 50, gapright 50, center");
		add(settings_button, "wrap 20, gapleft 50, gapright 50, center");
		add(close_button, "wrap, gapleft 50, gapright 50, center");
		
	}
	
	
	// PUBLIC METHODS
	/*
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, null);
	}
	*/
	
	
	// PRIVATE METHODS
	
	
	
	// LISTENERS
	private class SettingsListener implements ActionListener {
		
		// Action Listener
		public void actionPerformed( ActionEvent e ) {
			
			
			/*
			This doesn't work
			
			SettingsPanel sp = new SettingsPanel( menu_frame );

			menu_frame.removeAll();
			menu_frame.add( sp );
			menu_frame.pack();
			menu_frame.revalidate();
			menu_frame.repaint();
			*/
			
			JFrame f = new JFrame();
			f.setResizable( false );
			
			SettingsPanel sp = new SettingsPanel( f, menu_frame );
			
			f.add(sp);
			f.pack();
			f.setLocationRelativeTo( null );
			f.setVisible(true);
			
		}
		
	}
	
	
	private class StartListener implements ActionListener {
		
		// Action Listener
		public void actionPerformed( ActionEvent e )  {
			
			
			JFrame choose_week_frame = new JFrame();
			choose_week_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			choose_week_frame.setResizable(false);
			choose_week_frame.addWindowListener( new MainWindowListener() );
			
			ChooseWeekPanel cwp = new ChooseWeekPanel( menu_frame, choose_week_frame, ChooseWeekPanel.PREVIOUS_WEEK, SettingsPanel.NEITHER );
			
			choose_week_frame.add(cwp);
			choose_week_frame.pack();
			choose_week_frame.setLocationRelativeTo( null );
			choose_week_frame.setVisible( true );
			
		}
		
	}
	
	
	private class CloseListener implements ActionListener {
		
		public void actionPerformed( ActionEvent e ) {
			
			menu_frame.setVisible( false );
			menu_frame.dispose();
			
			System.exit(0);
			
		}
		
	}
	
	
}
