package com.github.scottswolfe.kathyscleaning.menu.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.scottswolfe.kathyscleaning.completed.controller.CompletedControllerHelper;
import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.helper.FileChooserHelper;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
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
            
            menuFrame.setVisible(false);
            menuFrame.dispose();
            
            GeneralController<TabbedPane, CompletedModel> newController =
            new GeneralController<>(Form.COMPLETED);
            
            String message = "Would you like to import the schedule from a previously completed file?";
            String title = "";
            String[] options = {"Yes", "No"};            
            int response = JOptionPane.showOptionDialog(new JFrame(),
                    message, title, JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            
            File file = null;
            if (response == JOptionPane.YES_OPTION) {
                file = FileChooserHelper.selectFile(
                        FileChooserHelper.SAVE_FILE_DIR, FileChooserHelper.TXT);
            }
            newController.initializeForm(newController);
            
            if (file != null) {
                CompletedControllerHelper.importSchedule(file, newController.getView());
            }
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
            SessionModel.setSaveFile(file);
            try {
                Files.copy(file.toPath(), GeneralController.TEMP_SAVE_FILE.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            GeneralController<TabbedPane, CompletedModel> newController =
                    new GeneralController<>(Form.COMPLETED);
            
            newController.initializeForm(newController);
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
