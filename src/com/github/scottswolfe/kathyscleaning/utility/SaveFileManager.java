package com.github.scottswolfe.kathyscleaning.utility;

import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;

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
        }
    }
}
