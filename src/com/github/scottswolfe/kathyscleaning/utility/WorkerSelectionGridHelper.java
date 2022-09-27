package com.github.scottswolfe.kathyscleaning.utility;

import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class WorkerSelectionGridHelper {

    private static final WorkerSelectionGridHelper workerSelectionGridHelperInstance = new WorkerSelectionGridHelper();

    public static WorkerSelectionGridHelper getInstance() {
        return workerSelectionGridHelperInstance;
    }

    private WorkerSelectionGridHelper() {}

    public void updateWorkersAndKeepSelections(
        @Nonnull final List<List<Pair<String, Boolean>>> workerSelectionGrid,
        @Nonnull final List<String> workerNames
    ) {
        final List<String> existingSelectedWorkers = getSelectedWorkers(workerSelectionGrid);
        int workerCount = 0;
        for (int row = 0; row < workerSelectionGrid.size(); row++) {
            for (int column = 0; column < workerSelectionGrid.get(row).size(); column++) {
                final String workerName = workerCount < workerNames.size() ? workerNames.get(workerCount) : "";
                final boolean isSelected = existingSelectedWorkers.contains(workerName);
                workerSelectionGrid.get(row).set(column, Pair.of(workerName, isSelected));
                workerCount++;
            }
        }
    }

    public List<String> getSelectedWorkers(
        @Nonnull final List<List<Pair<String, Boolean>>> workerSelectionGrid
    ) {
        final List<String> selectedWorkers = new ArrayList<>();
        for (int row = 0; row < workerSelectionGrid.size(); row++) {
            for (int column = 0; column < workerSelectionGrid.get(row).size(); column++) {
                if (workerSelectionGrid.get(row).get(column).getRight()) {
                    selectedWorkers.add(workerSelectionGrid.get(row).get(column).getLeft());
                }
            }
        }
        return selectedWorkers;
    }
}
