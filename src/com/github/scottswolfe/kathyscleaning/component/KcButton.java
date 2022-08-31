package com.github.scottswolfe.kathyscleaning.component;

import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

public class KcButton extends JButton {

    public KcButton(final String label, final ActionListener listener) {
        super(label);
        setFont(getFont().deriveFont(Settings.FONT_SIZE));
        addActionListener(listener);
    }
}
