package com.github.scottswolfe.kathyscleaning.general.model;

import java.io.File;
import java.util.Calendar;

import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.utility.CalendarMethods;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;
import com.github.scottswolfe.kathyscleaning.utility.SaveFileManager;

import javax.annotation.Nonnull;

public class SessionModel {

    /**
     * The current save file for the week
     */
    private static File saveFile;

    /**
     *  Tracks whether a save file been chosen in the current session
     */
    private static boolean saveFileChosen;

    /**
     * The first day of the finished week
     */
    private static Calendar completedStartDay;

    public static void initialize() {
        SessionModel.saveFile = null;
        SessionModel.saveFileChosen = false;
        SessionModel.completedStartDay = CalendarMethods.getFirstDayOfWeek();
    }

    public static void save() {
        SessionSaveObject object = new SessionSaveObject(saveFile, saveFileChosen, completedStartDay);
        object.save(SaveFileManager.TEMP_SAVE_FILE);
    }

    public static void load(File file) {
        SessionSaveObject object = (SessionSaveObject)
                JsonMethods.loadFromFileJSON(SessionSaveObject.class, file, Form.SESSION.getNum());
        updateCurrentFileIfChanged(object, file);
        object.load();
    }

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
        SessionModel.saveFileChosen = true;
        SessionModel.saveFile = saveFile;
    }

    /**
     * @return the saveFileChosen
     */
    public static boolean isSaveFileChosen() {
        return SessionModel.saveFileChosen;
    }

    /**
     * @param saveFileChosen the saveFileChosen to set
     */
    public static void setSaveFileChosen(boolean saveFileChosen) {
        SessionModel.saveFileChosen = saveFileChosen;
    }

    /**
     * @return the completedStartDay
     */
    public static Calendar getCompletedStartDay() {
        return CalendarMethods.copy(SessionModel.completedStartDay);
    }

    /**
     * @param completedStartDay the completedStartDay to set
     */
    public static void setCompletedStartDay(Calendar completedStartDay) {
        SessionModel.completedStartDay = CalendarMethods.trim(completedStartDay);
    }

    public static Calendar getWeekendStartDay() {
        final Calendar date = getCompletedStartDay();
        date.add(Calendar.DATE, -1);
        return date;
    }

    public static Calendar getScheduledStartDay() {
        final Calendar date = getCompletedStartDay();
        date.add(Calendar.DATE, 7);
        return date;
    }

    private static void updateCurrentFileIfChanged(SessionSaveObject object, File file) {
        if (object.saveFile == null || !object.saveFile.getAbsolutePath().equals(file.getAbsolutePath())) {
            object.saveFile = file;
        }
    }

    private static class SessionSaveObject {

        private File saveFile;
        private final boolean saveFileChosen;
        private final Calendar completedStartDay;

        SessionSaveObject(final File saveFile, final boolean saveFileChosen, final Calendar completedStartDay) {
            this.saveFile = saveFile;
            this.saveFileChosen = saveFileChosen;
            this.completedStartDay = completedStartDay;
        }

        void save(@Nonnull final File file) {
            JsonMethods.saveToFileJSON(this, SessionSaveObject.class, file, Form.SESSION.getNum());
        }

        void load() {
            SessionModel.setSaveFile(saveFile);
            SessionModel.setSaveFileChosen(saveFileChosen);
            SessionModel.setCompletedStartDay(completedStartDay);
        }
    }
}
