package com.github.scottswolfe.kathyscleaning.utility;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class TimeMethods {

    public static double getNumberOfHours(
        final String beginTime,
        final String endTime,
        final TimeWindow timeWindow
    ) {
        final Pair<String, String> times = convertTo24HourFormat(beginTime, endTime, timeWindow);

        if (times.getLeft().compareTo(times.getRight()) > 0) {
            throw new RuntimeException("Begin time is greater than end time. This should not be possible. "
                + "Begin time: " + beginTime + "; End time: " + endTime + ".");
        }

        final int beginTimeMinutes = getMinutesIntoDay(times.getLeft());
        final int endTimeMinutes = getMinutesIntoDay(times.getRight());

        return (endTimeMinutes - beginTimeMinutes) / 60.0;
    }

    private static int getMinutesIntoDay(final String time) {
        final String[] tokens = time.split(":");
        return Integer.parseInt(tokens[0]) * 60 + Integer.parseInt(tokens[1]);
    }

    /**
     *  This method takes a beginning and ending time and converts them to 24-hour format. It
     *  infers whether the times are AM or PM based on the times and the given TimeWindow which
     *  suggests which 12-hour time window the times are likely in. This does not allow times to
     *  wrap around midnight. If times go cross 12:00 it is assumed that they are crossing noon.
     *
     *  Example Mappings:
     *    [7:38 - 9:15]   -> [07:38 - 09:15] or [19:38 - 21:15]
     *    [9:15 - 1:45]   -> [09:15 - 13:45]
     *    [11:15 - 11:45] -> [11:15 - 11:45] or [23:15 - 23:45]
     *    [13:22 - 15:17] -> [13:22 - 15:17]
     *    [23:45 - 00:15] -> IllegalArgumentException
     */
    public static Pair<String, String> convertTo24HourFormat(
        final String beginTime,
        final String endTime,
        final TimeWindow timeWindow
    ) {
        if (beginTime == null || beginTime.isEmpty()) {
            throw new IllegalArgumentException("Begin time is null or empty");
        }
        if (endTime == null || endTime.isEmpty()) {
            throw new IllegalArgumentException("End time is null or empty");
        }

        if (isInvalid(beginTime) || isInvalid(endTime)) {
            throw new IllegalArgumentException("Invalid times. Begin Time: '" + beginTime + "'; End Time: '" + endTime + "'");
        }

        final String normalizedBeginTime = beginTime.length() == 5 ? beginTime : "0" + beginTime;
        final String normalizedEndTime = endTime.length() == 5 ? endTime : "0" + endTime;

        // The case where we wrap over midnight
        if (doesCrossMidnight(normalizedBeginTime, normalizedEndTime)) {
            throw new IllegalArgumentException(
                "Error: Begin and end times wrap over midnight. Begin Time: '" + beginTime + "'; End Time: '" + endTime + "'"
            );
        }

        // The case where we wrap over noon
        else if (
            normalizedEndTime.compareTo(normalizedBeginTime) < 0
            || (normalizedEndTime.split(":")[0].equals("12")
                && normalizedBeginTime.split(":")[0].compareTo("12") < 0)
        ) {
            return Pair.of(normalizedBeginTime, to24HourPM(normalizedEndTime));
        }

        // The case where we don't wrap over noon or midnight
        else {
            if (normalizedBeginTime.compareTo(timeWindow.getWindowStart()) >= 0
                || normalizedBeginTime.startsWith("00:")
                || normalizedBeginTime.compareTo("12:00") >= 0
            ) {
                return Pair.of(normalizedBeginTime, normalizedEndTime);
            } else {
                return Pair.of(add12Hours(normalizedBeginTime), add12Hours(normalizedEndTime));
            }
        }
    }

    /**
     *  Checking if valid 24-hour time format between 00:00 and 23:59.
     *  Example times: 0:30, 00:59, 06:20, 9:38, 13:50, 22:09.
     */
    private static boolean isInvalid(final String time) {
        if (time.length() < 4 || time.length() > 5) {
            return true;
        }

        final String[] tokens = time.split(":");
        if (tokens.length != 2) {
            return true;
        }

        // Validate the hour
        if (tokens[0].length() < 1 || tokens[0].length() > 2) {
            return true;
        }
        for (char c : tokens[0].toCharArray()) {
            if (!Character.isDigit(c)) {
                return true;
            }
        }
        if (tokens[0].length() == 2) {
            if (Character.getNumericValue(tokens[0].charAt(0)) > 2) {
                return true;
            } else if (
                Character.getNumericValue(tokens[0].charAt(0)) == 2
                    && Character.getNumericValue(tokens[0].charAt(1)) > 3
            ) {
                return true;
            }
        }

        // Validate the minutes
        if (tokens[1].length() != 2) {
            return true;
        }
        for (char c : tokens[1].toCharArray()) {
            if (!Character.isDigit(c)) {
                return true;
            }
        }
        if (Character.getNumericValue(tokens[1].charAt(0)) > 5) {
            return true;
        }

        return false;
    }

    private static String to24HourPM(final String time) {
        final String[] tokens = time.split(":");

        if (tokens[0].compareTo("12") >= 0) {
            return time;
        } else {
            final String hour = String.valueOf(Integer.parseInt(tokens[0]) + 12);
            return hour + ":" + tokens[1];
        }
    }

    private static String add12Hours(final String time) {
        final String[] tokens = time.split(":");

        String hour;
        if (tokens[0].compareTo("11") > 0) {
            hour = String.valueOf(Integer.parseInt(tokens[0]) - 12);
            hour = hour.length() == 2 ? hour : "0" + hour;
        } else {
            hour = String.valueOf(Integer.parseInt(tokens[0]) + 12);
        }

        return hour + ":" + tokens[1];
    }

    private static boolean doesCrossMidnight(final String beginTime, final String endTime) {
        if (endTime.compareTo(beginTime) >= 0) {
            return false;
        }

        if (beginTime.compareTo("13:00") >= 0) {
            return true;
        }

        return beginTime.compareTo("12:00") >= 0 && endTime.compareTo("12:00") >= 0;
    }
}
