package com.github.scottswolfe.kathyscleaning.scheduled.model;

public class ScheduledLBCException {

    private final String workerName;
    private final String meetTime;
    private final String note;

    public static ScheduledLBCException from(
        final String workerName,
        final String meetTime,
        final String note
    ) {
        return new ScheduledLBCException(workerName, meetTime, note);
    }

    private ScheduledLBCException(
        final String workerName,
        final String meetTime,
        final String note
    ) {
        this.workerName = workerName;
        this.meetTime = meetTime;
        this.note = note;
    }

    public String getWorkerName() {
        return workerName;
    }

    public String getMeetTime() {
        return meetTime;
    }

    public String getNote() {
        return note;
    }

    public boolean isEmpty() {
        return workerName.isEmpty() && meetTime.isEmpty() && note.isEmpty();
    }
}
