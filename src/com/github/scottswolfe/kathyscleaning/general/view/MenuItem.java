package com.github.scottswolfe.kathyscleaning.general.view;

import javax.swing.JMenuItem;

import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

@SuppressWarnings("serial")
public class MenuItem extends JMenuItem {

    public MenuItem(String s) {
        super(s);
        this.setFont(this.getFont().deriveFont(Settings.FONT_SIZE));
    }
    
}
