package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.general.helper.FileChooserHelper;
import com.github.scottswolfe.kathyscleaning.general.helper.FileNameHelper;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.interfaces.FileMenuListener;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.menu.view.ChooseWeekPanel;

public class MenuBarController <ViewObject, ModelObject> implements FileMenuListener { 
    
/* INSTANCE VARIABLES ======================================================= */
    
    /**
     * The controller that this class calls on to do the reading and writing
     */
    Controller<ViewObject, ModelObject> controller;
    
    
    
/* CONSTRUCTORS ============================================================= */
    
    public  MenuBarController(Controller<ViewObject, ModelObject> controller) {
        this.controller = controller;
    }
    
    
    
/* PUBLIC METHODS =========================================================== */
    
    @Override
    public void menuItemSave() {
        if (Settings.saveFileChosen == false) {
            menuItemSaveAs();
        } else {
            File file = Settings.saveFile;
            controller.readInputAndWriteToFile(file);
        }
    }

    @Override
    public void menuItemSaveAs() {
        File file = null;
        if (Settings.saveFileChosen == false) {
            file = FileChooserHelper.saveAs(
                    FileChooserHelper.SAVE_FILE_DIR, createSuggestedName(
                    FileChooserHelper.SAVE_FILE_DIR.getAbsolutePath(),
                    FileChooserHelper.TXT), FileChooserHelper.TXT);
        } else {
            file = FileChooserHelper.saveAs(Settings.saveFile); 
        }
        if (file != null) {
            Settings.saveFile = file;
            Settings.saveFileChosen = true;
            controller.readInputAndWriteToFile(file);
        }
    }

    @Override
    public void menuItemOpen() {
        File file = FileChooserHelper.open(FileChooserHelper.SAVE_FILE_DIR, null);
        if (file != null) {
            Settings.saveFile = file;
            Settings.saveFileChosen = true;
            controller.readFileAndWriteToView(file);
        }
    }
    
    @Override
    public void menuItemGenExcel() {
        controller.readInputAndWriteToFile(null);
        File file = FileChooserHelper.saveAs(
                    Settings.getExcelSaveLocation(), createSuggestedName(
                            Settings.getExcelSaveLocation().getAbsolutePath(),
                    FileChooserHelper.XLSX), FileChooserHelper.XLSX);
        if (file != null) {
            GeneralExcelHelper.generateExcelDocument(file);
        }
    }
    
    public void menuItemChangeDate() {
        JFrame frame = new JFrame();
        ChooseWeekPanel panel = new ChooseWeekPanel(frame, controller);
        frame.add(panel);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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
    
    public class GenExcelMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemGenExcel();
        }
    }
    
    
    public class ChangeDateMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemChangeDate();
        }
    }
    
    
    
/* PRIVATE METHODS ========================================================== */
    
    private String createSuggestedName(String directory, String extension) {
        return FileNameHelper.createDatedFileName(directory, extension);
    }

}
