package com.github.scottswolfe.kathyscleaning.menu.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.ApplicationCoordinator;
import com.github.scottswolfe.kathyscleaning.general.helper.FileChooserHelper;
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

            ApplicationCoordinator.getInstance().navigateToForm(Form.COMPLETED);
        }
    }

    /**
     * Listener for the Open button.
     */
    public class OpenListener implements ActionListener {
        public void actionPerformed(ActionEvent e)  {
            File file = FileChooserHelper.open(FileChooserHelper.SAVE_FILE_DIR,
                                               FileChooserHelper.KC);
            if (file == null) {
                return;
            }

            ApplicationCoordinator.getInstance().loadFile(file);
            ApplicationCoordinator.getInstance().navigateToForm(Form.COMPLETED);

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
