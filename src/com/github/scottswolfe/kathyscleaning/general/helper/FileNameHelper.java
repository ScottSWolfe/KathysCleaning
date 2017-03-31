package com.github.scottswolfe.kathyscleaning.general.helper;

import java.io.File;
import java.util.Calendar;

import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.utility.CalendarMethods;

public class FileNameHelper {

/* CLASS VARIABLES ========================================================== */
    
    private static final String[] MONTHS = { "January", "February", "March",
            "April", "May", "June", "July",
            "August", "September", "October",
            "November", "December" };
    
    
    
/* PUBLIC METHODS =========================================================== */
    
    public static String createDatedFileName(String directory, String extension) {
        String fileName = ""; 
        fileName += fullDate();
        fileName += copyNumber(directory, fileName, extension);
        return fileName;
    }
    
    public static String generateSaveName(CompletedModel completedModel) {
        String save_name = new String();
        Calendar c = SessionModel.getCompletedStartDay();
        Calendar copy = (Calendar) c.clone();
        int firstday = copy.get(Calendar.DAY_OF_MONTH);
        copy.add( Calendar.DAY_OF_MONTH, 4);
        int lastday = copy.get( Calendar.DAY_OF_MONTH );
        
        if (lastday < firstday) {
            save_name += MONTHS[c.get(Calendar.MONTH)] +
                        firstday +
                        "-" +
                        MONTHS[copy.get( Calendar.MONTH )] +
                        lastday + "," +
                        c.get(Calendar.YEAR) +
                        ".xlsx";
        }
        else {
            save_name += MONTHS[c.get(Calendar.MONTH)] +
                        firstday +
                        "-" +
                        lastday + "," +
                        c.get(Calendar.YEAR) +
                        ".xlsx";
        }
        return save_name;
    }
    
    public static String incrementPathnameCount(String pathname, int count, Calendar c) {
        int firstday = getFirstDayOfWeek(c);
        int lastday = getLastDayOfWeek(c);
        if ( lastday < firstday ) {
            pathname = new String(     
                    MONTHS[c.get(Calendar.MONTH)] +
                    firstday +
                    "-" +
                    MONTHS[getNewMonth(c)] +
                    lastday + "," +
                    c.get(Calendar.YEAR) +
                    "("+count+")" +                 
                    ".xlsx");
        }
        else {
            pathname = new String(     
                    MONTHS[c.get(Calendar.MONTH)] +
                    firstday +
                    "-" +
                    lastday + "," +
                    c.get(Calendar.YEAR) +
                    "("+count+")" +                 
                    ".xlsx");
        }
        return pathname;
    }
    
    public static String getDayString(Calendar c) {
        String day = new String();
        int firstday = getFirstDayOfWeek(c);
        int lastday = getLastDayOfWeek(c);
        if (lastday < firstday) {
            day += firstday +
                   " - " +
                   MONTHS[getNewMonth(c)] +
                   " " +
                   lastday;
        } else {
            day += firstday +
                   " - " +
                   lastday;
        }
        return day;
    }
    
    public static String getMonthString(Calendar c) {
        return MONTHS[c.get(Calendar.MONTH)];
    }
    
    public static String getYearString(Calendar c) {
        return String.valueOf(c.get(Calendar.YEAR));
    }
    
    
/* PRIVATE METHODS ========================================================== */
    
    private static int getFirstDayOfWeek(Calendar c) {
        return c.get(Calendar.DAY_OF_MONTH);
    }
    
    private static int getLastDayOfWeek(Calendar c) {
        Calendar copy = (Calendar) c.clone();
        copy.add(Calendar.DAY_OF_MONTH, 4);
        return copy.get(Calendar.DAY_OF_MONTH);
    }
    
    private static int getNewMonth(Calendar c) {
        Calendar copy = (Calendar) c.clone();
        copy.add(Calendar.DAY_OF_MONTH, 4);
        return copy.get(Calendar.MONTH);
    }
    
    private static String fullDate() {
        Calendar calendar = CalendarMethods.getFirstDayOfWeek();
        return new String(
                calendar.get(Calendar.YEAR) +
                "_" +
                (calendar.get(Calendar.MONTH) + 1) +
                "_" +
                calendar.get(Calendar.DATE));
    }
    
    private static String copyNumber(String directory, String fileName, String extension) {
        String copyNumber = "";
        File file;
        int i = 0;
        while(true) {
            file = new File(directory + "/" + fileName + copyNumber + "." + extension);
            if (!file.exists()) {
                return copyNumber;
            }
            i++;
            copyNumber = " (" + i + ")";
        }
    }
    
}
