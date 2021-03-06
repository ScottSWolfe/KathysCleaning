package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;

import com.github.scottswolfe.kathyscleaning.completed.controller.CompletedControllerHelper;
import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.covenant.view.CovenantPanel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener.Action;
import com.github.scottswolfe.kathyscleaning.general.helper.ExcelMethods;
import com.github.scottswolfe.kathyscleaning.general.helper.FileChooserHelper;
import com.github.scottswolfe.kathyscleaning.general.helper.FileNameHelper;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.interfaces.FileMenuListener;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendModel;
import com.github.scottswolfe.kathyscleaning.weekend.view.WeekendPanel;

public class MenuBarController <ViewObject, ModelObject> implements FileMenuListener { 
    
/* INSTANCE VARIABLES ======================================================= */
    
    /**
     * The controller that this class calls on to do the reading and writing
     */
    Controller<ViewObject, ModelObject> controller;
    
    private static final List<String> TEMPLATE_LIST = new ArrayList<>();
    {
        TEMPLATE_LIST.add("Week A Template" + "." + FileChooserHelper.KC);
        TEMPLATE_LIST.add("Week B Template" + "." + FileChooserHelper.KC);
    }
    
    
    
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
            File file = SessionModel.getSaveFile();
            saveFile(file);
        }
    }

    @Override
    public void menuItemSaveAs() {
        File file = null;
        if (SessionModel.isSaveFileChosen() == false) {
            file = FileChooserHelper.saveAs(
                    FileChooserHelper.SAVE_FILE_DIR, createSuggestedName(
                    FileChooserHelper.SAVE_FILE_DIR.getAbsolutePath(),
                    FileChooserHelper.KC), FileChooserHelper.KC);
        } else {
            file = SessionModel.getSaveFile();
            file = checkNameForTemplate(file);
            file = FileChooserHelper.saveAs(file);
        }
        if (file != null) {
            saveFile(file);
        }
    }

    @Override
    public void menuItemOpen() {
        boolean response = MainWindowListener.askUserIfSaveBeforeAction(controller, Action.OPEN, false);
        if (response == true) {
            File file = FileChooserHelper.open(FileChooserHelper.SAVE_FILE_DIR, FileChooserHelper.KC);
            if (file != null) {
                loadFile(file);
            }
        }
    }
    
    @Override
    public void menuItemGenExcel() {
        ExcelMethods.chooseFileAndGenerateExcelDoc(controller);
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
    
    private static String createSuggestedName(String directory, String extension) {
        return FileNameHelper.createDatedFileName(directory, extension);
    }
    
    private void saveFile(File file) {
        if (prepareFileName(file) == false) {
            return;
        }
        SessionModel.setSaveFile(file);
        controller.readInputAndWriteToFile(file);
        controller.setTitleText();
    }
    
    private void loadFile(File file) {
        SessionModel.load(file);
        controller.readFileAndWriteToView(file);
        controller.setTitleText();
    }
    
    public static boolean prepareFileName(File file) {
        file = checkExtension(file);
        boolean response = askIfOverwriteTemplate(file);
        if (response == false) {
            return false;
        }
        return true;
    }

    private static boolean askIfOverwriteTemplate(File file) {
        if (SessionModel.isSaveFileChosen() == false) {
            return true;
        }
        if (TEMPLATE_LIST.contains(file.getName()) == false) {
            return true;
        }
        String[] options = {"Overwrite",  "Don't Overwrite"};
        int OVERWRITE = 0;
        @SuppressWarnings("unused")
        int NOT_OVERWRITE = 1;
        String message  = "<html>Are you sure you want overwrite " + file.getName() + "?";
        int response = JOptionPane.showOptionDialog(new JFrame(), message,
                                     null, JOptionPane.DEFAULT_OPTION,
                                     JOptionPane.WARNING_MESSAGE, null, options, 0);

        if (response == OVERWRITE) {
            return true;
        } else {
            return false;
        }
    }
    
    private static File checkExtension(File file) {
        String path = file.getPath();
        if (FilenameUtils.isExtension(path, FileChooserHelper.KC)) {
            return file;
        } else {
            return new File(path + "." + FileChooserHelper.KC);    
        }
    }
    
    private static File checkNameForTemplate(File file) {
        if (TEMPLATE_LIST.contains(file.getName()) == false) {
            return file;
        } else {
            String fileName = createSuggestedName(
                            FileChooserHelper.SAVE_FILE_DIR.getAbsolutePath(),
                            FileChooserHelper.KC);
            file = new File(FileChooserHelper.SAVE_FILE_DIR + "/" + fileName + "." + FileChooserHelper.KC);
            return file;
        }
    }

}
