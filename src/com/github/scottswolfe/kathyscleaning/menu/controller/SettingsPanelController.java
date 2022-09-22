package com.github.scottswolfe.kathyscleaning.menu.controller;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.menu.model.SettingsModel;
import com.github.scottswolfe.kathyscleaning.menu.view.MenuPanel;
import com.github.scottswolfe.kathyscleaning.menu.view.SettingsPanel;

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
        public void actionPerformed(ActionEvent e) {
            try {
                Desktop.getDesktop().open(SettingsModel.getExcelTemplateFile());
            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(new JFrame(), "Error: the chosen file could not be opened");
            } catch (IOException e2) {
                e2.printStackTrace();
                JOptionPane.showMessageDialog(new JFrame(), "Error: the chosen file could not be opened");
            }
        }

    }

    public class ViewFolderListener implements ActionListener {

        // ACTION LISTENER
        public void actionPerformed( ActionEvent e ) {

            try {
                Desktop.getDesktop().open(SettingsModel.getExcelSaveLocation());
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

            Settings.changeLookAndFeelSystem();

            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(SettingsModel.getExcelTemplateFile().getParentFile());
            chooser.setDialogTitle("Choose Default Excel Template");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("XLSX files", "xlsx");
            chooser.setFileFilter(filter);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = new File(chooser.getSelectedFile().getPath());
                SettingsModel.setExcelTemplateFile(file);
                settingsPanel.setExcelSelectionTextfield(
                        chooser.getSelectedFile().getName());
                SettingsModel.save(Settings.SETTINGS_SAVE_FILE);
            } else {
              // do nothing
            }
            Settings.changeLookAndFeelProgram();
        }

    }

    public class ChangeFolderListener implements ActionListener {

        // ACTION LISTENER
        public void actionPerformed( ActionEvent e ) {

            Settings.changeLookAndFeelSystem();

            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(SettingsModel.getExcelSaveLocation());
            chooser.setDialogTitle("Choose Save Location");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = new File(chooser.getSelectedFile().getPath());
                SettingsModel.setExcelSaveLocation(file);
                settingsPanel.setExcelSaveLocationTextfield(
                        SettingsModel.getExcelSaveLocation().getName());
                SettingsModel.save(Settings.SETTINGS_SAVE_FILE);
            } else {
              // do nothing
            }
            Settings.changeLookAndFeelProgram();
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

            int fontSizeFactor = settingsPanel.getFontSizeSliderValue();
            SettingsModel.setFontSizeFactor(fontSizeFactor);
            SettingsModel.save(Settings.SETTINGS_SAVE_FILE);

            JFrame newMenuFrame = new JFrame();
            MenuPanel menuPanel = new MenuPanel(newMenuFrame);

            settingsFrame.setVisible(false);
            settingsFrame.dispose();

            newMenuFrame.add(menuPanel);
            newMenuFrame.setResizable(false);
            newMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            newMenuFrame.pack();
            newMenuFrame.setLocationRelativeTo(null);
            newMenuFrame.setVisible(true);
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
