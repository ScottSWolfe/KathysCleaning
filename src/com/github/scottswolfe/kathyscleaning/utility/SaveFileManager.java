package com.github.scottswolfe.kathyscleaning.utility;

import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCModel;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.menu.model.SettingsModel;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendModel;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class SaveFileManager {

    public static SaveFileManager from() {
        return new SaveFileManager();
    }

    private SaveFileManager() {}

    public void loadFile(@Nonnull final File file) {
        SessionModel.load(file);
        try {
            Files.copy(file.toPath(), GeneralController.TEMP_SAVE_FILE.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
            StaticMethods.shareErrorMessage("Failed to load file: " + file.getName(), ex);
            System.exit(1);
        }
    }

    public void initializeSaveFiles() throws IOException {

        try {
            SettingsModel.load(Settings.SETTINGS_SAVE_FILE);
        } catch (NullPointerException e) { // currently load will throw NPE if it cannot find the file
            e.printStackTrace();
            SettingsModel.initialize();
            SettingsModel.save(Settings.SETTINGS_SAVE_FILE);
        }

        GlobalData.getInstance().initializeData();

        SessionModel.initialize();
        CompletedModel completed = new CompletedModel();
        CovenantModel covenant = CovenantModel.from();
        LBCModel lbc = LBCModel.from();
        WeekendModel weekend = new WeekendModel();
        NW_Data nw_data = new NW_Data();

        SessionModel.save();
        JsonMethods.saveToFileJSON(completed, CompletedModel.class, GeneralController.TEMP_SAVE_FILE, Form.COMPLETED.getNum());
        JsonMethods.saveToFileJSON(covenant, CovenantModel.class, GeneralController.TEMP_SAVE_FILE, Form.COVENANT.getNum());
        JsonMethods.saveToFileJSON(lbc, LBCModel.class, GeneralController.TEMP_SAVE_FILE, Form.LBC.getNum());
        JsonMethods.saveToFileJSON(weekend, WeekendModel.class, GeneralController.TEMP_SAVE_FILE, Form.WEEKEND.getNum());
        JsonMethods.saveToFileJSON(nw_data, NW_Data.class, GeneralController.TEMP_SAVE_FILE, Form.SCHEDULED.getNum());
    }
}
