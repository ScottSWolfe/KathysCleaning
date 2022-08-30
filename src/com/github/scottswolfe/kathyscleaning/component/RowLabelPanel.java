package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class RowLabelPanel extends JPanel {

    public static RowLabelPanel from(final String label) {
        return new RowLabelPanel(label);
    }

    private RowLabelPanel(final String label) {
        final JLabel dayLabel = new JLabel();
        dayLabel.setText(label);
        dayLabel.setFont(dayLabel.getFont().deriveFont(Settings.HEADER_FONT_SIZE));
        add(dayLabel);
    }
}