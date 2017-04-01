package com.github.scottswolfe.kathyscleaning.general.view;

import javax.swing.JMenu;

import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

public class Menu extends JMenu {

    public Menu(String s) {
        super(s);
        this.setFont(this.getFont().deriveFont(Settings.FONT_SIZE));
    }
    
}
