package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import javax.swing.JLabel;

public class KcLabel extends JLabel {

    public static KcLabel from(final String label) {
        return new KcLabel(label);
    }

    private KcLabel(final String label) {
        super();
        setText(label);
        setFont(getFont().deriveFont(Settings.FONT_SIZE));
        setBackground(Settings.BACKGROUND_COLOR);
    }
}
