package com.github.scottswolfe.kathyscleaning.menu.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.menu.view.ChooseWeekPanel;
import com.github.scottswolfe.kathyscleaning.menu.view.MenuPanel;

/**
 * Controller that controls the MenuPanel. 
 */
public class MenuPanelController {

/* FIELDS =================================================================== */
    
    /**
     * The associated MenuPanel that this controller controls.
     */
    MenuPanel menuPanel;
    
    /**
     * The frame for the MenuPanel.
     */
    JFrame menuFrame;
    
    
    

/* CONSTRUCTORS ============================================================= */
    
    /**
     * Constructor for MenuPanelController class.
     */
    public MenuPanelController(MenuPanel panel, JFrame frame) {
        menuPanel = panel;
        menuFrame = frame;
    }
    
    
    
    
/* LISTENERS ================================================================ */
    
    /**
     * Listener for Settings button.
     */
    public class SettingsListener implements ActionListener {
        
        public void actionPerformed( ActionEvent e ) {
            
            menuFrame.setVisible(false);
            menuFrame.dispose();
            
            SettingsPanelController.initializeSettingsPanelFrame();
        }
    }
    
    
    /**
     * Listener for the Start button.
     */
    public class StartListener implements ActionListener {
        
        // Action Listener
        public void actionPerformed( ActionEvent e )  {
            
            JFrame choose_week_frame = new JFrame();
            choose_week_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            choose_week_frame.setResizable(false);
            choose_week_frame.addWindowListener( new MainWindowListener() );
            
            ChooseWeekPanel cwp = new ChooseWeekPanel(menuFrame, choose_week_frame, ChooseWeekPanel.PREVIOUS_WEEK, Settings.NEITHER);
            
            choose_week_frame.add(cwp);
            choose_week_frame.pack();
            choose_week_frame.setLocationRelativeTo( null );
            choose_week_frame.setVisible( true );
        }
        
    }
    
    
    /**
     * Listener for the Close button.
     */
    public class CloseListener implements ActionListener {
        
        public void actionPerformed( ActionEvent e ) {
            
            menuFrame.setVisible(false);
            menuFrame.dispose();
            
            System.exit(0);
            
        }
        
    }
    
    

    
/* PUBLIC METHODS =========================================================== */
    
    /**
     * Initializes and launches a frame with a menu panel.
     */
    public static void initializeMenuPanelFrame() {
        
        JFrame frame = new JFrame();
        MenuPanel menuPanel = new MenuPanel(frame);
        
        menuPanel.getMenuFrame().add(menuPanel);
        menuPanel.getMenuFrame().setResizable(false);
        menuPanel.getMenuFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        menuPanel.getMenuFrame().pack();
        menuPanel.getMenuFrame().setLocationRelativeTo(null);
        menuPanel.getMenuFrame().setVisible(true);
    }
    
    
    
    
/* GETTERS/SETTERS ========================================================== */
    
    /**
     * Set the menuPanel.
     */
    public void setPanel(MenuPanel panel) {
        menuPanel = panel;
    }
    
}
