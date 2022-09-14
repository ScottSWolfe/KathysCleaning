package com.github.scottswolfe.kathyscleaning.scheduled.view;

import com.github.scottswolfe.kathyscleaning.component.KcComboBox;
import com.github.scottswolfe.kathyscleaning.component.KcLabel;
import com.github.scottswolfe.kathyscleaning.component.KcTextField;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeKeyListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.model.ScheduledLBCException;
import com.github.scottswolfe.kathyscleaning.scheduled.model.ScheduledLBCExceptions;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ScheduledLBCExceptionsPanel extends JPanel implements Supplier<ScheduledLBCExceptions> {

    private final List<ExceptionRow> exceptionRows;

    public static ScheduledLBCExceptionsPanel from(
        final ScheduledLBCExceptions scheduledLBCExceptions,
        final WorkerList workerList
    ) {
        return new ScheduledLBCExceptionsPanel(scheduledLBCExceptions, workerList);
    }

    private ScheduledLBCExceptionsPanel(
        final ScheduledLBCExceptions scheduledLBCExceptions,
        final WorkerList workerList
    ) {

        final KcLabel workerNameLabel = KcLabel.from("Name");
        final KcLabel meetTimeLabel = KcLabel.from("Time");
        final KcLabel noteLabel = KcLabel.from("Note");

        exceptionRows = new ArrayList<>();
        for (int row = 0; row < ScheduledLBCPanel.LBC_SCHEDULED_EXCEPTIONS_COUNT; row++) {
            if (row < scheduledLBCExceptions.size()) {
                exceptionRows.add(ExceptionRow.from(
                    workerList.getWorkerNames(),
                    scheduledLBCExceptions.get(row).getWorkerName(),
                    scheduledLBCExceptions.get(row).getMeetTime(),
                    scheduledLBCExceptions.get(row).getNote()
                ));
            } else {
                exceptionRows.add(ExceptionRow.from(workerList.getWorkerNames()));
            }
        }

        setLayout(new MigLayout());
        setBackground(Settings.BACKGROUND_COLOR);
        add(workerNameLabel);
        add(meetTimeLabel);
        add(noteLabel, "wrap");

        exceptionRows.forEach(exceptionRow -> {
            add(exceptionRow.workerNameComboBox);
            add(exceptionRow.meetTimeTextField);
            add(exceptionRow.noteTextField, "wrap");
        });
    }

    public ScheduledLBCExceptions getScheduledLBCExceptions() {
        return ScheduledLBCExceptions.from(
            exceptionRows.stream()
                .map(exceptionRow -> ScheduledLBCException.from(
                    String.valueOf(exceptionRow.workerNameComboBox.getSelectedItem()),
                    exceptionRow.meetTimeTextField.getText(),
                    exceptionRow.noteTextField.getText()
                ))
            .collect(Collectors.toList())
        );
    }

    @Override
    public ScheduledLBCExceptions get() {
        return getScheduledLBCExceptions();
    }

    private static class ExceptionRow {
        private final JComboBox<String> workerNameComboBox;
        private final JTextField meetTimeTextField;
        private final JTextField noteTextField;

        private static ExceptionRow from(
            final List<String> workerNames,
            final String selectedWorkerName,
            final String meetTime,
            final String note
        ) {
            return new ExceptionRow(workerNames, selectedWorkerName, meetTime, note);
        }

        private static ExceptionRow from(final List<String> workerNames) {
            return new ExceptionRow(workerNames, "", "", "");
        }

        private ExceptionRow(
            final List<String> workerNames,
            final String selectedWorkerName,
            final String meetTime,
            final String note
        ) {
            workerNameComboBox = KcComboBox.from(workerNames, selectedWorkerName, false);
            meetTimeTextField = KcTextField.from(meetTime, (TimeKeyListener::new));
            noteTextField = KcTextField.from(note, 15);
        }
    }
}
