package com.github.scottswolfe.kathyscleaning.utility;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TimeMethodsTest {

    @Test
    public void convertTo24HourTime_sameHourBeforeWindowStart() {
        assert24HourTimeConversion(
            "1:38", "1:42",
            "13:38", "13:42",
            TimeWindow.HOUSES
        );

        assert24HourTimeConversion(
            "1:38", "1:42",
            "13:38", "13:42",
            TimeWindow.COVENANT
        );

        assert24HourTimeConversion(
            "1:38", "1:42",
            "13:38", "13:42",
            TimeWindow.LBC
        );
    }

    @Test
    public void convertTo24HourTime_sameHourAfterWindowStart() {
        assert24HourTimeConversion(
            "7:00", "7:50",
            "07:00", "07:50",
            TimeWindow.HOUSES
        );

        assert24HourTimeConversion(
            "12:00", "12:50",
            "12:00", "12:50",
            TimeWindow.COVENANT
        );

        assert24HourTimeConversion(
            "6:00", "6:50",
            "06:00", "06:50",
            TimeWindow.LBC
        );
    }

    @Test
    public void convertTo24HourTime_differentHourBeforeWindowStart() {
        assert24HourTimeConversion(
            "3:20", "5:15",
            "15:20", "17:15",
            TimeWindow.HOUSES
        );

        assert24HourTimeConversion(
            "3:20", "5:15",
            "15:20", "17:15",
            TimeWindow.COVENANT
        );

        assert24HourTimeConversion(
            "3:20", "5:15",
            "15:20", "17:15",
            TimeWindow.LBC
        );
    }

    @Test
    public void convertTo24HourTime_differentHourAfterWindowStart() {
        assert24HourTimeConversion(
            "7:45", "10:33",
            "07:45", "10:33",
            TimeWindow.HOUSES
        );

        assert24HourTimeConversion(
            "11:05", "12:59",
            "11:05", "12:59",
            TimeWindow.COVENANT
        );

        assert24HourTimeConversion(
            "7:45", "10:33",
            "07:45", "10:33",
            TimeWindow.LBC
        );
    }

    @Test
    public void convertTo24HourTime_wrapOverNoon() {
        assert24HourTimeConversion(
            "9:30", "1:15",
            "09:30", "13:15",
            TimeWindow.HOUSES
        );

        assert24HourTimeConversion(
            "9:30", "1:15",
            "09:30", "13:15",
            TimeWindow.COVENANT
        );

        assert24HourTimeConversion(
            "9:30", "1:15",
            "09:30", "13:15",
            TimeWindow.LBC
        );
    }

    @Test
    public void convertTo24HourTime_wrapOverNoonTo12thHour() {
        assert24HourTimeConversion(
            "11:59", "12:01",
            "11:59", "12:01",
            TimeWindow.HOUSES
        );

        assert24HourTimeConversion(
            "11:59", "12:01",
            "11:59", "12:01",
            TimeWindow.COVENANT
        );

        assert24HourTimeConversion(
            "11:59", "12:01",
            "11:59", "12:01",
            TimeWindow.LBC
        );
    }

    @Test
    public void convertTo24HourTime_fromNoon() {
        assert24HourTimeConversion(
            "12:00", "1:30",
            "12:00", "13:30",
            TimeWindow.HOUSES
        );

        assert24HourTimeConversion(
            "12:00", "1:30",
            "12:00", "13:30",
            TimeWindow.COVENANT
        );

        assert24HourTimeConversion(
            "12:00", "1:30",
            "12:00", "13:30",
            TimeWindow.LBC
        );
    }

    @Test
    public void convertTo24HourTime_toNoon() {
        assert24HourTimeConversion(
            "10:30", "12:00",
            "10:30", "12:00",
            TimeWindow.HOUSES
        );

        assert24HourTimeConversion(
            "10:30", "12:00",
            "10:30", "12:00",
            TimeWindow.COVENANT
        );

        assert24HourTimeConversion(
            "10:30", "12:00",
            "10:30", "12:00",
            TimeWindow.LBC
        );
    }

    @Test
    public void convertTo24HourTime_sameTime_BeforeTimeWindowStart() {
        assert24HourTimeConversion(
            "3:11", "3:11",
            "15:11", "15:11",
            TimeWindow.HOUSES
        );

        assert24HourTimeConversion(
            "9:48", "9:48",
            "21:48", "21:48",
            TimeWindow.COVENANT
        );

        assert24HourTimeConversion(
            "3:11", "3:11",
            "15:11", "15:11",
            TimeWindow.LBC
        );
    }

    @Test
    public void convertTo24HourTime_sameTime_AfterTimeWindowStart() {
        assert24HourTimeConversion(
            "8:00", "8:00",
            "08:00", "08:00",
            TimeWindow.HOUSES
        );

        assert24HourTimeConversion(
            "10:01", "10:01",
            "10:01", "10:01",
            TimeWindow.COVENANT
        );

        assert24HourTimeConversion(
            "5:37", "5:37",
            "05:37", "05:37",
            TimeWindow.LBC
        );
    }

    @Test
    public void convertTo24HourTime_oneTimeHasLeadingZero() {
        assert24HourTimeConversion(
            "07:38", "9:15",
            "07:38", "09:15",
            TimeWindow.HOUSES
        );

        assert24HourTimeConversion(
            "07:38", "9:15",
            "19:38", "21:15",
            TimeWindow.COVENANT
        );

        assert24HourTimeConversion(
            "07:38", "9:15",
            "07:38", "09:15",
            TimeWindow.LBC
        );
    }

    @Test
    public void convertTo24HourTime_startingAt0() {
        assert24HourTimeConversion(
            "0:00", "1:00",
            "00:00", "01:00",
            TimeWindow.HOUSES
        );
        assert24HourTimeConversion(
            "0:00", "1:00",
            "00:00", "01:00",
            TimeWindow.COVENANT
        );
        assert24HourTimeConversion(
            "0:00", "1:00",
            "00:00", "01:00",
            TimeWindow.LBC
        );
    }

    @Test
    public void convertTo24HourTime_invalidTimes() {
        assertThrows(
            IllegalArgumentException.class,
            () -> TimeMethods.convertTo24HourFormat("25:00", "25:30", TimeWindow.HOUSES)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> TimeMethods.convertTo24HourFormat("1:30", "1:60", TimeWindow.HOUSES)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> TimeMethods.convertTo24HourFormat("0:0", "1:00", TimeWindow.HOUSES)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> TimeMethods.convertTo24HourFormat("10:00", "1:0", TimeWindow.HOUSES)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> TimeMethods.convertTo24HourFormat("1:00", "130", TimeWindow.HOUSES)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> TimeMethods.convertTo24HourFormat("1b:00", "12:30", TimeWindow.HOUSES)
        );
    }

    @Test
    public void convertTo24HourTime_alreadyIn24HourFormat() {
        assert24HourTimeConversion(
            "15:38", "15:40",
            "15:38", "15:40",
            TimeWindow.LBC
        );

        assert24HourTimeConversion(
            "23:58", "23:59",
            "23:58", "23:59",
            TimeWindow.LBC
        );
    }

    @Test
    public void convertTo24HourTime_timesIn24HourFormat_wrapOverMidnight() {
        assertThrows(
            IllegalArgumentException.class,
            () -> TimeMethods.convertTo24HourFormat("15:40", "15:38", TimeWindow.HOUSES)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> TimeMethods.convertTo24HourFormat("12:59", "12:58", TimeWindow.COVENANT)
        );

        assertThrows(
            IllegalArgumentException.class,
            () -> TimeMethods.convertTo24HourFormat("23:45", "00:15", TimeWindow.HOUSES)
        );
    }

    @Test
    public void convertTo24HourTime_timesNotIn24HourFormat_wrapOverNoon() {
        assert24HourTimeConversion(
            "1:40", "1:38",
            "01:40", "13:38",
            TimeWindow.LBC
        );

        assert24HourTimeConversion(
            "11:59", "11:58",
            "11:59", "23:58",
            TimeWindow.COVENANT
        );
    }

    @Test
    public void getNumberOfHours_sameHour() {
        assertNumberOfHours("1:38", "1:44", TimeWindow.HOUSES, 0.1);
        assertNumberOfHours("1:38", "1:44", TimeWindow.COVENANT, 0.1);
        assertNumberOfHours("1:38", "1:44", TimeWindow.LBC, 0.1);
    }

    @Test
    public void getNumberOfHours_acrossNoon() {
        assertNumberOfHours("11:15", "1:45", TimeWindow.HOUSES, 2.5);
        assertNumberOfHours("11:15", "1:45", TimeWindow.COVENANT, 2.5);
        assertNumberOfHours("11:15", "1:45", TimeWindow.LBC, 2.5);
    }

    @Test
    public void getNumberOfHours_early() {
        assertNumberOfHours("5:30", "6:30", TimeWindow.HOUSES, 1.0);
        assertNumberOfHours("5:30", "6:30", TimeWindow.COVENANT, 1.0);
        assertNumberOfHours("5:30", "6:30", TimeWindow.LBC, 1.0);
    }

    @Test
    public void getNumberOfHours_late() {
        assertNumberOfHours("8:30", "10:45", TimeWindow.HOUSES, 2.25);
        assertNumberOfHours("8:30", "10:45", TimeWindow.COVENANT, 2.25);
        assertNumberOfHours("8:30", "10:45", TimeWindow.LBC, 2.25);
    }

    @Test
    public void getNumberOfHours_longTimeRange() {
        assertNumberOfHours("1:59", "1:53", TimeWindow.HOUSES, 11.9);
        assertNumberOfHours("1:59", "1:53", TimeWindow.COVENANT, 11.9);
        assertNumberOfHours("1:59", "1:53", TimeWindow.LBC, 11.9);
    }

    @Test
    public void getNumberOfHours_sameStartAndEndTimes() {
        assertNumberOfHours("9:38", "9:38", TimeWindow.HOUSES, 0);
        assertNumberOfHours("9:38", "9:38", TimeWindow.COVENANT, 0);
        assertNumberOfHours("9:38", "9:38", TimeWindow.LBC, 0);
    }

    @Test
    public void getNumberOfHours_acrossMidnight() {
        assertThrows(
            IllegalArgumentException.class,
            () -> TimeMethods.getNumberOfHours("12:30", "12:15", TimeWindow.COVENANT)
        );
    }

    private void assert24HourTimeConversion(
        final String beginTimeBefore,
        final String endTimeBefore,
        final String beginTimeAfter,
        final String endTimeAfter,
        final TimeWindow timeWindow
    ) {
        Pair<String, String> times = TimeMethods.convertTo24HourFormat(beginTimeBefore, endTimeBefore, timeWindow);
        assertEquals(beginTimeAfter, times.getLeft());
        assertEquals(endTimeAfter, times.getRight());
    }

    private void assertNumberOfHours(
        final String beginTime,
        final String endTime,
        final TimeWindow timeWindow,
        double expectedHours
    ) {
        final double actualHours = TimeMethods.getNumberOfHours(beginTime, endTime, timeWindow);
        assertEquals(expectedHours, actualHours);
    }
}
