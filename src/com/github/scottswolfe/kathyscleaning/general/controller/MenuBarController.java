package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import com.github.scottswolfe.kathyscleaning.completed.controller.CompletedControllerHelper;
import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.covenant.view.CovenantPanel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.helper.FileChooserHelper;
import com.github.scottswolfe.kathyscleaning.general.helper.FileNameHelper;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.interfaces.FileMenuListener;
import com.github.scottswolfe.kathyscleaning.menu.model.SettingsModel;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendModel;
import com.github.scottswolfe.kathyscleaning.weekend.view.WeekendPanel;

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
        if (!SessionModel.isSaveFileChosen()) {
            menuItemSaveAs();
        } else {
            controller.readInputAndWriteToFile(SessionModel.getSaveFile());
        }
    }

    @Override
    public void menuItemSaveAs() {
        File file = null;
        if (!SessionModel.isSaveFileChosen()) {
            file = FileChooserHelper.saveAs(
                    FileChooserHelper.SAVE_FILE_DIR, createSuggestedName(
                    FileChooserHelper.SAVE_FILE_DIR.getAbsolutePath(),
                    FileChooserHelper.TXT), FileChooserHelper.TXT);
        } else {
            file = FileChooserHelper.saveAs(SessionModel.getSaveFile()); 
        }
        if (file != null) {
            SessionModel.setSaveFile(file);
            controller.readInputAndWriteToFile(file);
        }
    }

    @Override
    public void menuItemOpen() {
        File file = FileChooserHelper.open(FileChooserHelper.SAVE_FILE_DIR, null);
        if (file != null) {
            SessionModel.setSaveFile(file);
            controller.readFileAndWriteToView(file);
        }
    }
    
    @Override
    public void menuItemGenExcel() {
        controller.readInputAndWriteToFile(null);
        File file = FileChooserHelper.saveAs(
                    SettingsModel.getExcelSaveLocation(), createSuggestedName(
                    SettingsModel.getExcelSaveLocation().getAbsolutePath(),
                    FileChooserHelper.XLSX), FileChooserHelper.XLSX);
        if (file != null) {
            GeneralExcelHelper.generateExcelDocument(file);
        }
    }
    
    public void menuItemChangeDate() {
        controller.updateDate();
    }
    
    public void menuItemGoHouses() {
        controller.readInputAndWriteToFile(GeneralController.TEMP_SAVE_FILE);
        controller.eliminateWindow();
        GeneralController<TabbedPane, CompletedModel> newController =
                new GeneralController<>(Form.COMPLETED);
        newController.initializeForm(newController);
    }
    
    public void menuItemGoCovenant() {
        controller.readInputAndWriteToFile(GeneralController.TEMP_SAVE_FILE);
        controller.eliminateWindow();
        GeneralController<CovenantPanel, CovenantModel> newController =
                new GeneralController<>(Form.COVENANT);
        newController.initializeForm(newController);
    }
    
    public void menuItemGoWeekend() {
        controller.readInputAndWriteToFile(GeneralController.TEMP_SAVE_FILE);
        controller.eliminateWindow();
        GeneralController<WeekendPanel, WeekendModel> newController =
                new GeneralController<>(Form.WEEKEND);
        newController.initializeForm(newController);
    }
    
    public void menuItemGoNextWeek() {
        controller.readInputAndWriteToFile(GeneralController.TEMP_SAVE_FILE);
        controller.eliminateWindow();
        GeneralController<TabbedPane, NW_Data> newController =
                new GeneralController<>(Form.SCHEDULED);
        newController.initializeForm(newController);
    }
    
    public void menuItemLoadSchedule() {
        File file = FileChooserHelper.open(FileChooserHelper.SAVE_FILE_DIR, null);
        if (file != null) {
            CompletedControllerHelper.importSchedule(file, (TabbedPane) controller.getView());
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
    
    
    public class ChangeDateMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemChangeDate();
        }
    }
    
    public class HousesMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemGoHouses();
        }
    }
    
    public class CovenantMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemGoCovenant();
        }
    }
    
    public class WeekendMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemGoWeekend();
        }
    }
    
    public class NextWeekMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemGoNextWeek();
        }
    }
    
    public class LoadScheduleMenuItemListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            menuItemLoadSchedule();
        }
    }

    
    
/* PRIVATE METHODS ========================================================== */
    
    private String createSuggestedName(String directory, String extension) {
        return FileNameHelper.createDatedFileName(directory, extension);
    }

}
