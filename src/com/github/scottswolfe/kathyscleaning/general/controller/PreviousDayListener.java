package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;

    public class PreviousDayListener implements ActionListener {

    private final TabbedPane tp;

    public PreviousDayListener(TabbedPane tp) {
        this.tp = tp;
    }

    public void actionPerformed(ActionEvent e) {
        if (tp.getSelectedIndex() > 0) {
            tp.setSelectedIndex(tp.getSelectedIndex() - 1);
        }
    }
}
