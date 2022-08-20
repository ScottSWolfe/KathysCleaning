package com.github.scottswolfe.kathyscleaning.utility;

public enum TimeWindow {

    HOUSES("06:30"),   // 6:30am - 6:30pm
    COVENANT("10:00"), // 10:00am - 10:00pm
    LBC("05:00");      // 5:00am - 5:00pm

    /**
     * The 24-hour formatted time representation of the start of a 12-hour time range during which
     * a type of work is typically done. This window is used as a suggestion when determining
     * whether the beginning and ending times of some work are AM or PM.
     */
    private final String windowStart;

    TimeWindow(final String windowStart) {
        this.windowStart = windowStart;
    }

    public String getWindowStart() {
        return windowStart;
    }
}
