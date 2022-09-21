package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTabbedPane;

public class PreviousDayListener implements ActionListener {

    private final JTabbedPane tp;

    public PreviousDayListener(JTabbedPane tp) {
        this.tp = tp;
    }

    public void actionPerformed(ActionEvent e) {
        if (tp.getSelectedIndex() > 0) {
            tp.setSelectedIndex(tp.getSelectedIndex() - 1);
        }
    }
}
