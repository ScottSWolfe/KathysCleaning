package com.github.scottswolfe.kathyscleaning.menu.controller;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.menu.view.MenuEditCovenantWorkersPanel;
import com.github.scottswolfe.kathyscleaning.menu.view.MenuEditHouseWorkersPanel;
import com.github.scottswolfe.kathyscleaning.menu.view.MenuPanel;
import com.github.scottswolfe.kathyscleaning.menu.view.SettingsPanel;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;

/**
 * This class links the Settings class and the SettingsPanel GUI and 
 * responds to user's actions.
 */
public class SettingsPanelController {
    
    
/* FIELDS =================================================================== */
    
    /**
     * The associated SettingsPanel that this controller controls.
     */
    SettingsPanel settingsPanel;
    
    /**
     * The frame for the SettingsPanel
     */
    JFrame settingsFrame;
    
    
 
    
/* CONSTRUCTORS ============================================================= */
    
    /**
     * Constructor for SettingsPanelController class.
     */
    public SettingsPanelController(SettingsPanel panel, JFrame frame) {
        settingsPanel = panel;
        settingsFrame = frame;
    }
    
    
    
     
/* LISTENERS ================================================================ */
    
    public class ViewExcelListener implements ActionListener {
        
        // ACTION LISTENER
        public void actionPerformed( ActionEvent e ) {
            
            try {
                Desktop.getDesktop().open(Settings.getExcelTemplateFile());
            } catch (IllegalArgumentException e1) {
                    System.out.println("Desktop Open Error");
                    JOptionPane.showMessageDialog(new JFrame(), "Error: the chosen document could not be opened");
            } catch (IOException e2) {
                e2.printStackTrace();
            } 

        }
        
    }

    
    public class ViewFolderListener implements ActionListener {
        
        // ACTION LISTENER
        public void actionPerformed( ActionEvent e ) {
            
            try {
                Desktop.getDesktop().open(Settings.getExcelSaveLocation());
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(new JFrame(), "Error: the chosen folder could not be viewed.");
            } catch(NullPointerException e2) {
                JOptionPane.showMessageDialog(new JFrame(), "Error: the chosen folder could not be viewed.");
            }
            
        }
        
    }

    
    public class ChangeFileListener implements ActionListener {
        
