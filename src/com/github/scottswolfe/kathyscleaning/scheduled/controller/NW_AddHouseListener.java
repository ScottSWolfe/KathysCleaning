package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.general.controller.ApplicationCoordinator;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_HousePanel;


public class NW_AddHouseListener implements ActionListener {

    NW_DayPanel day_panel;
    NW_HousePanel house_panel;
    WorkerList dwd;
    JFrame frame;

    public static final int MAX_HOUSE_PANELS = 5;

    public NW_AddHouseListener(NW_DayPanel day_panel, NW_HousePanel house_panel, WorkerList dwd, JFrame frame) {
        this.day_panel = day_panel;
        this.house_panel = house_panel;
        this.dwd = dwd;
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent e){

        if (day_panel.getNumHousePanels() >= MAX_HOUSE_PANELS) {
            return;
        }

        // initializing variables
        int num = day_panel.getNumHousePanels();
        int index = -1;
        for(int i = 0; i < num; i++) {
            if (day_panel.house_panels.get(i) == house_panel){
                index = i;
            }
        }

        //remove old panels scroll pane
        for(int i = 0; i < num; i++) {
            day_panel.jsp_panel.remove(day_panel.house_panels.get(i));
        }

        // add new house panel
        day_panel.house_panels.add(index + 1, new NW_HousePanel(day_panel.header_panel.getWorkerList(), day_panel, frame));

        // reset focus listeners
        day_panel.addFlexibleFocusListeners();

        // add panels back to scroll pane
        for(NW_HousePanel house_panel : day_panel.house_panels) {
            day_panel.jsp_panel.add(house_panel, new String("wrap " + DayPanel.PANEL_PADDING + ", growx") );
        }

        ApplicationCoordinator.getInstance().refreshWindow();
    }
}
