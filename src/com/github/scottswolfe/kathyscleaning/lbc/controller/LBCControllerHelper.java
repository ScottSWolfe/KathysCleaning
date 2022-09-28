package com.github.scottswolfe.kathyscleaning.lbc.controller;

import com.github.scottswolfe.kathyscleaning.enums.DayOfWeek;
import com.github.scottswolfe.kathyscleaning.enums.SaveType;
import com.github.scottswolfe.kathyscleaning.general.controller.ApplicationCoordinator;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCDay;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCHeader;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCModel;
import com.github.scottswolfe.kathyscleaning.lbc.view.LBCPanel;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LBCControllerHelper implements ControllerHelper<LBCPanel, LBCModel> {

    @Override
    public LBCModel initializeModel() {
        return LBCModel.from();
    }

    @Override
    public LBCPanel initializeView() {
        return LBCPanel.from((event) -> ApplicationCoordinator.getInstance().launchNextForm());
    }

    @Override
    public LBCModel readViewIntoModel(final LBCPanel view) {

        final LBCHeader lbcHeader = LBCHeader.from(view.getHeaderWorkerSelectionGrid());

        final Map<DayOfWeek, LBCDay> lbcDays = view.getLBCDayPanels().stream()
            .map(lbcDayPanel -> LBCDay.from(
                lbcDayPanel.getDayOfWeek(),
                lbcDayPanel.getAmountEarned(),
                lbcDayPanel.getBeginTime(),
                lbcDayPanel.getEndTime(),
                lbcDayPanel.getWorkerSelectionGrid(),
                lbcDayPanel.getExceptionData()
            ))
            .collect(Collectors.toMap(LBCDay::getDayOfWeek, lbcDay -> lbcDay));

        return LBCModel.from(lbcHeader, lbcDays);
    }

    @Override
    public void writeModelToView(final LBCModel model, final LBCPanel view) {

        view.setHeaderWorkerSelectionGrid(model.getLbcHeader().getWorkerSelectionGrid());

        view.getLBCDayPanels().forEach(lbcDayPanel -> {

            final LBCDay lbcDay = model.getLbcDays().get(lbcDayPanel.getDayOfWeek());
            if (lbcDay == null) {
                throw new RuntimeException("LBCDay should not be null. Day of week: " + lbcDayPanel.getDayOfWeek());
            }

            lbcDayPanel.setAmountEarned(lbcDay.getAmountEarned());
            lbcDayPanel.setBeginTime((lbcDay.getBeginTime()));
            lbcDayPanel.setEndTime(lbcDay.getEndTime());
            lbcDayPanel.setWorkers(lbcDay.getWorkerSelectionGrid());
            lbcDayPanel.setExceptionData(lbcDay.getExceptionData());
        });
    }

    @Override
    public void saveModelToFile(LBCModel model, File file) {
        JsonMethods.saveToFileJSON(model, LBCModel.class, file, SaveType.LBC);
    }

    @Override
    public LBCModel loadFromFile(File file) {
        final LBCModel model = JsonMethods.loadFromFileJSON(LBCModel.class, file, SaveType.LBC);

        if (model.getLbcHeader() == null && model.getLbcDays() == null) {
            return LBCModel.from();
        }

        return model;
    }

    @Override
    public void updateWorkersOnModel(
        @Nonnull final LBCModel lbcModel,
        @Nonnull final List<String> workerNames
    ) {
        lbcModel.updateWorkersAndKeepSelections(workerNames);
    }
}