        // ACTION LISTENER
        public void actionPerformed( ActionEvent e ) {
            
            // Change look and feel to default system look and feel
            Settings.changeLookAndFeelSystem();
            
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory( new File(System.getProperty("user.home") + "\\Desktop") );
            chooser.setDialogTitle("Choose Default Excel Template");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("XLSX files", "xlsx");
            chooser.setFileFilter(filter);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                
                Settings.setExcelTemplateFile(chooser.getSelectedFile());
                settingsPanel.setExcelSelectionTextfield(
                        chooser.getSelectedFile().getName());
                
            } else {
              // do nothing
            }
            
            
            // Change look and feel back to program default
            Settings.changeLookAndFeelProgram();
            
        }
        
    }
    
    
    public class ChangeFolderListener implements ActionListener {
        
        // ACTION LISTENER
        public void actionPerformed( ActionEvent e ) {
            
            // Change look and feel to system default
            Settings.changeLookAndFeelSystem();
            
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory( new File(System.getProperty("user.home") + "\\Desktop") );
            chooser.setDialogTitle("Choose Save Location");
            //FileNameExtensionFilter filter = new FileNameExtensionFilter("XLSX files", "xlsx");
            //chooser.setFileFilter(filter);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                Settings.setExcelSaveLocation(chooser.getSelectedFile());
                settingsPanel.setExcelSaveLocationTextfield(
                        Settings.getExcelSaveLocation().getName() );
            } else {
              // do nothing
            }
            
            // change look and feel back to nimbus
            Settings.changeLookAndFeelProgram();    
        }
        
    }
    
    /**
     * Listener for the edit workers buttons
     */
    public class EditWorkerListener implements ActionListener {

        // FIELDS
        int type;
        JFrame container_frame;
        
        // CONSTRUCTOR
        public EditWorkerListener (int type, JFrame container_frame) {
            this.type = type;
            this.container_frame = container_frame;
        }
        
        @Override
        public void actionPerformed(ActionEvent arg0) {
            
            JFrame new_frame = new JFrame();
            new_frame.setResizable(false);
            new_frame.addWindowListener(new FrameCloseListener(container_frame));

            if (type == Settings.HOUSES_WORKERS) {
                MenuEditHouseWorkersPanel panel = new MenuEditHouseWorkersPanel(new_frame);
                new_frame.add(panel);
            }
            else {
                MenuEditCovenantWorkersPanel panel = new MenuEditCovenantWorkersPanel(new_frame);
                new_frame.add(panel);
            }
            new_frame.pack();
            StaticMethods.findSetLocation(new_frame);
            new_frame.setVisible(true);
        }
    }
    
    
    /**
     * Listener for the Cancel button.
     */
    public class CancelButtonListener implements ActionListener {
        public void actionPerformed( ActionEvent e ) {
            
            settingsFrame.setVisible(false);
            settingsFrame.dispose();
            
            JFrame menuFrame = new JFrame();
            menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            menuFrame.setResizable(false);
            
            MenuPanel panel = new MenuPanel(menuFrame);
            
            menuFrame.add(panel);
            menuFrame.pack();
            menuFrame.setLocationRelativeTo(null);
            menuFrame.setVisible(true);
        }
    }
    
    
    /**
     * Listener for the Submit button.
     */
    public class SubmitButtonListener implements ActionListener {
        
        public void actionPerformed( ActionEvent e ) {
            
            // Write all settings data to SettingsSaveFile
            BufferedWriter buffWriter;
            try {
                
                FileWriter fileWriter = new FileWriter(Settings.SETTINGS_SAVE_FILE);
                buffWriter = new BufferedWriter(fileWriter);
                
                buffWriter.write(
                        Settings.getExcelTemplateFile().getAbsolutePath());
                buffWriter.newLine();
                buffWriter.write(
                        Settings.getExcelSaveLocation().getAbsolutePath());
                buffWriter.newLine();
                buffWriter.write(
                        String.valueOf(settingsPanel.getFontSizeSliderValue()));
                
                buffWriter.close();
                
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            
            // changing the font size (if changed)
            int fontSizeFactor = settingsPanel.getFontSizeSliderValue();
            
            Settings.setFontSizeFactor(fontSizeFactor);
                                
            settingsFrame.setVisible(false);
            settingsFrame.dispose();
                
            JFrame newMenuFrame = new JFrame();
            MenuPanel menuPanel = new MenuPanel( newMenuFrame );
                
            newMenuFrame.add(menuPanel);
            newMenuFrame.setResizable(false);
            newMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
            newMenuFrame.pack();

                
            newMenuFrame.setLocationRelativeTo(null);
            newMenuFrame.setVisible(true);

            
            // close Settings Frame
            settingsFrame.setVisible(false);
            settingsFrame.dispose();
            

        }
        
    }
    
    
    /**
     * Window listener for SettingsPanel.
     */
    public class SettingsPanelWindowListener implements WindowListener {

        @Override
        public void windowActivated(WindowEvent e) {
            
        }

        @Override
        public void windowClosed(WindowEvent e) {
            
        }

        @Override
        public void windowClosing(WindowEvent e) {
            MenuPanelController.initializeMenuPanelFrame();
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
            
        }

        @Override
        public void windowIconified(WindowEvent e) {

        }

        @Override
        public void windowOpened(WindowEvent e) {
            
        }
        
    }
    
    
    
/* PUBLIC METHODS =========================================================== */
    
    /**
     * Initializes and launches a frame with a settings panel.
     */
    public static void initializeSettingsPanelFrame() {
        
        JFrame frame = new JFrame();
        SettingsPanel settingsPanel = new SettingsPanel(frame);

        settingsPanel.getSettingsFrame().setResizable(false);
        settingsPanel.getSettingsFrame().addWindowListener(
                settingsPanel.getController().new
                SettingsPanelWindowListener());
        
        settingsPanel.getSettingsFrame().add(settingsPanel);
        settingsPanel.getSettingsFrame().pack();

        settingsPanel.getSettingsFrame().setLocationRelativeTo(null);
        settingsPanel.getSettingsFrame().setVisible(true);
    }
    
    
    
    
/* GETTERS/SETTERS ========================================================== */
    
    public void setSettingsPanel(SettingsPanel panel) {
        settingsPanel = panel;
    }
    
    public void setFrame(JFrame frame) {
        settingsFrame = frame;
    }
    
}
