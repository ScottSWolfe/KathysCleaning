package com.github.scottswolfe.kathyscleaning.menu.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.completed.controller.CompletedControllerHelper;
import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener;
import com.github.scottswolfe.kathyscleaning.general.helper.FileChooserHelper;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
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
     * Listener for the New button.
     */
    public class NewListener implements ActionListener {
        
        // Action Listener
        public void actionPerformed( ActionEvent e )  {
            
            JFrame choose_week_frame = new JFrame();
            choose_week_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            choose_week_frame.setResizable(false);
            choose_week_frame.addWindowListener( new MainWindowListener() );
            
            ChooseWeekPanel cwp = new ChooseWeekPanel(menuFrame, choose_week_frame, ChooseWeekPanel.PREVIOUS_WEEK, Settings.NEITHER);
            
            choose_week_frame.add(cwp);
            choose_week_frame.pack();
            choose_week_frame.setLocationRelativeTo(null);
            choose_week_frame.setVisible(true);
        }
    }
    
    /**
     * Listener for the Open button.
     */
    public class OpenListener implements ActionListener {
        public void actionPerformed(ActionEvent e)  {                        
            File file = FileChooserHelper.open(FileChooserHelper.SAVE_FILE_DIR,
                                               FileChooserHelper.TXT);
            if (file == null) {
                return;
            }
            Settings.saveFile = file;
            Settings.saveFileChosen = true;
            try {
                Files.copy(file.toPath(), GeneralController.TEMP_SAVE_FILE.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            GeneralController<TabbedPane, Data> newController =
                    new GeneralController<>(Form.COMPLETED);
            CompletedControllerHelper helper = new CompletedControllerHelper();
            Data model = helper.loadFromFile(file);
            
            newController.initializeForm(newController, model.getDate(), 0, model.getDay()[0].getHeaderData().getWeekSelected());
            newController.readFileAndWriteToView(file);
            
            menuFrame.setVisible(false);
            menuFrame.dispose();
        }
    }
    
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
