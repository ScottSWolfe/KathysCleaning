package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChangeDayPanel extends JPanel implements FocusableCollection {

    private final JButton previousDayButton;
    private final JButton nextDayButton;

    public static ChangeDayPanel from(
        final ActionListener previousDayButtonListener,
        final ActionListener nextDayButtonListener
    ) {
        return new ChangeDayPanel(
            previousDayButtonListener,
            nextDayButtonListener
        );
    }

    private ChangeDayPanel(
        final ActionListener previousDayButtonListener,
        final ActionListener nextDayButtonListener
    ) {
        super();

        previousDayButton = createButton("Previous", previousDayButtonListener);
        nextDayButton = createButton("  Next  ", nextDayButtonListener);

        setLayout(new MigLayout("insets 2","[][]","[grow]"));
        setBackground(Settings.HEADER_BACKGROUND);
        add(previousDayButton);
        add(nextDayButton);

        connectFocusableComponents();
    }

    private KcButton createButton(final String label, final ActionListener buttonPressListener) {
        final KcButton button = new KcButton(label, buttonPressListener);
        button.setBackground(Settings.CHANGE_DAY_COLOR);
        button.setForeground(Settings.FOREGROUND_COLOR);
        button.setPreferredSize(new Dimension(100,40));
        return button;
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Collections.singletonList(Arrays.asList(previousDayButton, nextDayButton));
    }
}
