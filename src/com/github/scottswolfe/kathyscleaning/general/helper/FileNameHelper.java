package com.github.scottswolfe.kathyscleaning.general.helper;

import java.io.File;
import java.util.Calendar;

import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;

import javax.annotation.Nonnull;

public class FileNameHelper {

    private static final String[] MONTHS = {
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    };

    public static String createDatedFileName(
        @Nonnull final String directory,
        @Nonnull final String extension
    ) {
        final String fileName = fullDate();
        return addCopyNumberIfNeeded(directory, fileName, extension);
    }

    public static String addCopyNumberIfNeeded(
        @Nonnull final String directory,
        @Nonnull final String fileName,
        @Nonnull final String extension
    ) {
        return fileName + copyNumber(directory, fileName, extension);
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

    private static String fullDate() {
        Calendar calendar = SessionModel.getCompletedStartDay();
        return calendar.get(Calendar.YEAR)
            + "_"
            + toTwoDigits(String.valueOf(calendar.get(Calendar.MONTH) + 1))
            + "_"
            + toTwoDigits(String.valueOf(calendar.get(Calendar.DATE)));
    }

    private static String toTwoDigits(@Nonnull final String number) {
        if (number.length() == 1) {
            return "0" + number;
        } else if (number.length() == 2) {
            return number;
        } else {
            throw new IllegalArgumentException("Unexpected number. Should be 1 or 2 digits long. Number: " + number);
        }
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
