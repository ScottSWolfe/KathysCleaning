package com.github.scottswolfe.kathyscleaning.general.model;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.Calendar;
import java.util.List;

@Gson.TypeAdapters
@Value.Immutable
public abstract class SharedDataModel {
    public abstract Calendar completedStartDay();
    public abstract List<String> availableWorkerNames();
}
