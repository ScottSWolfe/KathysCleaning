package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.annotation.Nonnull;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RowLabelPanel extends JPanel {

    private static final String DEFAULT_ROW_LABEL_CONSTRAINTS = "center";

    private final JLabel rowLabel;

    public static RowLabelPanel from(final String label) {
        return new RowLabelPanel(label, DEFAULT_ROW_LABEL_CONSTRAINTS);
    }

    public static RowLabelPanel from(@Nonnull final String label, @Nonnull final String rowLabelConstraints) {
        return new RowLabelPanel(label, rowLabelConstraints);
    }

    private RowLabelPanel(@Nonnull final String label, @Nonnull final String rowLabelConstraints) {
        rowLabel = new JLabel();
        rowLabel.setText(label);
        rowLabel.setFont(rowLabel.getFont().deriveFont(Settings.HEADER_FONT_SIZE));

        setLayout(new MigLayout("fill"));
        setBackground(Settings.BACKGROUND_COLOR);
        add(rowLabel, rowLabelConstraints);
    }

    public String getLabelText() {
        return rowLabel.getText();
    }
}
