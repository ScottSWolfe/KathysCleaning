package com.github.scottswolfe.kathyscleaning.utility;

import java.util.Calendar;

/**
 * Utility class that stores static methods relating to days and dates
 */
public class CalendarMethods {

    public enum Week {
        PREV, NEXT;
    }
    
    /**
     * Returns the first day of the previous week
     * 
     * @return the first day of the week
     */
    public static Calendar getFirstDayOfWeek() {
        return getFirstDayOfWeek(Week.PREV);
    }
    
    /**
     * Returns the first day of the given week
     * 
     * @return the first day of the week
     */
    public static Calendar getFirstDayOfWeek(Week week) {
        int increment = -1;
        if (week == Week.NEXT) {
            increment = 1;
        }
        Calendar calendar = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, increment);
        }
        return calendar;
    }
    
    
    
}
