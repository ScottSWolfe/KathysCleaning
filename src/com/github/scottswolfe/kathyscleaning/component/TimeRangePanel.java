package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.general.controller.TimeDocumentFilter;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeKeyListener;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

public class TimeRangePanel extends JPanel {

    private final JTextField timeBeginTextField;
    private final JTextField timeEndTextField;

    public static TimeRangePanel from() {
        return new TimeRangePanel();
    }

    private TimeRangePanel() {
        super();

        final JLabel timeLabel = new JLabel("Time");
        timeLabel.setFont(timeLabel.getFont().deriveFont(Settings.FONT_SIZE));

        timeBeginTextField = createTimeTextField();
        timeEndTextField = createTimeTextField();

        setLayout(new MigLayout("insets 0, ay 50%"));
        setBackground(Settings.BACKGROUND_COLOR);

        add(timeLabel, "cell 0 0 1 1, wrap, ax 50%");
        add(timeBeginTextField, "cell 0 1");
        add(timeEndTextField, "cell 0 1, gap 0");
    }

    private JTextField createTimeTextField() {
        final JTextField timeTextField = new JTextField(5);
        timeTextField.setFont(timeTextField.getFont().deriveFont(Settings.FONT_SIZE));

        final TimeDocumentFilter timeDocumentFilter = new TimeDocumentFilter(timeTextField);
        final AbstractDocument timeTextFieldDocument = (AbstractDocument) timeTextField.getDocument();
        timeTextFieldDocument.setDocumentFilter(timeDocumentFilter);
        timeTextField.addKeyListener(new TimeKeyListener(timeDocumentFilter));

        return timeTextField;
    }
}
