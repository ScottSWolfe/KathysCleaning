package com.github.scottswolfe.kathyscleaning.general.model;

import java.awt.Color;

public class ButtonColors {

    private final Color buttonColor;
    private final Color textColor;

    public static ButtonColors from(final Color buttonColor, final Color textColor) {
        return new ButtonColors(buttonColor, textColor);
    }

    private ButtonColors(final Color buttonColor, final Color textColor) {
        this.buttonColor = buttonColor;
        this.textColor = textColor;
    }

    public Color getButtonColor() {
        return buttonColor;
    }

    public Color getTextColor() {
        return textColor;
    }
}
