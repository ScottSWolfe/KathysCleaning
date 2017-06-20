package com.github.scottswolfe.kathyscleaning.component;

import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

@SuppressWarnings("serial")
public class KcButton extends JButton {

    public KcButton(String label) {
        super(label);
        setFont(getFont().deriveFont(Settings.FONT_SIZE));
    }
    
    public KcButton(String label, ActionListener listener) {
        this(label);
        addActionListener(listener);
    }
    
}
