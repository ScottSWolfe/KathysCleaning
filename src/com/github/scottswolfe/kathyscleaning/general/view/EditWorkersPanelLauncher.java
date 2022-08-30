package com.github.scottswolfe.kathyscleaning.general.view;

import com.github.scottswolfe.kathyscleaning.component.WorkerComboBoxGridPanel;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;

import java.awt.event.WindowListener;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *  This is used to launch a panel used to update the names in a list of workers.
 */
public class EditWorkersPanelLauncher {

    private final PopUpFormLauncher popUpFormLauncher = PopUpFormLauncher.from();

    private WorkerComboBoxGridPanel workerComboBoxesPanel;

    public static EditWorkersPanelLauncher from() {
        return new EditWorkersPanelLauncher();
    }

    private EditWorkersPanelLauncher() {}

    public void launchPanel(
        final List<String> currentWorkerNames,
        final List<String> availableWorkerNames,
        final boolean allowRepeatSelections,
        final Runnable onCancel,
        final Consumer<List<List<String>>> onSubmit,
        final WindowListener popUpWindowListener
    ) {
        launchPanel(
            currentWorkerNames,
            availableWorkerNames,
            currentWorkerNames.size(),
            1,
            allowRepeatSelections,
            onCancel,
            onSubmit,
            popUpWindowListener
        );
    }

    public void launchPanel(
        final List<String> currentWorkerNames,
        final List<String> availableWorkerNames,
        final int rowCount,
        final int columnCount,
        final boolean allowRepeatSelections,
        final Runnable onCancel,
        final Consumer<List<List<String>>> onSubmit,
        final WindowListener popUpWindowListener
    ) {
        workerComboBoxesPanel = WorkerComboBoxGridPanel.from(
            currentWorkerNames,
            availableWorkerNames,
            rowCount,
            columnCount,
            Settings.BACKGROUND_COLOR
        );

        popUpFormLauncher.launchPanel(
            workerComboBoxesPanel,
            onCancel,
            () -> onSubmitInternal(onSubmit, workerComboBoxesPanel.getSelectedWorkers(), allowRepeatSelections),
            popUpWindowListener
        );
    }

    private void onSubmitInternal(
        final Consumer<List<List<String>>> onSubmit,
        final List<List<String>> updatedWorkerNames,
        final boolean allowRepeatSelections
    ) {
        if (!allowRepeatSelections) {
            final List<String> flatListUpdatedWorkerNames = updatedWorkerNames.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

            if (StaticMethods.isRepeatWorker(flatListUpdatedWorkerNames)) {
                StaticMethods.shareRepeatWorker();
                throw new PopUpFormLauncher.ButtonListenerException();
            }
        }

        onSubmit.accept(updatedWorkerNames);
    }
}
