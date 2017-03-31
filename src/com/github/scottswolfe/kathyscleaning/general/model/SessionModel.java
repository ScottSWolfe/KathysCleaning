package com.github.scottswolfe.kathyscleaning.general.model;

import java.io.File;
import java.util.Calendar;

import com.github.scottswolfe.kathyscleaning.utility.CalendarMethods;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;
import com.github.scottswolfe.kathyscleaning.utility.CalendarMethods.Week;

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
    
    /**
     * The first day of the upcoming week
     */
    private static Calendar scheduledStartDay;


    
/* PUBLIC METHODS =========================================================== */
    
    public static void initialize() {
        saveFile = null;
        saveFileChosen = false;
        completedStartDay = CalendarMethods.getFirstDayOfWeek();
        scheduledStartDay = CalendarMethods.getFirstDayOfWeek(Week.NEXT); 
    }

    public static void save(File file) {
        SessionSaveObject object = 
                new SessionModel().new SessionSaveObject(saveFile, saveFileChosen, completedStartDay, scheduledStartDay);
        object.save(file);
    }
    
    public static void load(File file) {
        SessionSaveObject object = (SessionSaveObject)
                JsonMethods.loadFromFileJSON(SessionSaveObject.class, file, 4);
        object.load();
    }
    
    
    
/* GETTERS/SETTERS ========================================================== */
    
    /**
     * @return the saveFile
     */
    public static File getSaveFile() {
        return saveFile;
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
        return saveFileChosen;
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
        return (Calendar) completedStartDay.clone();
    }

    /**
     * @param completedStartDay the completedStartDay to set
     */
    public static void setCompletedStartDay(Calendar completedStartDay) {
        SessionModel.completedStartDay = (Calendar) completedStartDay.clone();
    }

    /**
     * @return the scheduledStartDay
     */
    public static Calendar getScheduledStartDay() {
        return scheduledStartDay;
    }

    /**
     * @param scheduledStartDay the scheduledStartDay to set
     */
    public static void setScheduledStartDay(Calendar scheduledStartDay) {
        SessionModel.scheduledStartDay = (Calendar) scheduledStartDay.clone();
    }

    
    
/* PRIVATE CLASS ============================================================ */
    
    private class SessionSaveObject {

        public File saveFile;
        public boolean saveFileChosen;
        public Calendar completedStartDay;
        public Calendar scheduledStartDay;

        SessionSaveObject(File saveFile, boolean saveFileChosen,
                Calendar completedStartDay, Calendar scheduledStartDay) {
            this.saveFile = saveFile;
            this.saveFileChosen = saveFileChosen;
            this.completedStartDay = completedStartDay;
            this.scheduledStartDay = scheduledStartDay;
        }
        
        void save(File file) {
            JsonMethods.saveToFileJSON(this, SessionSaveObject.class, file, 4);
        }
        
        void load() {
            SessionModel.setSaveFile(saveFile);
            SessionModel.setSaveFileChosen(saveFileChosen);
            SessionModel.setCompletedStartDay(completedStartDay);
            SessionModel.setScheduledStartDay(scheduledStartDay);
        }
    }
    
}
