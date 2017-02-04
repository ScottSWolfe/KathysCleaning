package com.github.scottswolfe.kathyscleaning.general.helper;

import java.util.Calendar;

import com.github.scottswolfe.kathyscleaning.completed.model.Data;

public class DateHelper {

    private static final String[] MONTHS = { "January", "February", "March",
            "April", "May", "June", "July",
            "August", "September", "October",
            "November", "December" };
    
    public static String generateSaveName(Data data) {
        String save_name = new String();
        Calendar c = data.date;
        Calendar copy2 = (Calendar) c.clone();
        int firstday = copy2.get(Calendar.DAY_OF_MONTH);
        copy2.add( Calendar.DAY_OF_MONTH, 4);
        int lastday = copy2.get( Calendar.DAY_OF_MONTH );
        
        if (lastday < firstday) {
            save_name += MONTHS[c.get(Calendar.MONTH)] +
                        firstday +
                        "-" +
                        MONTHS[copy2.get( Calendar.MONTH )] +
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
}
