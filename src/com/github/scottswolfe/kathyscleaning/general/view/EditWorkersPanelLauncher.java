package com.github.scottswolfe.kathyscleaning.general.view;

import com.github.scottswolfe.kathyscleaning.component.MenuEditWorkersPanel;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;

/**
 *  This is used to launch a panel used to update the names in a list of workers.
 */
public class EditWorkersPanelLauncher {

    public static EditWorkersPanelLauncher from() {
        return new EditWorkersPanelLauncher();
    }

    private EditWorkersPanelLauncher() {}

    public void launchPanel(
        final List<String> currentWorkerNames,
        final List<String> availableWorkerNames,
        final int rowCount,
        final int columnCount,
        final boolean allowRepeatSelections,
        final Runnable onCancel,
        final Consumer<List<String>> onSubmit
    ) {
        GenericPanelLauncher.from().launchPanel(
            () -> MenuEditWorkersPanel.from(currentWorkerNames, availableWorkerNames, rowCount, columnCount),
            onCancel,
            (updatedWorkerNames) -> onSubmitInternal(onSubmit, updatedWorkerNames, allowRepeatSelections)
        );
    }

    private void onSubmitInternal(
        @Nonnull final Consumer<List<String>> onSubmit,
        @Nonnull final List<String> updatedWorkerNames,
        final boolean allowRepeatSelections
    ) {
        if (!allowRepeatSelections) {
            if (StaticMethods.isRepeatWorker(updatedWorkerNames)) {
                StaticMethods.shareRepeatWorker();
                throw new PopUpFormLauncher.ButtonListenerException();
            }
        }
        onSubmit.accept(updatedWorkerNames);
    }
}
