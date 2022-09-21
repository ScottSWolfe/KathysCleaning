package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTabbedPane;

public class NextDayListener implements ActionListener {

    private final JTabbedPane tp;

    public NextDayListener(JTabbedPane tp) {
        this.tp = tp;
    }

    public void actionPerformed(ActionEvent e) {
        if (tp.getSelectedIndex() < tp.getTabCount() - 1) {
            tp.setSelectedIndex(tp.getSelectedIndex() + 1);
        }
    }
}
