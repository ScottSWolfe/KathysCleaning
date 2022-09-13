package com.github.scottswolfe.kathyscleaning.scheduled.view;

import com.github.scottswolfe.kathyscleaning.component.ChangeDayPanel;
import com.github.scottswolfe.kathyscleaning.component.DatePanel;
import com.github.scottswolfe.kathyscleaning.component.RowLabelPanel;
import com.github.scottswolfe.kathyscleaning.component.SubmitFormPanel;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ScheduledHeaderPanel extends JPanel implements FocusableCollection {

    private final DatePanel datePanel;
    private final ChangeDayPanel changeDayPanel;
    private final SubmitFormPanel submitFormPanel;

    public static ScheduledHeaderPanel from(
        final Calendar calendar,
        final ActionListener onPreviousButtonPress,
        final ActionListener onNextButtonPress,
        final ActionListener onSubmitFormButtonPress
    ) {
        return new ScheduledHeaderPanel(
            calendar,
            onPreviousButtonPress,
            onNextButtonPress,
            onSubmitFormButtonPress
        );
    }

    private ScheduledHeaderPanel(
        final Calendar calendar,
        final ActionListener onPreviousButtonPress,
        final ActionListener onNextButtonPress,
        final ActionListener onSubmitFormButtonPress
    ) {
        super();

        final RowLabelPanel nextWeekLabelPanel = RowLabelPanel.from("Next Week's Schedule");
        datePanel = DatePanel.from(calendar);
        changeDayPanel = ChangeDayPanel.from(onPreviousButtonPress, onNextButtonPress);
        submitFormPanel = SubmitFormPanel.from("Create Excel Doc", onSubmitFormButtonPress);

        setLayout(new MigLayout("fill, insets 0"));
        setBackground(Settings.HEADER_BACKGROUND);
        setBorder(BorderFactory.createMatteBorder(1, 1, 2, 1, Color.BLACK));
        add(nextWeekLabelPanel);
        add(new JSeparator(SwingConstants.VERTICAL), "growy");
        add(datePanel, "width 180");
        add(changeDayPanel);
        add(new JSeparator(SwingConstants.VERTICAL), "growy");
        add(submitFormPanel);

        connectFocusableComponents();
    }

    public void setDate(final Calendar calendar) {
        datePanel.setDate(calendar);
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Collections.singletonList(
            Arrays.asList(changeDayPanel, submitFormPanel)
        );
    }
}
