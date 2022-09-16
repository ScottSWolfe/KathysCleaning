package com.github.scottswolfe.kathyscleaning.lbc.model;

import com.github.scottswolfe.kathyscleaning.enums.DayOfWeek;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LBCModel {

    public static final List<DayOfWeek> DAYS = Arrays.asList(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY
    );

    private final LBCHeader lbcHeader;
    private final Map<DayOfWeek, LBCDay> lbcDays;

    public static LBCModel from(final LBCHeader lbcHeader, final Map<DayOfWeek, LBCDay> lbcDays) {
        return new LBCModel(lbcHeader, lbcDays);
    }

    public static LBCModel from() {
        return new LBCModel(
            LBCHeader.from(),
            DAYS.stream().collect(Collectors.toMap(day -> day, day -> LBCDay.from(day)))
        );
    }

    private LBCModel(final LBCHeader lbcHeader, final Map<DayOfWeek, LBCDay> lbcDays) {
        this.lbcHeader = lbcHeader;
        this.lbcDays = lbcDays;
    }

    public LBCHeader getLbcHeader() {
        return lbcHeader;
    }

    public Map<DayOfWeek, LBCDay> getLbcDays() {
        return lbcDays;
    }

    public void setWorkers(final List<List<String>> workerNames) {
        lbcHeader.setWorkers(workerNames);
        lbcDays.values().forEach(lbcDay -> lbcDay.setWorkers(workerNames));
    }
}
