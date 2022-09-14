package com.github.scottswolfe.kathyscleaning.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class WorkerComboBoxGridPanel extends ComboBoxGridPanel {

    public static WorkerComboBoxGridPanel from(
        final List<String> currentWorkers,
        final List<String> availableWorkers,
        int rowCount,
        int columnCount,
        final Color backgroundColor
    ) {
        return new WorkerComboBoxGridPanel(currentWorkers, availableWorkers, rowCount, columnCount, backgroundColor);
    }

    private WorkerComboBoxGridPanel(
        final List<String> currentWorkers,
        final List<String> availableWorkers,
        int rowCount,
        int columnCount,
        final Color backgroundColor
    ) {
        super(
            createWorkerNamesInGrid(currentWorkers, rowCount, columnCount),
            availableWorkers,
            backgroundColor
        );
    }

    private static List<List<String>> createWorkerNamesInGrid(
        final List<String> selectedWorkers,
        int rowCount,
        int columnCount
    ) {
        final List<List<String>> comboBoxSelections = new ArrayList<>(rowCount);
        int workerAddedCount = 0;
        for (int row = 0; row < rowCount; row++) {
            final List<String> comboBoxSelectionsInRow = new ArrayList<>(columnCount);
            comboBoxSelections.add(comboBoxSelectionsInRow);
            for (int column = 0; column < columnCount; column++) {
                String selectedWorkerName = "";
                if (workerAddedCount < selectedWorkers.size()) {
                    selectedWorkerName = selectedWorkers.get(workerAddedCount);
                    workerAddedCount++;
                }
                comboBoxSelectionsInRow.add(selectedWorkerName);
            }
        }
        return comboBoxSelections;
    }

    public List<List<String>> getSelectedWorkers() {
        final List<List<String>> comboBoxSelections = new ArrayList<>(rowCount());
        for (int row = 0; row < rowCount(); row++) {
            final List<String> comboBoxSelectionsInRow = new ArrayList<>(columnCount());
            comboBoxSelections.add(comboBoxSelectionsInRow);
            for (int column = 0; column < columnCount(); column++) {
                comboBoxSelectionsInRow.add(String.valueOf(getComponent(row, column).getSelectedItem()));
            }
        }
        return comboBoxSelections;
    }
}
