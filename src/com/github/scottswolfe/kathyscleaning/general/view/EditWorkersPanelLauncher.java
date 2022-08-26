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

    public static void launchPanel(
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

    public static void launchPanel(
        final List<String> currentWorkerNames,
        final List<String> availableWorkerNames,
        final int rowCount,
        final int columnCount,
        final boolean allowRepeatSelections,
        final Runnable onCancel,
        final Consumer<List<List<String>>> onSubmit,
        final FrameCloseListener frameCloseListener
    ) {
        final JFrame editWorkerFrame = createFrame(frameCloseListener);
        final JPanel editWorkersPanel = EditWorkersPanel.from(
            currentWorkerNames,
            availableWorkerNames,
            rowCount,
            columnCount,
            () -> onCancelInternal(editWorkerFrame, onCancel),
            (updatedWorkerNames) -> onSubmitInternal(
                editWorkerFrame, onSubmit, updatedWorkerNames, allowRepeatSelections
            )
        );

        editWorkerFrame.add(editWorkersPanel);
        editWorkerFrame.pack();
        StaticMethods.findSetLocation(editWorkerFrame);
        editWorkerFrame.setVisible(true);
    }

    private EditWorkersPanelLauncher() {}

    private static void onCancelInternal(final JFrame frame, final Runnable onCancel) {
        onCancel.run();
        frame.setVisible(false);
        frame.dispose();
    }

    private static void onSubmitInternal(
        final JFrame frame,
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

        frame.setVisible(false);
        frame.dispose();
    }

    private static JFrame createFrame(final FrameCloseListener frameCloseListener) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.addWindowListener(frameCloseListener);
        return frame;
    }
}
