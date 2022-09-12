package com.github.scottswolfe.kathyscleaning.scheduled.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ScheduledLBCExceptions {

    final List<ScheduledLBCException> scheduledLBCExceptions;

    public static ScheduledLBCExceptions from() {
        return new ScheduledLBCExceptions(new ArrayList<>());
    }

    public static ScheduledLBCExceptions from(final List<ScheduledLBCException> scheduledLBCExceptions) {
        return new ScheduledLBCExceptions(scheduledLBCExceptions);
    }

    private ScheduledLBCExceptions(final List<ScheduledLBCException> scheduledLBCExceptions) {
        this.scheduledLBCExceptions = scheduledLBCExceptions;
    }

    public int size() {
        return scheduledLBCExceptions.size();
    }

    public ScheduledLBCException get(final int index) {
        return scheduledLBCExceptions.get(index);
    }

    public boolean isEmpty() {
        return scheduledLBCExceptions.stream().allMatch(ScheduledLBCException::isEmpty);
    }

    public Stream<ScheduledLBCException> stream() {
        return scheduledLBCExceptions.stream();
    }

    public Optional<ScheduledLBCException> getExceptionIfExists(final String workerName) {
        return scheduledLBCExceptions.stream()
            .filter(exception -> exception.getWorkerName().equals(workerName))
            .findFirst();
    }
}
