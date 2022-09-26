package com.github.scottswolfe.kathyscleaning.covenant.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantEntry;
import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.covenant.view.CovenantPanel;
import com.github.scottswolfe.kathyscleaning.enums.DayOfWeek;
import com.github.scottswolfe.kathyscleaning.enums.SaveType;
import com.github.scottswolfe.kathyscleaning.general.controller.FormController;
import com.github.scottswolfe.kathyscleaning.general.model.WorkTime;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;

public class CovenantControllerHelper implements ControllerHelper<CovenantPanel, CovenantModel> {

    @Override
    public CovenantModel initializeModel() {
        return CovenantModel.from();
    }

    @Override
    public CovenantPanel initializeView(
        FormController<CovenantPanel, CovenantModel> controller,
        MainFrame<CovenantPanel, CovenantModel> parentFrame
    ) {
        final CovenantListeners covenantListeners = new CovenantListeners();
        final CovenantPanel covenantPanel = new CovenantPanel(covenantListeners);
        covenantPanel.setFrame(parentFrame);
        covenantListeners.setCovPanel(covenantPanel);
        return covenantPanel;
    }

    @Override
    public CovenantModel readViewIntoModel(CovenantPanel view) {

        final List<CovenantEntry> entries = new ArrayList<>();
        for (int i = 0; i < CovenantPanel.ROWS; i++) {
            final List<WorkTime> workTimes = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                workTimes.add(
                    WorkTime.from(
                        DayOfWeek.fromIndex(j),
                        view.getBeginTimeTextfield()[i][j].getText(),
                        view.getEndTimeTextfield()[i][j].getText()
                    )
                );
            }
            entries.add(CovenantEntry.from(
                view.getNameLabels()[i].getText(),
                workTimes
            ));
        }

        final List<Double> amountsEarned = new ArrayList<>();
        for (int i = 0; i < CovenantPanel.COLS; i++) {
            String amount = view.getEarnedTextfields()[i].getText();
            if (!amount.isEmpty()) {
                amountsEarned.add(Double.parseDouble(amount));
            } else {
                amountsEarned.add(0.0);
            }
        }

        return CovenantModel.from(entries, amountsEarned);
    }

    @Override
    public void writeModelToView(CovenantModel model, CovenantPanel view) {

        final List<CovenantEntry> entries = model.getEntries();

        for (int row = 0; row < CovenantPanel.ROWS; row++) {

            final String workerName;
            final List<WorkTime> workTimes;
            if (row < entries.size()) {
                workerName = entries.get(row).getWorker();
                workTimes = entries.get(row).getWorkTimes();
            } else {
                workerName = "";
                workTimes = new ArrayList<>();
            }

            view.getNameLabels()[row].setText(workerName);
            for (int count = 0; count < workTimes.size(); count++) {
                view.getBeginTimeTextfield()[row][count].setText(workTimes.get(count).getBeginTime());
                view.getEndTimeTextfield()[row][count].setText(workTimes.get(count).getEndTime());
            }
        }

        for (int count = 0; count < model.getAmountsEarned().size(); count++) {
            final double amount = model.getAmountsEarned().get(count);
            if (amount != 0) {
                view.getEarnedTextfields()[count].setText(String.valueOf(amount));
            } else {
                view.getEarnedTextfields()[count].setText("");
            }
        }
    }

    @Override
    public void saveModelToFile(CovenantModel model, File file) {
        JsonMethods.saveToFileJSON(model, CovenantModel.class, file, SaveType.COVENANT);
    }

    @Override
    public CovenantModel loadFromFile(File file) {
        return JsonMethods.loadFromFileJSON(CovenantModel.class, file, SaveType.COVENANT);
    }

    @Override
    public void updateWorkersOnModel(final CovenantModel covenantModel, final List<List<String>> workerNames) {
        covenantModel.setWorkers(workerNames.stream().flatMap(Collection::stream).collect(Collectors.toList()));
    }
}
