package com.github.scottswolfe.kathyscleaning.general.view;

import com.github.scottswolfe.kathyscleaning.component.EditWorkersPanel;
import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;

import javax.swing.*;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *  This is used to launch a panel used to update the names in a list of workers.
 */
public class EditWorkersPanelLauncher {

    private JFrame createdFrame = null;

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
        final FrameCloseListener frameCloseListener
    ) {
        launchPanel(
            currentWorkerNames,
            availableWorkerNames,
            currentWorkerNames.size(),
            1,
            allowRepeatSelections,
            onCancel,
            onSubmit,
            frameCloseListener
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
        final FrameCloseListener frameCloseListener
    ) {
        final JPanel editWorkersPanel = EditWorkersPanel.from(
            currentWorkerNames,
            availableWorkerNames,
            rowCount,
            columnCount,
            () -> onCancelInternal(onCancel),
            (updatedWorkerNames) -> onSubmitInternal(onSubmit, updatedWorkerNames, allowRepeatSelections)
        );

        TemporaryPanelLauncher.launchPanel(
            editWorkersPanel,
            (createdFrame) -> this.createdFrame = createdFrame,
            frameCloseListener
        );
    }

    private void onCancelInternal(final Runnable onCancel) {
        onCancel.run();
        getCreatedFrame().setVisible(false);
        getCreatedFrame().dispose();
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
                return;
            }
        }

        onSubmit.accept(updatedWorkerNames);

        getCreatedFrame().setVisible(false);
        getCreatedFrame().dispose();
    }

    private JFrame getCreatedFrame() {
        if (createdFrame == null) {
            throw new NullPointerException("createdFrame must be set before it is accessed.");
        }
        return createdFrame;
    }
}
