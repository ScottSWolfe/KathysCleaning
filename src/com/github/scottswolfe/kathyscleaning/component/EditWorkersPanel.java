package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.utility.FocusableConnector;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class EditWorkersPanel extends JPanel implements FocusableCollection {

    private final WorkerComboBoxGridPanel workerComboBoxesPanel;
    private final JButton cancelButton;
    private final JButton submitButton;

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
        workerComboBoxesPanel = WorkerComboBoxGridPanel.from(
            currentWorkerNames, availableWorkerNames, rowCount, columnCount, Settings.BACKGROUND_COLOR
        );
        cancelButton = Button.from("Cancel", onCancel);
        submitButton = Button.from("Submit", () -> onSubmit.accept(workerComboBoxesPanel.getSelectedWorkers()));

        setLayout(new MigLayout());
        setBackground(Settings.BACKGROUND_COLOR);

        add(workerComboBoxesPanel, "wrap");
        add(cancelButton, "gapleft push, split 2");
        add(submitButton);

        FocusableConnector.from().connect(this);
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Arrays.asList(
            Arrays.asList(GAP, workerComboBoxesPanel),
            Arrays.asList(cancelButton, submitButton)
        );
    }
}
