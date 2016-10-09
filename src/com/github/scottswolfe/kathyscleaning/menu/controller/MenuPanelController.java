package com.github.scottswolfe.kathyscleaning.menu.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener;
import com.github.scottswolfe.kathyscleaning.menu.view.ChooseWeekPanel;
import com.github.scottswolfe.kathyscleaning.menu.view.MenuPanel;
import com.github.scottswolfe.kathyscleaning.menu.view.SettingsPanel;

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
            
            JFrame settingsFrame = new JFrame();
            settingsFrame.setResizable(false);
            
            SettingsPanel settingsPanel = 
                    new SettingsPanel(settingsFrame, menuFrame);
            
            settingsFrame.add(settingsPanel);
            settingsFrame.pack();

            settingsFrame.setLocationRelativeTo( null );
            settingsFrame.setVisible(true);
        }
    }
    
    
    public class StartListener implements ActionListener {
        
        // Action Listener
        public void actionPerformed( ActionEvent e )  {
            
            
            JFrame choose_week_frame = new JFrame();
            choose_week_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            choose_week_frame.setResizable(false);
            choose_week_frame.addWindowListener( new MainWindowListener() );
            
            ChooseWeekPanel cwp = new ChooseWeekPanel( menuFrame, choose_week_frame, ChooseWeekPanel.PREVIOUS_WEEK, SettingsPanel.NEITHER );
            
            choose_week_frame.add(cwp);
            choose_week_frame.pack();
            choose_week_frame.setLocationRelativeTo( null );
            choose_week_frame.setVisible( true );
            
        }
        
    }
    
    
    public class CloseListener implements ActionListener {
        
        public void actionPerformed( ActionEvent e ) {
            
            menuFrame.setVisible(false);
            menuFrame.dispose();
            
            System.exit(0);
            
        }
        
    }
    
    
    
    
/* GETTERS/SETTERS ========================================================== */
    
    public void setPanel(MenuPanel panel) {
        menuPanel = panel;
    }
    
}
