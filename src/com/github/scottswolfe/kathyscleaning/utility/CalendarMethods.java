package com.github.scottswolfe.kathyscleaning.utility;

import javax.annotation.Nonnull;
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
        final Calendar calendar = trim(Calendar.getInstance());
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, increment);
        }
        return calendar;
    }

    public static Calendar copy(@Nonnull final Calendar date) {
        final Calendar copy = Calendar.getInstance();

        copy.set(
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DATE),
            date.get(Calendar.HOUR_OF_DAY),
            date.get(Calendar.MINUTE),
            date.get(Calendar.SECOND)
        );
        copy.set(Calendar.MILLISECOND, date.get(Calendar.MILLISECOND));

        return copy;
    }

    public static Calendar trim(@Nonnull final Calendar date) {
        final Calendar normalizedDate = Calendar.getInstance();

        normalizedDate.set(
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DATE),
            0,
            0,
            0
        );
        normalizedDate.set(Calendar.MILLISECOND, 0);

        return normalizedDate;
    }
}
