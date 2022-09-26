package com.github.scottswolfe.kathyscleaning.general.helper;

import com.github.scottswolfe.kathyscleaning.enums.SaveType;
import com.github.scottswolfe.kathyscleaning.general.model.ImmutableSharedDataModel;
import com.github.scottswolfe.kathyscleaning.general.model.SharedDataModel;
import com.github.scottswolfe.kathyscleaning.utility.CalendarMethods;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;
import com.github.scottswolfe.kathyscleaning.utility.SaveFileManager;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Calendar;

public class SharedDataManager {

    private static final SharedDataManager sharedDataManagerInstance = new SharedDataManager();

    private SharedDataModel sharedDataModel;

    public static SharedDataManager getInstance() {
        return sharedDataManagerInstance;
    }

    private SharedDataManager() {
        sharedDataModel = ImmutableSharedDataModel.builder()
            .completedStartDay(CalendarMethods.getFirstDayOfWeek())
            .availableWorkerNames(ImmutableList.of())
            .build();
    }

    public void writeToTemporarySaveFile() {
        JsonMethods.saveToFileJSON(
            sharedDataModel,
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

    public Calendar readScheduledStartDayFromFile(@Nonnull final File file) {
        final SharedDataModel sharedDataModel = JsonMethods.loadFromFileJSON(
            SharedDataModel.class,
            file,
            SaveType.SESSION
        );
        return toScheduledStartDay(sharedDataModel.completedStartDay());
    }

    public Calendar getCompletedStartDay() {
        return CalendarMethods.copy(sharedDataModel.completedStartDay());
    }

    public void setCompletedStartDay(@Nonnull final Calendar completedStartDay) {
        sharedDataModel = ImmutableSharedDataModel.copyOf(sharedDataModel)
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
}
