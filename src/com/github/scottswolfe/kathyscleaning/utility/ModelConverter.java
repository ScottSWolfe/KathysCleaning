package com.github.scottswolfe.kathyscleaning.utility;

import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.completed.model.HouseData;
import com.github.scottswolfe.kathyscleaning.enums.DayOfWeek;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCDay;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCModel;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_DayData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_HouseData;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ModelConverter {

    public static ModelConverter from() {
        return new ModelConverter();
    }

    private ModelConverter() {}

    public CompletedModel toCompleted(@Nonnull final NW_Data scheduledModel) {

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

    public LBCModel toLBC(
        @Nonnull final NW_Data scheduledModel,
        @Nonnull final LBCModel existingLBCModel
    ) {
        final LBCModel newLBCModel = LBCModel.copyOf(existingLBCModel);

        for (int index = 0; index < newLBCModel.getLbcDays().values().size(); index++) {

            final LBCDay lbcDay = existingLBCModel.getLbcDays().get(DayOfWeek.fromIndex(index));

            final LBCDay newLBCday = LBCDay.from(
                DayOfWeek.fromIndex(index),
                lbcDay.getAmountEarned(),
                "",
                "",
                lbcDay.getWorkerSelectionGrid(),
                new ExceptionData()
            );

            if (index < scheduledModel.getDay().length) {
                // Monday to Friday Case

                final List<List<Pair<String, Boolean>>> workerSelectionGrid =
                    scheduledModel.getDay()[index].getLbcData().getWorkerSelectionGrid();

                final List<String> selectedWorkers = workerSelectionGrid.stream()
                    .flatMap(Collection::stream)
                    .filter(Pair::getRight)
                    .map(Pair::getLeft)
                    .collect(Collectors.toList());

                newLBCday.setSelectedWorkers(selectedWorkers);
            } else {
                // Saturday Case

                newLBCday.setSelectedWorkers(Collections.emptyList());
            }

            newLBCModel.getLbcDays().put(DayOfWeek.fromIndex(index), newLBCday);
        }

        return newLBCModel;
    }
}
