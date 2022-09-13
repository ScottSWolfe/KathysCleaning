package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_HousePanel;

public class NW_CopyWorkersListener implements ActionListener {

    TabbedPane tabbed_pane;
    NW_DayPanel day_panel;

    public NW_CopyWorkersListener(TabbedPane tabbed_pane, NW_DayPanel day_panel) {
        this.tabbed_pane = tabbed_pane;
        this.day_panel = day_panel;
    }

    public void actionPerformed(ActionEvent e) {
        List<String> selected_names = day_panel.header_panel.getSelectedWorkers();
        for(NW_HousePanel house_panel : day_panel.house_panels) {
            house_panel.worker_panel.setSelected(selected_names);
        }
    }
}
