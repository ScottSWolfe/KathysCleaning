package com.github.scottswolfe.kathyscleaning.menu.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.scottswolfe.kathyscleaning.menu.controller.MenuPanelController;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;

/**
 * This is the first panel the user sees on startup. This panel allows the user
 * to route to different parts of the program:
 *      -start
 *      -settings
 *      -close
 */
@SuppressWarnings("serial")
public class MenuPanel extends JPanel {
	
    
/* FIELDS =================================================================== */
    
    /**
     * Controller that controls this panel.
     */
    MenuPanelController controller;
    
    /**
     * Frame that contains this panel.
     */
    JFrame menuFrame;
	
    
    
    
/* COMPONENTS =============================================================== */
	
    JLabel compname_label;
	JLabel subname_label;
	JLabel menu_label;
	JButton start_button;
	JButton settings_button;
	JButton close_button;
	
		
	
/* CONSTRUCTOR ============================================================== */
	
	public MenuPanel(JFrame menuFrame) {
		
		this.menuFrame = menuFrame;
		
		controller = new MenuPanelController(this, menuFrame);
		
		setLayout( new MigLayout("", "[250]", "[][][][][]100") );
		setBackground( new Color(1,187,244) );
		
		compname_label = new JLabel();
		compname_label.setText("Kathy's Cleaning");
		compname_label.setForeground(Settings.FOREGROUND_COLOR);
		compname_label.setFont( compname_label.getFont().deriveFont( (float) 40 ) );
		
		subname_label = new JLabel();
		subname_label.setText("Payroll and Schedule Data Entry");
		subname_label.setFont( subname_label.getFont().deriveFont(Settings.HEADER_FONT_SIZE) );
		subname_label.setForeground(Settings.FOREGROUND_COLOR);
		
		menu_label = new JLabel();
		menu_label.setText("Menu");
		menu_label.setFont( menu_label.getFont().deriveFont(Settings.HEADER_FONT_SIZE) );
		menu_label.setForeground(Settings.FOREGROUND_COLOR);
		
		Dimension preferred_size = new Dimension(200,50);
		
		start_button = new JButton();
		start_button.setText( "Start" );
		start_button.setFont( start_button.getFont().deriveFont(Settings.FONT_SIZE) );
		start_button.setPreferredSize( preferred_size );
		start_button.addActionListener(controller.new StartListener());
		
		settings_button = new JButton();
		settings_button.setText( "Settings" );
		settings_button.setFont( settings_button.getFont().deriveFont(Settings.FONT_SIZE) );
		settings_button.setPreferredSize( preferred_size );
		settings_button.addActionListener(controller.new SettingsListener());
		
		close_button = new JButton();
		close_button.setText( "Close" );
		close_button.setFont( close_button.getFont().deriveFont(Settings.FONT_SIZE) );
		close_button.setPreferredSize( preferred_size );
		close_button.addActionListener(controller.new CloseListener());
		
		add(compname_label, "center, wrap 0 ");
		add(subname_label, "wrap 50, center");
		add(menu_label, "wrap 10, center");
		add(start_button, "wrap 20, gapleft 50, gapright 50, center");
		add(settings_button, "wrap 20, gapleft 50, gapright 50, center");
		add(close_button, "wrap, gapleft 50, gapright 50, center");
	}
	
	
	
	
/* GETTERS/SETTERS ========================================================== */
	
	/**
     * Set the controller for this panel. 
     */
	public void setController(MenuPanelController controller) {
	    this.controller = controller;
	}
	
	/**
	 * Get the controller for this panel.
	 */
	public MenuPanelController getController() {
        return controller;
    }
	
	/**
     * Set the menuFrame.
     */
    public void setMenuFrame(JFrame frame) {
        menuFrame = frame;
    }
    
    /**
     * Get the menuFrame.
     */
    public JFrame getMenuFrame() {
        return menuFrame;
    }
	
	
}
