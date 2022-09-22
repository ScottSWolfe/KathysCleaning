package com.github.scottswolfe.kathyscleaning.utility;

import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.completed.model.HouseData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_DayData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_HouseData;

import javax.annotation.Nonnull;

public class ScheduledToCompletedModelConverter {

    public static ScheduledToCompletedModelConverter from() {
        return new ScheduledToCompletedModelConverter();
    }

    private ScheduledToCompletedModelConverter() {}

    public CompletedModel convert(@Nonnull final NW_Data scheduledModel) {

        final CompletedModel completedModel = new CompletedModel();

        for (int day = 0; day < scheduledModel.getDay().length; day++) {

            final NW_DayData scheduledDayData = scheduledModel.getDay()[day];
            final DayData completedDayData = completedModel.getDay()[day];

            for (int house = 0; house < scheduledDayData.houseData.length; house++) {

                final NW_HouseData scheduledHouseData = scheduledDayData.houseData[house];

                final HouseData completedHouseData;
                if (house < completedModel.getDay()[day].houseData.length) {
                    completedHouseData = completedModel.getDay()[day].houseData[house];
                } else {
                    completedHouseData = new HouseData();
                    completedDayData.addHouseData(completedHouseData);
                }

                completedHouseData.setHouseName(scheduledHouseData.getHouseName());
                completedHouseData.setSelectedWorkers(scheduledHouseData.getWorkers().getSelectedWorkerNames());
            }
        }

        return completedModel;
    }
}
