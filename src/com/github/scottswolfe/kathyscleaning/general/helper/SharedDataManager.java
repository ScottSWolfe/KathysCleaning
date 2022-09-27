package com.github.scottswolfe.kathyscleaning.general.helper;

import com.github.scottswolfe.kathyscleaning.enums.SaveType;
import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.general.model.ImmutableSharedDataModel;
import com.github.scottswolfe.kathyscleaning.general.model.SharedDataModel;
import com.github.scottswolfe.kathyscleaning.utility.CalendarMethods;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;
import com.github.scottswolfe.kathyscleaning.utility.SaveFileManager;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Calendar;
import java.util.List;

public class SharedDataManager {

    private static final SharedDataManager sharedDataManagerInstance = new SharedDataManager();
    private static final GlobalData globalData = GlobalData.getInstance();

    private SharedDataModel sharedDataModel;

    public static SharedDataManager getInstance() {
        return sharedDataManagerInstance;
    }

    private SharedDataManager() {}

    public void writeToTemporarySaveFile() {
        JsonMethods.saveToFileJSON(
            getSharedDataModel(),
            SharedDataModel.class,
            SaveFileManager.TEMP_SAVE_FILE,
            SaveType.SESSION
        );
    }

    public void loadFromTemporarySaveFile() {
        sharedDataModel = JsonMethods.loadFromFileJSON(
            SharedDataModel.class,
            SaveFileManager.TEMP_SAVE_FILE,
            SaveType.SESSION
        );
    }

    public List<String> getAvailableWorkerNames() {
        return ImmutableList.copyOf(getSharedDataModel().availableWorkerNames());
    }

    public void setAvailableWorkerNames(@Nonnull final List<String> availableWorkerNames) {
        sharedDataModel = ImmutableSharedDataModel.copyOf(getSharedDataModel())
            .withAvailableWorkerNames(availableWorkerNames);
    }

    public List<String> getCovenantWorkerNames() {
        return ImmutableList.copyOf(getSharedDataModel().covenantWorkerNames());
    }

    public void setCovenantWorkerNames(@Nonnull final List<String> covenantWorkerNames) {
        sharedDataModel = ImmutableSharedDataModel.copyOf(getSharedDataModel())
            .withCovenantWorkerNames(covenantWorkerNames);
    }

    public Calendar readScheduledStartDayFromFile(@Nonnull final File file) {
        final SharedDataModel sharedDataModel = JsonMethods.loadFromFileJSON(
            SharedDataModel.class,
            file,
            SaveType.SESSION
        );
        return toScheduledStartDay(sharedDataModel.completedStartDay());
    }

    public Calendar getCompletedStartDay() {
        return CalendarMethods.copy(getSharedDataModel().completedStartDay());
    }

    public void setCompletedStartDay(@Nonnull final Calendar completedStartDay) {
        sharedDataModel = ImmutableSharedDataModel.copyOf(getSharedDataModel())
            .withCompletedStartDay(CalendarMethods.trim(completedStartDay));
    }

    public Calendar getWeekendStartDay() {
        final Calendar date = getCompletedStartDay();
        date.add(Calendar.DATE, -1);
        return date;
    }

    public Calendar getScheduledStartDay() {
        final Calendar date = getCompletedStartDay();
        return toScheduledStartDay(date);
    }

    private Calendar toScheduledStartDay(@Nonnull final Calendar completedStartDay) {
        final Calendar scheduledStartDay = CalendarMethods.copy(completedStartDay);
        scheduledStartDay.add(Calendar.DATE, 7);
        return scheduledStartDay;
    }

    private SharedDataModel getSharedDataModel() {
        if (sharedDataModel == null) {
            sharedDataModel = ImmutableSharedDataModel.builder()
                .completedStartDay(CalendarMethods.getFirstDayOfWeek())
                .availableWorkerNames(globalData.getDefaultWorkerNames())
                .covenantWorkerNames(globalData.getDefaultWorkerNames())
                .build();
        }
        return sharedDataModel;
    }
}
