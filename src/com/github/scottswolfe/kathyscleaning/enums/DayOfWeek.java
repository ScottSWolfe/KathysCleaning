package com.github.scottswolfe.kathyscleaning.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum DayOfWeek {

    MONDAY("Monday", 0),
    TUESDAY("Tuesday", 1),
    WEDNESDAY("Wednesday", 2),
    THURSDAY("Thursday", 3),
    FRIDAY("Friday", 4),
    SATURDAY("Saturday", 5),
    SUNDAY("Sunday", 6);

    private final String name;
    private final int index;

    DayOfWeek(final String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static DayOfWeek fromIndex(final int index) {
        return Arrays.stream(DayOfWeek.values())
            .filter(dayOfWeek -> dayOfWeek.index == index)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Unexpected DayOfWeek index"));
    }

    public String getName() {
        return name;
    }

    public static Set<String> getSetOfNames() {
        return Arrays.stream(DayOfWeek.values())
            .map(DayOfWeek::getName)
            .collect(Collectors.toSet());
    }
}
