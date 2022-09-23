package com.github.scottswolfe.kathyscleaning.utility;

import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.helper.FileChooserHelper;
import com.github.scottswolfe.kathyscleaning.general.helper.FileNameHelper;
import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCModel;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.menu.model.SettingsModel;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendModel;
import com.google.common.collect.ImmutableList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.annotation.Nonnull;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class SaveFileManager {

    /**
     * The temporary file in which the current state of the program is stored.
     *
     * todo: make it so this file is no longer necessary and all current data is stored only in memory.
     */
    public static final File TEMP_SAVE_FILE = new File(System.getProperty("user.dir") + "\\save\\temp\\currentSave");

    /**
     * A hardcoded list of file names that the user can use as special prepopulated templates for
     * typical work weeks.
     */
    private static final List<String> TEMPLATE_LIST = ImmutableList.of(
        "Week A Template" + "." + FileChooserHelper.KC,
        "Week B Template" + "." + FileChooserHelper.KC
    );

    public static SaveFileManager from() {
        return new SaveFileManager();
    }

    private SaveFileManager() {}

    public boolean save() {
        if (SessionModel.isSaveFileChosen()) {
            return save(SessionModel.getSaveFile());
        } else {
            return saveAs();
        }
    }

    public boolean save(@Nonnull final File file) {

        final File newFile = appendExtensionIfNeeded(file);

        if (!askIfOverwriteTemplate(newFile)) {
            return false;
        }

        SessionModel.setSaveFile(newFile);

        try {
            Files.copy(TEMP_SAVE_FILE.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            StaticMethods.shareErrorMessage("Failed to save file: " + newFile.getName(), e);
            return false;
        }
    }

    public boolean saveAs() {
        File file;
        if (!SessionModel.isSaveFileChosen()) {
            file = FileChooserHelper.saveAs(
                FileChooserHelper.SAVE_FILE_DIR,
                createSuggestedName(
                    FileChooserHelper.SAVE_FILE_DIR.getAbsolutePath(),
                    FileChooserHelper.KC
                ),
                FileChooserHelper.KC
            );
        } else {
            file = SessionModel.getSaveFile();
            file = checkNameForTemplate(file);
            file = FileChooserHelper.saveAs(file);
        }

        if (file != null) {
            return save(file);
        } else {
            return false;
        }
    }

    public boolean open() {
        final File file = FileChooserHelper.open(FileChooserHelper.SAVE_FILE_DIR, FileChooserHelper.KC);
        if (file != null) {
            load(file);
            return true;
        } else {
            return false;
        }
    }

    public boolean saveAndOpen() {
        final boolean shouldCompleteAction = askUserIfSaveBeforeAction(Action.OPEN_FILE);
        if (shouldCompleteAction) {
            open();
        }
        return shouldCompleteAction;
    }

    private void load(@Nonnull final File file) {
        SessionModel.load(file);
        try {
            Files.copy(file.toPath(), TEMP_SAVE_FILE.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
            StaticMethods.shareErrorMessage("Failed to load file: " + file.getName(), ex);
            System.exit(1);
        }
    }

    public void initializeSaveFiles() {

        try {
            SettingsModel.load(Settings.SETTINGS_SAVE_FILE);
        } catch (NullPointerException e) { // currently load will throw NPE if it cannot find the file
            e.printStackTrace();
            SettingsModel.initialize();
            SettingsModel.save(Settings.SETTINGS_SAVE_FILE);
        }

        try {
            GlobalData.getInstance().initializeData();
        } catch (Exception e) {
            StaticMethods.shareErrorMessage(
                "Could not get worker names from the Excel sheet. Make sure the Excel"
                    + "\n"
                    + "template is selected in the Settings menu and that the Excel sheet is"
                    + "\n"
                    + "correctly formatted."
            );
            e.printStackTrace();
        }

        CompletedModel completed = new CompletedModel();
        CovenantModel covenant = CovenantModel.from();
        LBCModel lbc = LBCModel.from();
        WeekendModel weekend = new WeekendModel();
        NW_Data nw_data = new NW_Data();

        SessionModel.writeToTemporarySaveFile();
        JsonMethods.saveToFileJSON(completed, CompletedModel.class, TEMP_SAVE_FILE, Form.COMPLETED.getNum());
        JsonMethods.saveToFileJSON(covenant, CovenantModel.class, TEMP_SAVE_FILE, Form.COVENANT.getNum());
        JsonMethods.saveToFileJSON(lbc, LBCModel.class, TEMP_SAVE_FILE, Form.LBC.getNum());
        JsonMethods.saveToFileJSON(weekend, WeekendModel.class, TEMP_SAVE_FILE, Form.WEEKEND.getNum());
        JsonMethods.saveToFileJSON(nw_data, NW_Data.class, TEMP_SAVE_FILE, Form.SCHEDULED.getNum());
    }

    private static boolean askIfOverwriteTemplate(@Nonnull final File file) {

        if (!SessionModel.isSaveFileChosen()) {
            return true;
        }
        if (!TEMPLATE_LIST.contains(file.getName())) {
            return true;
        }

        final String[] options = {"Overwrite",  "Don't Overwrite"};
        final int OVERWRITE = 0;
        final String message  = "<html>Are you sure you want overwrite " + file.getName() + "?";

        final int response = JOptionPane.showOptionDialog(
            new JFrame(),
            message,
            null,
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            options,
            0
        );

        return response == OVERWRITE;
    }

    private static File appendExtensionIfNeeded(@Nonnull final File file) {
        final String path = file.getPath();
        if (FilenameUtils.isExtension(path, FileChooserHelper.KC)) {
            return file;
        } else {
            return new File(path + "." + FileChooserHelper.KC);
        }
    }

    private static File checkNameForTemplate(@Nonnull final File file) {
        if (!TEMPLATE_LIST.contains(file.getName())) {
            return file;
        } else {
            String fileName = createSuggestedName(
                FileChooserHelper.SAVE_FILE_DIR.getAbsolutePath(),
                FileChooserHelper.KC
            );
            return new File(FileChooserHelper.SAVE_FILE_DIR + "/" + fileName + "." + FileChooserHelper.KC);
        }
    }

    private static String createSuggestedName(String directory, String extension) {
        return FileNameHelper.createDatedFileName(directory, extension);
    }

    private boolean areEqual(@Nonnull final File file1, @Nonnull final File file2) {
        try {
            return FileUtils.contentEquals(file1, file2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns true if the client should complete the action it plans to take. Returns false if the
     * user backed out at some point and the client should not complete the action.
     */
    public boolean askUserIfSaveBeforeAction(@Nonnull final Action action) {

        if (SessionModel.isSaveFileChosen() && areEqual(TEMP_SAVE_FILE, SessionModel.getSaveFile())) {
            return true;
        }

        final String[] options = { "Save",  "Don't Save", "Cancel" };
        final int SAVE = 0;
        final int NOT_SAVE = 1;
        final int CANCEL = 2;
        final int EXIT = -1;

        final String message;
        if (action == Action.OPEN_FILE) {
            message = "<html>Would you like to save the current file before opening another?";
        } else if (action == Action.CLOSE_PROGRAM) {
            message = "<html>Would you like to save your file before closing?";
        } else {
            throw new RuntimeException("Unexpected action: " + action);
        }

        int response = JOptionPane.showOptionDialog(
            new JFrame(),
            message,
            null,
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            options,
            0
        );

        final boolean shouldClientCompleteAction;
        if (response == SAVE) {
            shouldClientCompleteAction = save();
        } else if (response == CANCEL || response == EXIT) {
            shouldClientCompleteAction = false;
        } else if (response == NOT_SAVE) {
            shouldClientCompleteAction = true;
        } else {
            throw new RuntimeException("Unexpected response value: " + response);
        }

        return shouldClientCompleteAction;
    }

    public enum Action {
        OPEN_FILE,
        CLOSE_PROGRAM;
    }
}
