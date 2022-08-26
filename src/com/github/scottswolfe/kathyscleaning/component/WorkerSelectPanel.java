package com.github.scottswolfe.kathyscleaning.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JCheckBox;

import com.github.scottswolfe.kathyscleaning.general.model.Worker;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Allows the user to select a set of workers.
 */
public class WorkerSelectPanel extends CheckBoxGridPanel {

    public static final int DEFAULT_ROW_COUNT = 2;
    public static final int DEFAULT_COLUMN_COUNT = 5;

    public static WorkerSelectPanel from(
        final WorkerList workers,
        final Color backgroundColor
    ) {
        return from(
            workers,
            DEFAULT_ROW_COUNT,
            DEFAULT_COLUMN_COUNT,
            backgroundColor
        );
    }

    public static WorkerSelectPanel from(
        final WorkerList workers,
        int rowCount,
        int columnCount,
        final Color backgroundColor
    ) {
        return new WorkerSelectPanel(
            workers,
            rowCount,
            columnCount,
            backgroundColor
        );
    }

    private WorkerSelectPanel(
        final WorkerList workers,
        int rowCount,
        int columnCount,
        final Color backgroundColor
    ) {
        super(
            createWorkerCheckBoxLabelsAndStatuses(workers.getWorkers(), rowCount, columnCount),
            backgroundColor
        );
    }

    private static List<List<Pair<String, Boolean>>> createWorkerCheckBoxLabelsAndStatuses(
        List<Worker> workers,
        int rowCount,
        int columnCount
    ) {
        final List<List<Pair<String, Boolean>>> checkBoxLabelsAndStatuses = new ArrayList<>(rowCount);
        int workerAddedCount = 0;
        for (int row = 0; row < rowCount; row++) {
            final List<Pair<String, Boolean>> checkBoxLabelsAndStatusesInRow = new ArrayList<>(columnCount);
            checkBoxLabelsAndStatuses.add(checkBoxLabelsAndStatusesInRow);
            for (int column = 0; column < columnCount; column++) {
                String label = "";
                boolean isSelected = false;
                if (workerAddedCount < workers.size()) {
                    label = workers.get(workerAddedCount).getName();
                    isSelected = workers.get(workerAddedCount).isSelected();
                    workerAddedCount++;
                }
                checkBoxLabelsAndStatusesInRow.add(new ImmutablePair<>(label, isSelected));
            }
        }
        return checkBoxLabelsAndStatuses;
    }

    public void setWorkers(final List<List<Pair<String, Boolean>>> workerSelectionGrid) {
        if (workerSelectionGrid.size() != rowCount()) {
            throw new IllegalArgumentException("Given worker selection grid has the wrong number of rows");
        }
        if (workerSelectionGrid.get(0).size() != columnCount()) {
            throw new IllegalArgumentException("Given worker selection grid has the wrong number of columns");
        }

        for (int row = 0; row < rowCount(); row++) {
            for (int column = 0; column < columnCount(); column++) {
                setCheckBoxLabel(row, column, workerSelectionGrid.get(row).get(column).getLeft());
                setCheckBoxSelected(row, column, workerSelectionGrid.get(row).get(column).getRight());
            }
        }
    }

    public void setWorkers(final WorkerList workers) {
        int workerAddedCount = 0;
        final Iterator<JCheckBox> iterator = iterator();
        while (iterator.hasNext()) {
            final JCheckBox checkBox = iterator.next();
            String label = "";
            boolean isSelected = false;
            if (workerAddedCount < workers.size()) {
                label = workers.get(workerAddedCount).getName();
                isSelected = workers.get(workerAddedCount).isSelected();
                workerAddedCount++;
            }
            checkBox.setText(label);
            checkBox.setSelected(isSelected);
        }
    }

    public WorkerList getWorkers() {
        final WorkerList workers = new WorkerList();
        final Iterator<JCheckBox> iterator = iterator();
        while (iterator.hasNext()) {
            final JCheckBox checkBox = iterator.next();
            final String name = checkBox.getText();
            if (name != null && !name.isEmpty()) {
                workers.add(new Worker(name, checkBox.isSelected()));
            }
        }
        return workers;
    }

    public void setSelected(List<String> workers) {
        uncheckAllBoxes();

        final Iterator<JCheckBox> iterator = iterator();
        while (iterator.hasNext()) {
            final JCheckBox checkBox = iterator.next();
            for (String worker : workers) {
                if (worker != null && !worker.isEmpty() && worker.equals(checkBox.getText())) {
                    checkBox.setSelected(true);
                    break;
                }
            }
        }
    }

    public List<String> getSelectedWorkerNames() {
        final List<String> selectedWorkers = new ArrayList<>();

        final Iterator<JCheckBox> iterator = iterator();
        while (iterator.hasNext()) {
            final JCheckBox checkBox = iterator.next();
            if (checkBox.getText() != null && !checkBox.getText().isEmpty() && checkBox.isSelected()) {
                selectedWorkers.add(checkBox.getText());
            }
        }

        return selectedWorkers;
    }

    public boolean isSelected(final String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        final Iterator<JCheckBox> iterator = iterator();
        while (iterator.hasNext()) {
            final JCheckBox checkBox = iterator.next();
            if (name.equals(checkBox.getText()) && checkBox.isSelected()) {
                return true;
            }
        }
        return false;
    }

    public String getNameAt(int row, int column) {
        return getCheckBoxLabel(row, column);
    }
}
