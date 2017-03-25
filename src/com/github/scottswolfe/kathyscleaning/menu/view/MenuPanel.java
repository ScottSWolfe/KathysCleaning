package com.github.scottswolfe.kathyscleaning.menu.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
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
	JButton new_button;
	JButton open_button;
	JButton settings_button;
	JButton close_button;
	
	Dimension preferred_size = new Dimension(200,50);
	
	
	
/* CONSTRUCTOR ============================================================== */
	
	public MenuPanel(JFrame menuFrame) {
		
		this.menuFrame = menuFrame;
		
		controller = new MenuPanelController(this, menuFrame);
		
		setLayout( new MigLayout("", "[250]", "[][][][][][]100") );
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
				
		new_button = createButton("New", controller.new NewListener());
	    open_button = createButton("Open", controller.new OpenListener());
	    settings_button = createButton("Settings", controller.new SettingsListener());
	    close_button = createButton("Close", controller.new CloseListener());
		
		add(compname_label, "center, wrap 0 ");
		add(subname_label, "wrap 50, center");
		add(menu_label, "wrap 10, center");
		add(new_button, "wrap 20, gapleft 50, gapright 50, center");
		add(open_button, "wrap 20, gapleft 50, gapright 50, center");
		add(settings_button, "wrap 20, gapleft 50, gapright 50, center");
		add(close_button, "wrap, gapleft 50, gapright 50, center");
	}
	
	
	
/* PRIVATE METHODS ========================================================== */
	
	private JButton createButton(String text, ActionListener listener) {
	    JButton button = new JButton();
	    button.setText(text);
	    button.setFont(button.getFont().deriveFont(Settings.FONT_SIZE));
	    button.setPreferredSize(preferred_size);
	    button.addActionListener(listener);
	    return button;
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
