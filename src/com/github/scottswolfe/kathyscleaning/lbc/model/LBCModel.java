package com.github.scottswolfe.kathyscleaning.lbc.model;

import com.github.scottswolfe.kathyscleaning.enums.DayOfWeek;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
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
            DAYS.stream().collect(Collectors.toMap(day -> day, LBCDay::from, (day1, day2) -> day1, LinkedHashMap::new))
        );
    }

    public static LBCModel copyOf(@Nonnull final LBCModel lbcModel) {
        return new LBCModel(
            LBCHeader.copyOf(lbcModel.lbcHeader),
            ImmutableMap.copyOf(lbcModel.lbcDays)
        );
    }

    private LBCModel(final LBCHeader lbcHeader, final Map<DayOfWeek, LBCDay> lbcDays) {
        this.lbcHeader = lbcHeader;
        this.lbcDays = lbcDays.values().stream()
            .sorted(Comparator.comparingInt(day -> day.getDayOfWeek().getIndex()))
            .collect(Collectors.toMap(LBCDay::getDayOfWeek, day -> day, (day1, day2) -> day1, LinkedHashMap::new));
    }

    public LBCHeader getLbcHeader() {
        return lbcHeader;
    }

    public Map<DayOfWeek, LBCDay> getLbcDays() {
        return lbcDays;
    }

    public void updateWorkersAndKeepSelections(@Nonnull final List<String> workerNames) {
        lbcHeader.updateWorkersAndKeepSelections(workerNames);
        lbcDays.values().forEach(lbcDay -> lbcDay.updateWorkersAndKeepSelections(workerNames));
    }
}
