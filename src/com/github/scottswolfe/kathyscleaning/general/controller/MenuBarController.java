package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import com.github.scottswolfe.kathyscleaning.general.helper.FileChooserHelper;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.interfaces.FileMenuListener;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

public class MenuBarController implements FileMenuListener { 
    
/* INSTANCE VARIABLES ======================================================= */
    
    /**
     * The controller that this class calls on to do the reading and writing
     */
    Controller controller;
    
    
    
/* CONSTRUCTORS ============================================================= */
    
    public MenuBarController(Controller controller) {
        this.controller = controller;
    }
    
    
    
/* PUBLIC METHODS =========================================================== */
    
    @Override
    public void menuItemSave() {
        if (Settings.currentSaveFile == null) {
            menuItemSaveAs();
        } else {
            controller.readInputAndWriteToFile();
        }
    }

    @Override
    public void menuItemSaveAs() {
        File file = FileChooserHelper.saveAs(FileChooserHelper.SAVE_FILE_DIR, null);
        if (file != null) {
            Settings.currentSaveFile = file;
            controller.readInputAndWriteToFile(); 
        }
    }

    @Override
    public void menuItemOpen() {
        File file = FileChooserHelper.open(FileChooserHelper.SAVE_FILE_DIR, null);
        if (file != null) {
            controller.readFileAndWriteToView();
        }
    }
    
    
    
/* LISTENERS ================================================================ */
    
    public class SaveMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemSave();
        }
    }
    
    public class SaveAsMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemSaveAs();
        }
    }
    
    public class LoadMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemOpen();
        }
    }

}
