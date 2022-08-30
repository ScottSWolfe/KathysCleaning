package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;

public class NextDayListener implements ActionListener {

    private final TabbedPane tp;

    public NextDayListener(TabbedPane tp) {
        this.tp = tp;
    }

    public void actionPerformed(ActionEvent e) {
        if (tp.getSelectedIndex() < tp.getTabCount() - 1) {
            tp.setSelectedIndex(tp.getSelectedIndex() + 1);
        }
    }
}
