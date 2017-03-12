package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import com.github.scottswolfe.kathyscleaning.general.helper.FileChooserHelper;
import com.github.scottswolfe.kathyscleaning.general.helper.FileNameHelper;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.interfaces.FileMenuListener;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

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
        File file = Settings.saveFile;
        if (file == null) {
            menuItemSaveAs();
        } else {
            controller.readInputAndWriteToFile(file);
        }
    }

    @Override
    public void menuItemSaveAs() {
        File file = null;
        if (Settings.saveFile == null) {
            file = FileChooserHelper.saveAs(
                    FileChooserHelper.SAVE_FILE_DIR, createSuggestedName(
                    FileChooserHelper.SAVE_FILE_DIR.getAbsolutePath(),
                    FileChooserHelper.TXT), FileChooserHelper.TXT);
        } else {
            file = FileChooserHelper.saveAs(Settings.saveFile); 
        }
        if (file != null) {
            Settings.saveFile = file;
            controller.readInputAndWriteToFile(file);
        }
    }

    @Override
    public void menuItemOpen() {
        File file = FileChooserHelper.open(FileChooserHelper.SAVE_FILE_DIR, null);
        if (file != null) {
            Settings.saveFile = file;
            controller.readFileAndWriteToView(file);
        }
    }
    
    @Override
    public void menuItemGenExcel() {
        controller.readInputAndWriteToFile(null);
        File file = null;
        if (Settings.excelFile == null) {
            file = FileChooserHelper.saveAs(
                    Settings.getExcelSaveLocation(), createSuggestedName(
                            Settings.getExcelSaveLocation().getAbsolutePath(),
                    FileChooserHelper.XLSX), FileChooserHelper.XLSX);
        } else {
            file = FileChooserHelper.saveAs(Settings.excelFile); 
        }
        if (file != null) {
            Settings.excelFile = file;
            controller.writeModelToExcel(file);
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
    
    public class GenExcelMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemGenExcel();
        }
    }
    
    
    
/* PRIVATE METHODS ========================================================== */
    
    private String createSuggestedName(String directory, String extension) {
        return FileNameHelper.createDatedFileName(directory, extension);
    }

}
