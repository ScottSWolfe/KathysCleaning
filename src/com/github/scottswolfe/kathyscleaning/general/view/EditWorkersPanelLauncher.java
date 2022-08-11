package com.github.scottswolfe.kathyscleaning.general.view;

import com.github.scottswolfe.kathyscleaning.general.controller.FlexibleFocusListener;
import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javax.swing.JComponent.UNDEFINED_CONDITION;

/**
 *  This is used to launch a panel used to update the names in a list of workers.
 */
public class EditWorkersPanelLauncher {

    // Clients should use launchPanel to create a panel to edit worker names.
    private EditWorkersPanelLauncher() {}

    public static void launchPanel(
        List<String> currentWorkerNames,
        List<String> availableWorkerNames,
        Consumer<List<String>> onSubmit,
        Runnable onCancel,
        FrameCloseListener frameCloseListener
    ) {
        final JFrame editWorkerFrame = createFrame(frameCloseListener);

        final JPanel editWorkersPanel = createEditWorkersPanel(
            currentWorkerNames,
            availableWorkerNames,
            () -> onCancelInternal(editWorkerFrame, onCancel),
            (updatedWorkerNames) -> onSubmitInternal(editWorkerFrame, onSubmit, updatedWorkerNames)
        );

        editWorkerFrame.add(editWorkersPanel);
        editWorkerFrame.pack();
        StaticMethods.findSetLocation(editWorkerFrame);
        editWorkerFrame.setVisible(true);
    }

    private static void onCancelInternal(JFrame frame, Runnable onCancel) {
        onCancel.run();
        frame.setVisible(false);
        frame.dispose();
    }

    private static void onSubmitInternal(JFrame frame, Consumer<List<String>> onSubmit, List<String> updatedWorkerNames) {

        // if contains repeat selections, notify user and end method
        if (StaticMethods.isRepeatWorker(updatedWorkerNames)) {
            StaticMethods.shareRepeatWorker();
            return;
        }

        onSubmit.accept(updatedWorkerNames);

        // close EditWorkersPanel
        frame.setVisible(false);
        frame.dispose();
    }

    private static JPanel createEditWorkersPanel(
        List<String> currentWorkerNames,
        List<String> availableWorkerNames,
        Runnable onCancel,
        Consumer<List<String>> onSubmit
    ) {
        final List<JComboBox<String>> workerComboBoxes = createWorkerComboBoxes(
            currentWorkerNames, availableWorkerNames
        );
        final JButton cancelButton = createCancelButton(onCancel);
        final JButton submitButton = createSubmitButton(onSubmit, workerComboBoxes);

        addFlexibleFocusListeners(workerComboBoxes);

        final JPanel editWorkersPanel = new JPanel();

        editWorkersPanel.setLayout(new MigLayout());
        editWorkersPanel.setBackground(Settings.BACKGROUND_COLOR);

        for (JComboBox<String> workerComboBox : workerComboBoxes) {
            editWorkersPanel.add(workerComboBox, "wrap 10");
        }
        editWorkersPanel.add(cancelButton, "split 2");
        editWorkersPanel.add(submitButton, "");

        return editWorkersPanel;
    }

    private static List<JComboBox<String>> createWorkerComboBoxes(
        List<String> currentWorkerNames,
        List<String> availableWorkerNames
    ) {
        final List<JComboBox<String>> workerComboBoxes = new ArrayList<>();

        for (String currentWorkerName : currentWorkerNames) {

            JComboBox<String> workerComboBox = new JComboBox<>();
            workerComboBox.setEditable(true);
            workerComboBox.setSize(10, UNDEFINED_CONDITION);
            workerComboBox.setFont(workerComboBox.getFont().deriveFont(Settings.FONT_SIZE));

            // Add combo box options
            Set<String> availableNamesAdded = new HashSet<>();

            String emptyChoice = "";
            workerComboBox.addItem(emptyChoice);
            availableNamesAdded.add(emptyChoice);

            List<String> allWorkerNames = Stream.concat(
                availableWorkerNames.stream(),
                currentWorkerNames.stream()
            ).collect(Collectors.toList());

            for (String workerName : allWorkerNames) {
                if (!availableNamesAdded.contains(workerName)) {
                    workerComboBox.addItem(workerName);
                    availableNamesAdded.add(workerName);
                }
            }

            workerComboBox.setSelectedItem(currentWorkerName);

            workerComboBoxes.add(workerComboBox);
        }

        return workerComboBoxes;
    }

    private static JButton createCancelButton(Runnable onCancel) {
        final JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont( cancelButton.getFont().deriveFont( Settings.FONT_SIZE ) );
        cancelButton.setBackground(Settings.MAIN_COLOR);
        cancelButton.setForeground( Settings.FOREGROUND_COLOR );
        cancelButton.addActionListener((event) -> onCancel.run());
        return cancelButton;
    }

    private static JButton createSubmitButton(Consumer<List<String>> onSubmit, List<JComboBox<String>> workerComboBoxes) {
        final JButton submitButton = new JButton("Submit");
        submitButton.setFont( submitButton.getFont().deriveFont( Settings.FONT_SIZE ) );
        submitButton.setBackground(Settings.MAIN_COLOR);
        submitButton.setForeground( Settings.FOREGROUND_COLOR );
        submitButton.addActionListener((event) -> onSubmit.accept(getSelectedWorkers(workerComboBoxes)));
        return submitButton;
    }

    private static JFrame createFrame(FrameCloseListener frameCloseListener) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.addWindowListener(frameCloseListener);
        return frame;
    }

    private static void addFlexibleFocusListeners (List<JComboBox<String>> workerComboBoxes) {
        for (int i = 0; i < workerComboBoxes.size(); i++) {

            JComboBox<String> up_cb = null;
            JComboBox<String> down_cb = null;

            if (i > 0) {
                up_cb = workerComboBoxes.get(i - 1);
            }
            if (i < workerComboBoxes.size() - 1) {
                down_cb = workerComboBoxes.get(i + 1);
            }

            workerComboBoxes.get(i).getEditor().getEditorComponent().addFocusListener(
                new FlexibleFocusListener(
                    workerComboBoxes.get(i),
                    FlexibleFocusListener.COMBOBOX,
                    null,
                    null,
                    up_cb,
                    down_cb,
                    null
                )
            );
        }
    }

    private static List<String> getSelectedWorkers(List<JComboBox<String>> workerComboBoxes) {
        return workerComboBoxes.stream()
            .map(JComboBox::getSelectedItem)
            .map(String::valueOf)
            .collect(Collectors.toList());
    }
}	
