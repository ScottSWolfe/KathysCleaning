package com.github.scottswolfe.kathyscleaning.general.model;

import java.io.File;

public class SessionModel {

    /**
     * The current save file for the week
     */
    private static File saveFile = null;

    /**
     * @return the saveFile
     */
    public static File getSaveFile() {
        return SessionModel.saveFile;
    }

    /**
     * @param saveFile the saveFile to set
     */
    public static void setSaveFile(File saveFile) {
        SessionModel.saveFile = saveFile;
    }

    /**
     * @return the saveFileChosen
     */
    public static boolean isSaveFileChosen() {
        return SessionModel.saveFile != null;
    }
}
