package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.general.controller.TimeDocumentFilter;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeKeyListener;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import java.awt.Component;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TimeRangePanel extends JPanel implements FocusableCollection {

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

        connectFocusableComponents();
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

    public String getBeginTimeText() {
        return timeBeginTextField.getText();
    }

    public void setBeginTimeText(final String beginTime) {
        timeBeginTextField.setText(beginTime);
    }

    public String getEndTimeText() {
        return timeEndTextField.getText();
    }

    public void setEndTimeText(final String endTime) {
        timeEndTextField.setText(endTime);
    }

    @Override
    public List<List<? extends Component>> getComponentsAsGrid() {
        return Collections.singletonList(Arrays.asList(timeBeginTextField, timeEndTextField));
    }
}
