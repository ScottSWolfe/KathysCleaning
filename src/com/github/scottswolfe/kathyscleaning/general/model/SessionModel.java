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
    private static File saveFile = null;

    /**
     * The first day of the finished week
     */
    private static Calendar completedStartDay = CalendarMethods.getFirstDayOfWeek();

    public static void writeToTemporarySaveFile() {
        final SessionData sessionData = new SessionData(completedStartDay);
        JsonMethods.saveToFileJSON(sessionData, SessionData.class, SaveFileManager.TEMP_SAVE_FILE, Form.SESSION.getNum());
    }

    public static void load() {
        SessionData sessionData = JsonMethods.loadFromFileJSON(
            SessionData.class,
            SaveFileManager.TEMP_SAVE_FILE,
            Form.SESSION.getNum()
        );
        SessionModel.setCompletedStartDay(sessionData.completedStartDay);
    }

    public static Calendar readScheduledStartDayFromFile(@Nonnull final File file) {
        final SessionData sessionData = JsonMethods.loadFromFileJSON(SessionData.class, file, Form.SESSION.getNum());
        return toScheduledStartDay(sessionData.completedStartDay);
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
        SessionModel.saveFile = saveFile;
    }

    /**
     * @return the saveFileChosen
     */
    public static boolean isSaveFileChosen() {
        return SessionModel.saveFile != null;
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
        return toScheduledStartDay(date);
    }

    private static Calendar toScheduledStartDay(@Nonnull final Calendar completedStartDay) {
        final Calendar scheduledStartDay = CalendarMethods.copy(completedStartDay);
        scheduledStartDay.add(Calendar.DATE, 7);
        return scheduledStartDay;
    }

    private static class SessionData {

        private final Calendar completedStartDay;

        SessionData(final Calendar completedStartDay) {
            this.completedStartDay = completedStartDay;
        }
    }
}
