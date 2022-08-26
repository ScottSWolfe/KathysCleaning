package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;

public class CopyWorkersListener implements ActionListener {

    private final DayPanel dayPanel;

    public CopyWorkersListener(final DayPanel dayPanel) {
        this.dayPanel = dayPanel;
    }

    public void actionPerformed(ActionEvent e) {
        dayPanel.setWorkerSelectionsForAllHouses(dayPanel.header_panel.dwp.getWorkerSelectionGrid());
    }
}
