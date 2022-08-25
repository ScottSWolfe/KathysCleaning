package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import javax.swing.JButton;

public class Button extends JButton {

    public static Button from(
        final String label,
        final Runnable onPress
    ) {
        return new Button(label, onPress);
    }

    private Button(
        final String label,
        final Runnable onPress
    ) {
        super(label);
        setFont(getFont().deriveFont(Settings.FONT_SIZE));
        setBackground(Settings.MAIN_COLOR);
        setForeground(Settings.FOREGROUND_COLOR);
        addActionListener((event) -> onPress.run());
    }
}
