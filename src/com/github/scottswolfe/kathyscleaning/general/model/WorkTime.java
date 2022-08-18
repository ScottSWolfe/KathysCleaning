package com.github.scottswolfe.kathyscleaning.general.model;

import com.github.scottswolfe.kathyscleaning.enums.DayOfWeek;

public class WorkTime {

    // Warning: potentially nullable due to this being added after WorkTime was already in use
    final private DayOfWeek dayOfWeek;

    private String beginTime;
    private String endTime;

    private WorkTime(DayOfWeek dayOfWeek, String beginTime, String endTime) {
        this.dayOfWeek = dayOfWeek;
        this.beginTime = beginTime;
        this.endTime = endTime;
    }

    public static WorkTime from(DayOfWeek dayOfWeek, String beginTime, String endTime) {
        return new WorkTime(dayOfWeek, beginTime, endTime);
    }

    public static WorkTime from(String beginTime, String endTime) {
        return new WorkTime(null, beginTime, endTime);
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}
