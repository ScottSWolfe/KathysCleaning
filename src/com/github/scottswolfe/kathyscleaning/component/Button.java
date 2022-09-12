package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.general.model.ButtonColors;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import javax.swing.JButton;

public class Button extends JButton {

    public static Button from(
        final String label,
        final ButtonColors colors,
        final Runnable onPress
    ) {
        return new Button(label, colors, onPress);
    }

    private Button(
        final String label,
        final ButtonColors colors,
        final Runnable onPress
    ) {
        super(label);
        setFont(getFont().deriveFont(Settings.FONT_SIZE));
        setColors(colors);
        addActionListener((event) -> onPress.run());
    }

    public void setColors(final ButtonColors buttonColors) {
        setBackground(buttonColors.getButtonColor());
        setForeground(buttonColors.getTextColor());
    }
}
