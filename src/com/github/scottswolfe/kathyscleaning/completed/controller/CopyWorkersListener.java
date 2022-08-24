package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;


public class CopyWorkersListener implements ActionListener {

//  FIELDS

    TabbedPane tabbed_pane;
    DayData day_data;
    DayPanel day_panel;


//  CONSTRUCTOR

    public CopyWorkersListener(TabbedPane tabbed_pane, DayData day_data, DayPanel day_panel) {
        this.tabbed_pane = tabbed_pane;
        this.day_data = day_data;
        this.day_panel = day_panel;
    }


//  LISTENER

    public void actionPerformed(ActionEvent e) {
        List<String> selected_names = day_panel.header_panel.dwp.getSelectedWorkerNames();
        for(int k = 0; k < day_panel.house_panel.length; k++) {
            day_panel.house_panel[k].workerSelectPanel.setSelected(selected_names);
        }
    }

}
