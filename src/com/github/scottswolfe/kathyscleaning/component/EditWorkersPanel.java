package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.util.List;
import java.util.function.Consumer;

public class EditWorkersPanel extends JPanel {

    public static EditWorkersPanel from(
        final List<String> currentWorkerNames,
        final List<String> availableWorkerNames,
        final int rowCount,
        final int columnCount,
        final Runnable onCancel,
        final Consumer<List<List<String>>> onSubmit
    ) {
        return new EditWorkersPanel(
            currentWorkerNames,
            availableWorkerNames,
            rowCount,
            columnCount,
            onCancel,
            onSubmit
        );
    }

    private EditWorkersPanel(
        final List<String> currentWorkerNames,
        final List<String> availableWorkerNames,
        final int rowCount,
        final int columnCount,
        final Runnable onCancel,
        final Consumer<List<List<String>>> onSubmit
    ) {
        final WorkerComboBoxGridPanel workerComboBoxesPanel = WorkerComboBoxGridPanel.from(
            currentWorkerNames, availableWorkerNames, rowCount, columnCount, Settings.BACKGROUND_COLOR
        );
        final JButton cancelButton = Button.from("Cancel", onCancel);
        final JButton submitButton = Button.from("Submit", () -> onSubmit.accept(workerComboBoxesPanel.getSelectedWorkers()));

        setLayout(new MigLayout());
        setBackground(Settings.BACKGROUND_COLOR);

        add(workerComboBoxesPanel, "wrap");
        add(cancelButton, "split 2");
        add(submitButton);

        // todo: use focus connector
    }
}
