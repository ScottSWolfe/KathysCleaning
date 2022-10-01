package com.github.scottswolfe.kathyscleaning.completed.view;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionEntry;
import com.github.scottswolfe.kathyscleaning.component.KcComboBox;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeDocumentFilter;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeKeyListener;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;

public class CompletedExceptionsPanel extends JPanel implements FocusableCollection {

    private static final int EXCEPTION_ROW_COUNT = 3;

    final List<ExceptionRow> exceptionRows;

    public static CompletedExceptionsPanel from(
        final List<String> workerNames,
        final Supplier<ExceptionData> exceptionDataSupplier
    ) {
        return new CompletedExceptionsPanel(
            workerNames,
            exceptionDataSupplier
        );
    }

    private CompletedExceptionsPanel(
        final List<String> workerNames,
        final Supplier<ExceptionData> exceptionDataSupplier
    ) {
        final JLabel nameLabel = new JLabel("Name");
        nameLabel.setFont(nameLabel.getFont().deriveFont(Settings.FONT_SIZE));

        final JLabel timeLabel = new JLabel("Time");
        timeLabel.setFont(timeLabel.getFont().deriveFont(Settings.FONT_SIZE));

        exceptionRows = createExceptionRows(workerNames, exceptionDataSupplier);

        final StringBuilder migLayoutConstraintsBuilder = new StringBuilder();
        for (int row = 0; row < EXCEPTION_ROW_COUNT; row++) {
            migLayoutConstraintsBuilder.append("[grow]");
        }
        setLayout(new MigLayout(
            "insets 5",
            migLayoutConstraintsBuilder.toString(),
            "[grow]3[grow]6[grow]6[grow]"
        ));
        setBackground(Settings.BACKGROUND_COLOR);

        add(nameLabel, "ax 10%");
        add(timeLabel, "span 2 1, wrap, ax center, gapx 15");
        for (ExceptionRow exceptionRow : exceptionRows) {
            add(exceptionRow.nameComboBox, "ay center, growy");
            add(exceptionRow.beginTime, "ay center, growy, gapx 15");
            add(exceptionRow.endTime, "wrap, ay center, growy");
        }

        connectFocusableComponents();
    }

    private List<ExceptionRow> createExceptionRows(
        final List<String> workerNames,
        final Supplier<ExceptionData> exceptionDataSupplier
    ) {
        final List<ExceptionRow> exceptionRows = new ArrayList<>(EXCEPTION_ROW_COUNT);

        for (int row = 0; row < EXCEPTION_ROW_COUNT; row++) {
            exceptionRows.add(ExceptionRow.from(
                KcComboBox.from(workerNames, "", false,10),
                createTimeTextField(),
                createTimeTextField()
            ));
        }

        final ExceptionData exceptionData = exceptionDataSupplier.get();
        if (exceptionData.isException()) {
            for (int row = 0; row < EXCEPTION_ROW_COUNT; row++) {
                final ExceptionEntry entry = exceptionData.getEntry(row);
                exceptionRows.get(row).nameComboBox.setSelectedItem(entry.getWorker_name());
                exceptionRows.get(row).beginTime.setText(entry.getTime_begin());
                exceptionRows.get(row).endTime.setText(entry.getTime_end());
            }
        }

        return exceptionRows;
    }

    private JTextField createTimeTextField() {
        final JTextField timeTextField = new JTextField(5);
        timeTextField.setFont(timeTextField.getFont().deriveFont(Settings.FONT_SIZE));

        final AbstractDocument timeTextFieldDocument = (AbstractDocument) timeTextField.getDocument();
        final TimeDocumentFilter timeDocumentFilter = new TimeDocumentFilter(timeTextField);
        timeTextFieldDocument.setDocumentFilter(timeDocumentFilter);
        timeTextField.addKeyListener(new TimeKeyListener(timeDocumentFilter));

        return timeTextField;
    }

    public ExceptionData getExceptionData() {
        final ExceptionData exceptionData = new ExceptionData();
        final List<ExceptionEntry> entries = new ArrayList<>();
        for (ExceptionRow exceptionRow : exceptionRows) {
            final ExceptionEntry entry = new ExceptionEntry(
                String.valueOf(exceptionRow.nameComboBox.getSelectedItem()),
                exceptionRow.beginTime.getText(),
                exceptionRow.endTime.getText()
            );
            entries.add(entry);
        }
        exceptionData.setEntries(entries);
        return exceptionData;
    }

    @Override
    public List<List<? extends Component>> getComponentsAsGrid() {
        return exceptionRows.stream()
            .map(exceptionRow -> Arrays.asList(exceptionRow.nameComboBox, exceptionRow.beginTime, exceptionRow.endTime))
            .collect(Collectors.toList());
    }

    private static class ExceptionRow {
        JComboBox<String> nameComboBox;
        JTextField beginTime;
        JTextField endTime;

        private static ExceptionRow from(
            final JComboBox<String> nameComboBox,
            final JTextField beginTime,
            final JTextField endTime
        ) {
            return new ExceptionRow(nameComboBox, beginTime, endTime);
        }

        private ExceptionRow(
            final JComboBox<String> nameComboBox,
            final JTextField beginTime,
            final JTextField endTime
        ) {
            this.nameComboBox = nameComboBox;
            this.beginTime = beginTime;
            this.endTime = endTime;
        }
    }
}
